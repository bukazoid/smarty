import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.css";
import { useDispatch, useSelector } from "react-redux";
import { useHistory, useParams } from "react-router";
import { Form } from "react-bootstrap";
import { actNewUser, actSaveUser, loadUser } from "../reducers/userReducer";
import { getHateOasId } from "../common/tools";

export default function User() {
  const dispatch = useDispatch();
  const store = useSelector((state) => state);
  const [state, setState] = useState(null);
  const userId = useParams().userId;
  const history = useHistory();

  console.log("userId", userId);

  const selectedId = getHateOasId(store.users.selected);

  useEffect(() => {
    if (selectedId != null && selectedId != userId) {
      console.log("new user created, navigate to new url");
      history.push("/admin/user/" + selectedId);
    }
    if (!state) {
      if (userId == "new") {
        setState("CREATING");
        dispatch(actNewUser());
      } else {
        setState("LOADING");
        dispatch(loadUser(userId));
      }
    }
  });

  if (!store.users.selected) {
    return "Loading";
  }

  const selected = store.users.selected;

  const handleChange = (field, value) => {
    selected[field] = value;
  };

  const handleSave = () => {
    dispatch(actSaveUser(userId, selected));
  };

  return (
    <div>
      <Form.Group controlId="login">
        <Form.Label>login</Form.Label>
        <Form.Control
          type="text"
          name="login"
          defaultValue={selected.login}
          disabled={userId != "new"}
          onChange={(e) => handleChange(e.target.name, e.target.value)}
        />
      </Form.Group>

      <Form.Group controlId="type">
        <Form.Label>type</Form.Label>
        <Form.Control
          as="select"
          name="type"
          defaultValue={selected.type}
          disabled={userId != "new"}
          onChange={(e) => handleChange(e.target.name, e.target.value)}
        >
          <option>ADMIN</option>
          <option>USER</option>
        </Form.Control>
      </Form.Group>

      <Form.Group controlId="password">
        <Form.Label>password</Form.Label>
        <Form.Control
          type="password"
          name="password"
          defaultValue={selected.password}
          onChange={(e) => handleChange(e.target.name, e.target.value)}
        ></Form.Control>
      </Form.Group>
      

      <button type="button" className="btn btn-primary" onClick={handleSave}>
        {userId == "new" ? "Create" : "Save"}
      </button>
    </div>
  );
}
