import Component from 'metal-component';
import templates from './SettingsHome.soy';
import Soy from 'metal-soy';

import '../Topbar.es';
import 'dxp-cloud-sidebar/DXPCloudSidebar.es';

class SettingsHome extends Component {
}

Soy.register(SettingsHome, templates);

export default SettingsHome;