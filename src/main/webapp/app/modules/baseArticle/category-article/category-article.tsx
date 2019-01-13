import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './category-article.reducer';
import { ICategoryArticle } from 'app/shared/model/category-article.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICategoryArticleProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class CategoryArticle extends React.Component<ICategoryArticleProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { categoryArticleList, match } = this.props;
    return (
      <div>
        <h2 id="category-article-heading">
          Categories de la base Articles
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Créer une nouvelle Catégorie
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>Code</th>
                <th>Description</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {categoryArticleList.map((categoryArticle, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${categoryArticle.id}`} color="link" size="sm">
                      {categoryArticle.code}
                    </Button>
                  </td>
                  <td>{categoryArticle.description}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${categoryArticle.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${categoryArticle.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${categoryArticle.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Supprimer</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ categoryArticle }: IRootState) => ({
  categoryArticleList: categoryArticle.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CategoryArticle);
