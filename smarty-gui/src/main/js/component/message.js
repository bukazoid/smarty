import React from "react";
import ReactDOM from "react-dom";
import { Alert } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";

export default function Message() {
  //const dispatch = useDispatch();
  const store = useSelector((state) => state);
  const ctx = store.message;

  if (!ctx.text) {
    return "";
  }

  return <Alert variant={ctx.variant}>{ctx.text}</Alert>;
}
