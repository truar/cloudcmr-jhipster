import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

export const ACTION_TYPES = {
  IMPORT_FFS: 'ffsManagement/IMPORT_FFS'
};

const initialState = {
  loading: false,
  errorMessage: null,
  loaded: false
};

export type FFSManagementState = Readonly<typeof initialState>;

// Reducer
export default (state: FFSManagementState = initialState, action): FFSManagementState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.IMPORT_FFS):
      return {
        ...state,
        loading: true
      };
    case FAILURE(ACTION_TYPES.IMPORT_FFS):
      return {
        ...state,
        loading: false,
        loaded: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.IMPORT_FFS):
      return {
        ...state,
        loaded: true,
        loading: false
      };
    default:
      return state;
  }
};

const apiUrl = 'api/members/file';

export const importFFSFile: ICrudPutAction<FormData> = exportFFS => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.IMPORT_FFS,
    payload: axios.post(apiUrl, exportFFS),
    meta: {
      successMessage: 'Import réussi'
    }
  });
  return result;
};
