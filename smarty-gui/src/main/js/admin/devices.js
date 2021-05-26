import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.css";
import { useDispatch, useSelector } from "react-redux";
import { actLoadDevices } from "../reducers/deviceReducer";
import BootstrapTable from "react-bootstrap-table-next";
import { Link } from "react-router-dom";
import paginationFactory from "react-bootstrap-table2-paginator";
import { getHateOasId } from "../common/tools";

export default function Devices() {
  const dispatch = useDispatch();
  const store = useSelector((state) => state);

  const ctx = store.devices;

  useEffect(() => {
    if (!ctx.state) {
      dispatch(actLoadDevices());
    }
  });

  const deviceColumn = (cell, row, rowIndex, formatExtraData) => {
    return (
      <Link
        to={{
          pathname: "device" + "/" + row.id,
        }}
      >
        {row.humanName}
      </Link>
    );
  };

  const columns = [
    {
      dataField: "createTime",
      text: "Create Time",
      sort: true,
    },
    {
      dataField: "provider",
      text: "provider",
      sort: true,
    },
    {
      dataField: "name",
      text: "name",
      sort: true,
    },
    {
      dataField: "humanName",
      text: "humanName",
      sort: true,
      formatter: deviceColumn,
    },
    {
      dataField: "location",
      text: "location",
      sort: true,
    },
  ];

  const rows = ctx.list.map((col) => {
    const clone = { ...col };
    clone.createTime = new Date(clone.createTime).toLocaleString("en-GB"); // in en-US day and month mixes

    clone.id = getHateOasId(clone);

    return clone;
  });

  const onTableChange = (
    type,
    { page, sizePerPage, filters, sortField, sortOrder, cellEdit }
  ) => {
    dispatch(actLoadDevices(page - 1, sizePerPage, sortField, sortOrder));
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
        columns={columns}
        pagination={paginationFactory({
          page: ctx.page.number + 1,
          sizePerPage: ctx.page.size,
          totalSize: ctx.page.totalElements,
        })}
        onTableChange={onTableChange}
      />
      <Link
        to={{
          pathname: "device" + "/" + "new",
        }}
      >
        new
      </Link>
    </div>
  );
}
