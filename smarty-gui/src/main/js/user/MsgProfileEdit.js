import React, { useEffect, useState } from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import { getHateOasId } from "../common/tools";

export default function MsgProfileEdit(props) {
  const [profile, updateProfile] = useState({
    ...props.profile,
  });

  if (!props.profile) {
    return "no profile to edit";
  }

  if (!props.mdsList) {
    return "no MDS available";
  }

  if (!profile.mdsName) {
    updateProfile({ ...profile, mdsName: props.mdsList[0] });
  }

  const handleChange = (field, value) => {
    console.log(field, "  - ", value);
    updateProfile({ ...profile, [field]: value });
  };

  const mdsOptions = [];
  props.mdsList.map((mds) => {
    mdsOptions.push(
      <option id={mds} key={mds} value={mds}>
        {mds}
      </option>
    );
  });

  return (
    <fieldset>
      <legend>Edit Messaging profile</legend>
      <Container>
        <Form.Group controlId="name">
          <Form.Label>name</Form.Label>
          <Form.Control
            type="text"
            name="name"
            defaultValue={profile.name}
            onChange={(e) => handleChange(e.target.name, e.target.value)}
          />
        </Form.Group>

        <Form.Group controlId="mdsName">
          <Form.Label>MDS Name</Form.Label>
          <Form.Control
            as="select"
            name="mdsName"
            defaultValue={profile.mdsName}
            onChange={(e) => handleChange(e.target.name, e.target.value)}
          >
            {mdsOptions}
          </Form.Control>
        </Form.Group>

        <Form.Group controlId="target">
          <Form.Label>destination</Form.Label>
          <Form.Control
            type="text"
            name="target"
            defaultValue={profile.target}
            onChange={(e) => handleChange(e.target.name, e.target.value)}
          />
        </Form.Group>

        <Row>
          <Col>
            <Button onClick={() => props.onCancel()} variant="secondary">
              Cancel
            </Button>
          </Col>
          <Col>
            <Button onClick={() => props.onSave(profile)}>
              {profile.id ? "Save" : "Create"}
            </Button>
          </Col>
        </Row>
      </Container>
    </fieldset>
  );
}
