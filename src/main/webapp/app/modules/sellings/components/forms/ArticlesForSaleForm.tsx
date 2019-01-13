import React from 'react';
import { Row, Col, FormGroup, Input, Label, Button } from 'reactstrap';
import './ArticleForSaleForm.css';

const ArticlesForSaleForm = props => {
  const { articles, handleAddArticle, handleChange, handleArticleChange, selectedArticle, quantity, disabled } = props;
  return (
    <div className="ArticleForSaleForm">
      <h3>Veuillez choisir un article</h3>
      <FormGroup row>
        <Input onChange={handleArticleChange} value={selectedArticle.id} type="select" name="article_sale">
          <option disabled selected value="">
            {' '}
            -- Sélectionnez un article --{' '}
          </option>
          {articles.map(article => (
            <option value={`${article.id}`}>
              {article.code} - {article.description}
            </option>
          ))}
        </Input>
      </FormGroup>
      <FormGroup row>
        <Label sm={4} for="quantity">
          Quantité :{' '}
        </Label>
        <Col sm={8}>
          <Input type="text" name="quantity" onChange={handleChange} value={quantity} />
        </Col>
      </FormGroup>
      <FormGroup check row>
        <Button onClick={handleAddArticle} disabled={disabled}>
          Ajouter
        </Button>
      </FormGroup>
    </div>
  );
};

export default ArticlesForSaleForm;
