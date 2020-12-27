import React, { Component, Fragment } from 'react';
import { req } from '../util/my_request';

class FormShow extends Component {
  constructor(props) {
    super(props);
    this.state = {
      q: 'loading',
      er: '',
      form: null,
    };
  }

  componentDidMount() {
    const {
      formId,
    } = this.props;
    req(`/form/get_by_id_for_client?id=${formId}`)
    .then(form => {
      this.setState({
        q: 'loaded',
        form: form,
      });
    })
    .catch(ex => {
      this.setState({
        q: '',
        er: ex.message,
      });
    })
  }

  toProtocol = () => {
    const {
      toProtocol,
      formId,
    } = this.props;
    toProtocol(formId);
  }

  handleChange(fieldId, ev) {
    const { target } = ev;
    const { value } = target;
    const { form } = this.state;
    const { fields } = form;
    const newFields = fields.map(x => ({...x}));
    newFields.find(x => x.id === fieldId).value = value;
    const newForm = {...form};
    newForm.fields = newFields;
    this.setState({
      form: newForm,
    });
  }

  back = () => {
    this.props.back(this.props.requestId);
  }

  render() {
    const {
      q,
      er,
      form,
    } = this.state;
    let trList = null;
    if (form && form.fields) {
      trList = form.fields
        .map(x => (
          <tr key={x.id}>
            <td>{x.description}</td>
            <td>{x.value}</td>
            <td>{x.units}</td>
          </tr>
        ))
    }
    let mainTable = null;
    if (form) {
      mainTable = (
        <table>
          <tbody>
            <tr>
              <td>Штрихкод</td>
              <td>{form.barcode}</td>
            </tr>
            <tr>
              <td>Вид анализа</td>
              <td>{form.analysis}</td>
            </tr>
            <tr>
              <td>Фамилия</td>
              <td>{form.requestSurname}</td>
            </tr>
            <tr>
              <td>Имя</td>
              <td>{form.requestName}</td>
            </tr>
            <tr>
              <td>Отчество</td>
              <td>{form.requestPatronymic}</td>
            </tr>
          </tbody>
        </table>
      );
    }
    return (
      <Fragment>
        <h1>Бланк анализов</h1>
        {q === 'loading' && 'Загрузка...'}
        {er && <div style={{color: 'red'}}>{er}</div>}
        {mainTable}
        <table>
          <thead>
            <tr>
              <th>Показатель</th>
              <th>Значение</th>
              <th>Единицы измерения</th>
            </tr>
          </thead>
          <tbody>
            {trList}
          </tbody>
        </table>
        <input type="button" onClick={this.back} value="Назад" />
      </Fragment>
    );
  }
}

export default FormShow;