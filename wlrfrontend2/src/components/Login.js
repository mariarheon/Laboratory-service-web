import React, { Component } from 'react';
import { auth } from '../util/auth';

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      login: '',
      password: '',
      er: '',
    };
  }

  handleChange = ev => {
    const { target } = ev;
    const name = target.name;
    const value = target.value;
    this.setState({
      [name]: value,
    });
  }

  handleSubmit = ev => {
    ev.preventDefault();
    const { login, password } = this.state;
    const { toMain } = this.props;
    auth(login, password)
      .then(user => {
        toMain();
      })
      .catch(ex => {
        this.setState({
          er: ex.message,
        });
      });
  }

  render() {
    const { er, login, password } = this.state;
    const { handleRegisterClick } = this.props;
    return (
      <div>
        <h1>Авторизация</h1>
        <form onSubmit={this.handleSubmit}>
          {er && <div style={{color: 'red'}}>{er}</div>}
          <div>
            <label>Логин:
              <input type="text" name="login" value={login} placeholder="логин" onChange={this.handleChange} />
            </label>
          </div>
          <div>
            <label>Пароль:
              <input type="password" name="password" value={password} placeholder="пароль" onChange={this.handleChange} />
            </label>
          </div>
          <div>
            <input type="submit" value="Войти" />
          </div>
        </form>
        <div>
          <input type="button" value="Нет аккаунта? Зарегистрироваться!" onClick={handleRegisterClick}/>
        </div>
      </div>
    );
  }
}

export default Login;