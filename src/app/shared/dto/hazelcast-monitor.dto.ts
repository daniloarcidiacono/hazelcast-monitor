import {AbstractTopicDTO} from './topics.dto';
import {ProductDTO} from '@shared/dto/topic-products.dto';

export interface AbstractMessageDTO {
  messageType: 'subscribe' | 'unsubscribe' | 'notice' | 'error';
  messageId?: number;
}

export interface ErrorMessageDTO extends AbstractMessageDTO {
  messageType: 'error';
  errors: string[];
}

export interface UnsubscribeRequestDTO extends AbstractMessageDTO {
  messageType: 'unsubscribe';
  subscriptionId: number;
}

export interface SubscribeRequestDTO extends AbstractMessageDTO {
  messageType: 'subscribe';
  frequency: number;
  topic: AbstractTopicDTO;
}

export interface SubscribeResponseDTO extends AbstractMessageDTO {
  messageType: 'subscribe';
  subscriptionId: number;
  topic: AbstractTopicDTO;
}

export interface SubscriptionNoticeResponseDTO<T extends ProductDTO> extends AbstractMessageDTO {
  messageType: 'notice';
  subscriptionId: number;
  topicType: string;
  notice: T;
}
