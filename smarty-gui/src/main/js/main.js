import "bootstrap/dist/css/bootstrap.css";
import "react-bootstrap-table-next/dist/react-bootstrap-table2.min.css";

import React from "react";
import { Switch, Route, Router, BrowserRouter } from "react-router-dom";

import Root from "./admin/root";
import Devices from "./admin/devices";
import Values from "./admin/values";
import NotFound from "./admin/notfound";
import Device from "./admin/device";
import Login from "./admin/login";
import Users from "./admin/users";
import User from "./admin/user";
import Message from "./component/message";
import Views from "./admin/views";
import View from "./admin/view";
import ViewsUser from "./user/ViewsUser";
import ViewUser from "./user/ViewUser";

const Main = (props) => {
  return (
    <center>
      <a href="/">root</a>
      <br />
      <a href="/api">repo API</a>
      <br />
      <a href="/logout">logout</a>
      <hr />
      <div style={{ width: "70%" }}>
        <Message />
        <Switch>
          <Route exact path="/" component={Root} />
          <Route path="/login" component={Login} />
          <Route path="/admin/devices" component={Devices} />
          <Route path="/admin/device/:deviceId" component={Device} />
          <Route path="/admin/values" component={Values} />
          <Route path="/admin/users" component={Users} />
          <Route path="/admin/user/:userId" component={User} />
          <Route path="/admin/views" component={Views} />
          <Route path="/admin/view/:viewId" component={View} />

          <Route path="/user/views" component={ViewsUser} />
          <Route path="/user/view/:viewId" component={ViewUser} />

          <Route component={NotFound} />
        </Switch>
      </div>
    </center>
  );
};

export default Main;
