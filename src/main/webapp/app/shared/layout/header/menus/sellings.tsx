import React from 'react';
import { NavItem, NavLink } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const SellingMenu = () => (
  <NavItem>
    <NavLink href="/#/ventes">
      <FontAwesomeIcon icon="donate" fixedWidth />
      &nbsp; Ventes
    </NavLink>
  </NavItem>
);
