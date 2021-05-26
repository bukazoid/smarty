import React from "react";
import Device from "./admin/device";
import Devices from "./admin/devices";
import Login from "./admin/login";
import Root from "./admin/root";
import Users from "./admin/users";
import Values from "./admin/values";
import Views from "./admin/views";

// NOT IS USE YET
const Routes = {
  "/": () => <Root />,
  "/login": () => <Login />,
  "/admin/devices": () => <Devices />,
  "/admin/device/:deviceId": () => <Device />,
  "/admin/users": () => <Users />,
  "/admin/user/:userId": () => <User />,
  "/admin/values": () => <Values />,
  "/admin/views": () => <Views />,
};

export default routes;
