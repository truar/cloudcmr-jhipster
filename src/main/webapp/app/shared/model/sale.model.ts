import { ISoldArticle } from 'app/shared/model/soldArticle.model';
import { IPayer } from './payer.model';

export const enum SaleStatus {
  NEW = 'NEW',
  IN_PROGRESS = 'IN_PROGRESS',
  COLSED = 'CLOSED'
}

export interface ISale {
  id?: number;
  payer?: IPayer;
  payer2?: IPayer;
  soldArticles?: ISoldArticle[];
  status?: SaleStatus;
  total?: number;
}

export const defaultValue: Readonly<ISale> = {};
