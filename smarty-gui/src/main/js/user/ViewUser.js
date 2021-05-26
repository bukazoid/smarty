import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.css";
import { useDispatch, useSelector } from "react-redux";
import { useHistory, useParams } from "react-router";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import { doGetJson, getHateOasId } from "../common/tools";
import { actLoadView, actSaveView } from "../reducers/viewReducer";
import ProjectionList from "../component/view/ProjectionList";
import { loadValues } from "../reducers/valueReducer";
import MsgProfileList from "./MsgProfileList";
import BootstrapTable from "react-bootstrap-table-next";
import ViewProjections from "../component/view/ViewProjections";

export default function ViewUser() {
  const dispatch = useDispatch();
  const store = useSelector((state) => state);
  const viewId = useParams().viewId;
  const history = useHistory();

  const [editProjection, setEditProjection] = useState();

  //console.log("viewId", viewId);
  const ctx = store.views;
  const selected = ctx.selected;
  const selectedId = getHateOasId(selected);

  //console.log("selectedId: ", selectedId);

  useEffect(() => {
    dispatch(actLoadView(viewId));
    dispatch(loadValues(0, 999 /* all */));
  }, []);

  if (!selected) {
    return "Loading";
  }

  const handleChange = (field, value) => {
    selected[field] = value;
  };

  const handleSave = () => {
    dispatch(actSaveView(viewId, selected));
  };

  const onEditProjection = (projectionId) => {
    console.log("onEditProjection: ", projectionId);
    setEditProjection(projectionId);
  };

  return (
    <Container>
      <Row>
        <Col>
          <fieldset>
            <legend>View</legend>
            <Form.Group controlId="name">
              <Form.Control
                type="text"
                name="name"
                defaultValue={selected.name}
                onChange={(e) => handleChange(e.target.name, e.target.value)}
              />
            </Form.Group>

            <Button onClick={handleSave}>
              {viewId == "new" ? "Create" : "Save"}
            </Button>
          </fieldset>
          <fieldset>
            <legend>Profiles</legend>
            <ViewProjections profiles={store.views.viewProfiles} />
          </fieldset>
        </Col>
        <Col>
          <fieldset>
            <legend>Projections</legend>
            <ProjectionList
              projections={store.projections.projections}
              values={store.values.list}
              viewId={viewId}
              onEditProjection={onEditProjection}
            />
          </fieldset>
        </Col>
      </Row>
      <Row>
        <Col>
          <MsgProfileList viewId={viewId} projectionId={editProjection} />
        </Col>
      </Row>
    </Container>
  );
}
