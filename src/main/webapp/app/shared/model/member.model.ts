import { Moment } from 'moment';
import { IAddress } from 'app/shared/model//address.model';
import { IPhone } from 'app/shared/model//phone.model';

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
  licenceNumber?: string;
  licenceCreationDate?: Moment;
  subscription?: string;
  email2?: string;
  season?: number;
  addresses?: IAddress[];
  phones?: IPhone[];
  sff?: boolean;
}

export const defaultValue: Readonly<IMember> = {};
