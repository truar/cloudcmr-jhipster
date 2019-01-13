import React, { Component } from 'react';
import MembersSearchForm from './components/forms/MembersSearchForm';
import { getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Row, Col, FormGroup, Button } from 'reactstrap';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { IRootState } from 'app/shared/reducers';
import debounce from 'lodash/debounce';
import { getEntities, searchEntities, reset } from '../../entities/member/member.reducer';
import { getEntities as getArticles } from 'app/modules/baseArticle/article/article.reducer';
import {
  getCurrentSalesSession,
  openSalesSession,
  sellArticle,
  removeSoldArticle,
  assignPayer,
  assignSecondPayer,
  removeSecondPayer,
  closeRunningSale,
  closeSalesSession
} from 'app/modules/sellings/sellings.reducer';
import { RouteComponentProps } from 'react-router';
import { connect } from 'react-redux';
import ArticlesForSaleForm from './components/forms/ArticlesForSaleForm';
import { IMember } from 'app/shared/model/member.model';
import { IArticle } from 'app/shared/model/article.model';
import SoldArticlesTable from './components/SoldArticlesTable';
import OpenSalesSessionForm from './components/forms/OpenSalesSessionForm';
import CloseSalesSessionDialogForm from './components/forms/CloseSalesSessionDialogForm';
import './Selling.css';
import PayerForm from './components/forms/PayerForm';
import { IPayer } from 'app/shared/model/payer.model';

export interface ISellingProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface ISearchAndPaginateBaseState extends IPaginationBaseState {
  search: '';
  selectedMember: IMember;
  selectedArticle: IArticle;
  quantity: 1;
  cashWhenNew: 0;
  cashWhenClose: 0;
  secondPayer: IMember;
  secondPayerPaymentAmount: number;
  secondPayerPaymentType: '';
  firstPayer: IMember;
  firstPayerPaymentType: '';
  closingSalesSession: {};
}

export type ISellingState = ISearchAndPaginateBaseState;

