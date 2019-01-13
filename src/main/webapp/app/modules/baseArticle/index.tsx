import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Article from './article';
import CategoryArticle from './category-article';

/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      <ErrorBoundaryRoute path={`${match.url}/articles`} component={Article} />
      <ErrorBoundaryRoute path={`${match.url}/categories`} component={CategoryArticle} />
    </Switch>
  </div>
);

export default Routes;
