import { Moment } from 'moment';
import { ICategoryArticle } from './category-article.model';

export interface IArticle {
  id?: number;
  code?: string;
  description?: string;
  price?: number;
  startDate?: Moment;
  endDate?: Moment;
  category?: ICategoryArticle;
  comment?: string;
}

export const defaultValue: Readonly<IArticle> = {};
