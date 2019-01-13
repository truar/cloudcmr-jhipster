import React from 'react';
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { AvForm, AvGroup, AvField, AvInput } from 'availity-reactstrap-validation';
import { Button, Label } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import AddressesForm from './AddressesForm';
import PhonesForm from './PhonesForm';

const InsideMemberUpdateForm = props => {
  const { isNew, member, updating, disabled } = props;
  return (
    <>
      <AvGroup>
        <Label id="licenceNumberLabel" for="licenceNumber">
          {' '}
          Numéro de licence FFS{' '}
        </Label>
        <AvField disabled={disabled} id="member-licenceNumber" type="text" name="licenceNumber" />
      </AvGroup>
      <AvGroup>
        <Label id="firstNameLabel" for="firstName">
          {' '}
          Prénom{' '}
        </Label>
        <AvField disabled={disabled} id="member-firstName" type="text" name="firstName" />
      </AvGroup>
      <AvGroup>
        <Label id="lastNameLabel" for="lastName">
          {' '}
          Nom{' '}
        </Label>
        <AvField disabled={disabled} id="member-lastName" type="text" name="lastName" />
      </AvGroup>
      <AvGroup>
        <Label id="emailLabel" for="email">
          {' '}
          Email{' '}
        </Label>
        <AvField disabled={disabled} id="member-email" type="text" name="email" />
      </AvGroup>
      <AvGroup>
        <Label id="birthDateLabel" for="birthDate">
          {' '}
          Date de naissance{' '}
        </Label>
        <AvField disabled={disabled} id="member-birthDate" type="date" className="form-control" name="birthDate" />
      </AvGroup>
      <AvGroup>
        <Label id="genderLabel">Sexe</Label>
        <AvInput
          disabled={disabled}
          id="member-gender"
          type="select"
          className="form-control"
          name="gender"
          value={(!isNew && member.gender) || 'MALE'}
        >
          <option value="MALE">Homme</option>
          <option value="FEMALE">Femme</option>
        </AvInput>
      </AvGroup>
      <AvGroup>
        <Label id="licenceCreationDateLabel" for="licenceCreationDate">
          {' '}
          Date de création de la licence FFS{' '}
        </Label>
        <AvInput
          id="member-licenceCreationDate"
          type="datetime-local"
          className="form-control"
          name="licenceCreationDate"
          disabled={disabled}
          value={isNew ? null : convertDateTimeFromServer(member.licenceCreationDate)}
        />
      </AvGroup>
      <AvGroup>
        <Label id="uscaNumberLabel" for="uscaNumber">
          {' '}
          Numéro de carte USCA{' '}
        </Label>
        <AvField disabled={disabled} id="member-uscaNumber" type="text" name="uscaNumber" />
      </AvGroup>
      <AvGroup>
        <Label id="subscriptionLabel" for="subscription">
          {' '}
          Cotisation{' '}
        </Label>
        <AvField disabled={disabled} id="member-subscription" type="text" name="subscription" />
      </AvGroup>
      <AvGroup>
        <Label id="seasonLabel" for="season">
          {' '}
          Saison{' '}
        </Label>
        <AvField disabled={disabled} id="member-season" type="string" className="form-control" name="season" />
      </AvGroup>
      <AvGroup>
        <Label id="email2Label" for="email2">
          {' '}
          Email 2{' '}
        </Label>
        <AvField id="member-email2" type="text" name="email2" />
      </AvGroup>
      <AvGroup>
        <Label id="commentLabel" for="comment">
          {' '}
          Commentaire{' '}
        </Label>
        <AvField id="member-comment" type="text" name="comment" />
      </AvGroup>

      <AddressesForm member={member} />
      <PhonesForm member={member} isNew={isNew} />

      <Button color="primary" id="save-entity" type="submit" disabled={updating}>
        <FontAwesomeIcon icon="save" />
        &nbsp; Enregistrer
      </Button>
    </>
  );
};

const MemberUpdateForm = props => {
  const { loading, isNew, member, updating, handleSubmit } = props;
  const model = isNew ? {} : member;
  const disabled = member.sff;
  return (
    <AvForm model={model} onSubmit={handleSubmit}>
      {loading ? (
        <p>Loading...</p>
      ) : isNew ? (
        <>
          <div>
            <InsideMemberUpdateForm isNew={isNew} member={{}} updating={updating} disabled={disabled} />
          </div>
        </>
      ) : (
        <InsideMemberUpdateForm isNew={isNew} member={member} updating={updating} disabled={disabled} />
      )}
    </AvForm>
  );
};

export default MemberUpdateForm;
