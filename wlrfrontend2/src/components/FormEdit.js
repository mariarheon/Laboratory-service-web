import React, { Component, Fragment } from 'react';
import { req } from '../util/my_request';

class FormEdit extends Component {
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
    req(`/form/get_by_id?id=${formId}`)
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

  handleSubmit = ev => {
    ev.preventDefault();
    const { form } = this.state;
    req('/form/edit_fields', form)
    .then(() => {
      this.props.back();
    })
    .catch(ex => {
      this.setState({
        er: ex.message,
      });
    });
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
            <td><input type="text" value={x.value} onChange={this.handleChange.bind(this, x.id)} /></td>
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
        <input type="button" onClick={this.toProtocol} value="Получить протокол действий" />
        <form onSubmit={this.handleSubmit}>
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
          <input type="submit" value="Применить изменения" />
        </form>
        <input type="button" onClick={this.props.back} value="Назад" />
      </Fragment>
    );
  }
}

export default FormEdit;