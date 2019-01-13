import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './phone.reducer';
import { IPhone } from 'app/shared/model/phone.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPhoneDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PhoneDetail extends React.Component<IPhoneDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { phoneEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Phone [<b>{phoneEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="type">Type</span>
            </dt>
            <dd>{phoneEntity.type}</dd>
            <dt>
              <span id="phoneNumber">Phone Number</span>
            </dt>
            <dd>{phoneEntity.phoneNumber}</dd>
            <dt>Member</dt>
            <dd>{phoneEntity.member ? phoneEntity.member.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/phone" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/phone/${phoneEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ phone }: IRootState) => ({
  phoneEntity: phone.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PhoneDetail);
