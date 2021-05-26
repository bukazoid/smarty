import { createStore, applyMiddleware, combineReducers } from "redux";

import { composeWithDevTools } from "redux-devtools-extension";
import thunk from "redux-thunk";
import addprojectionReducer from "./addProjectionReducer";
import deviceReducer from "./deviceReducer";
import messageReducer from "./messageReducer";
import userReducer from "./userReducer";
import valueReducer from "./valueReducer";
import viewReducer from "./viewReducer";

const rootReducer = combineReducers({
  devices: deviceReducer,
  values: valueReducer,
  users: userReducer,
  message: messageReducer,
  views: viewReducer,
  projections: addprojectionReducer,
});

const composedEnhancer = composeWithDevTools(applyMiddleware(thunk));

export const store = createStore(rootReducer, composedEnhancer);
