import Component from 'metal-component/src/Component';
import Footer from './Footer.es';
import Header from './Header.es';
import Soy from 'metal-soy/src/Soy';
import templates from './Gallery.soy';

class Gallery extends Component {
	created() {
		console.log('Gallery component');
	}
}

// Register component
Soy.register(Gallery, templates);

export default Gallery;