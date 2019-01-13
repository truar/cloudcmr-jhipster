import React from 'react';

import { TextFormat } from 'react-jhipster';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export const DateDisplayer = props =>
  props.date === null ? <span>Pas de date</span> : <TextFormat type="date" value={props.date} format={APP_LOCAL_DATE_FORMAT} />;
