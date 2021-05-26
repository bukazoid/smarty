import { doFetch, doGet, doGetJson, genUrl } from "../common/tools";
import { actAlarm, actMessage } from "./messageReducer";

const defaultState = {
  state: null,
  list: [],
  page: {
    size: 10,
  },
};

export const VALUES_LOAD_START = "value/load/start";
export const VALUES_LOAD_SUCCESS = "value/load/success";
export const VALUES_LOAD_ERROR = "value/load/error";

export const REGISTER_ERROR = "value/register/error";
//export const REGISTER_ERROR = "value/register/success";

const STATE_NONE = "NONE";
const STATE_ERROR = "ERROR";
const STATE_LOADING = "LOADING";
const STATE_LOADED = "LOAD";

export default function valueReducer(state = defaultState, action) {
  switch (action.type) {
    case VALUES_LOAD_START:
      return { ...state, state: STATE_LOADING, list: [] };
    case VALUES_LOAD_SUCCESS:
      return {
        ...state,
        state: STATE_LOADED,
        list: action.payload._embedded.values,
        page: action.payload.page,
      };
    case VALUES_LOAD_ERROR:
      return { ...state, error: action.payload, state: STATE_ERROR };
    default:
      return state;
  }
}

const valuesLoadStart = () => {
  return { type: VALUES_LOAD_START };
};

const valuesLoadSuccess = (json) => {
  return { type: VALUES_LOAD_SUCCESS, payload: json };
};

export function loadValues(
  page = 0,
  size = 10,
  sortField = "name",
  sortOrder = "asc"
) {
  return (dispatch) => {
    dispatch(valuesLoadStart());

    const url = genUrl("values", page, size, sortField, sortOrder);
    doGetJson(
      url,
      (json) => {
        dispatch(actMessage("values loaded"));
        dispatch(valuesLoadSuccess(json));
      },
      (e) => {
        dispatch(actAlarm("values(sensors) not loaded"));
        console.log("error on loading data", e);
      }
    );
  };
}

const registerError = (status) => {
  return { type: REGISTER_ERROR, payload: status };
};

export function actRegister(id, successEvent) {
  return (dispatch) => {
    doGet(
      "/api/monitoron/" + id,
      (text) => {
        dispatch(actMessage("Registaration result: ", text));
        if (successEvent != null) {
          dispatch(successEvent);
        }
      },
      (e) => {
        console.log("error on registering", e);
        dispatch(actAlarm("can't register"));
      }
    );
  };
}

export function actUnregister(id, successEvent) {
  return (dispatch) => {
    doFetch("/api/monitoroff/" + id, "GET")
      .then((response) => {
        if (!response.ok) {
          // reload values
          dispatch(registerError(response.status));
        }
        return response.json();
      })
      .then((json) => {
        // reload values
        if (successEvent != null) {
          dispatch(successEvent);
        }
        //dispatch(loadValues());
      })
      .catch((e) => {
        //store.dispatch({ type: "device/load/exception", payload: e }); // maybe more common error
        console.log("error on registering", e);
      });
  };
}
