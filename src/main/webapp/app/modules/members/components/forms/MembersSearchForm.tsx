import React from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Button, Col, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IMember } from 'app/shared/model/member.model';

export interface IMemberSearchLineProps {
  member: IMember;
  handleMemberClick: any;
}

class MemberSearchLine extends React.Component<IMemberSearchLineProps, {}> {
  constructor(props) {
    super(props);
  }

  handleMemberClick = () => {
    this.props.handleMemberClick(this.props.member);
  };

  render() {
    const member = this.props.member;
    return (
      <tr>
        <td>{member.licenceNumber}</td>
        <td>{member.lastName}</td>
        <td>{member.firstName}</td>
        <td className="text-right">
          <div className="btn-group flex-btn-group-container">
            <Button title="Modifier" onClick={this.handleMemberClick} color="primary" size="sm">
              <FontAwesomeIcon icon="pencil-alt" />
            </Button>
          </div>
        </td>
      </tr>
    );
  }
}

const MemberSearch = props => {
  const { activePage, handleLoadMore, linksNext, memberList, handleMemberClick, sort, search, handleChange } = props;
  return (
    <Col xs="4">
      <div style={{ position: 'fixed' }}>
        <input type="text" name="search" placeholder="Recherche..." value={search} onChange={handleChange} />
        <InfiniteScroll
          dataLength={memberList.length}
          next={handleLoadMore}
          hasMore={activePage - 1 < linksNext}
          loader={<div className="loader">Chargement ...</div>}
          endMessage={<b>Plus d'autres membres</b>}
          height={680}
          scrollThreshold="100px"
        >
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('licenceNumber')}>
                  Licence # <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('lastName')}>
                  Nom <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('firstName')}>
                  Prénom <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {memberList.map((member, i) => (
                <MemberSearchLine key={`key-${i}`} member={member} handleMemberClick={handleMemberClick} />
              ))}
            </tbody>
          </Table>
        </InfiniteScroll>
      </div>
    </Col>
  );
};

export default MemberSearch;
