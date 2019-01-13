import { IMember } from 'app/shared/model//member.model';

export const enum PhoneType {
  PHONE = 'PHONE',
  MOBILE = 'MOBILE'
}

export interface IPhone {
  id?: number;
  type?: PhoneType;
  phoneNumber?: string;
  member?: IMember;
}

export const defaultValue: Readonly<IPhone> = {};
