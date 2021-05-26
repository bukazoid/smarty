import React from "react";
import { useDispatch, useSelector } from "react-redux";
import BootstrapTable from "react-bootstrap-table-next";
import {
  actRegister,
  actUnregister,
  loadValues,
} from "../reducers/valueReducer";
import { Link } from "react-router-dom";
import paginationFactory from "react-bootstrap-table2-paginator";

export default function Values() {
  const dispatch = useDispatch();
  const store = useSelector((state) => state); // link storage

  const ctx = store.values;

  if (!ctx.state) {
    dispatch(loadValues());
    return "Loading";
  }

  const deviceColumn = (cell, row, rowIndex, formatExtraData) => {
    return (
      <Link
        to={{
          pathname: "device" + "/" + row.deviceId,
        }}
      >
        {row.deviceHumanName}
      </Link>
    );
  };

  const monitorColumn = (cell, row, rowIndex, formatExtraData) => {
    if (row.monitored) {
      return (
        <button
          type="button"
          className="btn btn-secondary"
          onClick={() => dispatch(actUnregister(row.id, loadValues()))}
        >
          Stop monitoring
        </button>
      );
    }

    return (
      <button
        type="button"
        className="btn btn-primary"
        onClick={() => dispatch(actRegister(row.id, loadValues()))}
      >
        Monitor it
      </button>
    );
    //return <Button onClick={() => console.log("clicked!")}>REGISTER</Button>;
  };

  const columns = [
    {
      dataField: "deviceName",
      text: "device",
      sort: true,
      formatter: deviceColumn,
    },
    {
      dataField: "name",
      text: "name",
      sort: true,
    },
    {
      text: "monitoring",
      dataField: "id", // need to put something here to avout console's error
      sort: false,
      formatter: monitorColumn,
    },
    {
      dataField: "type",
      text: "type",
      sort: true,
    },
    {
      dataField: "timestampt",
      text: "timestampt",
      sort: true,
    },
    {
      dataField: "value",
      text: "value",
      sort: true,
    },
  ];

  const values = store.values.list.map((col) => {
    const clone = { ...col };
    clone.timestampt = new Date(clone.timestampt).toLocaleString("en-GB"); // in en-US day and month mixes

    // find id
    const splits = clone._links.self.href.split("/");
    clone.id = splits[splits.length - 1];

    return clone;
  });

  const onTableChange = (
    type,
    { page, sizePerPage, filters, sortField, sortOrder, cellEdit }
  ) => {
    dispatch(loadValues(page - 1, sizePerPage, sortField, sortOrder));
  };

  return (
    <BootstrapTable
      remote
      bootstrap4
      keyField="id"
      data={values}
      columns={columns}
      pagination={paginationFactory({
        page: ctx.page.number + 1,
        sizePerPage: ctx.page.size,
        totalSize: ctx.page.totalElements,
      })}
      onTableChange={onTableChange}
    />
  );
}
