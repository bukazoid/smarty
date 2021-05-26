import React from "react";
import { useDispatch, useSelector } from "react-redux";
import BootstrapTable from "react-bootstrap-table-next";
import { actRegister, actUnregister } from "../reducers/valueReducer";
import { actLoadDeviceSensors } from "../reducers/deviceReducer";

export default function SensorList(props) {
  const dispatch = useDispatch();
  const store = useSelector((state) => state); // link storage
  if (!props.list) {
    return "";
  }

  const monitorColumn = (cell, row, rowIndex, formatExtraData) => {
    if (row.monitored) {
      return (
        <button
          type="button"
          className="btn btn-secondary"
          onClick={() =>
            dispatch(
              actUnregister(row.id, actLoadDeviceSensors(props.deviceId))
            )
          }
        >
          Stop monitoring
        </button>
      );
    }

    return (
      <button
        type="button"
        className="btn btn-primary"
        onClick={() =>
          dispatch(actRegister(row.id, actLoadDeviceSensors(props.deviceId)))
        }
      >
        Monitor it
      </button>
    );
  };

  const columns = [
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
  ];

  const rows = props.list.map((col) => {
    const clone = { ...col };
    // find id
    const splits = clone._links.self.href.split("/");
    clone.id = splits[splits.length - 1];

    return clone;
  });

  return (
    <BootstrapTable bootstrap4 keyField="id" data={rows} columns={columns} defaultSorted={[{
      dataField: 'name',
      order: 'asc'}]} />
  );
}
