import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.css";
import { useDispatch, useSelector } from "react-redux";
import BootstrapTable from "react-bootstrap-table-next";
import { getHateOasId } from "../../common/tools";
import { Button, Col, Row } from "react-bootstrap";
import paginationFactory from "react-bootstrap-table2-paginator";
import {
  actAttachSensor,
  actDetachSensor,
  actGetViewProjections,
  actLoadAddProjectionDevices,
  actLoadAddProjectionDeviceSensors as actLoadProjectionSensors,
} from "../../reducers/addProjectionReducer";

export default function ProjectionAdd(props) {
  const dispatch = useDispatch();
  const store = useSelector((state) => state);
  const ctx = store.projections;

  if (!ctx.devices || ctx.devices.length == 0) {
    return (
      <Row>
        <Col>
          <Button onClick={() => dispatch(actLoadAddProjectionDevices())}>
            Add Projection
          </Button>
        </Col>
      </Row>
    );
  }

  const genDeviceColumn = (id) => {
    if (ctx.deviceId == id) {
      return <Button variant="secondary">Choosen</Button>;
    }
    return (
      <Button onClick={() => dispatch(actLoadProjectionSensors(id))}>
        Choose
      </Button>
    );
  };

  const getDeviceColumns = () => [
    {
      dataField: "humanName",
      text: "name",
      sort: true,
    },
    {
      dataField: "location",
      text: "location",
      sort: true,
    },
    {
      dataField: "choose",
      text: "choose",
      sort: false,
    },
  ];

  const getDeviceRows = () => {
    if (!ctx.devices) {
      return [];
    }
    return ctx.devices.map((row) => {
      const id = getHateOasId(row);
      return { ...row, id: id, choose: genDeviceColumn(id) };
    });
  };

  const onTableChange = (
    type,
    { page, sizePerPage, filters, sortField, sortOrder, cellEdit }
  ) => {
    dispatch(
      actLoadAddProjectionDevices(page - 1, sizePerPage, sortField, sortOrder)
    );
  };

  const getSensorsRows = () => {
    if (!ctx.sensors) {
      return [];
    }
    return ctx.sensors.map((col) => {
      const obj = { ...col, id: getHateOasId(col) };

      if (isAlreadyAdded(obj.id)) {
        obj.attachBtn = (
          <Button
            variant="secondary"
            onClick={() => dispatch(actDetachSensor(props.viewId, obj.id))}
          >
            Detach
          </Button>
        );
      } else {
        obj.attachBtn = (
          <Button
            onClick={() => dispatch(actAttachSensor(props.viewId, obj.id))}
          >
            Attach
          </Button>
        );
      }

      return obj;
    });
  };

  const isAlreadyAdded = (id) => {
    if (!props.projections) {
      return false;
    }

    return !!props.projections.find((p) => {
      //console.log("id to compare: ", getHateOasId(p.sensor));
      if (p.sensorId == id) {
        return true;
      }
      return false;
    });
  };

  const getSensorsColumns = () => {
    return [
      {
        dataField: "name",
        text: "name",
        sort: true,
      },
      {
        dataField: "type",
        text: "type",
        sort: true,
      },
      {
        dataField: "attachBtn",
        text: "add",
        sort: false,
        //        formatter: addSensorColumn,
      },
    ];
  };

  return (
    <Row style={{ padding: "10px" }}>
      <Col>
        <BootstrapTable
          remote
          bootstrap4
          keyField="id"
          data={getDeviceRows()}
          columns={getDeviceColumns()}
          pagination={paginationFactory({
            page: ctx.devicesPage.number + 1,
            sizePerPage: ctx.devicesPage.size,
            totalSize: ctx.devicesPage.totalElements,
            hideSizePerPage: true,
          })}
          onTableChange={onTableChange}
        />
      </Col>
      <Col>
        <BootstrapTable
          bootstrap4
          keyField="id"
          data={getSensorsRows()}
          columns={getSensorsColumns()}
          pagination={paginationFactory({
            hideSizePerPage: true,
          })}
          defaultSorted={[
            {
              dataField: "name",
              order: "asc",
            },
          ]}
        />
      </Col>
    </Row>
  );
}
