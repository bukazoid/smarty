import React from "react";
import ReactDOM from "react-dom";

import { BrowserRouter } from "react-router-dom";
import Main from "./main";
import "bootstrap/dist/css/bootstrap.css";

import { Provider } from "react-redux";
import { store } from "./reducers/store";

ReactDOM.render(
  <Provider store={store}>
    <BrowserRouter>
      <Main />
    </BrowserRouter>
  </Provider>,
  document.getElementById("react")
);
