import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IMember } from 'app/shared/model/member.model';
import { getEntities as getMembers } from 'app/entities/member/member.reducer';
import { getEntity, updateEntity, createEntity, reset } from './phone.reducer';
import { IPhone } from 'app/shared/model/phone.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPhoneUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPhoneUpdateState {
  isNew: boolean;
  memberId: string;
}

export class PhoneUpdate extends React.Component<IPhoneUpdateProps, IPhoneUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      memberId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getMembers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { phoneEntity } = this.props;
      const entity = {
        ...phoneEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/phone');
  };

  render() {
    const { phoneEntity, members, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="cloudcmrApp.phone.home.createOrEditLabel">Create or edit a Phone</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : phoneEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="phone-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="typeLabel">Type</Label>
                  <AvInput
                    id="phone-type"
                    type="select"
                    className="form-control"
                    name="type"
                    value={(!isNew && phoneEntity.type) || 'PHONE'}
                  >
                    <option value="PHONE">PHONE</option>
                    <option value="MOBILE">MOBILE</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="phoneNumberLabel" for="phoneNumber">
                    Phone Number
                  </Label>
                  <AvField
                    id="phone-phoneNumber"
                    type="text"
                    name="phoneNumber"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      pattern: { value: '^0[0-9]*$', errorMessage: "This field should follow pattern for '^0[0-9]*.." }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="member.id">Member</Label>
                  <AvInput
                    id="phone-member"
                    type="select"
                    className="form-control"
                    name="member.id"
                    value={isNew ? members[0] && members[0].id : phoneEntity.member.id}
                  >
                    {members
                      ? members.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/phone" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  members: storeState.member.entities,
  phoneEntity: storeState.phone.entity,
  loading: storeState.phone.loading,
  updating: storeState.phone.updating,
  updateSuccess: storeState.phone.updateSuccess
});

const mapDispatchToProps = {
  getMembers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PhoneUpdate);
