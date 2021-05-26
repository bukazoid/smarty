import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.css";
import { useDispatch, useSelector } from "react-redux";
import BootstrapTable from "react-bootstrap-table-next";
import { Link } from "react-router-dom";
import paginationFactory from "react-bootstrap-table2-paginator";
import { actLoadUserViews } from "../reducers/viewReducer";
import { Col, Container, Row } from "react-bootstrap";
import MsgProfileList from "./MsgProfileList";

export default function ViewsUser() {
  const dispatch = useDispatch();
  const store = useSelector((state) => state);
  const ctx = store.views;

  useEffect(() => {
    dispatch(actLoadUserViews());
  }, []);

  const objectColumn = (cell, row, rowIndex, formatExtraData) => {
    return (
      <Link
        to={{
          pathname: "view" + "/" + row.id,
        }}
      >
        {row.name}
      </Link>
    );
  };

  const getColumns = () => {
    return [
      {
        dataField: "name",
        text: "name",
        sort: true,
        formatter: objectColumn,
      },
    ];
  };

  const rows = ctx.list.map((col) => {
    const clone = { ...col };
    clone.createTime = new Date(clone.createTime).toLocaleString("en-GB"); // in en-US day and month mixes

    return clone;
  });

  return (
    <Container>
      <Row>
        <Col>
          <BootstrapTable
            bootstrap4
            keyField="id"
            data={rows}
            columns={getColumns()}
            pagination={paginationFactory({
              sizePerPage: 10,
              hideSizePerPage: true,
            })}
          />
        </Col>
      </Row>
      <Row>
        <Col>
          <MsgProfileList />
        </Col>
      </Row>
    </Container>
  );
}
