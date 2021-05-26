import React, { useState } from "react";
import { Link, useHistory, useLocation } from "react-router-dom";

import "bootstrap/dist/css/bootstrap.css";
import { Form } from "react-bootstrap";
import Button from "react-bootstrap/Button";
import { doPost } from "../common/tools";

// A custom hook that builds on useLocation to parse
// the query string for you.
function useQuery() {
  return new URLSearchParams(useLocation().search);
}

export default function Login() {
  const [state, setState] = useState({
    one2login: null,
    pass2login: null,
    ["remember-me"]: true,
  });
  const history = useHistory();

  function handleSubmit(event) {
    event.preventDefault();
    const formData = new FormData();
    formData.append("one2login", state.one2login);
    formData.append("pass2login", state.pass2login);
    formData.append("remember-me", state["remember-me"]);

    doPost(
      "/login",
      formData,
      (response) => {
        console.log("success, ", response);
      },
      (e) => {
        console.log("error", e);
      }
    );
  }

  const onChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    const checked = event.target.checked;
    if (event.target.type == "checkbox") {
      setState({ ...state, [name]: checked });
    } else {
      setState({ ...state, [name]: value });
    }
  };

  return (
    <Form action="/login" method="POST" onSubmit={handleSubmit}>
      <Form.Group controlId="one2login">
        <Form.Label>Login:</Form.Label>
        <Form.Control
          type="text"
          name="one2login"
          defaultValue={state.one2login}
          onChange={onChange}
        />
      </Form.Group>
      <Form.Group controlId="pass2login">
        <Form.Label>Password:</Form.Label>
        <Form.Control
          type="password"
          name="pass2login"
          defaultValue={state.pass2login}
          onChange={onChange}
        />
      </Form.Group>

      <Form.Group controlId="remember-me">
        <Form.Label>remember-me:</Form.Label>
        <Form.Control
          type="checkbox"
          name="remember-me"
          checked={state["remember-me"]}
          onChange={onChange}
        />
      </Form.Group>

      <Button type="submit">Login</Button>
    </Form>
  );
}
