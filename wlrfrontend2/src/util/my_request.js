const urlStart = 'http://localhost:3553';

function reqAny(url, requestBody, token) {
  const otherParams = {};
  const headers = {};
  if (token) {
    headers['Authorization'] = 'Bearer ' + token;
  }
  if (requestBody !== undefined) {
    headers["content-type"] = "application/json; charset=UTF-8";
    otherParams.body = JSON.stringify(requestBody);
    otherParams.method = 'POST';
  } else {
    otherParams.method = 'GET';
  }
  otherParams.headers = headers;
  return fetch(urlStart + url, otherParams)
  .catch(ex => {
    throw new Error('Сервер выключен')
  })
  .then(resp => {
    if (resp.status === 200) {
      return resp.json();
    }
    console.log('Статус: ' + resp.status);
    if (resp.status === 401) {
      throw new Error('Неавторизованный запрос');
    }
    throw new Error('Внутренняя ошибка сервера');
  })
  .then(res => {
    console.log(res);
    if (res.er) {
      throw new Error(res.er);
    }
    return res;
  });
}

function reqUnauthed(url, requestBody) {
  return reqAny(url, requestBody);
}

function req(url, requestBody) {
  return reqAny(url, requestBody, localStorage.getItem('token'));
}

export { reqUnauthed, req };
