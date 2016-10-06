'use strict';

import dom from 'metal-dom/src/all/dom';
import Surface from 'senna/src/surface/Surface';
import {isString} from 'metal/src/metal';

class LiferaySurface extends Surface {
	addContent(screenId, content) {
		if (isString(content)) {
			content = dom.buildFragment(content);
		}

		Liferay.DOMTaskRunner.runTasks(content);

		return super.addContent(screenId, content);
	}
}

export default LiferaySurface;