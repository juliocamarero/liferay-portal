import Component from 'metal-component';
import templates from './Home.soy';
import Soy from 'metal-soy';

import './Content.es';
import './Sidebar.es';
import './Topbar.es';

class Home extends Component {
}

Soy.register(Home, templates);

export default Home;