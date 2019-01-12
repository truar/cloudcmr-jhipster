import { Moment } from 'moment';

export const enum GenderType {
  MALE = 'MALE',
  FEMALE = 'FEMALE'
}

export interface IMember {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  birthDate?: Moment;
  gender?: GenderType;
  uscaNumber?: string;
  comment?: string;
}

export const defaultValue: Readonly<IMember> = {};
