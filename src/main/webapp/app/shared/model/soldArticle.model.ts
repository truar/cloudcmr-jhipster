import { IMember } from 'app/shared/model/member.model';
import { IArticle } from 'app/shared/model/article.model';

export interface ISoldArticle {
  id?: number;
  quantity?: number;
  member?: IMember;
  article?: IArticle;
}

export const defaultValue: Readonly<ISoldArticle> = {};
