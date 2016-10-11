import Component from 'metal-component/src/Component';
import Footer from './Footer.es';
import Header from './Header.es';
import templates from './Home.soy';
import Soy from 'metal-soy/src/Soy';

class Home extends Component {
	created() {
		console.log('Home component');
	}
}

// Register component
Soy.register(Home, templates);

export default Home;