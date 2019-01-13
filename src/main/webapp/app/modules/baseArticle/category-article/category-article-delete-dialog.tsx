import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICategoryArticle } from 'app/shared/model/category-article.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './category-article.reducer';

export interface ICategoryArticleDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CategoryArticleDeleteDialog extends React.Component<ICategoryArticleDeleteDialogProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  confirmDelete = event => {
    this.props.deleteEntity(this.props.categoryArticleEntity.id);
    this.handleClose(event);
  };

  handleClose = event => {
    event.stopPropagation();
    this.props.history.goBack();
  };

  render() {
    const { categoryArticleEntity } = this.props;
    return (
      <Modal isOpen toggle={this.handleClose}>
        <ModalHeader toggle={this.handleClose}>Confirmer la suppression ?</ModalHeader>
        <ModalBody id="cloudcmrApp.categoryArticle.delete.question">
          Voulez vous vraiment supprimer la catégorie {categoryArticleEntity.code} ?
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={this.handleClose}>
            <FontAwesomeIcon icon="ban" />
            &nbsp; Annuler
          </Button>
          <Button id="jhi-confirm-delete-categoryArticle" color="danger" onClick={this.confirmDelete}>
            <FontAwesomeIcon icon="trash" />
            &nbsp; Supprimer
          </Button>
        </ModalFooter>
      </Modal>
    );
  }
}

const mapStateToProps = ({ categoryArticle }: IRootState) => ({
  categoryArticleEntity: categoryArticle.entity
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CategoryArticleDeleteDialog);
