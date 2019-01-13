import React from 'react';
import { Table, Col, Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ISoldArticle } from 'app/shared/model/soldArticle.model';

export interface ISoldArticleTableLine {
  soldArticle: ISoldArticle;
  handleDelete: any;
}

class SoldArticleTableLine extends React.Component<ISoldArticleTableLine, {}> {
  constructor(props) {
    super(props);
  }

  handleDelete = () => {
    this.props.handleDelete(this.props.soldArticle);
  };

  render() {
    const { soldArticle } = this.props;
    return (
      <tr>
        <td>{soldArticle.member.lastName}</td>
        <td>{soldArticle.member.firstName}</td>
        <td>{soldArticle.article.description}</td>
        <td>{soldArticle.quantity}</td>
        <td>{soldArticle.article.price} €</td>
        <td>
          <div className="btn-group flex-btn-group-container">
            <Button title="Supprimer" onClick={this.handleDelete} color="danger" size="sm">
              <FontAwesomeIcon icon="trash" />
            </Button>
          </div>
        </td>
      </tr>
    );
  }
}

const SoldArticlesTable = props => {
  const { soldArticles, handleDelete } = props;
  return (
    <div style={{ height: '350px', overflow: 'scroll' }}>
      <h3>Article / Adhérents / Achat</h3>
      <Table responsive>
        <thead>
          <tr>
            <th className="hand">Nom</th>
            <th className="hand">Prénom</th>
            <th className="hand">Article</th>
            <th className="hand">Quantité</th>
            <th className="hand">Prix</th>
            <th />
          </tr>
        </thead>
        <tbody>
          {soldArticles &&
            soldArticles.map((soldArticle, i) => (
              <SoldArticleTableLine key={`soldArticles-${i}`} soldArticle={soldArticle} handleDelete={handleDelete} />
            ))}
        </tbody>
      </Table>
    </div>
  );
};

export default SoldArticlesTable;
