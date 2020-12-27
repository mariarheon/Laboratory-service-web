import React, { Component } from 'react';
import { isAuthed, logout } from '../util/auth';
import Login from './Login';
import Register from './Register';
import Admin from './Admin';
import Assistant from './Assistant';
import Client from './Client';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      who: this.getWho(),
    }
  }

  getUser() {
    return JSON.parse(localStorage.getItem('user'));
  }

  getWho() {
    return isAuthed() ? this.getUser().role : null;
  }

  toMain = () => {
    this.setState({
      who: this.getWho(),
    });
  }

  toAuth = () => {
    this.setState({
      who: null,
    })
  }

  handleRegisterClick = () => {
    this.setState({
      who: 'Register',
    });
  }

  handleLogout = () => {
    logout();
    this.setState({
      who: null,
    });
  }

  render() {
    const { who } = this.state;
    let content = null;
    let logoutContent = null;
    if (who === 'ADMIN' || who === 'ASSISTANT' || who === 'CLIENT') {
      let roleFormatted = null;
      if (who === 'ADMIN') {
        content = <Admin />;
        roleFormatted = 'Администратор';
      } else if (who === 'ASSISTANT') {
        content =  <Assistant />;
        roleFormatted = 'Лаборант';
      } else if (who === 'CLIENT') {
        content = <Client />;
        roleFormatted = 'Клиент';
      }
      const user = this.getUser();
      logoutContent = (
        <div>
          {`${user.surname} ${user.name} ${user.patronymic} (${roleFormatted})`}
          &nbsp;&nbsp;&nbsp;
          <input type="button" onClick={this.handleLogout} value="Выйти" />
        </div>
      );
    } else if (who === 'Register') {
      content = <Register toAuth={this.toAuth} />;
    } else {
      content = <Login toMain={this.toMain} handleRegisterClick={this.handleRegisterClick} />;
    }
    return (
      <div>
        {logoutContent}
        {content}
      </div>
    )
  }
}

export default App;
