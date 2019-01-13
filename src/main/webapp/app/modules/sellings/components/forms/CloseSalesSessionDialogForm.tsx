import React from 'react';
import { Input, Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';

const CloseSalesSessionDialogForm = props => {
  const { cashWhenClose, handleChangeCashWhenClose, handleCloseSalesSessionClick, toggleModal, closingSalesSession } = props;
  return (
    <Modal isOpen={closingSalesSession} toggle={toggleModal}>
      <ModalHeader toggle={toggleModal}>Fermeture de la vente</ModalHeader>
      <ModalBody>
        Pour terminer la session de ventes, veuillez renseigner le montant restant dans votre caisse
        <Input type="text" name="cashWhenClose" onChange={handleChangeCashWhenClose} value={cashWhenClose} />
      </ModalBody>
      <ModalFooter>
        <Button color="primary" onClick={handleCloseSalesSessionClick}>
          Clore la session
        </Button>{' '}
        <Button color="secondary" onClick={toggleModal}>
          Annuler
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default CloseSalesSessionDialogForm;