export class Selling extends Component<ISellingProps, ISellingState> {
  state: ISellingState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE),
    search: '',
    quantity: 1,
    selectedMember: {},
    selectedArticle: {},
    cashWhenNew: 0,
    cashWhenClose: 0,
    secondPayer: {},
    secondPayerPaymentAmount: 0,
    secondPayerPaymentType: '',
    firstPayer: {},
    firstPayerPaymentType: '',
    closingSalesSession: false
  };

  debouncedSearchEntities = debounce(() => {
    this.searchEntities();
  }, 500);

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate() {
    if (this.props.updateSuccess) {
      this.reset();
    }
  }

  handleSearchChange = event => {
    this.setState({ search: event.target.value, activePage: 1 }, this.debouncedSearchEntities);
  };

  handleArticleChange = event => {
    const { articles } = this.props;
    const selectedArticle = articles.find(item => item.id === parseInt(event.target.value, 10));
    this.setState({ selectedArticle });
  };

  handleChange = event => {
    this.setState({ quantity: event.target.value });
  };

  handleChangeCashWhenNew = event => {
    this.setState({ cashWhenNew: event.target.value });
  };

  handleChangeCashWhenClose = event => {
    this.setState({ cashWhenClose: event.target.value });
  };

  handleMemberClick = member => {
    this.setState({ selectedMember: member });
  };

  handlePayerClick = member => {
    this.setState({ firstPayer: member });
  };

  handleChangePayerPaymentType = event => {
    this.setState({ firstPayerPaymentType: event.target.value });
  };

  handleChangeSecondPayerPaymentType = event => {
    this.setState({ secondPayerPaymentType: event.target.value });
  };

  handleChangeSecondPayerPaymentAmount = event => {
    this.setState({ secondPayerPaymentAmount: event.target.value });
  };

  handleSecondPayerClick = member => {
    this.setState({ secondPayer: member });
  };

  handleClickAddPayer = () => {
    const { firstPayer, firstPayerPaymentType } = this.state;
    this.props.assignPayer(firstPayer.id, firstPayerPaymentType);
  };

  handleClickAddSecondPayer = () => {
    const { secondPayer, secondPayerPaymentAmount, secondPayerPaymentType } = this.state;
    this.props.assignSecondPayer(secondPayer.id, secondPayerPaymentAmount, secondPayerPaymentType);
  };

  handleOpenSalesSession = () => {
    this.props.openSalesSession(this.state.cashWhenNew);
  };

  handleAddArticle = () => {
    const { selectedMember, selectedArticle, quantity } = this.state;
    this.props.sellArticle(selectedArticle.id, selectedMember.id, quantity);
  };

  handleDelete = soldArticle => {
    this.props.removeSoldArticle(soldArticle);
  };

  handleLoadMore = () => {
    this.setState({ activePage: this.state.activePage + 1 }, () => this.getMembers());
  };

  handleCloseRunningSaleClick = () => {
    this.props.closeRunningSale();
    this.reset();
  };

  handleCloseSalesSessionClick = () => {
    this.props.closeSalesSession(this.state.cashWhenClose);
    this.toggleModal();
  };

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 1, search: '' }, () => {
      this.getEntities();
    });
    this.props.getCurrentSalesSession();
  };

  sort = prop => () => {
    this.setState({ order: this.state.order === 'asc' ? 'desc' : 'asc', sort: prop }, () => {
      this.reset();
    });
  };

  getEntities = () => {
    this.getMembers();
    this.props.getArticles();
  };

  getMembers = () => {
    const { search, activePage, itemsPerPage, sort, order } = this.state;
    this.props.getMembers(search, activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  searchEntities = () => {
    const { search, itemsPerPage, sort, order } = this.state;
    this.props.searchEntities(search, 0, itemsPerPage, `${sort},${order}`);
  };

  handleClickDeletePayer = () => {
    this.props.removeSecondPayer();
  };

  renderPayerRecap = (payer, index) => {
    return (
      <div className="payer">
        Payeur {index} : {payer.uscaNumber} {payer.lastName} {payer.firstName} {payer.paymentAmount} € {payer.paymentType}{' '}
        {index > 1 && (
          <Button title="Supprimer" onClick={this.handleClickDeletePayer} color="danger" size="sm">
            <FontAwesomeIcon icon="trash" />
          </Button>
        )}
      </div>
    );
  };

  renderRunningSalesForm = () => {
    const { memberList, articles, runningSale } = this.props;
    const {
      selectedArticle,
      quantity,
      selectedMember,
      firstPayer,
      firstPayerPaymentType,
      secondPayer,
      secondPayerPaymentAmount,
      secondPayerPaymentType
    } = this.state;
    const soldArticles = runningSale.soldArticles;
    const payer = runningSale.payer || {};
    const payer2 = runningSale.payer2 || {};
    const addArticleButtonDisabled = !selectedMember.id || !selectedArticle.id;
    const confirmButtonDisabled =
      !runningSale.payer || !runningSale.soldArticles || (runningSale.soldArticles && runningSale.soldArticles.length === 0);
    const closeSessionDisabled = runningSale.soldArticles && runningSale.soldArticles.length > 0;

    return (
      <Row>
        <Col sm={{ size: 8, offset: 4 }}>
          <h2>Ventes en cours</h2>
        </Col>
        <MembersSearchForm
          activePage={this.state.activePage}
          handleLoadMore={this.handleLoadMore}
          linksNext={this.props.links.next}
          sort={this.sort}
          memberList={memberList}
          handleMemberClick={this.handleMemberClick}
          search={this.state.search}
          handleChange={this.handleSearchChange}
          selectedMember={selectedMember}
          handlePayerClick={this.handlePayerClick}
          handleSecondPayerClick={this.handleSecondPayerClick}
        />
        <Col xs="3">
          <ArticlesForSaleForm
            articles={articles}
            handleAddArticle={this.handleAddArticle}
            handleArticleChange={this.handleArticleChange}
            handleChange={this.handleChange}
            selectedArticle={selectedArticle}
            quantity={quantity}
            disabled={addArticleButtonDisabled}
          />
          {firstPayer.id && (
            <PayerForm
              index={1}
              payer={firstPayer}
              paymentType={firstPayerPaymentType}
              handleAddPlayerClick={this.handleClickAddPayer}
              handleChangePaymentType={this.handleChangePayerPaymentType}
              canEditPaymentAmount={false}
            />
          )}
          {secondPayer.id && (
            <PayerForm
              index={2}
              payer={secondPayer}
              paymentAmount={secondPayerPaymentAmount}
              paymentType={secondPayerPaymentType}
              handleChange={this.handleChangeSecondPayerPaymentAmount}
              handleAddPlayerClick={this.handleClickAddSecondPayer}
              handleChangePaymentType={this.handleChangeSecondPayerPaymentType}
              canEditPaymentAmount
            />
          )}
        </Col>
        <Col xs={{ size: 5 }}>
          <SoldArticlesTable soldArticles={soldArticles} handleDelete={this.handleDelete} />
          <div style={{ height: '150px', marginTop: '50px' }}>
            <p className="total">Total : {runningSale.total} €</p>
            {payer.firstName && this.renderPayerRecap(payer, 1)}
            {payer2.firstName && this.renderPayerRecap(payer2, 2)}
          </div>
          <FormGroup row>
            <Button onClick={this.handleCloseRunningSaleClick} color="primary" size="lg" disabled={confirmButtonDisabled}>
              Confirmer
            </Button>
          </FormGroup>
          <FormGroup style={{ float: 'right' }} row>
            <Button onClick={this.toggleModal} color="danger" size="sm" disabled={closeSessionDisabled}>
              Clore la session
            </Button>
          </FormGroup>
        </Col>
      </Row>
    );
  };

  renderOpenSalesSessionForm = () => {
    const { cashWhenNew } = this.state;
    return (
      <OpenSalesSessionForm
        cashWhenNew={cashWhenNew}
        handleChangeCashWhenNew={this.handleChangeCashWhenNew}
        handleOpenSalesSession={this.handleOpenSalesSession}
      />
    );
  };

  toggleModal = () => {
    this.setState(prevState => ({ closingSalesSession: !prevState.closingSalesSession }));
  };

  renderModal = () => {
    const { cashWhenClose, closingSalesSession } = this.state;
    return (
      <CloseSalesSessionDialogForm
        cashWhenClose={cashWhenClose}
        closingSalesSession={closingSalesSession}
        handleChangeCashWhenClose={this.handleChangeCashWhenClose}
        handleCloseSalesSessionClick={this.handleCloseSalesSessionClick}
        toggleModal={this.toggleModal}
      />
    );
  };

  render() {
    const { needOpenSalesSession } = this.props;
    const toRender = needOpenSalesSession ? this.renderOpenSalesSessionForm() : this.renderRunningSalesForm();
    return (
      <div className="Selling">
        {this.renderModal()}
        {toRender}
      </div>
    );
  }
}

const mapStateToProps = ({ member, article, sellings }: IRootState) => ({
  articles: article.entities,
  memberList: member.entities,
  totalItems: member.totalItems,
  links: member.links,
  memberEntity: member.entity,
  updateSuccess: member.updateSuccess,
  loading: member.loading,
  updating: member.updating,
  needOpenSalesSession: sellings.needOpenSalesSession,
  runningSale: sellings.runningSale,
  salesLoading: sellings.loadingSession
});

const mapDispatchToProps = {
  getMembers: getEntities,
  getArticles,
  searchEntities,
  getCurrentSalesSession,
  openSalesSession,
  sellArticle,
  removeSoldArticle,
  assignPayer,
  assignSecondPayer,
  removeSecondPayer,
  closeRunningSale,
  closeSalesSession,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Selling);
