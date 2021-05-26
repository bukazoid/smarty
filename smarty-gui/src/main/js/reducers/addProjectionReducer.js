import {
  doDelete,
  doFetch,
  doGet,
  doGetJson,
  doPost,
  doPut,
  genUrl,
  getHateOasId,
} from "../common/tools";
import { actAlarm, actMessage } from "./messageReducer";

// devies are loaded page by page
// sensors are loaded by device, so whole block (it is not too much sensors by the way)
const defaultState = {
  devices: [],
  sensors: [],
  state: null,
  devicesPage: {
    size: 10,
    number: 0,
  },
};

export const LOADING = "loading";
export const LOADED = "loaded";

export const ADD_PROJECTION_LOADING = "addprojection/loading";
export const ADD_PROJECTION_DEVICES = "addprojection/devices";
export const ADD_PROJECTION_SENSORS = "addprojection/sensors";
export const SET_VIEW_PROJECTIONS = "addprojection/projections";

export default function addprojectionReducer(state = defaultState, action) {
  switch (action.type) {
    case ADD_PROJECTION_LOADING:
      return { ...state, state: LOADING };
    case ADD_PROJECTION_DEVICES:
      return {
        ...state,
        state: LOADED,
        devices: action.payload._embedded.devices,
        devicesPage: action.payload.page,
      };
    case ADD_PROJECTION_SENSORS:
      return {
        ...state,
        state: LOADED,
        sensors: action.payload._embedded.sensors,
        deviceId: action.deviceId,
      };
    case SET_VIEW_PROJECTIONS:
      return {
        ...state,
        state: LOADED,
        projections: action.payload._embedded.projections,
      };
    default:
      return state;
  }
}

function addProjectionDevices(json) {
  return { type: ADD_PROJECTION_DEVICES, payload: json };
}

const addDeviceSensorsLoadSuccess = (json, deviceId) => {
  return { type: ADD_PROJECTION_SENSORS, payload: json, deviceId: deviceId };
};

export function actLoadAddProjectionDevices(
  page = 0,
  size = 10,
  sortField = "name",
  sortOrder = "asc"
) {
  return (dispatch) => {
    const url = genUrl("devices", page, size, sortField, sortOrder);
    doGetJson(
      url,
      (json) => {
        dispatch(addProjectionDevices(json));
      },
      (error) => {
        dispatch(actAlarm("devices load failed"));
        console.log("error on loading data", error);
      }
    );
  };
}

export function actLoadAddProjectionDeviceSensors(deviceId) {
  return (dispatch) => {
    doGetJson(
      "/api/devices/" + deviceId + "/sensors",
      (json) => dispatch(addDeviceSensorsLoadSuccess(json, deviceId)),
      (e) => {
        dispatch(actAlarm("sensors load is failed"));
        console.log("error on loading data", e);
      }
    );
  };
}

export function actAttachSensor(viewId, sensorId) {
  return (dispatch) => {
    doGetJson(
      "/api/views/attach/" + viewId + "/" + sensorId,
      (json) => {
        dispatch(actMessage("sensor attached"));
        dispatch(actGetViewProjections(viewId));
      },
      (e) => {
        dispatch(actAlarm("attaching is failed"));
        console.log("error on loading data", e);
      }
    );
  };
}

export function actDetachSensor(viewId, sensorId) {
  return (dispatch) => {
    doGetJson(
      "/api/views/detach/" + viewId + "/" + sensorId,
      (json) => {
        dispatch(actMessage("sensor detached"));
        dispatch(actGetViewProjections(viewId));
      },
      (e) => {
        dispatch(actAlarm("sensor is failed"));
        console.log("error on loading data", e);
      }
    );
  };
}

function setViewProjections(json) {
  return { type: SET_VIEW_PROJECTIONS, payload: json };
}

export function actGetViewProjections(viewId) {
  return (dispatch) => {
    doGetJson(
      "/api/projections/search/findByViewId?viewId=" + viewId,
      (json) => {
        dispatch(setViewProjections(json));
      },
      (e) => {
        dispatch(actAlarm("load projection is failed"));
        console.log("error on loading data", e);
      }
    );
  };
}

export function actSaveProjection(projection, viewId) {
  return (dispatch) => {
    doPut(
      "/api/projections/" + getHateOasId(projection),
      JSON.stringify(projection),
      (request) => {
        dispatch(actMessage("projection updated"));
        dispatch(actGetViewProjections(viewId));
      },
      (e) => {
        dispatch(actAlarm("projection update failed"));
      }
    );
  };
}
