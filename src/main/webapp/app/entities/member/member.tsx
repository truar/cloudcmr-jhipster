import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import debounce from 'lodash/debounce';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, searchEntities, reset } from './member.reducer';
import { IMember } from 'app/shared/model/member.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IMemberProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface ISearchAndPAginateBaseState extends IPaginationBaseState {
  search: '';
}

export type IMemberState = ISearchAndPAginateBaseState;

export class Member extends React.Component<IMemberProps, IMemberState> {
  state: IMemberState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE),
    search: ''
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
    this.setState({ activePage: 1 }, () => {
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

  getEntities = () => {
    const { search, activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(search, activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  searchEntities = () => {
    const { search, itemsPerPage, sort, order } = this.state;
    this.props.searchEntities(search, 0, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { memberList, match } = this.props;
    return (
      <div>
        <h2 id="member-heading">
          Gestion des adhérents
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Créer un nouvel adhérent
          </Link>
        </h2>
        <input type="text" name="search" placeholder="Recherche..." value={this.state.search} onChange={this.handleChange} />
        <div className="table-responsive">
          <InfiniteScroll
            pageStart={this.state.activePage}
            loadMore={this.handleLoadMore}
            hasMore={this.state.activePage - 1 < this.props.links.next}
            loader={<div className="loader">Loading ...</div>}
            threshold={0}
            initialLoad={false}
          >
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('id')}>
                    ID <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('firstName')}>
                    First Name <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('lastName')}>
                    Last Name <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('email')}>
                    Email <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('birthDate')}>
                    Birth Date <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('gender')}>
                    Gender <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('uscaNumber')}>
                    Usca Number <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('comment')}>
                    Comment <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('licenceNumber')}>
                    Licence Number <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('licenceCreationDate')}>
                    Licence Creation Date <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('subscription')}>
                    Subscription <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('email2')}>
                    Email 2 <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('season')}>
                    Season <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {memberList.map((member, i) => (
                  <tr key={`member-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${member.id}`} color="link" size="sm">
                        {member.id}
                      </Button>
                    </td>
                    <td>{member.firstName}</td>
                    <td>{member.lastName}</td>
                    <td>{member.email}</td>
                    <td>
                      <TextFormat type="date" value={member.birthDate} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td>{member.gender}</td>
                    <td>{member.uscaNumber}</td>
                    <td>{member.comment}</td>
                    <td>{member.licenceNumber}</td>
                    <td>
                      <TextFormat type="date" value={member.licenceCreationDate} format={APP_DATE_FORMAT} />
                    </td>
                    <td>{member.subscription}</td>
                    <td>{member.email2}</td>
                    <td>{member.season}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${member.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${member.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${member.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ member }: IRootState) => ({
  memberList: member.entities,
  totalItems: member.totalItems,
  links: member.links,
  entity: member.entity,
  updateSuccess: member.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  searchEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Member);
