import React, { Component, Fragment } from 'react';
import { req } from '../util/my_request';
import dateFormatted from '../util/date_formatted';
import parseDate from '../util/parse_date';

class RequestEdit extends Component {
  constructor(props) {
    super(props);
    this.state = {
      q: 'loading',
      er: '',
      allAnalysisList: [],
      analysisList: '',
      surname: '',
      name: '',
      patronymic: '',
      sex: 'FEMALE',
      passportSeries: '',
      passportNumber: '',
      arrivalDate: '',
    };
  }

  componentDidMount() {
    const { requestId } = this.props;
    Promise.all([
      req(`/request/get?id=${requestId}`),
      req(`/analysis/get_all`)
    ])
    .then(answer => {
      const [ req, analysisList ] = answer;
      this.setState({
        q: 'loaded',
        allAnalysisList: analysisList,
        analysisList: req.analysisList.map(x => x.name),
        surname: req.surname,
        name: req.name,
        patronymic: req.patronymic,
        sex: req.sex,
        passportSeries: req.passportSeries + '',
        passportNumber: req.passportNumber + '',
        arrivalDate: dateFormatted(req.arrivalTime),
      });
    })
    .catch(ex => {
      this.setState({
        q: '',
        er: ex.message,
      });
    });
  }

  handleSubmit = ev => {
    ev.preventDefault();
    const {
      allAnalysisList,
      analysisList,
      surname,
      name,
      patronymic,
      sex,
      passportSeries,
      passportNumber,
      arrivalDate,
    } = this.state;
    const arrivalDateParsed = parseDate(arrivalDate);
    if (!/^\d+$/.test(passportSeries)) {
      this.setState({
        er: 'Серия паспорта должна быть числом',
      });
    } else if (!/^\d+$/.test(passportNumber)) {
      this.setState({
        er: 'Номер паспорта должен быть числом',
      });
    } else if (!arrivalDateParsed) {
      this.setState({
        er: 'Дата должна быть в формате dd.mm.yyyy',
      });
    } else {
      req('/request/edit', {
        id: this.props.requestId,
        surname: surname,
        name: name,
        patronymic: patronymic,
        sex: sex,
        passportSeries: +passportSeries,
        passportNumber: +passportNumber,
        arrivalTime: arrivalDateParsed,
        analysisList: allAnalysisList.filter(x => analysisList.includes(x.name)),
      })
      .then(() => {
        this.props.toChooseTime(this.props.requestId);
      })
      .catch(ex => {
          this.setState({
            er: ex.message,
          });
      });
    }
  }

  handleChange = ev => {
    const { target } = ev;
    const name = target.name;
    const value = target.value;
    this.setState({
      [name]: value,
    });
  }

  handleAnalysisChange(analysisId, ev) {
    const { target } = ev;
    const { analysisList, allAnalysisList } = this.state;
    const checked = target.checked;
    let newAnalysisList = [...analysisList];
    const foundAnalysis = allAnalysisList.find(x => x.id === analysisId);
    if (checked) {
      newAnalysisList.push(foundAnalysis.name);
    } else {
      newAnalysisList = analysisList.filter(x => x !== foundAnalysis.name);
    }
    this.setState({
      analysisList: newAnalysisList,
    })
  }

  render() {
    const {
      q,
      er,
      allAnalysisList,
      analysisList,
      surname,
      name,
      patronymic,
      sex,
      passportSeries,
      passportNumber,
      arrivalDate,
    } = this.state;
    let formContent = '';
    if (q === 'loaded') {
      const analysisPart = allAnalysisList
        .map(x => (
          <div key={x.id}>
            <label>
              <input
                type="checkbox"
                checked={analysisList.includes(x.name)}
                onChange={this.handleAnalysisChange.bind(this, x.id)}
              />
              {x.name}
            </label>
          </div>
        ));
      formContent = (
        <form onSubmit={this.handleSubmit} >
          <input type="hidden" name="id" value="{{id}}" />
          <div>
              <label >Фамилия:
                  <input name="surname" type="text" value={surname} onChange={this.handleChange} placeholder="фамилия" />
              </label>
          </div>
          <div>
              <label >Имя:
                  <input name="name" type="text" value={name} onChange={this.handleChange} placeholder="имя" />
              </label>
          </div>
          <div>
              <label >Отчество:
                  <input name="patronymic" type="text" value={patronymic} onChange={this.handleChange} placeholder="отчество" />
              </label>
          </div>
          <div>
              <label >Пол:
                  <select name="sex" value={sex} onChange={this.handleChange}>
                      <option value="FEMALE">Женский</option>
                      <option value="MALE">Мужской</option>
                  </select>
              </label>
          </div>
          <div>
              <label >Паспорт, серия:
                  <input name="passportSeries" type="text" value={passportSeries} onChange={this.handleChange} placeholder="000000" />
              </label>
          </div>
          <div>
              <label >Паспорт, номер:
                  <input name="passportNumber" type="text" value={passportNumber} onChange={this.handleChange} placeholder="0000" />
              </label>
          </div>
          <div>
              <label >Желаемая дата прибытия:
                  <input name="arrivalDate" type="text" value={arrivalDate} onChange={this.handleChange} placeholder="25.01.2001" />
              </label>
          </div>
          <div>
              <p>Выберите нужные пункты:</p>
              {analysisPart}
          </div>
          <div>
              <input type="submit" value="Выбрать время" />
          </div>
          <div>
              <input type="button" onClick={this.props.back} value="Назад" />
          </div>
        </form>
      );
    }
    return (
      <Fragment>
        <h1>Согласование данных заявки</h1>
        {q === 'loading' && 'Загрузка...'}
        {er && <div style={{color: 'red'}}>{er}</div>}
        {formContent}
      </Fragment>
    );
  }
}

export default RequestEdit;