import { objectToQueryString } from "hookrouter";
import {
  doDelete,
  doFetch,
  doGet,
  doGetJson,
  doPost,
  doPut,
  genUrl,
  initHateOasIds,
} from "../common/tools";
import { actAlarm, actMessage } from "./messageReducer";

const defaultState = {
  list: [],
  state: null,
  page: {
    size: 10,
  },

  viewProfiles: [],
  projectionProfiles: [],
};

export const VIEWS_LOADING = "views/loading";
export const VIEWS_LOADED = "views/loaded";
export const VIEWS_NEW = "views/new";
export const VIEW_LOADED = "view/loaded";
export const STATE_LOADED = "loaded";
export const VIEW_USERS_LOADED = "view/users/loaded";
export const VIEW_ALL_USERS_LOADED = "view/all_users/loaded";

export const VIEW_SET_PROFILES = "view/setProfiles";
export const VIEW_SET_PROJECTION_PROFILES = "view/projection/setProfiles";

export default function viewReducer(state = defaultState, action) {
  switch (action.type) {
    case VIEWS_LOADING:
      return { ...state, state: VIEWS_LOADING };
    case VIEWS_LOADED:
      return {
        ...state,
        state: STATE_LOADED,
        list: action.payload,
        page: action.page,
      };
    case VIEWS_NEW:
      return {
        ...state,
        selectedState: STATE_LOADED,
        selected: { name: "view" },
      };
    case VIEW_LOADED:
      return {
        ...state,
        selectedState: STATE_LOADED,
        selected: action.payload,
      };
    case VIEW_USERS_LOADED:
      return {
        ...state,
        users2delete: action.payload,
      };
    case VIEW_ALL_USERS_LOADED:
      return {
        ...state,
        users2add: action.payload,
      };
    case VIEW_SET_PROFILES:
      return {
        ...state,
        viewProfiles: action.profiles,
      };
    case VIEW_SET_PROJECTION_PROFILES:
      return {
        ...state,
        projectionProfiles: action.profiles,
      };
    default:
      return state;
  }
}

export function viewsLoading() {
  return { type: VIEWS_LOADING };
}

export function viewsLoaded(payload) {
  return { type: VIEWS_LOADED, payload: payload };
}

export function actLoadUserViews() {
  return (dispatch) => {
    dispatch({ type: VIEWS_LOADING });

    const url = "/api/views/userViews";

    doGetJson(
      url,
      (json) => {
        dispatch(actMessage("views loaded"));
        dispatch({
          type: VIEWS_LOADED,
          payload: json,
          page: { size: json?.length || 0 },
        });
      },
      (e) => {
        dispatch(actAlarm("views loaded failed"));
        console.log("error on loading data", e);
      }
    );
  };
}

export function actLoadViews(
  page = 0,
  size = 10,
  sortField = "name",
  sortOrder = "asc"
) {
  return (dispatch) => {
    dispatch(viewsLoading());

    const url = genUrl("views", page, size, sortField, sortOrder);

    doGetJson(
      url,
      (json) => {
        dispatch(actMessage("views loaded"));

        console.log("downloaded: ", json._embedded.views.length);
        console.log("parsed: ", initHateOasIds(json._embedded.views).length);
        dispatch({
          type: VIEWS_LOADED,
          payload: initHateOasIds(json._embedded.views),
          page: json.page,
        });
      },
      (e) => {
        dispatch(actAlarm("views loaded failed"));
        console.log("error on loading data", e);
      }
    );
  };
}

export function actNewView() {
  return { type: VIEWS_NEW };
}

const viewLoadSuccess = (json) => {
  return { type: VIEW_LOADED, payload: json };
};

function actLoadViewAllUsers(viewId, users2delete) {
  return (dispatch) => {
    doGetJson(
      "/api/susers?" + "size=999",
      (json) => {
        const allUsers = json._embedded.users;
        // trash
        const users2add = allUsers.filter((el) => {
          const u2d = users2delete.find((du) => {
            return du._links.self.href == el._links.self.href;
          });
          return !u2d;
        });
        dispatch({ type: VIEW_ALL_USERS_LOADED, payload: users2add });
      },
      (e) => {
        dispatch(actAlarm("can't load view's users"));
        console.log("error on loading data", e);
      }
    );
  };
}

export function actLoadViewUsers(viewId) {
  return (dispatch) => {
    doGetJson(
      "/api/views/" + viewId + "/users" + "?size=999",
      (json) => {
        const users = json._embedded.users;
        dispatch({ type: VIEW_USERS_LOADED, payload: users });
        // load all users
        dispatch(actLoadViewAllUsers(viewId, users));
      },
      (e) => {
        dispatch(actAlarm("can't load view's users"));
        console.log("error on loading data", e);
      }
    );
  };
}

export function actLoadViewAndUsers(viewId) {
  return (dispatch) => {
    dispatch(actLoadView(viewId));
    dispatch(actLoadViewUsers(viewId));
  };
}

export function actLoadView(viewId) {
  return (dispatch) => {
//    dispatch(viewsLoading());

    doGetJson(
      "/api/views/" + viewId,
      (json) => {
        dispatch(viewLoadSuccess(json));
      },
      (e) => {
        dispatch(actAlarm("view load is failed"));
        console.log("error on loading data", e);
      }
    );
  };
}

export function actSaveView(id, view) {
  view.id = id == "new" ? null : id;
  const url = id == "new" ? "/api/views" : "/api/views/" + id;
  const method = id == "new" ? doPost : doPut;
  return (dispatch) => {
    dispatch(viewsLoading());

    method(
      url,
      JSON.stringify(view),
      (response) =>
        response.json().then((json) => {
          dispatch(actMessage("view saved"));
          dispatch(viewLoadSuccess(json));
        }),
      (e) => {
        dispatch(actAlarm("view save is failed"));
        console.log("error on loading data", e);
      }
    );
  };
}

export function actDeleteView(id) {
  return (dispatch) => {
    doDelete(
      "/api/views/" + id,
      (text) => {
        dispatch(actMessage("view deleted"));
        dispatch(actLoadViews());
      },
      (e) => {
        dispatch(actAlarm("view delete is failed"));
        console.log("error on loading data", e);
      }
    );
  };
}

export function actSetViewProfiles(profiles) {
  console.log("actSetViewProfiles");
  return { type: VIEW_SET_PROFILES, profiles: profiles };
}

export function actSetViewProjectionProfiles(profiles) {
  return { type: VIEW_SET_PROJECTION_PROFILES, profiles: profiles };
}
