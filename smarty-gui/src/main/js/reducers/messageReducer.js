import { doFetch } from "../common/tools";

const defaultState = {
  text: null,
  variant: "success",
};

export const MESSAGE = "message";
export const ALARM = "alarm";

export default function messageReducer(state = defaultState, action) {
  switch (action.type) {
    case MESSAGE:
      return { ...state, text: action.text, variant: "success" };
    case ALARM:
      return { ...state, text: action.text, variant: "danger" };

    default:
      return state;
  }
}

export function actMessage(text) {
  return { type: MESSAGE, text: text };
}

export function actAlarm(text) {
  return { type: ALARM, text: text };
}
