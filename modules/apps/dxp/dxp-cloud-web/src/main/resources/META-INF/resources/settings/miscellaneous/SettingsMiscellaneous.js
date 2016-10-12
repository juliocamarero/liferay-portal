import Component from 'metal-component';
import templates from './SettingsMiscellaneous.soy';
import Soy from 'metal-soy';

import '../sections/SettingsSections.soy';

import 'dxp-cloud-sidebar/DXPCloudSidebar.es';
import 'dxp-cloud-web/Topbar.es';

class SettingsMiscellaneous extends Component {
}

Soy.register(SettingsMiscellaneous, templates);

export default SettingsMiscellaneous;