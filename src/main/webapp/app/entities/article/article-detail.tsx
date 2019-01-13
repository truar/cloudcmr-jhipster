import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { DateDisplayer } from 'app/shared/components/DateDisplayer';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './article.reducer';
import { IArticle } from 'app/shared/model/article.model';
// tslint:disable-next-line:no-unused-variable

export interface IArticleDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ArticleDetail extends React.Component<IArticleDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { articleEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Article [<b>{articleEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="code">Code</span>
            </dt>
            <dd>{articleEntity.code}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{articleEntity.description}</dd>
            <dt>
              <span id="price">Prix</span>
            </dt>
            <dd>{articleEntity.price}</dd>
            <dt>
              <span id="startDate">Date de début de disponibilité</span>
            </dt>
            <dd>
              <DateDisplayer date={articleEntity.startDate} />
            </dd>
            <dt>
              <span id="endDate">Date de fin de disponibilité</span>
            </dt>
            <dd>
              <DateDisplayer date={articleEntity.endDate} />
            </dd>
            <dt>
              <span id="comment">Commentaire</span>
            </dt>
            <dd>{articleEntity.comment}</dd>
          </dl>
          <Button tag={Link} to="/entity/article" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/article/${articleEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Modifier</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ article }: IRootState) => ({
  articleEntity: article.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ArticleDetail);
