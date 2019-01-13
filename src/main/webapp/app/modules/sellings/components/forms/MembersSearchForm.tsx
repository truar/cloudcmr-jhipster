import React from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Button, Col, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IMember } from 'app/shared/model/member.model';

export interface IMemberSearchLineProps {
  member: IMember;
  handleMemberClick: any;
  handlePayerClick: any;
  handleSecondPayerClick: any;
  isSelected: boolean;
}

class MemberSearchLine extends React.Component<IMemberSearchLineProps, {}> {
  constructor(props) {
    super(props);
  }

  handleMemberClick = () => {
    this.props.handleMemberClick(this.props.member);
  };

  handlePayerClick = () => {
    this.props.handlePayerClick(this.props.member);
  };

  handleSecondPayerClick = () => {
    this.props.handleSecondPayerClick(this.props.member);
  };

  render() {
    const { member, isSelected } = this.props;
    const style = isSelected ? { backgroundColor: '#ccc' } : {};
    return (
      <tr style={style}>
        <td>{member.uscaNumber}</td>
        <td>{member.lastName}</td>
        <td>{member.firstName}</td>
        <td className="text-right">
          <div className="btn-group flex-btn-group-container">
            <Button title="Modifier" onClick={this.handleMemberClick} color="primary" size="sm">
              <FontAwesomeIcon icon="plus" />
            </Button>
            <Button title="Payeur" onClick={this.handlePayerClick} color="success" size="sm">
              <FontAwesomeIcon icon="dollar-sign" />
            </Button>
            <Button title="Second Payeur" onClick={this.handleSecondPayerClick} color="success" size="sm">
              <FontAwesomeIcon icon="dollar-sign" />
            </Button>
          </div>
        </td>
      </tr>
    );
  }
}

const MemberSearch = props => {
  const {
    activePage,
    handleLoadMore,
    linksNext,
    memberList,
    handleMemberClick,
    sort,
    search,
    handleChange,
    selectedMember,
    handlePayerClick,
    handleSecondPayerClick
  } = props;
  return (
    <Col xs="4">
      <h3>Veuillez sélectionner un adhérent</h3>
      <div>
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
                <th className="hand" onClick={sort('uscaNumber')}>
                  USCA # <FontAwesomeIcon icon="sort" />
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
                <MemberSearchLine
                  key={`key-${i}`}
                  isSelected={member.id === selectedMember.id}
                  member={member}
                  handleMemberClick={handleMemberClick}
                  handlePayerClick={handlePayerClick}
                  handleSecondPayerClick={handleSecondPayerClick}
                />
              ))}
            </tbody>
          </Table>
        </InfiniteScroll>
      </div>
    </Col>
  );
};

export default MemberSearch;
