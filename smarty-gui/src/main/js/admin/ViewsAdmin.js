import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.css";
import { useDispatch, useSelector } from "react-redux";
import { actLoadDevices } from "../reducers/deviceReducer";
import BootstrapTable from "react-bootstrap-table-next";
import { Link } from "react-router-dom";
import paginationFactory from "react-bootstrap-table2-paginator";
import {
  actDeleteView,
  actLoadUserViews,
  actLoadViews,
  actSaveView,
} from "../reducers/viewReducer";
import { Button } from "react-bootstrap";
import NavbarCollapse from "react-bootstrap/esm/NavbarCollapse";

export default function ViewsAdmin() {
  const dispatch = useDispatch();
  const store = useSelector((state) => state);
  const ctx = store.views;

  useEffect(() => {
    dispatch(actLoadViews());
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

  const cloneView = (view) => {
    const clone = { ...view, id: null };
    let num = 0;
    let found = null;
    do {
      num++;
      clone.name = view.name + num;
      console.log("look for " + clone.name);

      // search or existing view
      found = store.views.list.find((v) => v.name == clone.name);
      console.log("found " + JSON.stringify(found));
    } while (found != null && num < 50);

    dispatch(actSaveView("new", clone));
    setTimeout(() => dispatch(actLoadViews()), 200);
  };

  const deleteColumn = (cell, row, rowIndex, formatExtraData) => {
    return (
      <>
        <Button onClick={() => dispatch(actDeleteView(row.id))}>delete</Button>{" "}
        <Button onClick={() => cloneView(row)}>clone</Button>
      </>
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

      {
        dataField: "delete",
        text: "delete",
        sort: false,
        formatter: deleteColumn,
      },
    ];
  };

  const rows = ctx.list.map((col) => {
    const clone = { ...col };
    clone.createTime = new Date(clone.createTime).toLocaleString("en-GB"); // in en-US day and month mixes

    return clone;
  });

  const onTableChange = (
    type,
    { page, sizePerPage, filters, sortField, sortOrder, cellEdit }
  ) => {
    dispatch(actLoadViews(page - 1, sizePerPage, sortField, sortOrder));
  };

  return (
    <div
      style={{
        margin: "0",
        padding: "0",
      }}
    >
      <BootstrapTable
        remote
        bootstrap4
        keyField="id"
        data={rows}
        columns={getColumns()}
        pagination={paginationFactory({
          page: ctx.page.number + 1,
          sizePerPage: ctx.page.size,
          totalSize: ctx.page.totalElements,
          hideSizePerPage: true,
        })}
        onTableChange={onTableChange}
      />
      <Link
        to={{
          pathname: "./view" + "/" + "new",
        }}
      >
        new
      </Link>
    </div>
  );
}
