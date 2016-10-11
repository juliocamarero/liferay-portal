import Component from 'metal-component';
import templates from './AssetsHome.soy';
import Soy from 'metal-soy';

import './Topbar.es';
import 'dxp-cloud-sidebar/DXPCloudSidebar.es';

class AssetsHome extends Component {
}

Soy.register(AssetsHome, templates);

export default AssetsHome;