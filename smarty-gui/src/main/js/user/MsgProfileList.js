import React, { useEffect, useState } from "react";
import { Button, Row } from "react-bootstrap";
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from "react-bootstrap-table2-paginator";
import { useDispatch } from "react-redux";
import {
  doDelete,
  doGetJson,
  doPost,
  doPut,
  getHateOasId,
  initHateOasIds,
} from "../common/tools";
import { actAlarm, actMessage } from "../reducers/messageReducer";
import {
  actSetViewProfiles,
  actSetViewProjectionProfiles,
} from "../reducers/viewReducer";
import MsgProfileEdit from "./MsgProfileEdit";

export default function MsgProfileList(props) {
  const [profileList, setProfileList] = useState();

  const [profileEdit, setProfileEdit] = useState();

  const [mdsList, setMdsList] = useState();

  const [viewProfiles, setViewProfiles] = useState();
  const [projectionProfiles, setProjectionProfiles] = useState();

  const dispatch = useDispatch();

  const viewId = props.viewId;
  const projectionId = props.projectionId;

  const loadViewProfiles = (viewId) => {
    if (!viewId) {
      setViewProfiles([]);
      dispatch(actSetViewProfiles([]));
      return;
    }
    // get mds profiles by view
    doGetJson(
      "/api/views/" + viewId + "/profiles",
      (json) => {
        const profiles = json._embedded.msgProfiles;
        setViewProfiles(profiles);
        dispatch(actSetViewProfiles(profiles));
      },
      (e) => {
        dispatch(actAlarm("can't get profiles by views"));
      }
    );
  };

  const loadProjectionProfiles = (projectionId) => {
    if (!projectionId) {
      setProjectionProfiles([]);
      dispatch(actSetViewProjectionProfiles([]));
      return;
    }
    // get mds profiles by projection
    doGetJson(
      "/api/projections/" + projectionId + "/profiles",
      (json) => {
        const profiles = json._embedded.msgProfiles;
        setProjectionProfiles(profiles);
        dispatch(actSetViewProjectionProfiles(profiles));
      },
      (e) => {
        dispatch(actAlarm("can't get profiles by projection"));
      }
    );
  };

  useEffect(() => loadViewProfiles(viewId), [viewId]);

  useEffect(() => loadProjectionProfiles(projectionId), [projectionId]);

  const loadProfiles = () => {
    doGetJson("/api/msgProfiles/search/findByCurrentUser", (json) => {
      const profiles = json._embedded.msgProfiles;
      initHateOasIds(profiles);

      setProfileList(profiles);

      setProfileEdit(null);
    });
  };

  useEffect(() => {
    loadProfiles();

    doGetJson("/api/mds/", (json) => {
      setMdsList(json);
    });
  }, []);

  if (!profileList) {
    return "loading";
  }

  if (profileEdit) {
    const onCancel = () => {
      setProfileEdit(null);
    };
    const onSave = (profile) => {
      const action = profile.id ? doPut : doPost;

      const url = profile.id
        ? "/api/msgProfiles/" + profile.id
        : "/api/msgProfiles/";

      action(
        url,
        JSON.stringify(profile),
        (request) => {
          loadProfiles();
        },
        (e) => {
          dispatch(actAlarm("save profile if failed"));
        }
      );
    };

    return (
      <MsgProfileEdit
        profile={profileEdit}
        onSave={onSave}
        onCancel={onCancel}
        mdsList={mdsList}
      />
    );
  }

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
      {
        dataField: "edit",
        text: "edit",
        sort: true,
      },
    ];
  };

  const onAdd = () => {
    setProfileEdit({ name: null, mdsName: null, target: null });
  };

  const onEdit = (profile) => {
    setProfileEdit(profile);
  };

  const onDelete = (profile) => {
    doDelete(
      "/api/msgProfiles/" + profile.id,
      (response) => {
        loadProfiles();
      },
      (e) => {
        dispatch(actAlarm("CAn't delete profile"));
      }
    );
  };

  const onAttachView = (profile) => {
    doPost(
      "/api/views/" + viewId + "/profiles",
      JSON.stringify(profile),
      (r) => {
        dispatch(actMessage("attached succesfully"));
        loadViewProfiles(viewId);
      },
      (e) => dispatch(actAlarm("cannot attach"))
    );
  };

  const onDetachView = (profile) => {
    doDelete(
      "/api/views/" + viewId + "/profiles/" + profile.id,
      (r) => {
        dispatch(actMessage("detached succesfully"));
        loadViewProfiles(viewId);
      },
      (e) => dispatch(actAlarm("cannot detach"))
    );
  };

  const onAttachProjection = (profile) => {
    doPost(
      "/api/projections/" + projectionId + "/profiles",
      JSON.stringify(profile),
      (r) => {
        dispatch(actMessage("attached succesfully"));
        loadProjectionProfiles(projectionId);
      },
      (e) => dispatch(actAlarm("cannot attach"))
    );
  };

  const onDetachProjection = (profile) => {
    doDelete(
      "/api/projections/" + projectionId + "/profiles/" + profile.id,
      (r) => {
        dispatch(actMessage("detached succesfully"));
        loadProjectionProfiles(projectionId);
      },
      (e) => dispatch(actAlarm("cannot detach"))
    );
  };

  const genViewAttachBtn = (p) => {
    if (projectionId) {
      return "";
    }
    const vp = !viewProfiles
      ? null
      : viewProfiles.find((vp) => {
          if (getHateOasId(vp) == getHateOasId(p)) {
            return true;
          }
        });

    return vp ? (
      <Button variant="secondary" onClick={() => onDetachView(p)}>
        Detach(View)
      </Button>
    ) : (
      <Button onClick={() => onAttachView(p)}>Attach(View)</Button>
    );
  };

  const genProjectionAttachBtn = (p) => {
    if (!projectionId) {
      return "";
    }

    const pp = !projectionProfiles
      ? null
      : projectionProfiles.find((pp) => {
          if (getHateOasId(pp) == getHateOasId(p)) {
            return true;
          }
        });

    return pp ? (
      <Button variant="secondary" onClick={() => onDetachProjection(p)}>
        Detach(Projection)
      </Button>
    ) : (
      <Button onClick={() => onAttachProjection(p)}>Attach(Projection)</Button>
    );
  };

  const list = profileList.map((p) => {
    const editBtn = (
      <span>
        <Button onClick={() => onEdit(p)}>Edit</Button>{" "}
        <Button onClick={() => onDelete(p)}>Delete</Button>{" "}
        {genViewAttachBtn(p)} {genProjectionAttachBtn(p)}
      </span>
    );
    return { ...p, edit: editBtn };
  });

  return (
    <fieldset>
      <legend>Messaging profiles</legend>
      <Button style={{ margin: "10px" }} onClick={onAdd}>
        Add
      </Button>
      <BootstrapTable
        bootstrap4
        keyField="id"
        data={list}
        columns={getColumns()}
        pagination={paginationFactory({
          sizePerPage: 10,
          hideSizePerPage: true,
          hideSizePerPage: true,
        })}
      />
    </fieldset>
  );
}
