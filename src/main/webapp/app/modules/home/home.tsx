import './home.css';

import React from 'react';
import { Link } from 'react-router-dom';

import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';

export interface IHomeProp extends StateProps, DispatchProps {}

export class Home extends React.Component<IHomeProp> {
  componentDidMount() {
    this.props.getSession();
  }

  renderHomeLoggedIn(account) {
    return (
      <div>
        <Alert color="success">Vous êtes connectés en tant que : {account.login}</Alert>
      </div>
    );
  }

  renderHomeGuest() {
    return (
      <div>
        <Alert color="warning">
          Veuillez vous connecter en cliquant sur ce
          <Link to="/login" className="alert-link">
            {' '}
            lien
          </Link>
        </Alert>
      </div>
    );
  }

  render() {
    const { account } = this.props;
    return (
      <Row>
        <Col sm={{ size: 8, offset: 2 }}>
          <h2>Bienvenue sur l'application CloudCMR</h2>
          {account && account.login ? this.renderHomeLoggedIn(account) : this.renderHomeGuest()}
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated
});

const mapDispatchToProps = { getSession };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Home);
