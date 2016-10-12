import Component from 'metal-component';
import templates from './SettingsUserInformation.soy';
import Router from 'metal-router/src/Router';
import Soy from 'metal-soy';

import '../sections/SettingsSections.soy';

import 'dxp-cloud-sidebar/DXPCloudSidebar.es';
import 'dxp-cloud-web/Topbar.es';

class SettingsUserInformation extends Component {
}

Soy.register(SettingsUserInformation, templates);

Router.router().on('endNavigate', (event) => {
	// Reset form after submit navigate
	if (event.form) {
		event.form.reset();
	}
});

export default SettingsUserInformation;