AUI.add(
	'liferay-entry-selector',
	function(A) {
		var AArray = A.Array;

		var Lang = A.Lang;

		var NAME = 'entryselector';

		var TPL_SUMMARY_ENTRY = '<li class="list-entry" data-key="{key}" data-label="{label}">' +
			'<span>{label}</span>' +
			'<button class="btn btn-default remove-button" type="button">' +
				'<i class="icon-remove"></i>' +
			'</button>' +
		'</li>';

		var TPL_SELECT_LIST = '<ul class="list-unstyled">{entries}</ul>';

		var TPL_SELECT_ENTRY = '<li>' +
			'<label>' +
				'<input class="switch" data-key={key} data-label={label} type="checkbox">' +
				'<span aria-hidden="true" class="switch-bar">' +
					'<span class="switch-toggle" data-label-off="{label}" data-label-on="{label}">' +
						'<span class="button-icon button-icon-on switch-icon icon-ok-circle"></span>' +
						'<span class="button-icon button-icon-off switch-icon icon-{icon}"></span>' +
					'</span>' +
				'</span>' +
			'</label>' +
		'</li>';

		var EntrySelector = A.Component.create(
			{
				ATTRS: {
					entries: {
						setter: '_setEntries',
						validator: Lang.isArray
					},

					selectedEntries: {
						validator: Lang.isArray
					}
				},

				AUGMENTS: [Liferay.PortletBase],

				EXTENDS: A.Base,

				NAME: NAME,

				prototype: {
					initializer: function() {
						var instance = this;

						instance._dialogId = A.guid();
						instance._selectDialogContent = instance._getSelectDialogContent();

						instance._bindUI();
					},

					_bindUI: function() {
						var instance = this;

						instance._eventHandles = [
							instance.on('selectedEntriesChange', instance._syncUI, instance),
							instance.one('.select-button').on('click', instance._onSelectClick, instance),
							instance.one('.selected-items').delegate('click', instance._onSummaryItemClick, '.remove-button', instance)
						];
					},

					_syncUI: function(event) {
						var instance = this;

						var selectedEntriesKeys = [];

						var selectedEntriesNode = instance.one('.selected-items');

						selectedEntriesNode.empty();

						var selectedEntries = event ? event.newVal : instance.get('selectedEntries');

						AArray.each(
							selectedEntries,
							function(item) {
								selectedEntriesKeys.push(item.key);

								selectedEntriesNode.append(
									Lang.sub(TPL_SUMMARY_ENTRY, item)
								);
							}
						);

						var selectDialogInputs = instance._selectDialogContent.all('input');

						selectDialogInputs.each(
							function(item) {
								item.attr('checked', selectedEntriesKeys.indexOf(item.attr('data-key')) !== -1)
							}
						);

						instance.one('input').val(selectedEntriesKeys.join(','));
					},

					destructor: function() {
						var instance = this;

						(new A.EventHandle(instance._eventHandles)).detach();
					},

					_getSelectDialog: function() {
						var instance = this;

						return Liferay.Util.getWindow(instance._dialogId);
					},

					_getSelectDialogContent: function() {
						var instance = this;

						var entries = AArray.reduce(
							instance.get('entries'),
							'',
							function(previousValue, currentValue) {
								return previousValue + Lang.sub(TPL_SELECT_ENTRY, currentValue);
							}
						);

						var content = Lang.sub(
							TPL_SELECT_LIST,
							{
								entries: entries
							}
						);

						return A.Node.create(content);
					},

					_getSelectDialogFooterToolbar: function() {
						var instance = this;

						var footerToolbar = [
							{
								label: Liferay.Language.get('done'),
								on: {
									click: A.bind('_updateSelectedEntries', instance)
								}
							},
							{
								label: Liferay.Language.get('cancel'),
								on: {
									click: A.bind('_hideSelectDialog', instance)
								}
							}
						];

						return footerToolbar;
					},

					_hideSelectDialog: function() {
						var instance = this;

						instance._getSelectDialog().hide();
					},

					_onSelectClick: function(event) {
						var instance = this;

						instance._showSelectDialog();
					},

					_onSummaryItemClick: function(event) {
						var instance = this;

						event.currentTarget.ancestor('.list-entry').remove();

						var selectedEntries = [];

						instance.one('.selected-items').all('.list-entry').each(
							function(item) {
								selectedEntries.push(
									{
										key: item.attr('data-key'),
										label: item.attr('data-label')
									}
								);
							}
						);

						instance.set('selectedEntries', selectedEntries);
					},

					_setEntries: function(val) {
						var instance = this;

						var selectedEntries = AArray.filter(
							val,
							function(item) {
								return item.selected;
							}
						);

						instance.set('selectedEntries', selectedEntries);

						return val;
					},

					_showSelectDialog: function() {
						var instance = this;

						var dialog = instance._getSelectDialog();

						if (!dialog) {
							var dialogConfig = {
								'toolbars.footer': instance._getSelectDialogFooterToolbar(),
								width: 580
							};

							Liferay.Util.openWindow(
								{
									dialog: dialogConfig,
									id: instance._dialogId,
									title: Liferay.Language.get('select')
								},
								function(dialog) {
									dialog.setStdModContent('body', instance._selectDialogContent);
								}
							);
						}
						else {
							instance._syncUI();
							dialog.show();
						}
					},

					_updateSelectedEntries: function() {
						var instance = this;

						var dialog = instance._getSelectDialog();

						var selectedEntries = [];

						dialog.bodyNode.all('input:checked').each(
							function(item) {
								selectedEntries.push(
									{
										key: item.attr('data-key'),
										label: item.attr('data-label')
									}
								);
							}
						);

						instance.set('selectedEntries', selectedEntries);

						instance._hideSelectDialog();
					}
				}
			}
		);

		Liferay.EntrySelector = EntrySelector;
	},
	'',
	{
		requires: ['aui-component', 'liferay-portlet-base']
	}
);