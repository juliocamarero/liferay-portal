import Component from 'metal-component';
import templates from './SettingsIdentification.soy';
import Soy from 'metal-soy';

import '../sections/SettingsSections.soy';

import 'dxp-cloud-sidebar/DXPCloudSidebar.es';
import 'dxp-cloud-web/Topbar.es';

class SettingsIdentification extends Component {
}

Soy.register(SettingsIdentification, templates);

export default SettingsIdentification;