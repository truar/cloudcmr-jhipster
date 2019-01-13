import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICategoryArticle, defaultValue } from 'app/shared/model/category-article.model';

export const ACTION_TYPES = {
  FETCH_CATEGORYARTICLE_LIST: 'categoryArticle/FETCH_CATEGORYARTICLE_LIST',
  FETCH_CATEGORYARTICLE: 'categoryArticle/FETCH_CATEGORYARTICLE',
  CREATE_CATEGORYARTICLE: 'categoryArticle/CREATE_CATEGORYARTICLE',
  UPDATE_CATEGORYARTICLE: 'categoryArticle/UPDATE_CATEGORYARTICLE',
  DELETE_CATEGORYARTICLE: 'categoryArticle/DELETE_CATEGORYARTICLE',
  RESET: 'categoryArticle/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICategoryArticle>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CategoryArticleState = Readonly<typeof initialState>;

// Reducer

export default (state: CategoryArticleState = initialState, action): CategoryArticleState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CATEGORYARTICLE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CATEGORYARTICLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CATEGORYARTICLE):
    case REQUEST(ACTION_TYPES.UPDATE_CATEGORYARTICLE):
    case REQUEST(ACTION_TYPES.DELETE_CATEGORYARTICLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CATEGORYARTICLE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CATEGORYARTICLE):
    case FAILURE(ACTION_TYPES.CREATE_CATEGORYARTICLE):
    case FAILURE(ACTION_TYPES.UPDATE_CATEGORYARTICLE):
    case FAILURE(ACTION_TYPES.DELETE_CATEGORYARTICLE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CATEGORYARTICLE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CATEGORYARTICLE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CATEGORYARTICLE):
    case SUCCESS(ACTION_TYPES.UPDATE_CATEGORYARTICLE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CATEGORYARTICLE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/category-articles';

// Actions

export const getEntities: ICrudGetAllAction<ICategoryArticle> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CATEGORYARTICLE_LIST,
  payload: axios.get<ICategoryArticle>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICategoryArticle> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CATEGORYARTICLE,
    payload: axios.get<ICategoryArticle>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICategoryArticle> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CATEGORYARTICLE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICategoryArticle> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CATEGORYARTICLE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICategoryArticle> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CATEGORYARTICLE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
