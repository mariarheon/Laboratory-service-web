import React, { Component } from 'react';
import AssistantMain from './AssistantMain';
import FormEdit from './FormEdit';
import Protocol from './Protocol';

class Assistant extends Component {
  constructor(props) {
    super(props);
    this.state = {
      q: 'main',
      formId: -1,
    };
  }

  toMain = () => {
    this.setState({
      q: 'main',
      formId: -1,
    });
  }

  toEditForm = formId => {
    this.setState({
      q: 'edit',
      formId: formId,
    });
  }

  toProtocol = formId => {
    this.setState({
      q: 'protocol',
      formId: formId,
    });
  }

  render() {
    const {
      q,
      formId,
    } = this.state;
    if (q === 'main') {
      return (
        <AssistantMain
          toEditForm={this.toEditForm}
        />
      );
    }
    if (q === 'edit') {
      return (
        <FormEdit
          formId={formId}
          back={this.toMain}
          toProtocol={this.toProtocol}
        />
      );
    }
    if (q === 'protocol') {
      return (
        <Protocol
          formId={formId}
          toEditForm={this.toEditForm}
        />
      );
    }
    return <div>assistant</div>;
  }
}

export default Assistant;