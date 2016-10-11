import Component from 'metal-component';
import templates from './OverviewHome.soy';
import Soy from 'metal-soy';

import './Topbar.es';
import 'dxp-cloud-sidebar/DXPCloudSidebar.es';

class OverviewHome extends Component {
}

Soy.register(OverviewHome, templates);

export default OverviewHome;