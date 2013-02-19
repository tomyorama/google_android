/*------------------------------------*/
//	Utilis - jquery.document.js
/*------------------------------------*/

(function ($) {

	var DOCUMENT = 'document',
		FOLDER = 'folder';

	// Editor template delete data item
	$(document).on('click', '.document-editor .u-icon-delete', function (e) {
		var target = $(e.currentTarget);
		var tableRow = target.parents('tr:first');
		var hidden = tableRow.find(':hidden.data_delete_field');
		hidden.val(true);
		tableRow.hide();
	});

	var control = function (element, options) {
		this.options = options;
		this._element = $(element);

		// Create tree
		this.jstree = this._element.find('#tree').jstree({
			core: {
				html_titles: true
			},
			"json_data": {
				"ajax": {
					type: 'GET',
					url: this.options.url,
					data: $.proxy(function (n) {
						var itemId = this.options.rootId;
						if (n != -1) {
							var item = n.data('item');
							itemId = item.id;
						}
						return {
							id: itemId
						};
					}, this),
					success: $.proxy(function () {
						this.setVisibleButtons();
					}, this)
				}
			},
			"plugins": ["themes", "ui", "json_data"]
		});
		this.jsTreeRef = $.jstree._reference(this.jstree);

		// Tree on loaded
		this.jstree.on('loaded.jstree refresh.jstree', $.proxy(function (e, data) {
			var selectSelector = 'li[data_node_id]:first';
			if (this.selectedItem != null) {
				selectSelector = $.format('li[data_node_id={0}]', this.selectedItem.id);
				if (!$(selectSelector).length) {
					selectSelector = $.format('li[data_node_id={0}]', this.selectedItem.parentId);
				}
			}
			this.jsTreeRef.select_node(selectSelector);
			this.jsTreeRef.open_node();
			this.setVisibleButtons();
		}, this));

		// Tree on select node
		this.jstree.on('select_node.jstree', $.proxy(function (e, data) {
			this.selectedItem = data.rslt.obj.data('item');
			this.setVisibleButtons();
		}, this));

		// Commands
		this._element.on('click', '[data-command]', $.proxy(function (e) {
			var btn = $(e.currentTarget);
			var command = btn.attr('data-command');
			switch (command) {
				case 'refresh':
					this.jsTreeRef.refresh(-1);
					break;
			}
		}, this));

		// Dialog events
		this._element.on('create.dialog.utilis', '.document_dialog', $.proxy(function (e, args) {
			// Child assignments dialogs
			args.dialog.on('open.dialog.utilis', '.assignment_confirm_action', $.proxy(function (e_1, args_1) {
				args_1.url.search = args_1.url.search || {};
				if (!args_1.url.search.command) {
					var command = args_1.target.attr('data-command');
					args_1.url.search.command = command;
				}
			}, this));
			args.dialog.on('open.dialog.utilis', '.document_assignment_note_dialog', $.proxy(function (e_1, args_1) {
				var id = args_1.target.attr('data-id');
				args_1.url.search = { id: id };
			}, this));
			args.dialog.on('close.dialog.utilis', '.assignment_confirm_action', $.proxy(function (e_1, args_1) {
				if (!!args_1.value) {
					args.control.close();
					this.jsTreeRef.refresh(-1);
					$('#assignmentsGrid').grid('control').refresh();
				}
			}, this));
			// Child dialogs
			args.dialog.on('close.dialog.utilis', '.document_dialog', $.proxy(function (e_1, args_1) {
				if (!!args_1.value) {
					args.control.close();
					this.jsTreeRef.refresh(-1);
				}
			}, this));
		}, this));

		this._element.on('loaded.dialog.utilis', '.document_dialog', $.proxy(function (e, args) {
			// Template codes
			args.dialog.find(".multi-accordion")
				.addClass("ui-accordion ui-accordion-icons ui-widget ui-helper-reset")
				.find("h3")
				.addClass("ui-accordion-header ui-helper-reset ui-state-default ui-corner-top ui-corner-bottom")
				.hover(function () { $(this).toggleClass("ui-state-hover"); })
				.prepend('<span class="ui-icon ui-icon-triangle-1-e"></span>')
				.click(function () {
					$(this)
						.toggleClass("ui-accordion-header-active ui-state-active ui-state-default ui-corner-bottom")
						.find(".ui-icon").toggleClass("ui-icon-triangle-1-e ui-icon-triangle-1-s").end()
						.next().slideToggle();
					return false;
				})
				.next()
				.addClass("ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom")
				.hide();
		}, this));

		this._element.on('open.dialog.utilis', '.document_dialog', $.proxy(function (e, args) {
			var parentId = this.selectedItem.id;
			if (this.selectedItem.type == DOCUMENT) {
				parentId = this.selectedItem.parentId;
			}
			args.url.search = { id: this.selectedItem.id, parentId: parentId };
		}, this));

		this._element.on('close.dialog.utilis', '.document_dialog', $.proxy(function (e, args) {
			if (!!args.value) {
				this.jsTreeRef.refresh(-1);
			}
		}, this));

		// Permission dialog events
		this._element.on('create.dialog.utilis', '.permission_dialog', $.proxy(function (e, args) {
			// Bind child dialog event
			args.dialog.on('open.dialog.utilis', '.permission_dialog_add', $.proxy(function (e_1, args_1) {
				var itemId = args_1.target.attr('data-item-id');
				args_1.url.search = { itemId: itemId };
			}, this));
			args.dialog.on('open.dialog.utilis', '.permission_dialog_edit, .permission_dialog_delete', $.proxy(function (e_1, args_1) {
				var itemId = args_1.target.attr('data-item-id');
				var principalId = args_1.target.attr('data-principal-id');
				args_1.url.search = { principalId: principalId, itemId: itemId };
			}, this));
			args.dialog.on('close.dialog.utilis', '.permission_dialog_add, .permission_dialog_edit, .permission_dialog_delete', $.proxy(function (e_1, args_1) {
				if (!!args_1.value) {
					args.control.reload();
				}
			}, this));
		}, this));

		// Document assignements
		this._element.on('create.dialog.utilis', '.assignment_dialog', $.proxy(function (e, args) {
			args.dialog.on('open.dialog.utilis', '.assignment_dialog_delete', $.proxy(function (e_1, args_1) {
				var assignmentId = $(args_1.target).attr('data-id');
				args_1.url.search = { id: assignmentId };
			}, this));
			args.dialog.on('close.dialog.utilis', '.assignment_dialog_delete', $.proxy(function (e_1, args_1) {
				if (!!args_1.value) {
					args.control.reload();
				}
			}, this));
		}, this));
	};

	control.prototype = {
		setVisibleButtons: function () {
			var buttons = this._element.find('.tree-button').hide();
			var item = this.selectedItem;
			if (!!item) {
				buttons.filter($.format('.tree-{0}', item.type)).show();
				if (item.status != null && item.status != 'undefined') {
					buttons.filter($.format('.tree-status, .tree-status-{0}', item.status)).show();
				}
			}
		},
		refreshAndClose: function () {
			$.fn.dialogManager.closeActive();
			this.jsTreeRef.refresh(-1);
		}
	}

	$.fn.document = function (options) {
		return $.utilis.pluginInit.call(this, 'data-u-document', control, options, $.fn.document.defaults);
	};

	$.fn.document.defaults = {
		url: null
	};

})(jQuery);
