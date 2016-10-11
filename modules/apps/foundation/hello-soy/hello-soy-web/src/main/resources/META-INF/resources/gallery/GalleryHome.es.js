import Component from 'metal-component/src/Component';
import Footer from '../Footer.es';
import Header from '../Header.es';
import Soy from 'metal-soy/src/Soy';
import templates from './GalleryHome.soy';

class GalleryHome extends Component {
	created() {
		console.log('GalleryHome component');
	}
}

// Register component
Soy.register(GalleryHome, templates);

export default GalleryHome;