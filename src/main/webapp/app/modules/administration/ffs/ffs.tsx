import React from 'react';
import axios from 'axios';
import { connect } from 'react-redux';

import { AvForm, AvGroup, AvInput, AvField, AvFeedback } from 'availity-reactstrap-validation';

import { Table, Badge, Col, Row, Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { importFFSFile } from './ffs.reducer';

import { IRootState } from 'app/shared/reducers';

export interface IFFSPageProps extends StateProps, DispatchProps {}

export interface IFFSPageState {
  ffs: any;
  exportFFS: any;
  loaded: any;
}

export class FFSPage extends React.Component<IFFSPageProps, IFFSPageState> {
  state: IFFSPageState = {
    ffs: {},
    exportFFS: {},
    loaded: 0
  };

  componentDidMount() {}

  handleselectedFile = event => {
    this.setState({
      exportFFS: event.target.files[0]
    });
  };

  handleUpload = () => {
    const data = new FormData();
    data.append('exportFFS', this.state.exportFFS, this.state.exportFFS.name);
    this.props.importFFSFile(data);
  };

  render() {
    return (
      <div>
        <h2 id="health-page-heading">FFS</h2>
        <p>Veuillez séléctionner le fichier FFS que vous souhaitez importer : </p>
        <AvForm onValidSubmit={this.handleUpload}>
          <AvField type="file" name="exportFFS" id="exportFFS" onChange={this.handleselectedFile} />
          <Button color="primary" type="submit">
            <FontAwesomeIcon icon="cloud" />
            &nbsp; Importer
          </Button>
        </AvForm>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({});

const mapDispatchToProps = {
  importFFSFile
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FFSPage);
