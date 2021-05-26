import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.css";
import { useDispatch, useSelector } from "react-redux";
import { useHistory, useParams } from "react-router";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import {
  doGetJson,
  doPost,
  doPut,
  genUrl,
  getHateOasId,
} from "../common/tools";
import { actSaveView } from "../reducers/viewReducer";
import ProjectionAdd from "../component/view/ProjectionAdd";
import ProjectionList from "../component/view/ProjectionList";
import UserList from "../component/view/UserList";
import { loadUser } from "../reducers/userReducer";

export default function ViewAdmin() {
  const dispatch = useDispatch();
  const store = useSelector((state) => state);
  const viewId = useParams().viewId;
  const history = useHistory();

  const [view, setView] = useState();
  const [users2add, setUsers2add] = useState([]);
  const [viewUsers, setViewUsers] = useState([]);
  const [values, setValues] = useState([]);

  const loadUsers = () => {
    // load users
    doGetJson(
      "/api/views/" + viewId + "/users?size=999",
      (json) => {
        const users = json._embedded.users;
        setViewUsers(users);
        console.log("users: ", users.length);
      },
      (e) => {
        dispatch(actAlarm("can't load view's users"));
        console.log("error on loading data", e);
      }
    );
  };

  useEffect(() => {
    // load all users
    doGetJson(
      "/api/susers?size=999",
      (json) => {
        const allUsers = json._embedded.users;
        console.log("allUsers: ", allUsers.length);
        console.log("viewUsers: ", viewUsers.length);
        // trash
        const users2add = allUsers.filter((el) => {
          const u2d = viewUsers.find((du) => {
            console.log(
              "el, du, eq",
              getHateOasId(el),
              getHateOasId(du),
              getHateOasId(el) == getHateOasId(du)
            );
            return getHateOasId(el) == getHateOasId(du);
            //return du._links.self.href == el._links.self.href;
          });
          return !u2d;
        });

        setUsers2add(users2add);
      },
      (e) => {
        dispatch(actAlarm("can't load view's users"));
        console.log("error on loading data", e);
      }
    );
  }, [viewUsers]);

  useEffect(() => {
    if (viewId == "new") {
      setView({ name: "view" });
      return;
    }

    doGetJson(
      "/api/views/" + viewId,
      (json) => {
        setView(json);
      },
      (e) => {
        dispatch(actAlarm("view load is failed"));
        console.log("error on loading data", e);
      }
    );

    loadUsers();

    const url = genUrl("values", 0, 999);
    doGetJson(
      url,
      (json) => {
        setValues(json._embedded.values);
      },
      (e) => {
        dispatch(actAlarm("values(sensors) not loaded"));
        console.log("error on loading data", e);
      }
    );
  }, [viewId]);

  if (!view) {
    return "Loading";
  }

  const handleChange = (field, value) => {
    setView({ ...view, [field]: value });
  };

  const handleSave = () => {
    const id = viewId;
    view.id = id == "new" ? null : id;
    const url = id == "new" ? "/api/views" : "/api/views/" + id;
    const method = id == "new" ? doPost : doPut;

    method(
      url,
      JSON.stringify(view),
      (response) =>
        response.json().then((json) => {
          history.push(history.push("/admin/view/" + getHateOasId(json)));
        }),
      (e) => {
        dispatch(actAlarm("view save is failed"));
        console.log("error on loading data", e);
      }
    );
  };

  const onUsersUpdate = () => {
    loadUsers();
  }

  return (
    <Container>
      <Row>
        <Col>
          <Form.Group controlId="name">
            <Form.Label>title</Form.Label>
            <Form.Control
              type="text"
              name="name"
              defaultValue={view.name}
              onChange={(e) => handleChange(e.target.name, e.target.value)}
            />
          </Form.Group>

          <Button onClick={handleSave}>
            {viewId == "new" ? "Create" : "Save"}
          </Button>
          <hr />
          <UserList
            viewId={viewId}
            users2add={users2add}
            users2delete={viewUsers}
            onUpdate={onUsersUpdate}
          />
        </Col>
        <Col>
          <b>Projections</b>
          <ProjectionList
            projections={store.projections.projections}
            values={values}
            viewId={viewId}
          />
        </Col>
      </Row>
      <Row>
        <Col>
          <hr />
        </Col>
      </Row>

      <ProjectionAdd
        viewId={viewId}
        projections={store.projections.projections}
      />
    </Container>
  );
}
