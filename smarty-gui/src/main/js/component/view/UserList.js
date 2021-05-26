import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.css";
import { useDispatch, useSelector } from "react-redux";
import BootstrapTable from "react-bootstrap-table-next";
import { doDelete, doGet, doPost, getHateOasId } from "../../common/tools";
import { Button, Col, Row } from "react-bootstrap";
import paginationFactory from "react-bootstrap-table2-paginator";

export default function UserList(props) {
  const dispatch = useDispatch();
  const store = useSelector((state) => state);

  if (props.users2delete == null || props.users2add == null) {
    return "";
  }

  const getColumns = () => [
    {
      dataField: "login",
      text: "login",
      sort: true,
    },
    {
      dataField: "type",
      text: "type",
      sort: true,
    },
    {
      dataField: "action",
      text: "action",
      sort: false,
    },
  ];

  const delUser = (userId) => {
    //find user
    const user = props.users2delete.find((user) => {
      const uId = getHateOasId(user);
      return uId == userId;
    });

    if (!user) {
      console.log("user not found");
      return;
    }

    doDelete(
      "/api/views/" + props.viewId + "/users/" + getHateOasId(user),
      () => props.onUpdate(),
      () => console.log("err")
    );
  };

  const addUser = (userId) => {
    //find user
    const user = props.users2add.find((user) => {
      const uId = getHateOasId(user);
      return uId == userId;
    });

    if (!user) {
      console.log("user not found");
      return;
    }

    doPost(
      "/api/views/" + props.viewId + "/users",
      JSON.stringify(user),
      () => props.onUpdate(),
      () => console.log("err")
    );
  };

  const getRows = (users, u2add) => {
    return users.map((col) => {
      const id = getHateOasId(col);

      const row = { ...col, id: id };

      if (u2add) {
        row.action = <Button onClick={() => addUser(row.id)}>ADD</Button>;
      } else {
        row.action = <Button onClick={() => delUser(row.id)}>DELETE</Button>;
      }
      return row;
    });
  };

  return (
    <Row>
      <Col>
        <BootstrapTable
          bootstrap4
          keyField="id"
          data={getRows(props.users2delete, false)}
          columns={getColumns()}
          pagination={paginationFactory({
            page: 1,
            sizePerPage: 10,
            totalSize: props.users2delete.length,
          })}
          defaultSorted={[
            {
              dataField: "login",
              order: "asc",
            },
          ]}
        />
      </Col>
      <Col>
        <BootstrapTable
          bootstrap4
          keyField="id"
          data={getRows(props.users2add, true)}
          columns={getColumns()}
          pagination={paginationFactory({
            page: 1,
            sizePerPage: 10,
            totalSize: props.users2add.length,
            hideSizePerPage: true,
          })}
          defaultSorted={[
            {
              dataField: "login",
              order: "asc",
            },
          ]}
        />
      </Col>
    </Row>
  );
}
