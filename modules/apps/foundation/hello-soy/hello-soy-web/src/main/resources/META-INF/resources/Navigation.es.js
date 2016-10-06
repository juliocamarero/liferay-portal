import Component from 'metal-component/src/Component';
import Footer from './Footer.es';
import Header from './Header.es';
import Soy from 'metal-soy/src/Soy';
import templates from './Navigation.soy';

class Navigation extends Component {
	created() {
		console.log('Navigation component');
	}
}

// Register component
Soy.register(Navigation, templates);

export default Navigation;