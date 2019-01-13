import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { DateDisplayer } from 'app/shared/components/DateDisplayer';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './article.reducer';
// tslint:disable-next-line:no-unused-variable

export interface IArticleProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Article extends React.Component<IArticleProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { articleList, match } = this.props;
    return (
      <div>
        <h2 id="article-heading">
          Base Article
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Créer un nouvel Article
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Code</th>
                <th>Description</th>
                <th>Prix</th>
                <th>Date de début de disponibilité</th>
                <th>Date de fin de disponibilité</th>
                <th>Commentaire</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {articleList.map((article, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${article.id}`} color="link" size="sm">
                      {article.id}
                    </Button>
                  </td>
                  <td>{article.code}</td>
                  <td>{article.description}</td>
                  <td>{article.price} €</td>
                  <td>
                    <DateDisplayer date={article.startDate} />
                  </td>
                  <td>
                    <DateDisplayer date={article.endDate} />
                  </td>
                  <td>{article.comment}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${article.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${article.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${article.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ article }: IRootState) => ({
  articleList: article.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Article);
