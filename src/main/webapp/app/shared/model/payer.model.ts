export const enum PaymentType {
  CARD = 'CARD',
  CASH = 'CASH',
  CHEQUE = 'CHEQUE',
  OTHER = 'OTHER'
}

export interface IPayer {
  id?: number;
  memberId?: number;
  firstName?: string;
  lastName?: string;
  uscaNumber?: string;
  paymentAmount?: number;
  paymentType?: PaymentType;
}

export const defaultValue: Readonly<IPayer> = {};
