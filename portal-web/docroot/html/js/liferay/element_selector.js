AUI.add(
	'liferay-element-selector',
	function(A) {
		var AArray = A.Array;

		var Lang = A.Lang;

		var NAME = 'elementselector';

		var SELECTION_LIST_CONTAINER_TPL = '<ul class="row"></div>';

		var SELECTION_LIST_ENTRY_TPL = '<li class="lfr-element-selector-item col-md-3">' +
			'<label>' +
				'<input type="checkbox" name="{key}" />' +
				'<span class="icon icon-ok"></span>' +
				'<span class="icon icon-{icon}"></span>' +
				'{name}' +
			'</label>' +
		'</li>';

		var STR_BOUNDING_BOX = 'boundingBox';

		var SUMMARY_ENTRY_TPL = '<li class="textboxlistentry">' +
			'<span class="textboxlistentry-content">' +
				'<span class="textboxlistentry-text">{name}</span>' +
				'<span class="textboxlistentry-remove">' +
					'<i class="icon icon-remove"></i>' +
				'</span>' +
			'</span>' +
		'</li>';

		var ElementSelector = A.Component.create(
			{
				ATTRS: {
					elements: {
						validator: Lang.isArray,
						value: []
					},

					summaryEntryTpl: {
						validator: Lang.isString,
						value: SUMMARY_ENTRY_TPL
					}
				},

				EXTENDS: A.Widget,

				NAME: NAME,

				prototype: {
					bindUI: function() {
						var instance = this;

						var boundingBox = instance.get(STR_BOUNDING_BOX);

						instance._eventHandles = [
							boundingBox.one('button').on('click', instance._showSelectPopup, instance)
						];
					},

					syncUI: function() {
						var instance = this;

						var boundingBox = instance.get(STR_BOUNDING_BOX);

						var summaryNode = boundingBox.one('ul');

						summaryNode.empty();

						AArray.each(
							instance.get('elements'),
							function(item) {
								if (item.selected) {
									summaryNode.append(
										Lang.sub(instance.get('summaryEntryTpl'), item)
									);
								}
							}
						);
					},

					_onEntryClick: function(event) {
						var instance = this;

						console.log(event);
					},

					_showSelectPopup: function(event) {
						var instance = this;

						var popup = instance._getPopup();

						//popup.entriesNode.html(TPL_LOADING);
						//
						//popup.titleNode.html(Liferay.Language.get('tags'));
						//
						//popup.show();

						var entriesList = popup.bodyNode.one('ul');

						AArray.each(
							instance.get('elements'),
							function(item) {
								entriesList.append(
									Lang.sub(SELECTION_LIST_ENTRY_TPL, item)
								);
							}
						);
					},


					_getPopup: function() {
						var instance = this;

						if (!instance._popup) {
							var popup = Liferay.Util.Window.getWindow(
								{
									dialog: {
										width: 600
									}
								}
							);

							var bodyNode = popup.bodyNode;

							bodyNode.append(Lang.sub(SELECTION_LIST_CONTAINER_TPL));

							instance._eventHandles.push(
								bodyNode.delegate('click', instance._onEntryClick, '.textboxlistentry', instance)
							);

							/*
							var bodyNode = popup.bodyNode;

							bodyNode.html(STR_BLANK);

							var searchForm = A.Node.create(Lang.sub(TPL_SEARCH_FORM, [Liferay.Language.get('search')]));

							bodyNode.append(searchForm);

							var searchField = searchForm.one('input');

							var entriesNode = A.Node.create(TPL_TAGS_CONTAINER);

							bodyNode.append(entriesNode);

							popup.searchField = searchField;
							popup.entriesNode = entriesNode;

							instance._popup = popup;

							instance._initSearch();

							var onCheckboxClick = A.bind('_onCheckboxClick', instance);

							entriesNode.delegate('click', onCheckboxClick, 'input[type=checkbox]');
							*/

							instance._popup = popup;
						}

						return instance._popup;
					}
				}
			}
		);

		Liferay.ElementSelector = ElementSelector;
	},
	'',
	{
		requires: ['aui-component']
	}
);