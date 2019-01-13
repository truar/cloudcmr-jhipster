import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './member.reducer';
import { IMember } from 'app/shared/model/member.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMemberUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IMemberUpdateState {
  isNew: boolean;
}

export class MemberUpdate extends React.Component<IMemberUpdateProps, IMemberUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    values.licenceCreationDate = new Date(values.licenceCreationDate);

    if (errors.length === 0) {
      const { memberEntity } = this.props;
      const entity = {
        ...memberEntity,
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
    this.props.history.push('/entity/member');
  };

  render() {
    const { memberEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="cloudcmrApp.member.home.createOrEditLabel">Create or edit a Member</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : memberEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="member-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="firstNameLabel" for="firstName">
                    First Name
                  </Label>
                  <AvField id="member-firstName" type="text" name="firstName" />
                </AvGroup>
                <AvGroup>
                  <Label id="lastNameLabel" for="lastName">
                    Last Name
                  </Label>
                  <AvField id="member-lastName" type="text" name="lastName" />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="email">
                    Email
                  </Label>
                  <AvField id="member-email" type="text" name="email" />
                </AvGroup>
                <AvGroup>
                  <Label id="birthDateLabel" for="birthDate">
                    Birth Date
                  </Label>
                  <AvField id="member-birthDate" type="date" className="form-control" name="birthDate" />
                </AvGroup>
                <AvGroup>
                  <Label id="genderLabel">Gender</Label>
                  <AvInput
                    id="member-gender"
                    type="select"
                    className="form-control"
                    name="gender"
                    value={(!isNew && memberEntity.gender) || 'MALE'}
                  >
                    <option value="MALE">MALE</option>
                    <option value="FEMALE">FEMALE</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="uscaNumberLabel" for="uscaNumber">
                    Usca Number
                  </Label>
                  <AvField id="member-uscaNumber" type="text" name="uscaNumber" validate={{}} />
                </AvGroup>
                <AvGroup>
                  <Label id="commentLabel" for="comment">
                    Comment
                  </Label>
                  <AvField id="member-comment" type="text" name="comment" />
                </AvGroup>
                <AvGroup>
                  <Label id="licenceNumberLabel" for="licenceNumber">
                    Licence Number
                  </Label>
                  <AvField id="member-licenceNumber" type="text" name="licenceNumber" />
                </AvGroup>
                <AvGroup>
                  <Label id="licenceCreationDateLabel" for="licenceCreationDate">
                    Licence Creation Date
                  </Label>
                  <AvInput
                    id="member-licenceCreationDate"
                    type="datetime-local"
                    className="form-control"
                    name="licenceCreationDate"
                    value={isNew ? null : convertDateTimeFromServer(this.props.memberEntity.licenceCreationDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="subscriptionLabel" for="subscription">
                    Subscription
                  </Label>
                  <AvField id="member-subscription" type="text" name="subscription" />
                </AvGroup>
                <AvGroup>
                  <Label id="email2Label" for="email2">
                    Email 2
                  </Label>
                  <AvField id="member-email2" type="text" name="email2" />
                </AvGroup>
                <AvGroup>
                  <Label id="seasonLabel" for="season">
                    Season
                  </Label>
                  <AvField id="member-season" type="string" className="form-control" name="season" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/member" replace color="info">
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
  memberEntity: storeState.member.entity,
  loading: storeState.member.loading,
  updating: storeState.member.updating,
  updateSuccess: storeState.member.updateSuccess
});

const mapDispatchToProps = {
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
)(MemberUpdate);
