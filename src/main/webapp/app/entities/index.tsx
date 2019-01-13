import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Member from './member';
import Article from './article';
import CategoryArticle from './category-article';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/member`} component={Member} />
      <ErrorBoundaryRoute path={`${match.url}/article`} component={Article} />
      <ErrorBoundaryRoute path={`${match.url}/category-article`} component={CategoryArticle} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
