import React, { Component, Fragment } from 'react';
import requestStatusFormatted from '../util/request_status_formatted';
import sexFormatted from '../util/sex_formatted';
import analysisListFormatted from '../util/analysis_list_formatted';
import dateTimeFormatted from '../util/date_time_formatted';
import { req } from '../util/my_request';

class ClientMain extends Component {
  constructor(props) {
    super(props);
    this.state = {
      q: 'loading',
      items: [],
      er: '',
    };
  }

  componentDidMount() {
    req("/request/get_mine")
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

  handleResults(requestId, ev) {
    ev.preventDefault();
    this.props.toResults(requestId);
  };

  render() {
    const content = this.state.items.map(x => {
      return (
        <tr key={x.id}>
          <td>{x.surname}</td>
          <td>{x.name}</td>
          <td>{x.patronymic}</td>
          <td>{sexFormatted(x.sex)}</td>
          <td>{x.passportSeries}</td>
          <td>{x.passportNumber}</td>
          <td>{analysisListFormatted(x.analysisList)}</td>
          <td>{requestStatusFormatted(x.status)}</td>
          <td>{dateTimeFormatted(x.arrivalTime)}</td>
          <td>
            {
              (x.status === 'FINISHED' || x.status === 'CLIENT_IS_AWARE')
              &&
              <input type="button" onClick={this.handleResults.bind(this, x.id)} value="Результаты"  />
            }
          </td>
        </tr>
      );
    });
    return (
      <Fragment>
        <h1>Мои заявки</h1>
        {this.state.er && <div style={{color: 'red'}}>{this.state.er}</div>}
        <table>
          <thead>
            <tr>
              <th>Фамилия</th>
              <th>Имя</th>
              <th>Отчество</th>
              <th>Пол</th>
              <th>Серия</th>
              <th>Номер</th>
              <th>Анализы</th>
              <th>Статус</th>
              <th>Время прибытия</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {content}
          </tbody>
        </table >
        {this.state.q === 'loading' && 'Загрузка...'}
        <input type="button" onClick={this.props.toAddRequest} value="Создать заявку" />
      </Fragment>
    );
  }
}

export default ClientMain;