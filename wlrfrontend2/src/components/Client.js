import React, { Component } from 'react';
import ClientMain from './ClientMain';
import RequestAdd from './RequestAdd';
import RequestForms from './RequestForms';
import FormShow from './FormShow';

class Client extends Component {
  constructor(props) {
    super(props);
    this.state = {
      q: 'main',
      requestId: -1,
    };
  }

  toResults = requestId => {
    this.setState({
      q: 'results',
      requestId: requestId,
    });
  }

  toAddRequest = () => {
    this.setState({
      q: 'add',
      requestId: -1,
    })
  }

  toMain = () => {
    this.setState({
      q: 'main',
      requestId: -1,
    });
  }

  toSpecificForm = formId => {
    this.setState({
      q: 'form',
      formId: formId,
    });
  }

  render() {
    const {
      q,
      requestId,
      formId,
    } = this.state;
    if (q === 'main') {
      return (
        <ClientMain
          toAddRequest={this.toAddRequest}
          toResults={this.toResults}
        />
      );
    }
    if (q === 'add') {
      return <RequestAdd back={this.toMain} />;
    }
    if (q === 'results') {
      return <RequestForms requestId={requestId} back={this.toMain} showForm={this.toSpecificForm} />;
    }
    if (q === 'form') {
      return <FormShow requestId={requestId} formId={formId} back={this.toResults} />;
    }
    return <div>client</div>;
  }
}

export default Client;