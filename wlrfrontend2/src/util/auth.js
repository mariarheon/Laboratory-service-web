import { req, reqUnauthed } from './my_request';

function isAuthed() {
  return !!localStorage.getItem('token');
}

function auth(login, password) {
  logout();
  return reqUnauthed('/public/user/login', {
    login: login,
    password: password,
  })
  .then(jsono => {
    localStorage.setItem('token', jsono.token);
    return req('/user/current');
  })
  .then(jsono => {
    localStorage.setItem('user', JSON.stringify(jsono));
    return jsono;
  });
}

function logout() {
  localStorage.removeItem('user');
  localStorage.removeItem('token');
}

export { isAuthed, auth, logout };
