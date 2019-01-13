import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CategoryArticle from './category-article';
import CategoryArticleDetail from './category-article-detail';
import CategoryArticleUpdate from './category-article-update';
import CategoryArticleDeleteDialog from './category-article-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CategoryArticleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CategoryArticleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CategoryArticleDetail} />
      <ErrorBoundaryRoute path={match.url} component={CategoryArticle} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CategoryArticleDeleteDialog} />
  </>
);

export default Routes;
