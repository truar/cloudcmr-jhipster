import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './category-article.reducer';
import { ICategoryArticle } from 'app/shared/model/category-article.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICategoryArticleDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CategoryArticleDetail extends React.Component<ICategoryArticleDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { categoryArticleEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Catégorie [<b>{categoryArticleEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="code">Code</span>
            </dt>
            <dd>{categoryArticleEntity.code}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{categoryArticleEntity.description}</dd>
          </dl>
          <Button tag={Link} to="/entity/category-article" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/category-article/${categoryArticleEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ categoryArticle }: IRootState) => ({
  categoryArticleEntity: categoryArticle.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CategoryArticleDetail);
