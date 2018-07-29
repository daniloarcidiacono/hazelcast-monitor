import {Injectable} from '@angular/core';
import {
  AbstractMessageDTO,
  ErrorMessageDTO,
  SubscribeRequestDTO,
  SubscribeResponseDTO,
  SubscriptionNoticeResponseDTO,
  UnsubscribeRequestDTO
} from '@shared/dto/hazelcast-monitor.dto';
import {Observable, Observer, of, Subscription} from 'rxjs/index';
import {SharedWebSocketService} from '@shared/services/shared-websocket.service';
import {
  ClustersTopicDTO,
  DistributedObjectsTopicDTO,
  DistributedObjectTopicDTO,
  DistributedObjectType,
  MembersTopicDTO,
  StatisticsTopicDTO
} from '@shared/dto/topics.dto';
import {mergeAll} from 'rxjs/internal/operators';
import {
  ClustersProductDTO,
  DistributedObjectsProduct,
  ListProductDTO,
  ListsProductDTO,
  LocksProductDTO,
  MapProductDTO,
  MapsProductDTO,
  MembersProductDTO,
  MultiMapProductDTO,
  MultiMapsProductDTO, QueueProductDTO, QueuesProductDTO,
  ReplicatedMapProductDTO,
  ReplicatedMapsProductDTO, SetProductDTO, SetsProductDTO,
  StatisticsProductDTO
} from '@shared/dto/topic-products.dto';

@Injectable()
export class SharedHazelcastAgentService {
  private parsedMessages: Observable<AbstractMessageDTO>;

  public constructor(private wsService: SharedWebSocketService) {
    this.parsedMessages = this.parseMessages();
  }

  private parseMessages(): Observable<AbstractMessageDTO> {
    return new Observable<AbstractMessageDTO>((observer: Observer<AbstractMessageDTO>) => {
      const sub: Subscription = this.wsService.onMessageReceived.subscribe(
        (message: string) => {
          try {
            const parsedMessage: AbstractMessageDTO = JSON.parse(message);
            if (parsedMessage.messageType === 'error') {
              observer.error(parsedMessage as ErrorMessageDTO);
            } else {
              observer.next(parsedMessage);
            }
          } catch (e) {
            // Just ignore it
          }
        },
        (error: any) => {
          observer.error(error);
        },
        () => {
          observer.complete();
        }
      );

      return () => {
        sub.unsubscribe();
      };
    });
  }

  private filterById(id: number): (stream: Observable<AbstractMessageDTO>) => Observable<AbstractMessageDTO> {
    return (stream: Observable<AbstractMessageDTO>) => {
      return new Observable<AbstractMessageDTO>((observer: Observer<AbstractMessageDTO>) => {
        const sub: Subscription = stream.subscribe(
          (message: AbstractMessageDTO) => {
            if (message.messageId === id) {
              observer.next(message);
            }
          },
          (error: ErrorMessageDTO) => {
            observer.error(error);
          },
          () => {
            observer.complete();
          }
        );

        return () => {
          sub.unsubscribe();
        };
      });
    };
  }

  private filterNoticesBySubscriptionId<T>(subscriptionId: number): (stream: Observable<AbstractMessageDTO>) => Observable<SubscriptionNoticeResponseDTO<T>> {
    return (stream: Observable<AbstractMessageDTO>) => {
      return new Observable<SubscriptionNoticeResponseDTO<T>>((observer: Observer<SubscriptionNoticeResponseDTO<T>>) => {
        const sub: Subscription = stream.subscribe(
          (message: AbstractMessageDTO) => {
            if (message.messageType === 'notice') {
              const subNotice: SubscriptionNoticeResponseDTO<T> = message as SubscriptionNoticeResponseDTO<T>;
              if (subNotice.subscriptionId === subscriptionId) {
                observer.next(subNotice);
              }
            }
          },
          (error: ErrorMessageDTO) => {
            observer.error(error);
          },
          () => {
            observer.complete();
          }
        );

        return () => {
          sub.unsubscribe();
        };
      });
    };
  }

