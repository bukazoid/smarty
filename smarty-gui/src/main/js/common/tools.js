
const JSON_TYPE = "application/json";

export const handleApiError = (response) => {
  if (response.redirected) {
    console.log("is redirected:", response.redirected);
    console.log("response.url", response.url);
    window.location.href = response.url;
    return [];
  }

  if (response.ok) return response;

  console.log("response not ok, status: ", response.status);
  switch (response.status) {
    case 401:
      //      removeToken();
      //      window.location.href = APP_BASE_URL;
      throw Promise.resolve("Error.unauthorized-user");
    case 403:
      console.log("Client Error: " + response.statusText);
      throw Promise.resolve("Error.invalid-pass-or-login");
    case 404:
      console.log("Client Error: " + response.statusText);
      throw Promise.resolve("Error.resource-not-found");
    case 500:
      console.log("Server Error: " + response.statusText);
      throw Promise.resolve("Error.internal-server-error");
    case 503:
    case 504:
      console.log("Server Error: " + response.statusText);
      throw Promise.resolve("Error.server-not-available");
    default:
      throw response.text();
  }
};

/**
 * supports upload too
 */
export const doFetch = (url, method, data) => {
  const params = {
    method: method,
    body: data,
    headers: {
      "Content-Type": JSON_TYPE,
    },
    //    redirect: "manual",
  };
  if (data) params.body = data;
  if (data instanceof FormData) delete params.headers["Content-Type"];

  return fetch(url, params);
};

export const doFetchCallback = (url, method, body, success, fail) => {
  doFetch(url, method, body)
    .then(handleApiError)
    .then((response) => {
      success(response);
    })
    .catch((error) => {
      if (error instanceof Promise) {
        error.then((errorText) => {
          fail(errorText);
        });
      } else {
        // exception
        console.error("unknown error: ", error);
        fail(error);
      }
    });
};

export const genUrl = (
  base,
  page = 0,
  size = 10,
  sortField = "name",
  sortOrder = "asc"
) => {
  return (
    "/api/" +
    base +
    "?page=" +
    page +
    "&size=" +
    size +
    "&sort=" +
    sortField +
    "," +
    sortOrder
  );
};

//no body here
export const doDelete = (url, success, fail) =>
  doFetchCallback(url, "DELETE", null, success, fail);

export const doGet = (url, success, fail) =>
  doFetchCallback(url, "GET", null, success, fail);
export const doPut = (url, body, success, fail) =>
  doFetchCallback(url, "PUT", body, success, fail);
export const doPost = (url, body, success, fail) =>
  doFetchCallback(url, "POST", body, success, fail);

export const doGetJson = (url, success, fail) => {
  doGet(
    url,
    (response) => response.json().then((json) => success(json)),
    (error) => fail(error)
  );
};

export const getHateOasId = (object) => {
  if (!object || !object._links) {
    return null;
  }
  const splits = object._links.self.href.split("/");
  return splits[splits.length - 1];
};

export const initHateOasId = (object) => {
  const id = getHateOasId(object);
  if (id != null) {
    object.id = id;
  }
  return object;
};

export const initHateOasIds = (array) => {
  if (!array) {
    return;
  }
  array.forEach((element) => {
    initHateOasId(element);
  });

  return array;
};