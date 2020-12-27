import React, { Component, Fragment } from 'react';
import requestStatusFormatted from '../util/request_status_formatted';
import sexFormatted from '../util/sex_formatted';
import analysisListFormatted from '../util/analysis_list_formatted';
import dateTimeFormatted from '../util/date_time_formatted';
import { req } from '../util/my_request';

class AdminMain extends Component {
  constructor(props) {
    super(props);
    this.state = {
      q: 'loading',
      items: [],
      er: '',
    };
  }

  componentDidMount() {
    req("/request/get_all")
    .then(requests => {
      this.setState({
        q: '',
        items: requests,
      });
    })
    .catch(ex => {
      this.setState({
        q: '',
        er: ex.message,
      });
    });
  }

  handleEditRequest(requestId, ev) {
    ev.preventDefault();
    this.props.toEditRequest(requestId);
  };

  render() {
    const content = this.state.items.map(x => {
      return (
        <tr key={x.id}>
          <td>{requestStatusFormatted(x.status)}</td>
          <td>{x.client.phone}</td>
          <td>{x.client.name}</td>
          <td>
            {
              x.status === 'CREATED' &&
              <input
                type="button"
                value="Согласовать данные"
                onClick={this.handleEditRequest.bind(this, x.id)}
              />
            }
          </td>
          <td>{x.surname}</td>
          <td>{x.name}</td>
          <td>{x.patronymic}</td>
          <td>{sexFormatted(x.sex)}</td>
          <td>{x.passportSeries}</td>
          <td>{x.passportNumber}</td>
          <td>{analysisListFormatted(x.analysisList)}</td>
          <td>{dateTimeFormatted(x.arrivalTime)}</td>
        </tr>
      );
    });
    return (
      <Fragment>
        <h1>Заявки клиентов</h1>
        {this.state.er && <div style={{color: 'red'}}>{this.state.er}</div>}
        <table>
          <thead>
            <tr>
              <th>Статус</th>
              <th>Телефон</th>
              <th>Имя клиента</th>
              <th></th>
              <th>Фамилия</th>
              <th>Имя</th>
              <th>Отчество</th>
              <th>Пол</th>
              <th>Серия</th>
              <th>Номер</th>
              <th>Анализы</th>
              <th>Время прибытия</th>
            </tr>
          </thead>
          <tbody>
            {content}
          </tbody>
        </table >
        {this.state.q === 'loading' && 'Загрузка...'}
      </Fragment>
    );
  }
}

export default AdminMain;