import React from 'react';
import { Row, Col, FormGroup, InputGroup, InputGroupAddon, InputGroupText, Input, Label, Button } from 'reactstrap';
import './PayerForm.css';

const PayerForm = props => {
  const {
    payer,
    index,
    canEditPaymentAmount,
    handleChange,
    paymentAmount,
    handleAddPlayerClick,
    paymentType,
    handleChangePaymentType
  } = props;
  return (
    <div className="PayerForm">
      Payeur {index} :
      <p className="payer">
        {' '}
        {payer.uscaNumber} {payer.lastName} {payer.firstName}{' '}
      </p>
      {canEditPaymentAmount && (
        <InputGroup>
          <Input onChange={handleChange} value={paymentAmount} />
          <InputGroupAddon addonType="append">
            <InputGroupText>€</InputGroupText>
          </InputGroupAddon>
        </InputGroup>
      )}
      <FormGroup>
        <Label for="exampleSelect">Type de paiement</Label>
        <Input type="select" onChange={handleChangePaymentType} value={paymentType} id={`payer-${index}-payment-type`}>
          <option disabled selected value="">
            {' '}
            -- Sélectionnez un mode de paiement --{' '}
          </option>
          <option value="CARD">CB</option>
          <option value="CASH">CASH</option>
          <option value="CHEQUE">CHEQUE</option>
          <option value="OTHER">AUTRES</option>
        </Input>
      </FormGroup>
      <Button onClick={handleAddPlayerClick} size="sm">
        Ajouter le payeur
      </Button>
    </div>
  );
};

export default PayerForm;
