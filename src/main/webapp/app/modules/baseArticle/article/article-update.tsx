import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntities as getCategories } from 'app/entities/category-article/category-article.reducer';
import { getEntity, updateEntity, createEntity, reset } from './article.reducer';
import { IArticle } from 'app/shared/model/article.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IArticleUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IArticleUpdateState {
  isNew: boolean;
}

export class ArticleUpdate extends React.Component<IArticleUpdateProps, IArticleUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
    this.props.getCategories();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { articleEntity } = this.props;
      const entity = {
        ...articleEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/base_article/articles');
  };

  render() {
    const { articleEntity, categories, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="cloudcmrApp.article.home.createOrEditLabel">
              {isNew ? 'Créer un Article' : `Modifier l\'Article #${articleEntity.id}`}
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Chargement...</p>
            ) : (
              <AvForm model={isNew ? {} : articleEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <AvInput id="article-id" type="hidden" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="codeLabel" for="code">
                    Code
                  </Label>
                  <AvField id="article-code" type="text" name="code" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    Description
                  </Label>
                  <AvField id="article-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="priceLabel" for="price">
                    Prix
                  </Label>
                  <AvField id="article-price" type="text" name="price" />
                </AvGroup>
                <AvGroup>
                  <Label id="startDateLabel" for="startDate">
                    Date de début de disponibilité
                  </Label>
                  <AvField id="article-startDate" type="text" className="form-control" name="startDate" />
                </AvGroup>
                <AvGroup>
                  <Label id="endDateLabel" for="endDate">
                    Date de fin de disponibilité
                  </Label>
                  <AvField id="article-endDate" type="text" className="form-control" name="endDate" />
                </AvGroup>
                <AvGroup>
                  <Label for="category.id">Categorie</Label>
                  <AvInput
                    id="article-category"
                    type="select"
                    className="form-control"
                    name="category.id"
                    value={articleEntity.category !== 'undefined' ? categories[0] && categories[0].id : articleEntity.category.id}
                  >
                    {categories
                      ? categories.map(category => (
                          <option value={category.id} key={category.id}>
                            {category.code} - {category.description}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="commentLabel" for="comment">
                    Commentaire
                  </Label>
                  <AvField id="article-comment" type="text" name="comment" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/base_article/articles" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Retour</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Enregistrer
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  categories: storeState.categoryArticle.entities,
  articleEntity: storeState.article.entity,
  loading: storeState.article.loading,
  updating: storeState.article.updating,
  updateSuccess: storeState.article.updateSuccess
});

const mapDispatchToProps = {
  getCategories,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ArticleUpdate);
