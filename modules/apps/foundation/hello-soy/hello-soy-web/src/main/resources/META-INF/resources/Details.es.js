import Component from 'metal-component/src/Component';
import Soy from 'metal-soy/src/Soy';
import templates from './Details.soy';

/**
 * Details Component
 */
class Details extends Component {}

// Register component
Soy.register(Details, templates);

export default Details;