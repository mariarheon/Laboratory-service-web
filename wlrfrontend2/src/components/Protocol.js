import React, { Component, Fragment } from 'react';
import { req } from '../util/my_request';

class Protocol extends Component {
  constructor(props) {
    super(props);
    this.state = {
      q: 'loading',
      er: '',
      protocol: null,
    };
  }

  componentDidMount() {
    const {
      formId,
    } = this.props;
    req(`/protocol/get_by_form_id?form_id=${formId}`)
    .then(protocol => {
      this.setState({
        q: 'loaded',
        protocol: protocol.protocol,
      });
    })
    .catch(ex => {
      this.setState({
        q: '',
        er: ex.message,
      });
    })
  }

  handleBack = ev => {
    ev.preventDefault();
    const { formId } = this.props;
    this.props.toEditForm(formId);
  }

  render() {
    const {
      protocol,
      q,
      er,
    } = this.state;
    return (
      <Fragment>
        <h1>Протокол действий</h1>
        {er && <div style={{color: 'red'}}>{er}</div>}
        {q === 'loading' && 'Загрузка...'}
        <div>{protocol}</div>
        <input type="button" onClick={this.handleBack} value="Назад" />
      </Fragment>
    )
  }
}

export default Protocol;