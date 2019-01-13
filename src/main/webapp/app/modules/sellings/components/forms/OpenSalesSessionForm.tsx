import React from 'react';
import { Row, Col, FormGroup, Input, Button } from 'reactstrap';

const OpenSalesSessionForm = props => {
  const { cashWhenNew, handleChangeCashWhenNew, handleOpenSalesSession } = props;
  return (
    <Row>
      <Col sm={{ size: 8, offset: 2 }}>
        <h2>Bienvenue dans le module de Ventes</h2>
        <p>Il vous faut ouvrir une session de ventes afin de pouvoir démarrer les ventes.</p>
        <p>Merci de renseigner le montant présent dans votre caisse avant de poursuivre</p>
        <FormGroup>
          <Input type="text" name="cashWhenNew" onChange={handleChangeCashWhenNew} value={cashWhenNew} />
        </FormGroup>
        <FormGroup row>
          <Button color="primary" onClick={handleOpenSalesSession}>
            Démarrer
          </Button>
        </FormGroup>
      </Col>
    </Row>
  );
};

export default OpenSalesSessionForm;
