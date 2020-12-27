import React, { Component } from 'react';
import AdminMain from './AdminMain';
import RequestEdit from './RequestEdit';
import ChooseTime from './ChooseTime';

class Admin extends Component {
  constructor(props) {
    super(props);
    this.state = {
      q: 'main',
      requestId: -1,
    };
  }

  toMain = () => {
    this.setState({
      q: 'main',
      requestId: -1,
    });
  }

  toEditRequest = requestId => {
    this.setState({
      q: 'edit',
      requestId: requestId,
    });
  }

  toChooseTime = requestId => {
    this.setState({
      q: 'choose_time',
      requestId: requestId,
    });
  }

  render() {
    const { q } = this.state;
    if (q === 'main') {
      return <AdminMain toEditRequest={this.toEditRequest} />;
    }
    if (q === 'edit') {
      return <RequestEdit back={this.toMain} toChooseTime={this.toChooseTime} requestId={this.state.requestId} />;
    }
    if (q === 'choose_time') {
      return <ChooseTime back={this.toEditRequest} toMain={this.toMain} requestId={this.state.requestId} />;
    }
    return <div>admin</div>;
  }
}

export default Admin;