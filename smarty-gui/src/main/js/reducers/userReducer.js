import { doDelete, doFetch } from "../common/tools";
import { actAlarm, actMessage } from "./messageReducer";

const defaultState = {
  state: null,
  list: [],
  page: {
    size: 10,
  },

  selected: null,
};

export const USERS_LOAD_START = "users/load/start";
export const USERS_LOAD_SUCCESS = "users/load/success";
export const USER_LOAD_SUCCESS = "user/load/success";
export const USER_NEW = "user/new";
export const USERS_LOAD_ERROR = "users/load/error";

const STATE_NONE = "NONE";
const STATE_ERROR = "ERROR";
const STATE_LOADING = "LOADING";
const STATE_LOADED = "LOAD";

export default function userReducer(state = defaultState, action) {
  switch (action.type) {
    case USERS_LOAD_START:
      return { ...state, state: STATE_LOADING };
    case USERS_LOAD_SUCCESS:
      return {
        ...state,
        state: STATE_LOADED,
        list: action.payload._embedded.users,
        page: action.payload.page,
      };
    case USERS_LOAD_ERROR:
      return { ...state, error: action.payload }; //XXX: send evenr with message

    // selected device

    case USER_LOAD_SUCCESS:
      return {
        ...state,
        selectedState: STATE_LOADED,
        selected: action.payload,
      };

    case USER_NEW:
      return {
        ...state,
        selectedState: STATE_LOADED,
        selected: { login: "new", type: "USER", password: "" },
      };

    default:
      return state;
  }
}

//////////////////////////
//   actions
//////////////////////////
const usersLoadStart = () => {
  return { type: USERS_LOAD_START };
};

const usersLoadSuccess = (json) => {
  return { type: USERS_LOAD_SUCCESS, payload: json };
};

const userLoadSuccess = (json) => {
  return { type: USER_LOAD_SUCCESS, payload: json };
};

const usersLoadError = (status) => {
  return { type: USERS_LOAD_ERROR, payload: status };
};

export function actLoadUsers(
  page = 0,
  size = 10,
  sortField = "login",
  sortOrder = "asc"
) {
  return (dispatch) => {
    dispatch(usersLoadStart());
    doFetch(
      "/api/users?page=" +
        page +
        "&size=" +
        size +
        "&sort=" +
        sortField +
        "," +
        sortOrder,
      "GET"
    )
      .then((response) => {
        if (!response.ok) {
          dispatch(actAlarm("users loaded failed"));
          dispatch(usersLoadError(response.status));
        }
        return response.json();
      })
      .then((json) => {
        dispatch(actMessage("users loaded"));
        dispatch(usersLoadSuccess(json));
      })
      .catch((e) => {
        dispatch(actAlarm("users loaded failed"));
        console.log("error on loading data", e);
      });
  };
}

export function actNewUser() {
  return { type: USER_NEW };
}

export function loadUser(userId) {
  return (dispatch) => {
    dispatch(usersLoadStart());
    doFetch("/api/users/" + userId, "GET")
      .then((response) => {
        if (!response.ok) {
          dispatch(actAlarm("user load is failed"));
          dispatch(usersLoadError(response.status));
        }
        return response.json();
      })
      .then((json) => {
        dispatch(userLoadSuccess(json));
      })
      .catch((e) => {
        dispatch(actAlarm("user load is failed"));
        console.log("error on loading data", e);
      });
  };
}

export function actSaveUser(id, user) {
  user.id = id == "new" ? null : id;
  const url = id == "new" ? "/api/users" : "/api/users/" + id;
  const method = id == "new" ? "POST" : "PUT";
  return (dispatch) => {
    dispatch(usersLoadStart());
    doFetch(url, method, JSON.stringify(user))
      .then((response) => {
        if (!response.ok) {
          dispatch(actAlarm("user save is failed"));
          dispatch(usersLoadError(response.status));
        }
        return response.json();
      })
      .then((json) => {
        dispatch(actMessage("user saved"));
        dispatch(userLoadSuccess(json));
      })
      .catch((e) => {
        dispatch(actAlarm("user save is failed"));
        console.log("error on loading data", e);
      });
  };
}

export function actDeleteUser(id) {
  return (dispatch) => {
    dispatch(usersLoadStart());

    doDelete(
      "/api/users/" + id,
      (text) => {
        dispatch(actMessage("user deleted"));
        dispatch(actLoadUsers());
      },
      (e) => {
        dispatch(actAlarm("user delete is failed"));
        console.log("error on loading data", e);
      }
    );
  };
}
