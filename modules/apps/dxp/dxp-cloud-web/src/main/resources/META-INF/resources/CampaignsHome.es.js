import Component from 'metal-component';
import templates from './CampaignsHome.soy';
import Soy from 'metal-soy';

import './Topbar.es';
import 'dxp-cloud-sidebar/DXPCloudSidebar.es';

class CampaignsHome extends Component {
}

Soy.register(CampaignsHome, templates);

export default CampaignsHome;