import Component from 'metal-component/src/Component';
import Soy from 'metal-soy/src/Soy';
import templates from './GalleryDetails.soy';

/**
 * GalleryDetails Component
 */
class GalleryDetails extends Component {}

// Register component
Soy.register(GalleryDetails, templates);

export default GalleryDetails;