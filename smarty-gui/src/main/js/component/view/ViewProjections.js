import React, { useEffect, useState } from "react";
import BootstrapTable from "react-bootstrap-table-next";

export default function ViewProjections(props) {
  const profiles = props.profiles;
  if (!profiles || profiles.length == 0) {
    return "no message projection";
  }

  const getRows = () => {
    return profiles;
  };

  const getColumns = () => {
    return [
      {
        dataField: "name",
        text: "name",
        sort: true,
      },
      {
        dataField: "mdsName",
        text: "mdsName",
        sort: true,
      },
      // delete button can be here
      // {
      //   dataField: "edit",
      //   text: "edit",
      //   sort: true,
      // },
    ];
  };

  // table
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
