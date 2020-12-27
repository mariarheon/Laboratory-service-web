import React, { Component, Fragment } from 'react';
import { req } from '../util/my_request';
import timeFormatted from '../util/time_formatted';
import parseTime from '../util/parse_time';

class ChooseTime extends Component {
  constructor(props) {
    super(props);
    this.state = {
      q: 'loading',
      er: '',
      timeIntervals: [],
      arrivalTime: '',
    };
  }

  componentDidMount() {
    req(`/choose_time/get_possible_times?request_id=${this.props.requestId}`)
    .then(possibleTimes => {
      this.setState({
        q: 'loaded',
        timeIntervals: possibleTimes.timeIntervals,
      });
    })
    .catch(ex => {
      this.setState({
        q: '',
        er: ex.message,
      });
    });
  }

  handleChange = ev => {
    const value = ev.target.value;
    this.setState({
      arrivalTime: value,
    });
  }

  handleBack(requestId) {
    this.props.back(requestId);
  }

  handleSubmit = ev => {
    ev.preventDefault();
    const { arrivalTime } = this.state;
    const { requestId, toMain } = this.props;
    const arrivalTimeParsed = parseTime(arrivalTime);
    if (!arrivalTimeParsed) {
      this.setState({
        er: 'Время должно быть указано в формате hh:mm',
      });
    } else {
      req('/choose_time/set_arrival_time', {
        requestId: requestId,
        hh: arrivalTimeParsed.hh,
        mm: arrivalTimeParsed.mm,
      })
      .then(() => {
        toMain();
      })
      .catch(ex => {
        this.setState({
          er: ex.message,
        });
      });
    }
  }

  render() {
    const {
      q,
      er,
      arrivalTime,
      timeIntervals,
    } = this.state;
    const {
      requestId,
    } = this.props;
    const trList = timeIntervals
      .map(x => (
        <tr>
          <td>{timeFormatted(x.startTime)}</td>
          <td>{timeFormatted(x.endTime)}</td>
        </tr>
      ));
    return (
      <Fragment>
        <h1>Выберите время посещения из доступных промежутков</h1>
        {q === 'loading' && 'Загрузка...'}
        {er && <div style={{color: 'red'}}>{er}</div>}
        <table>
          <thead>
            <tr>
              <th>Начало промежутка</th>
              <th>Конец промежутка</th>
            </tr>
          </thead>
          <tbody>
            {trList}
          </tbody>
        </table>
        <form onSubmit={this.handleSubmit}>
          <div>
            <label>
              Желаемое время прибытия:
              <input type="text" value={arrivalTime} onChange={this.handleChange} placeholder="12:30" />
            </label>
          </div>
          <div>
            <input type="submit" value="Выбрать время" />
          </div>
          <div>
            <input type="button" onClick={this.handleBack.bind(this, requestId)} value="Назад" />
          </div>
        </form>
      </Fragment>
    );
  }
}

export default ChooseTime;