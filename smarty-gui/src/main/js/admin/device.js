import React, { useEffect } from "react";
import "bootstrap/dist/css/bootstrap.css";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router";
import { actSaveDevice, actLoadDevice, deviceLoadSuccess } from "../reducers/deviceReducer";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import SensorList from "../component/SensorList";
import context from "react-bootstrap/esm/AccordionContext";
import { getHateOasId } from "../common/tools";

export default function Device() {
  const dispatch = useDispatch();
  const store = useSelector((state) => state);
  const ctx = store.devices;
  const { deviceId } = useParams();

  const curId = getHateOasId(store.devices.selected);
  useEffect(() => {
    if (!store.devices.selectedState || deviceId != curId) {
      dispatch(actLoadDevice(deviceId));
    }
  });

  if (!store.devices.selected) {
    return "Loading";
  }

  const selected = ctx.selected;

  const handleChange = (field, value) => {
    const clone = { ...ctx.selected };
    clone[field] = value;

    dispatch(deviceLoadSuccess(clone));
  };

  const handleSave = () => {
    dispatch(actSaveDevice(deviceId, ctx.selected));
  };

  console.log("human name: ", selected.humanName);

  return (
    <Container>
      <Row>
        <Col>
          <Form.Group controlId="provider">
            <Form.Label>provider</Form.Label>
            <Form.Control
              type="text"
              name="provider"
              defaultValue={selected.provider}
              disabled
              onChange={(e) => handleChange(e.target.name, e.target.value)}
            />
          </Form.Group>

          <Form.Group controlId="name">
            <Form.Label>name</Form.Label>
            <Form.Control
              type="text"
              name="name"
              defaultValue={selected.name}
              disabled
              onChange={(e) => handleChange(e.target.name, e.target.value)}
            />
          </Form.Group>

          <Form.Group controlId="humanName">
            <Form.Label>humanName</Form.Label>
            <Form.Control
              type="text"
              name="humanName"
              defaultValue={selected.humanName}
              onChange={(e) => handleChange(e.target.name, e.target.value)}
            />
          </Form.Group>

          <Form.Group controlId="location">
            <Form.Label>location</Form.Label>
            <Form.Control
              type="text"
              name="location"
              defaultValue={selected.location}
              onChange={(e) => handleChange(e.target.name, e.target.value)}
            />
          </Form.Group>

          <Button onClick={handleSave}>Save</Button>
        </Col>
        <Col>
          <SensorList list={ctx.selectedSensors} deviceId={deviceId} />
        </Col>
      </Row>
    </Container>
  );
}
