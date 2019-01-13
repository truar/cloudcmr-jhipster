import React from 'react';
import { DropdownItem } from 'reactstrap';
import { NavDropdown } from '../header-components';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BaseArticleMenu = () => (
  <NavDropdown icon="hdd" name="Base article" id="base-article-menu">
    <DropdownItem tag={Link} to="/base_article/articles">
      <FontAwesomeIcon icon="hdd" fixedWidth />
      &nbsp;Articles
    </DropdownItem>
    <DropdownItem tag={Link} to="/base_article/categories">
      <FontAwesomeIcon icon="hdd" fixedWidth />
      &nbsp;Categories
    </DropdownItem>
  </NavDropdown>
);
