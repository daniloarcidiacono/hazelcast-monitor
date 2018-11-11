import {AbstractTopicDTO} from './topics.dto';
import {ProductDTO} from '@shared/dto/topic-products.dto';

export interface AbstractMessageDTO {
  messageType: 'subscribe' | 'update_subscription' | 'unsubscribe' | 'pull_subscription' | 'subscribe_response' | 'update_subscription_response' | 'notice' | 'error';
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
  parameters?: { [ index: string ]: string };
}

export interface PullSubscriptionRequestDTO extends AbstractMessageDTO {
  messageType: 'pull_subscription';
  subscriptionId: number;
}

export interface SubscribeResponseDTO extends AbstractMessageDTO {
  messageType: 'subscribe_response';
  subscriptionId: number;
  topic: AbstractTopicDTO;
}

export interface SubscriptionNoticeResponseDTO<T extends ProductDTO> extends AbstractMessageDTO {
  messageType: 'notice';
  subscriptionId: number;
  topicType: string;
  notice: T;
}

export interface UpdateSubscriptionRequestDTO extends AbstractMessageDTO {
  messageType: 'update_subscription';
  subscriptionId: number;
  parameters: { [ index: string ]: string };
}

export interface UpdateSubscriptionResponseDTO extends AbstractMessageDTO {
  messageType: 'update_subscription_response';
  subscriptionId: number;
  parameters: { [ index: string ]: string };
  error?: string;
}