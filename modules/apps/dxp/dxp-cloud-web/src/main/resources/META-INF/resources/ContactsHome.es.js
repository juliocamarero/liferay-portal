import Component from 'metal-component';
import templates from './ContactsHome.soy';
import Soy from 'metal-soy';

import './Topbar.es';
import './Sidebar.es';

class ContactsHome extends Component {
}

Soy.register(ContactsHome, templates);

export default ContactsHome;