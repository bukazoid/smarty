import { doFetch, doGetJson, doPut, genUrl } from "../common/tools";
import { actAlarm, actMessage } from "./messageReducer";

const defaultState = {
  state: null,
  list: [],
  page: {
    size: 10,
  },

  selected: null,
  selectedState: null,
};

export const DEVICES_LOAD_START = "devices/load/start";
export const DEVICES_LOAD_SUCCESS = "devices/load/success";

export const DEVICE_LOAD_START = "device/load/start";
export const DEVICE_LOAD_SUCCESS = "device/load/success";
export const DEVICE_SENSORS_LOAD_SUCCESS = "device/sensors";

const STATE_LOADING = "LOADING";
const STATE_LOADED = "LOAD";

export default function deviceReducer(state = defaultState, action) {
  switch (action.type) {
    case DEVICES_LOAD_START:
      return { ...state, state: STATE_LOADING };
    case DEVICES_LOAD_SUCCESS:
      return {
        ...state,
        state: STATE_LOADED,
        list: action.payload._embedded.devices,
        page: action.payload.page,
      };
    case DEVICE_LOAD_START:
      return { ...state, selectedState: STATE_LOADING };
    case DEVICE_LOAD_SUCCESS:
      return {
        ...state,
        selectedState: STATE_LOADED,
        selected: action.payload,
      };
    case DEVICE_SENSORS_LOAD_SUCCESS:
      //return { ...state, selectedSensors: action.payload._embedded.deviceSensors };
      return { ...state, selectedSensors: action.payload._embedded.sensors };

    default:
      return state;
  }
}

//////////////////////////
//   actions
//////////////////////////
const devicesLoadStart = () => {
  return { type: DEVICES_LOAD_START };
};

const devicesLoadSuccess = (json) => {
  return { type: DEVICES_LOAD_SUCCESS, payload: json };
};

export function actLoadDevices(
  page = 0,
  size = 10,
  sortField = "name",
  sortOrder = "asc"
) {
  return (dispatch) => {
    dispatch(devicesLoadStart());

    const url = genUrl("devices", page, size, sortField, sortOrder);

    doGetJson(
      url,
      (json) => {
        dispatch(actMessage("devices loaded"));
        dispatch(devicesLoadSuccess(json));
      },
      (error) => {
        dispatch(actAlarm("devices load failed"));
        console.log("error on loading data", error);
      }
    );
  };
}

const deviceLoadStart = () => {
  return { type: DEVICE_LOAD_START };
};

export const deviceLoadSuccess = (json) => {
  return { type: DEVICE_LOAD_SUCCESS, payload: json };
};

const deviceSensorsLoadSuccess = (json) => {
  return { type: DEVICE_SENSORS_LOAD_SUCCESS, payload: json };
};

export function actLoadDevice(deviceId) {
  return (dispatch) => {
    //dispatch(deviceLoadStart());
    doGetJson(
      "/api/devices/" + deviceId,
      (json) => {
        dispatch(deviceLoadSuccess(json));
        dispatch(actLoadDeviceSensors(deviceId));
      },
      (e) => {
        dispatch(actAlarm("device load is failed"));
        console.log("error on loading data", e);
      }
    );
  };
}

export function actLoadDeviceSensors(deviceId) {
  return (dispatch) => {
    dispatch(deviceLoadStart());
    doGetJson(
      "/api/devices/" + deviceId + "/sensors",
      (json) => {
        dispatch(deviceSensorsLoadSuccess(json));
      },
      (e) => {
        dispatch(actAlarm("device sensors load is failed"));
        console.log("error on loading data", e);
      }
    );
  };
}

export function actSaveDevice(deviceId, device) {
  return (dispatch) => {
    device.id = deviceId;
    dispatch(deviceLoadStart());

    doPut(
      "/api/devices/" + deviceId,
      JSON.stringify(device),
      (json) => {
        dispatch(actMessage("device is saved"));
        dispatch(deviceLoadSuccess(json));
      },
      (e) => {
        dispatch(actAlarm("device save is failed"));
        console.log("error on loading data", e);
      }
    );
  };
}
