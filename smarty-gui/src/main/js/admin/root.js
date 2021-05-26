import React from "react";
import { Link } from "react-router-dom";

import "bootstrap/dist/css/bootstrap.css";
import { useSelector } from "react-redux";

export default function Root() {
  return (
    <div>
      <Link to="/admin/devices">Devices</Link>
      <br />
      <Link to="/admin/values">Values</Link>
      <br />
      <Link to="/admin/users">Users</Link>
      &nbsp;&nbsp;&nbsp;&nbsp;<Link to="/admin/user/new">New</Link>
      <br />
      <Link to="/admin/views">Views</Link>
      &nbsp;&nbsp;&nbsp;&nbsp;<Link to="/admin/view/new">New</Link>
      <br />
    </div>
  );
}
