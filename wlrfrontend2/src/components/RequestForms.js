import React, { Component, Fragment } from 'react';
import { req } from '../util/my_request';

class RequestForms extends Component {
  constructor(props) {
    super(props);
    this.state = {
      q: 'loading',
      er: '',
      items: [],
    };
  }

  componentDidMount() {
    const {
      requestId,
    } = this.props;
    req(`/form/find_by_request_id?request_id=${requestId}`)
    .then(items => {
      this.setState({
        items: items,
        q: 'loaded',
      });
    });
  }

  showForm(formId, ev) {
    ev.preventDefault();
    this.props.showForm(formId);
  }

  render() {
    const {
      q,
      er,
      items,
    } = this.state;
    const trList = items.map(x => (
      <tr key={x.id}>
        <td>{x.barcode}</td>
        <td>{x.requestSurname}</td>
        <td>{x.requestName}</td>
        <td>{x.requestPatronymic}</td>
        <td>{x.analysis}</td>
        <td><input type="button" onClick={this.showForm.bind(this, x.id)} value="Просмотр" /></td>
      </tr>
    ))
    return (
      <Fragment>
        <h1>Бланки анализов</h1>
        {q === 'loading' && 'Загрузка...'}
        {er && <div style={{color: 'red'}}>{er}</div>}
        <table>
          <thead>
            <tr>
              <th>Штрих-код</th>
              <th>Фамилия</th>
              <th>Имя</th>
              <th>Отчество</th>
              <th>Анализ</th>
              <th>Просмотр</th>
            </tr>
          </thead>
          <tbody>
            {trList}
          </tbody>
        </table>
        <input type="button" onClick={this.props.back} value="Назад" />
      </Fragment>
    );
  }
}

export default RequestForms;