  private takeFirstSubscriptionResponse(stream: Observable<AbstractMessageDTO>): Observable<SubscribeResponseDTO> {
    return new Observable<SubscribeResponseDTO>((observer: Observer<SubscribeResponseDTO>) => {
      const sub: Subscription = stream.subscribe(
        (message: AbstractMessageDTO) => {
          if (message.messageType === 'subscribe') {
            const subResponse: SubscribeResponseDTO = message as SubscribeResponseDTO;
            observer.next(subResponse);
            observer.complete();
          }
        },
        (error: ErrorMessageDTO) => {
          observer.error(error);
        },
        () => {
          observer.complete();
        }
      );

      return () => {
        sub.unsubscribe();
      };
    });
  }

  private mapToNoticeStreams<T>(stream: Observable<SubscribeResponseDTO>): Observable<Observable<SubscriptionNoticeResponseDTO<T>>> {
    return new Observable<Observable<SubscriptionNoticeResponseDTO<T>>>((observer: Observer<Observable<SubscriptionNoticeResponseDTO<T>>>) => {
      const sub: Subscription = stream.subscribe(
        (subResponse: SubscribeResponseDTO) => {
          const noticeObs: Observable<SubscriptionNoticeResponseDTO<T>> = this.parsedMessages.pipe(
            this.filterNoticesBySubscriptionId<T>(subResponse.subscriptionId).bind(this)
          );

          observer.next(noticeObs);
        },
        (error: ErrorMessageDTO) => {
          observer.error(error);
        },
        () => {
          observer.complete();
        }
      );

      return () => {
        sub.unsubscribe();
      };
    });
  }

  private subTo<T>(subRequest: SubscribeRequestDTO): Observable<SubscriptionNoticeResponseDTO<T>> {
    const subResponsePromise: Promise<SubscribeResponseDTO> = this.parsedMessages.pipe(
      this.filterById(subRequest.messageId),
      this.takeFirstSubscriptionResponse.bind(this)
    ).toPromise() as Promise<SubscribeResponseDTO>;

    this.wsService.sendMessage(JSON.stringify(subRequest));

    return new Observable<SubscriptionNoticeResponseDTO<T>>((observer: Observer<SubscriptionNoticeResponseDTO<T>>) => {
      let noticeSub: Subscription;
      let subResponse: SubscribeResponseDTO;
      subResponsePromise.then((subscriptionResponse: SubscribeResponseDTO) => {
        const noticesObs: Observable<SubscriptionNoticeResponseDTO<T>> = of(subscriptionResponse).pipe(
          this.mapToNoticeStreams.bind(this),
          mergeAll()
        );

        subResponse = subscriptionResponse;
        noticeSub = noticesObs.subscribe(
          (notice: SubscriptionNoticeResponseDTO<T>) => {
            observer.next(notice);
          },
    (error: ErrorMessageDTO) => {
            observer.error(error);
          },
          () => {
            observer.complete();
          }
        );
      }).catch((error: ErrorMessageDTO) => {
        observer.error(error);
      });

      return () => {
        if (!!noticeSub) {
          noticeSub.unsubscribe();
        }

        if (!!subResponse) {
          const unSubRequest: UnsubscribeRequestDTO = {
            messageType: 'unsubscribe',
            subscriptionId: subResponse.subscriptionId
          };

          this.wsService.sendMessage(JSON.stringify(unSubRequest));
        }
      };
    });
  }

  public subscribeToClusters(): Observable<SubscriptionNoticeResponseDTO<ClustersProductDTO>> {
    const subRequest: SubscribeRequestDTO = {
      messageType: 'subscribe',
      messageId: this.wsService.generateMessageId(),
      frequency: 5,
      topic: <ClustersTopicDTO>{
        topicType: 'clusters'
      }
    };

    return this.subTo(subRequest);
  }

  public subscribeToStatistics(instanceName: string): Observable<SubscriptionNoticeResponseDTO<StatisticsProductDTO>> {
    const subRequest: SubscribeRequestDTO = {
      messageType: 'subscribe',
      messageId: this.wsService.generateMessageId(),
      frequency: 1,
      topic: <StatisticsTopicDTO>{
        topicType: 'stats',
        instanceName: instanceName
      }
    };

    return this.subTo(subRequest);
  }

  public subscribeToDistributedObjects(instanceName: string, distributedObjectType: DistributedObjectType): Observable<SubscriptionNoticeResponseDTO<DistributedObjectsProduct<any>>> {
    const subRequest: SubscribeRequestDTO = {
      messageType: 'subscribe',
      messageId: this.wsService.generateMessageId(),
      frequency: 1,
      topic: <DistributedObjectsTopicDTO>{
        topicType: 'distributed_object',
        instanceName: instanceName,
        distributedObjectType: distributedObjectType
      }
    };

    return this.subTo(subRequest);
  }

