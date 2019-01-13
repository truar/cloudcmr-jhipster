import React from 'react';
import { AvGroup, AvField } from 'availity-reactstrap-validation';
import { Label } from 'reactstrap';

const PhonesForm = props => {
  const { member, isNew } = props;
  return (
    <>
      <hr />
      <PhoneForm index={0} disabled={member.sff} />
      <PhoneForm index={1} disabled={member.sff} />
      <PhoneForm index={2} disabled={false} />
    </>
  );
};

const PhoneForm = props => {
  const { index, disabled } = props;
  return (
    <>
      <AvGroup>
        <AvField id="phone-type" type="hidden" className="form-control" name={`phones[${index}].type`} value="PHONE" />
        <Label id="phoneNumberLabel" for="phoneNumber">
          {' '}
          Téléphone #{index + 1}
        </Label>
        <AvField disabled={disabled} id="phone-phoneNumber" type="text" name={`phones[${index}].phoneNumber`} />
      </AvGroup>
    </>
  );
};

export default PhonesForm;
