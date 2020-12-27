import React, { Component, Fragment } from 'react';
import { req } from '../util/my_request';
import dateTimeFormatted from '../util/date_time_formatted';

class AssistantMain extends Component {
  constructor(props) {
    super(props);
    this.state = {
      q: 'loading',
      er: '',
      items: [],
    };
  }

  componentDidMount() {
    req('/form/get_mine')
    .then(items => {
      this.setState({
        items: items,
        q: 'loaded',
      });
    });
  }

  toEditForm(formId) {
    this.props.toEditForm(formId);
  }

  finishForm(formId) {
    req(`/form/finish?form_id=${formId}`)
    .then(() => req('/form/get_mine'))
    .then(items => {
      this.setState({
        items: items,
        q: 'loaded',
        er: '',
      });
    })
    .catch(ex => {
      this.setState({
        er: ex.message,
      });
    });
  }

  render() {
    const {
      q,
      er,
      items,
    } = this.state;
    const trList = items.map(x => (
      <tr key={x.id}>
        <td>{dateTimeFormatted(x.collectionStartTime)}</td>
        <td>{dateTimeFormatted(x.collectionEndTime)}</td>
        <td>{dateTimeFormatted(x.researchStartTime)}</td>
        <td>{dateTimeFormatted(x.researchEndTime)}</td>
        <td>{x.barcode}</td>
        <td>{x.requestSurname}</td>
        <td>{x.requestName}</td>
        <td>{x.requestPatronymic}</td>
        <td>{x.analysis}</td>
        <td><input type="button" onClick={this.toEditForm.bind(this, x.id)} value="Редактировать" /></td>
        <td><input type="button" onClick={this.finishForm.bind(this, x.id)} value="Завершить работу" /></td>
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
              <th>Начало сбора</th>
              <th>Конец сбора</th>
              <th>Начало исследования</th>
              <th>Конец исследования</th>
              <th>Штрих-код</th>
              <th>Фамилия</th>
              <th>Имя</th>
              <th>Отчество</th>
              <th>Анализ</th>
              <th>Редактировать</th>
              <th>Завершить</th>
            </tr>
          </thead>
          <tbody>
            {trList}
          </tbody>
        </table>
      </Fragment>
    );
  }
}

export default AssistantMain;