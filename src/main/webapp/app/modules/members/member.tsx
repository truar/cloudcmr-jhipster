import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import debounce from 'lodash/debounce';
// tslint:disable-next-line:no-unused-variable
import { getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import {
  getEntities,
  searchEntities,
  getEntity,
  resetEntity,
  updateEntity,
  createEntity,
  reset
} from '../../entities/member/member.reducer';
// tslint:disable-next-line:no-unused-variable
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import MembersSearchForm from './components/forms/MembersSearchForm';
import MemberUpdateForm from './components/forms/MemberUpdateForm';

export interface IMemberProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface ISearchAndPAginateBaseState extends IPaginationBaseState {
  search: '';
  isNew;
}

export type IMemberState = ISearchAndPAginateBaseState;

export class Member extends React.Component<IMemberProps, IMemberState> {
  state: IMemberState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE),
    search: '',
    isNew: true
  };

  debouncedSearchEntities = debounce(() => {
    this.searchEntities();
  }, 500);

  handleChange = event => {
    this.setState({ search: event.target.value, activePage: 1 }, () => this.debouncedSearchEntities());
  };

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate() {
    if (this.props.updateSuccess) {
      this.reset();
    }
  }

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 1, search: '' }, () => {
      this.getEntities();
    });
  };

  handleLoadMore = () => {
    this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => {
        this.reset();
      }
    );
  };

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
      this.setState({ isNew: true });
    }
  };

  handleMemberClick = member => {
    this.setState({ isNew: false }, () => this.props.getEntity(member.id));
  };

  handleNewMember = () => {
    this.setState({ isNew: true }, () => this.props.resetEntity());
  };

  getEntities = () => {
    const { search, activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(search, activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  searchEntities = () => {
    const { search, itemsPerPage, sort, order } = this.state;
    this.props.searchEntities(search, 0, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { memberEntity, memberList, loading, updating } = this.props;
    const { isNew } = this.state;
    return (
      <div>
        <h2 id="member-heading">
          Gestion des adhérents
          <Button onClick={this.handleNewMember} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Créer un nouvel adhérent
          </Button>
        </h2>
        <Row>
          <MembersSearchForm
            activePage={this.state.activePage}
            handleLoadMore={this.handleLoadMore}
            linksNext={this.props.links.next}
            sort={this.sort}
            memberList={memberList}
            handleMemberClick={this.handleMemberClick}
            search={this.state.search}
            handleChange={this.handleChange}
          />
          <Col xs="7">
            <div>
              <Row className="justify-content-center">
                <Col md="8">
                  <h2 id="cloudcmrApp.member.home.createOrEditLabel">
                    {isNew ? 'Créer un nouvel adhérent' : `Modifier l'adhérent #${memberEntity.id}`}
                  </h2>
                </Col>
              </Row>
              <Row className="justify-content-center">
                <Col md="8">
                  <MemberUpdateForm
                    loading={loading}
                    isNew={isNew}
                    member={memberEntity}
                    updating={updating}
                    handleSubmit={this.saveEntity}
                  />
                </Col>
              </Row>
            </div>
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ member }: IRootState) => ({
  memberList: member.entities,
  totalItems: member.totalItems,
  links: member.links,
  memberEntity: member.entity,
  updateSuccess: member.updateSuccess,
  loading: member.loading,
  updating: member.updating
});

const mapDispatchToProps = {
  getEntities,
  searchEntities,
  getEntity,
  resetEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Member);
