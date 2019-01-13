import { IArticle } from 'app/shared/model//article.model';

export interface ICategoryArticle {
  id?: number;
  code?: string;
  description?: string;
  articles?: IArticle[];
}

export const defaultValue: Readonly<ICategoryArticle> = {};
