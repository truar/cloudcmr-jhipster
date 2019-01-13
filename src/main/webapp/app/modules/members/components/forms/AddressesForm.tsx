import React from 'react';
import { AvGroup, AvField } from 'availity-reactstrap-validation';
import { Label } from 'reactstrap';

const AddressesForm = props => {
  const { member } = props;
  return (
    <>
      <AddressForm disabled={member.sff} key={`address-0`} index={0} />
      <AddressForm disabled={false} key={`address-1`} index={1} />
    </>
  );
};

const AddressForm = props => {
  const { index, disabled } = props;
  return (
    <>
      <hr />
      <p>Addresse #{index + 1}</p>
      <AvGroup>
        <Label id="address1Label" for="address1">
          {' '}
          Adresse 1{' '}
        </Label>
        <AvField disabled={disabled} id="address-address1" type="text" name={`addresses[${index}].address1`} />
      </AvGroup>
      <AvGroup>
        <Label id="address2Label" for="address2">
          {' '}
          Adresse 2{' '}
        </Label>
        <AvField disabled={disabled} id="address-address2" type="text" name={`addresses[${index}].address2`} />
      </AvGroup>
      <AvGroup>
        <Label id="address3Label" for="address3">
          {' '}
          Adresse 3{' '}
        </Label>
        <AvField disabled={disabled} id="address-address3" type="text" name={`addresses[${index}].address3`} />
      </AvGroup>
      <AvGroup>
        <Label id="zipcodeLabel" for="zipcode">
          {' '}
          Code postal{' '}
        </Label>
        <AvField disabled={disabled} id="address-zipcode" type="text" name={`addresses[${index}].zipcode`} />
      </AvGroup>
      <AvGroup>
        <Label id="cityLabel" for="city">
          {' '}
          Ville{' '}
        </Label>
        <AvField disabled={disabled} id="address-city" type="text" name={`addresses[${index}].city`} />
      </AvGroup>
    </>
  );
};

export default AddressesForm;
