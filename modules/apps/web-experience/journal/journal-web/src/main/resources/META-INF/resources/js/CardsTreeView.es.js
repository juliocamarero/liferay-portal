import Soy from 'metal-soy/src/Soy';

import core from 'metal/src/core';
import dom from 'metal-dom/src/dom';
import Treeview from 'metal-treeview';

import templates from './CardsTreeView.soy';

class CardsTreeview extends Treeview {
	/**
	 * Focus the given tree node.
	 * @param {!Object} nodeObj
	 * @protected
	 */
	focus_(nodeObj) {
		if (nodeObj) {
			this.element.querySelector('[data-treeitemid="' + nodeObj.id + '"] .card-row').focus();
		}
	}

	/**
	 * Focus the next tree node of given tree node.
	 * @param {!Element} node
	 * @protected
	 */
	focusNextNode_(node) {
		let path = node.getAttribute('data-treeview-path').split('-');

		let nodeObj = this.getNodeObj(path);

		let nextNodeObj;

		if (nodeObj.children && nodeObj.expanded) {
			path.push(0);

			nextNodeObj = this.getNodeObj(path);
		}
		else {
			while (!nextNodeObj && path.length > 0) {
				path[path.length - 1]++;

				nextNodeObj = this.getNodeObj(path);

				path.pop();
			}
		}

		this.focus_(nextNodeObj);
	}

	/**
	 * Focus the previous tree node of given tree node.
	 * @param {!Element} node
	 * @protected
	 */
	focusPrevNode_(node) {
		let path = node.getAttribute('data-treeview-path').split('-');

		let prevNodeObj;

		if (path[path.length - 1] === '0') {
			path.pop();

			prevNodeObj = this.getNodeObj(path);
		}
		else {
			path[path.length - 1]--;

			prevNodeObj = this.getNodeObj(path);

			while (prevNodeObj.children && prevNodeObj.expanded) {
				prevNodeObj = prevNodeObj.children[prevNodeObj.children.length - 1];
			}
		}

		this.focus_(prevNodeObj);
	}

	/**
	 * This is called when one of this tree view's nodes is clicked.
	 * @param {!Event} event
	 * @protected
	 */
	handleNodeClicked_(event) {
		let currentTarget = event.delegateTarget.parentNode.parentNode;

		let currentTargetId = currentTarget.getAttribute('data-treeitemid');

		if (this.multiSelection) {
			if (this.selectedNodes.indexOf(currentTargetId + ',') !== -1) {
				this.selectedNodes = this.selectedNodes.replace(currentTargetId + ',', '');
			}
			else {
				this.selectedNodes += currentTargetId + ',';
			}
		}
		else {
			this.selectedNodes = currentTargetId;
		}
	}

	/**
	 * This is called when one of this tree view's nodes toggler is clicked.
	 * @param {!Event} event
	 * @protected
	 */
	handleNodeTogglerClicked_(event) {
		this.toggleExpandedState_(event.delegateTarget.parentNode.parentNode.parentNode);
	}

	/**
	 * This is called when one of this tree view's nodes receives a keypress.
	 * If the pressed key is ENTER or SPACE, the node's expanded state will be toggled.
	 * @param {!Event} event
	 * @protected
	 */
	handleNodeKeyUp_(event) {
		let node = event.delegateTarget.parentNode.parentNode.parentNode.parentNode;

		if (event.keyCode === 37) {
			this.setNodeExpandedState_(node, {expanded: false});
		}
		else if (event.keyCode === 38) {
			this.focusPrevNode_(node);
		}
		else if (event.keyCode === 39) {
			this.setNodeExpandedState_(node, {expanded: true});
		}
		else if (event.keyCode === 40) {
			this.focusNextNode_(node);
		}
		else if (event.keyCode === 13 || event.keyCode === 32) {
			if (!dom.hasClass(event.delegateTarget.parentNode.parentNode, 'disabled')) {
				this.handleNodeClicked_(event);
			}
		}
	}

	/**
	 * Sets the expanded state of a node
	 * @param {!Element} node The tree node we want to change the expanded state to
	 * @param {!Object} state A state object with the new value of the expanded state
	 * @protected
	 */
	setNodeExpandedState_(node, state) {
		let path = node.getAttribute('data-treeview-path').split('-');

		let nodeObj = this.getNodeObj(path);

		nodeObj.expanded = state.expanded;

		this.nodes = this.nodes;
	}
}

/**
 * CardsTreeview state definition.
 * @type {!Object}
 * @static
 */
CardsTreeview.STATE = {
	disabledNodes: {
		validator: core.isString,
		value: ''
	},

	multiSelection: {
		validator: core.isBoolean,
		value: false
	},

	selectedNodes: {
		validator: core.isString,
		value: ''
	}
};

Soy.register(CardsTreeview, templates);

export default CardsTreeview;