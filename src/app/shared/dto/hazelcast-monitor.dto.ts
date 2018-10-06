import {AbstractTopicDTO} from './topics.dto';
import {ProductDTO} from '@shared/dto/topic-products.dto';

export interface AbstractMessageDTO {
  messageType: 'subscribe' | 'update_subscription' | 'unsubscribe' | 'compile_filters' | 'subscribe_response' | 'update_subscription_response' | 'notice' | 'compile_filters_response' | 'error';
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

export interface CompileFiltersRequestDTO extends AbstractMessageDTO {
  messageType: 'compile_filters';
  source: string;
}

export interface CompileFiltersResponseDTO extends AbstractMessageDTO {
  messageType: 'compile_filters_response';
  ok: boolean;
  errors?: string;
}

export interface UpdateSubscriptionRequestDTO extends AbstractMessageDTO {
  messageType: 'update_subscription';
  subscriptionId: number;
  parameter: string;
  value: string;
}

export interface UpdateSubscriptionResponseDTO extends AbstractMessageDTO {
  messageType: 'update_subscription_response';
  subscriptionId: number;
  parameter: string;
  value: string;
  error?: string;
}
