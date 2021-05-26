import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.css";
import { getHateOasId } from "../../common/tools";
import { useSelector } from "react-redux";
import { Button, Col, Form } from "react-bootstrap";
import BootstrapTable from "react-bootstrap-table-next";
import ViewProjections from "./ViewProjections";

export default function ProjectionEdit(props) {
  const [projection, updateProjection] = useState({ projection: null });
  const store = useSelector((state) => state);

  if (!props.projection) {
    return "";
  }

  const sId = getHateOasId(projection);
  const pId = getHateOasId(props.projection);
  // update projection is required

  if (sId != pId) {
    updateProjection(props.projection);
    return "";
  }

  const handleChange = (field, value) => {
    updateProjection({ ...projection, [field]: value });
  };

  return (
    <Col>
      <Form.Group controlId="name">
        <Form.Label>name</Form.Label>
        <Form.Control
          type="text"
          name="name"
          defaultValue={projection.name}
          onChange={(e) => handleChange(e.target.name, e.target.value)}
        />
      </Form.Group>

      <Form.Group controlId="minValue">
        <Form.Label>minValue</Form.Label>
        <Form.Control
          type="number"
          name="minValue"
          defaultValue={projection.minValue}
          onChange={(e) => handleChange(e.target.name, e.target.value)}
        />
      </Form.Group>

      <Form.Group controlId="maxValue">
        <Form.Label>maxValue</Form.Label>
        <Form.Control
          type="number"
          name="maxValue"
          defaultValue={projection.maxValue}
          onChange={(e) => handleChange(e.target.name, e.target.value)}
        />
      </Form.Group>

      <Button onClick={() => props.onSave(projection)}>Save</Button>

      <fieldset>
        <legend>Profiles</legend>
        <ViewProjections profiles={store.views.projectionProfiles} />
      </fieldset>
    </Col>
  );
}
