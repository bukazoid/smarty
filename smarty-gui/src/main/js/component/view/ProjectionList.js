import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.css";
import { useDispatch, useSelector } from "react-redux";
import BootstrapTable from "react-bootstrap-table-next";
import { getHateOasId } from "../../common/tools";
import { Button, Col, Row } from "react-bootstrap";
import {
  actGetViewProjections,
  actSaveProjection,
} from "../../reducers/addProjectionReducer";
import ProjectionEdit from "./ProjectionEdit";

/**
 * XXX: don't like props.onEditProjection function, maybe redux or something will be better
 */
export default function ProjectionList(props) {
  const dispatch = useDispatch();
  const [state, setState] = useState({ editProjection: null });
  const store = useSelector((state) => state);
  const pLength = props.projections?.length || 0;

  useEffect(() => {
    if (props.viewId != "new") {
      dispatch(actGetViewProjections(props.viewId));
    }
  }, []);

  if (pLength == 0) {
    return "";
  }

  if (state.editProjection) {
    const onSave = (projection) => {
      if (!projection.minValue) {
        projection.minValue = null;
      }
      if (!projection.maxValue) {
        projection.maxValue = null;
      }

      dispatch(actSaveProjection(projection, props.viewId));
      setState({ ...state, editProjection: null });
      props.onEditProjection(null);
    };

    return <ProjectionEdit projection={state.editProjection} onSave={onSave} />;
  }

  const getColumns = () => [
    {
      dataField: "name",
      text: "name",
      sort: true,
    },
    {
      dataField: "minValue",
      text: "min",
      sort: true,
    },
    {
      dataField: "maxValue",
      text: "max",
      sort: true,
    },
    {
      dataField: "value",
      text: "value",
      sort: true,
    },
    {
      dataField: "edit",
      text: "edit",
      sort: false,
    },
  ];

  const toFixed = (value) => {
    if (!value) {
      return value;
    }
    return value.toFixed(2);
  };

  const coloredValue = (value, min, max) => {
    if (min && value < min) {
      return <font color="red">{value}</font>;
    }
    if (max && value > max) {
      return <font color="red">{value}</font>;
    }
    return value;
  };

  const getValue = (sensorId) => {
    if (!props.values) {
      return "-";
    }

    const val = props.values.find((v) => {
      if (v.sensorId == sensorId) {
        return true;
      }
      return false;
    });
    if (!val) {
      return "---";
    }
    const value = val.value;
    if (value == Math.round(value)) {
      return value;
    }
    return value.toFixed(2);
  };

  const getRows = () => {
    if (!props.projections) {
      return [];
    }
    return props.projections.map((col) => {
      const id = getHateOasId(col);
      const editBtn = (
        <Button
          onClick={() => {
            setState({ ...state, editProjection: col });
            props.onEditProjection(id);
          }}
        >
          Edit
        </Button>
      );
      const value = getValue(col.sensorId);

      return {
        ...col,
        id: id,
        value: coloredValue(value, col.minValue, col.maxValue),
        minValue: toFixed(col.minValue),
        maxValue: toFixed(col.maxValue),
        edit: editBtn,
      };
    });
  };

  return (
    <BootstrapTable
      bootstrap4
      keyField="id"
      data={getRows()}
      columns={getColumns()}
      defaultSorted={[
        {
          dataField: "name",
          order: "asc",
        },
      ]}
    />
  );
}
