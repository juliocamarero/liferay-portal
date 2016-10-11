import Component from 'metal-component';
import templates from './LCSHome.soy';
import Soy from 'metal-soy';

import './Topbar.es';
import 'dxp-cloud-sidebar/DXPCloudSidebar.es';

class LCSHome extends Component {
}

Soy.register(LCSHome, templates);

export default LCSHome;