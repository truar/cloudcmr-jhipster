import { IMember } from 'app/shared/model//member.model';

export interface IAddress {
  id?: number;
  address1?: string;
  address2?: string;
  address3?: string;
  zipcode?: string;
  city?: string;
  country?: string;
  member?: IMember;
}

export const defaultValue: Readonly<IAddress> = {};