  public subscribeToMembers(instanceName: string): Observable<SubscriptionNoticeResponseDTO<MembersProductDTO>> {
    const subRequest: SubscribeRequestDTO = {
      messageType: 'subscribe',
      messageId: this.wsService.generateMessageId(),
      frequency: 1,
      topic: <MembersTopicDTO>{
        topicType: 'members',
        instanceName: instanceName
      }
    };

    return this.subTo(subRequest);
  }

  public subscribeToMaps(instanceName: string): Observable<SubscriptionNoticeResponseDTO<MapsProductDTO>> {
    return this.subscribeToDistributedObjects(instanceName, DistributedObjectType.MAP);
  }

  public subscribeToLists(instanceName: string): Observable<SubscriptionNoticeResponseDTO<ListsProductDTO>> {
    return this.subscribeToDistributedObjects(instanceName, DistributedObjectType.LIST);
  }

  public subscribeToLocks(instanceName: string): Observable<SubscriptionNoticeResponseDTO<LocksProductDTO>> {
    return this.subscribeToDistributedObjects(instanceName, DistributedObjectType.LOCK);
  }

  public subscribeToMultiMaps(instanceName: string): Observable<SubscriptionNoticeResponseDTO<MultiMapsProductDTO>> {
    return this.subscribeToDistributedObjects(instanceName, DistributedObjectType.MULTIMAP);
  }

  public subscribeToReplicatedMaps(instanceName: string): Observable<SubscriptionNoticeResponseDTO<ReplicatedMapsProductDTO>> {
    return this.subscribeToDistributedObjects(instanceName, DistributedObjectType.REPLICATEDMAP);
  }

  public subscribeToQueues(instanceName: string): Observable<SubscriptionNoticeResponseDTO<QueuesProductDTO>> {
    return this.subscribeToDistributedObjects(instanceName, DistributedObjectType.QUEUE);
  }

  public subscribeToSets(instanceName: string): Observable<SubscriptionNoticeResponseDTO<SetsProductDTO>> {
    return this.subscribeToDistributedObjects(instanceName, DistributedObjectType.SET);
  }

  public subscribeToDistributedObject(instanceName: string, distributedObjectType: DistributedObjectType, objectName: string): Observable<SubscriptionNoticeResponseDTO<any>> {
    const subRequest: SubscribeRequestDTO = {
      messageType: 'subscribe',
      messageId: this.wsService.generateMessageId(),
      frequency: 1,
      topic: <DistributedObjectTopicDTO>{
        topicType: 'distributed_object_details',
        instanceName: instanceName,
        distributedObjectType: distributedObjectType,
        objectName: objectName
      }
    };

    return this.subTo(subRequest);

  }

  public subscribeToMap(instanceName: string, mapName: string): Observable<SubscriptionNoticeResponseDTO<MapProductDTO>> {
    return this.subscribeToDistributedObject(instanceName, DistributedObjectType.MAP, mapName);
  }

  public subscribeToList(instanceName: string, listName: string): Observable<SubscriptionNoticeResponseDTO<ListProductDTO>> {
    return this.subscribeToDistributedObject(instanceName, DistributedObjectType.LIST, listName);
  }

  public subscribeToMultiMap(instanceName: string, multiMapName: string): Observable<SubscriptionNoticeResponseDTO<MultiMapProductDTO>> {
    return this.subscribeToDistributedObject(instanceName, DistributedObjectType.MULTIMAP, multiMapName);
  }

  public subscribeToReplicatedMap(instanceName: string, replicatedMapName: string): Observable<SubscriptionNoticeResponseDTO<ReplicatedMapProductDTO>> {
    return this.subscribeToDistributedObject(instanceName, DistributedObjectType.REPLICATEDMAP, replicatedMapName);
  }

  public subscribeToQueue(instanceName: string, queueName: string): Observable<SubscriptionNoticeResponseDTO<QueueProductDTO>> {
    return this.subscribeToDistributedObject(instanceName, DistributedObjectType.QUEUE, queueName);
  }

  public subscribeToSet(instanceName: string, setName: string): Observable<SubscriptionNoticeResponseDTO<SetProductDTO>> {
    return this.subscribeToDistributedObject(instanceName, DistributedObjectType.SET, setName);
  }
}
