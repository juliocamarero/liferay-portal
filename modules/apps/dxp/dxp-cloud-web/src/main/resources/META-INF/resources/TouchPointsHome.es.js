import Component from 'metal-component';
import templates from './TouchPointsHome.soy';
import Soy from 'metal-soy';

import './Topbar.es';
import 'dxp-cloud-sidebar/DXPCloudSidebar.es';

class TouchPointsHome extends Component {
}

Soy.register(TouchPointsHome, templates);

export default TouchPointsHome;