import Soy from 'metal-soy/src/Soy';

import core from 'metal/src/core';
import DOM from 'metal-dom/src/dom';
import Treeview from 'metal-treeview';

import templates from './TreeviewCards.soy';

class TreeviewCards extends Treeview {
	/**
	 * Expanded the given tree node.
	 * @param {!Element} node
	 * @protected
	 */
	expandState_(node) {
		let path = node.getAttribute('data-treeview-path').split('-');

		let nodeObj = this.getNodeObj(path);

		nodeObj.expanded = true;

		this.nodes = this.nodes;
	}

	/**
	 * Focus the given tree node.
	 * @param {!Object} nodeObj
	 * @protected
	 */
	focus_(nodeObj) {
		if (nodeObj) {
			this.element.querySelector('[data-treeitemid="' + nodeObj.id + '"] .selectable').focus();
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

		if (path[path.length -1] === '0') {
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
		this.onNodeClick(event);
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
		if (event.keyCode === 37) {
			this.unexpandState_(event.delegateTarget.parentNode.parentNode);
		}
		else if (event.keyCode === 38) {
			this.focusPrevNode_(event.delegateTarget.parentNode.parentNode);
		}
		else if (event.keyCode === 39) {
			this.expandState_(event.delegateTarget.parentNode.parentNode);
		}
		else if (event.keyCode === 40) {
			this.focusNextNode_(event.delegateTarget.parentNode.parentNode);
		}
		else if (event.keyCode === 13 || event.keyCode === 32) {
			this.handleNodeClicked_(event);
		}
	}

	/**
	 * Unexpanded the given tree node.
	 * @param {!Element} node
	 * @protected
	 */
	unexpandState_(node) {
		let path = node.getAttribute('data-treeview-path').split('-');

		let nodeObj = this.getNodeObj(path);

		nodeObj.expanded = false;

		this.nodes = this.nodes;
	}
}

Soy.register(TreeviewCards, templates);

/**
 * TreeviewCards state definition.
 * @type {!Object}
 * @static
 */
TreeviewCards.STATE = {
	/**
	 * This is the function called on tree view's nodes click
	 * @type {Function}
	 * @default Fn
	 */
	onNodeClick: {
		validator: core.isFunction,
		value: function() {}
	}
};

export default TreeviewCards;