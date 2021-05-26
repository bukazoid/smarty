import React from "react";
import { useDispatch, useSelector } from "react-redux";
import BootstrapTable from "react-bootstrap-table-next";
import { Link } from "react-router-dom";
import {
  actDeleteUser,
  actLoadUsers,
  actSaveUser,
} from "../reducers/userReducer";
import { Button } from "react-bootstrap";
import { getHateOasId } from "../common/tools";
import paginationFactory from "react-bootstrap-table2-paginator";
import context from "react-bootstrap/esm/AccordionContext";

export default function Users() {
  const dispatch = useDispatch();
  const store = useSelector((state) => state); // link storage

  if (!store.users.state) {
    dispatch(actLoadUsers());
    return "Loading";
  }

  const userColumn = (cell, row, rowIndex, formatExtraData) => {
    return (
      <Link
        to={{
          pathname: "user" + "/" + row.id,
        }}
      >
        {row.login}
      </Link>
    );
  };

  const deleteColumn = (cell, row, rowIndex, formatExtraData) => {
    return (
      <>
        <Button onClick={() => dispatch(actDeleteUser(row.id))}>delete</Button>{" "}
        <Button onClick={() => cloneUser(row)}>clone</Button>
      </>
    );
  };

  const cloneUser = (user) => {
    const clone = { ...user, id: null };

    delete clone["createTime"];

    let num = 0;
    let found = null;
    do {
      num++;
      clone.login = user.login + num;
      console.log("look for " + clone.login);

      // search or existing user
      found = store.users.list.find((u) => u.login == clone.login);
      console.log("found " + found);
    } while (found != null && num < 50);

    dispatch(actSaveUser("new", clone));
    setTimeout(() => dispatch(actLoadUsers()), 200);
  };

  const columns = [
    {
      dataField: "createTime",
      text: "Create Time",
      sort: true,
    },
    {
      dataField: "login",
      text: "login",
      sort: true,
      formatter: userColumn,
    },
    {
      dataField: "type",
      text: "type",
      sort: true,
    },
    {
      dataField: "delete",
      text: "delete",
      sort: false,
      formatter: deleteColumn,
    },
  ];

  const rows = store.users.list.map((col) => {
    const clone = { ...col };
    clone.createTime = new Date(clone.createTime).toLocaleString("en-GB"); // in en-US day and month mixes

    clone.id = getHateOasId(clone);

    return clone;
  });

  const onTableChange = (
    type,
    { page, sizePerPage, filters, sortField, sortOrder, cellEdit }
  ) => {
    console.log("sortField: ", sortField, sortOrder);
    dispatch(actLoadUsers(page - 1, sizePerPage, sortField, sortOrder));
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
          page: store.users.page.number + 1,
          sizePerPage: store.users.page.size,
          totalSize: store.users.page.totalElements,
        })}
        onTableChange={onTableChange}
      />
      <Link
        to={{
          pathname: "user" + "/" + "new",
        }}
      >
        new
      </Link>
    </div>
  );
}
