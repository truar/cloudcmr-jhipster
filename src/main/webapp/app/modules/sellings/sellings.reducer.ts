import axios from 'axios';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { ISale, defaultValue } from 'app/shared/model/sale.model';

export const ACTION_TYPES = {
  FETCH_CURRENT_SALES_SESSION: 'sales-session/FETCH_CURRENT_SALES_SESSION',
  OPEN_SALES_SESSION: 'sales-session/OPEN_SALES_SESSION',
  SELL_ARTICLE_SALES_SESSION: 'sales-session/SELL_ARTICLE_SALES_SESSION',
  REMOVE_ARTICLE_SALES_SESSION: 'sales-session/REMOVE_ARTICLE_SALES_SESSION',
  ASSIGN_PAYER_SALES_SESSION: 'sales-session/ASSIGN_PAYER_SALES_SESSION',
  CLOSE_RUNNING_SALE_SALES_SESSION: 'sales-session/CLOSE_RUNNING_SALE_SALES_SESSION',
  CLOSE_SALES_SESSION: 'sales-session/CLOSE_SALES_SESSION'
};

const initialState = {
  loadingSession: true,
  needOpenSalesSession: true,
  runningSale: defaultValue
};

export type SellingsState = Readonly<typeof initialState>;

export default (state: SellingsState = initialState, action): SellingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CURRENT_SALES_SESSION):
    case REQUEST(ACTION_TYPES.REMOVE_ARTICLE_SALES_SESSION):
    case REQUEST(ACTION_TYPES.ASSIGN_PAYER_SALES_SESSION):
    case REQUEST(ACTION_TYPES.CLOSE_RUNNING_SALE_SALES_SESSION):
    case REQUEST(ACTION_TYPES.OPEN_SALES_SESSION):
    case REQUEST(ACTION_TYPES.CLOSE_SALES_SESSION):
    case REQUEST(ACTION_TYPES.SELL_ARTICLE_SALES_SESSION):
      return {
        ...state,
        loadingSession: true
      };
    case SUCCESS(ACTION_TYPES.FETCH_CURRENT_SALES_SESSION):
      return {
        ...state,
        loadingSession: false,
        needOpenSalesSession: false,
        runningSale: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.OPEN_SALES_SESSION):
      return {
        ...state,
        loadingSession: false,
        needOpenSalesSession: false
      };
    case SUCCESS(ACTION_TYPES.CLOSE_SALES_SESSION):
      return {
        ...state,
        loadingSession: false,
        needOpenSalesSession: true
      };
    case SUCCESS(ACTION_TYPES.SELL_ARTICLE_SALES_SESSION):
    case SUCCESS(ACTION_TYPES.ASSIGN_PAYER_SALES_SESSION):
    case SUCCESS(ACTION_TYPES.REMOVE_ARTICLE_SALES_SESSION):
    case SUCCESS(ACTION_TYPES.CLOSE_RUNNING_SALE_SALES_SESSION):
      return {
        ...state
      };
    case FAILURE(ACTION_TYPES.FETCH_CURRENT_SALES_SESSION):
    case FAILURE(ACTION_TYPES.OPEN_SALES_SESSION):
    case FAILURE(ACTION_TYPES.SELL_ARTICLE_SALES_SESSION):
    case FAILURE(ACTION_TYPES.ASSIGN_PAYER_SALES_SESSION):
    case FAILURE(ACTION_TYPES.REMOVE_ARTICLE_SALES_SESSION):
    case FAILURE(ACTION_TYPES.CLOSE_RUNNING_SALE_SALES_SESSION):
    case FAILURE(ACTION_TYPES.CLOSE_SALES_SESSION):
      switch (action.payload.response.status) {
        case 404:
          return {
            ...state,
            needOpenSalesSession: true
          };
        default:
          return {
            ...state,
            loadingSession: false
          };
      }

    default:
      return state;
  }
};

const apiUrl = 'api/sales-session';

export const getCurrentSalesSession = () => {
  const requestUrl = `${apiUrl}/current`;
  return {
    type: ACTION_TYPES.FETCH_CURRENT_SALES_SESSION,
    payload: axios.get(requestUrl),
    meta: {
      disable: true
    }
  };
};

export const openSalesSession = cashWhenNew => {
  const requestUrl = `${apiUrl}/current/open`;
  return {
    type: ACTION_TYPES.OPEN_SALES_SESSION,
    payload: axios.post(requestUrl, { cash: cashWhenNew })
  };
};

export const sellArticle = (articleId, memberId, quantity) => async dispatch => {
  const requestUrl = `${apiUrl}/current/sellArticle`;
  const result = await dispatch({
    type: ACTION_TYPES.SELL_ARTICLE_SALES_SESSION,
    payload: axios.put(requestUrl, { articleId, memberId, quantity }),
    meta: {
      successMessage: 'Article ajouté à la vente'
    }
  });
  dispatch(getCurrentSalesSession());
  return result;
};

export const removeSoldArticle = soldArticle => async dispatch => {
  const requestUrl = `${apiUrl}/current/removeArticle/${soldArticle.id}`;
  const result = await dispatch({
    type: ACTION_TYPES.REMOVE_ARTICLE_SALES_SESSION,
    payload: axios.delete(requestUrl),
    meta: {
      successMessage: 'Article supprimé de la vente'
    }
  });
  dispatch(getCurrentSalesSession());
  return result;
};

export const assignPayer = (payerId, paymentType) => async dispatch => {
  const requestUrl = `${apiUrl}/current/assignPayer`;
  const result = await dispatch({
    type: ACTION_TYPES.ASSIGN_PAYER_SALES_SESSION,
    payload: axios.put(requestUrl, { payerId, paymentType }),
    meta: {
      successMessage: 'Payeur mis à jour'
    }
  });
  dispatch(getCurrentSalesSession());
  return result;
};

export const assignSecondPayer = (payerId, paymentAmount, paymentType) => async dispatch => {
  const requestUrl = `${apiUrl}/current/assignSecondPayer`;
  const result = await dispatch({
    type: ACTION_TYPES.ASSIGN_PAYER_SALES_SESSION,
    payload: axios.put(requestUrl, { payerId, paymentAmount, paymentType }),
    meta: {
      successMessage: 'Second payeur mis à jour'
    }
  });
  dispatch(getCurrentSalesSession());
  return result;
};

export const removeSecondPayer = () => async dispatch => {
  const requestUrl = `${apiUrl}/current/removeSecondPayer`;
  const result = await dispatch({
    type: ACTION_TYPES.ASSIGN_PAYER_SALES_SESSION,
    payload: axios.put(requestUrl),
    meta: {
      successMessage: 'Second payeur supprimé'
    }
  });
  dispatch(getCurrentSalesSession());
  return result;
};

export const closeRunningSale = () => async dispatch => {
  const requestUrl = `${apiUrl}/current/closeRunningSale`;
  const result = await dispatch({
    type: ACTION_TYPES.CLOSE_RUNNING_SALE_SALES_SESSION,
    payload: axios.put(requestUrl),
    meta: {
      successMessage: 'Vente terminée'
    }
  });
  dispatch(getCurrentSalesSession());
  return result;
};

export const closeSalesSession = cashOnClose => {
  const requestUrl = `${apiUrl}/current/close`;
  return {
    type: ACTION_TYPES.CLOSE_SALES_SESSION,
    payload: axios.put(requestUrl, { cash: cashOnClose }),
    meta: {
      successMessage: 'Session de Vente terminée'
    }
  };
};
