import React, { Component } from 'react';
import { reqUnauthed } from '../util/my_request';

class Register extends Component {
  constructor(props) {
    super(props);
    this.state = {
      login: '',
      surname: '',
      name: '',
      patronymic: '',
      sex: 'FEMALE',
      passportSeries: '',
      passportNumber: '',
      phone: '',
      role: 'CLIENT',
      pass: '',
      passConfirmed: '',
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
    const {
      login, surname, name, patronymic, sex,
      passportSeries, passportNumber, phone, role,
      pass, passConfirmed
    } = this.state;
    const { toAuth } = this.props;

    return reqUnauthed('/public/user/register', {
      login: login,
      surname: surname,
      name: name,
      patronymic: patronymic,
      sex: sex,
      passportSeries: passportSeries,
      passportNumber: passportNumber,
      phone: phone,
      role: role,
      pass: pass,
      passConfirmed: passConfirmed,
    })
    .then(() => {
      toAuth();
    })
    .catch(ex => {
      this.setState({
        er: ex.message,
      });
    });
  }

  render() {
    const {
      er, login, surname, name, patronymic,
      sex, passportSeries, passportNumber, phone, role,
      pass, passConfirmed
    } = this.state;
    const { toAuth } = this.props;
    return (
      <div>
        <h1>Регистрация</h1>
        <form onSubmit={this.handleSubmit}>
          {er || <div style={{ color: 'red' }}>{er}</div>}
          <div>
            <label>Логин:
                <input value={login}  onChange={this.handleChange} name="login" type="text" placeholder="логин" />
            </label>
          </div>
          <div>
            <label >Фамилия:
                <input value={surname}  onChange={this.handleChange} name="surname" type="text" placeholder="фамилия" />
            </label>
          </div>
          <div>
            <label >Имя:
                <input value={name}  onChange={this.handleChange} name="name" type="text" placeholder="имя" />
            </label>
          </div>
          <div>
            <label >Отчество:
                <input value={patronymic}  onChange={this.handleChange} name="patronymic" type="text" placeholder="отчество" />
            </label>
          </div>
          <div>
            <label >Пол:
                <select value={sex} onChange={this.handleChange} name="sex">
                <option value="FEMALE">Женский</option>
                <option value="MALE">Мужской</option>
              </select>
            </label>
          </div>
          <div>
            <label>Паспорт, серия:
                <input value={passportSeries} onChange={this.handleChange} name="passportSeries" type="text" placeholder="000000" />
            </label>
          </div>
          <div>
            <label >Паспорт, номер:
                <input value={passportNumber} onChange={this.handleChange} name="passportNumber" type="text" placeholder="0000" />
            </label>
          </div>
          <div>
            <label >Телефон:
                <input value={phone} onChange={this.handleChange} name="phone" type="text" placeholder="телефон" />
            </label>
          </div>
          <div>
            <label >Роль:
              <select value={role} onChange={this.handleChange} name="role">
                <option value="ADMIN">Администратор</option>
                <option value="ASSISTANT">Лаборант</option>
                <option value="CLIENT">Клиент</option>
              </select>
            </label>
          </div>
          <div>
            <label >Пароль:
                <input value={pass} onChange={this.handleChange} name="pass" type="password" placeholder="пароль" />
            </label>
          </div>
          <div>
            <label >Повторите пароль:
                <input value={passConfirmed} onChange={this.handleChange} name="passConfirmed" type="password" placeholder="пароль" />
            </label>
          </div>
          <div>
            <input type="submit" value="Создать аккаунт" />
          </div>
          <div>
            <input type="button" onClick={toAuth} value="Назад" />
          </div>
        </form>
      </div>
    );
  }
}

export default Register;