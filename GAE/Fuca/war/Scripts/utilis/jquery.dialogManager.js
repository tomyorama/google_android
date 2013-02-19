/*------------------------------------*/
//	Utilis - jquery.dialogManager.js
/*------------------------------------*/

(function ($) {
	$(document).on('click', '[data-u-dialog-open]', function (e) {
		var target = $(e.currentTarget);
		var dialogId = target.attr('data-u-dialog-open');

		var dialog = $('#' + dialogId).dialogManager();
		var dialogControl = dialog.dialogManager('control');
		dialogControl.open(target);
		e.preventDefault();
	});

	var control = function (element, options) {
		this.options = options;
		this._element = $(element);
		this.isOpen = false;
		this.url = $.utilis.parseUrl(this.options.url);

		if (this.options.autoopen)
			this.open();
	};

	control.prototype = {
		open: function (target) {
			if (!this.isOpen) {
				// Trigger on open event
				var continueOpen = true;
				var args = {
					cancel: function () { continueOpen = false; },
					control: this,
					target: target,
					url: $.extend(true, {}, this.url)
				};
				this._element.trigger('open.dialog.utilis', args);
				if (continueOpen) {
					var url = args.url;
					this.reload(url, true);
				}
			}
		},
		close: function (closeObj) {
			// Trigger on open event
			var continueClose = true;
			var args = {
				cancel: function () { continueClose = false; },
				control: this,
				value: closeObj || null
			};
			this._element.trigger('close.dialog.utilis', args);
			if (continueClose) {
				this.dialog.dialog('destroy');
				this.dialog.remove();
				this.dialog = null;
				this.isOpen = false;
				$.fn.dialogManager.active = null;
			}
		},
		reload: function (url, forceOpen) {
			this.lastUrl = url || this.lastUrl;
			if (!!this.lastUrl) {
				if (this.isOpen || !!forceOpen) {
					// Get async
					$.ajax({
						url: this.lastUrl.combine()
					})
					.done($.proxy(function (data) {
						this._createDialog(data);
						/* UTILIS - ajax loaded event */
						this.dialog.trigger('loaded.ajax');
					}, this));
				}
			} else {
				this._createDialog();
			}
		},

		// private
		_createDialog: function (html) {
			var isDialogExist = this.dialog != null;
			var dialogContent = this.dialog || $('<div/>');
			dialogContent.html(html);
			var dialogButtons = this._getDialogButtons(dialogContent);
			var dialogOptions = $.extend({}, this.options.dialog, {
				buttons: dialogButtons,
				close: $.proxy(function () { this.close(); }, this)
			});
			this.dialog = dialogContent.dialog(dialogOptions);
			this.isOpen = true;
			if (!isDialogExist) {
				// Handle ajax loaded event
				this.dialog.on('loaded.ajax', $.proxy(this._ajaxLoaded, this));
				// Trigger dialog create event
				var args = { control: this, dialog: this.dialog };
				this._element.trigger('create.dialog.utilis', args);
			}
			$.fn.dialogManager.active = this;
		},
		_getDialogButtons: function (container) {
			var inputButtons = container.find('[data-u-dialog-button=true]');
			var dialogButtons = inputButtons.map($.proxy(function (i, n) {
				var $n = $(n);
				var isCloseBtn = $n.is('[data-u-dialog-button-close=true]');
				var clickHandler = null;
				if (isCloseBtn)
					clickHandler = function () { $(this).dialog('close'); };
				else
					clickHandler = function (e) { $n.click(); };
				var button = {
					text: $n.val() || $n.text(),
					click: clickHandler,
					'class': $n.attr('class')
				};
				return button;
			}, this));
			inputButtons.hide();
			return dialogButtons;
		},
		_ajaxLoaded: function () {
			// Update dialog buttons
			var dialogButtons = this._getDialogButtons(this.dialog);
			if (this.dialog) {
				this.dialog.dialog('option', 'buttons', dialogButtons);
			}

			// Check if have save obj element
			var dataSave = this.dialog.find('[data-u-dialog-save]:first').attr('data-u-dialog-save');
			if (!!dataSave) {
				// Close dialog with return argument
				var retValue = $.parseJSON(dataSave);
				this.close(retValue);
			} else {
				// Trigger dialog loaded event
				var args = { control: this, dialog: this.dialog };
				this._element.trigger('loaded.dialog.utilis', args);
			}
		}
	}

	$.fn.dialogManager = function (options) {
		return $.utilis.pluginInit.call(this, 'data-u-dialog', control, options, $.fn.dialogManager.defaults);
	};

	$.fn.dialogManager.active = null;

	$.fn.dialogManager.closeActive = function () {
		var active = $.fn.dialogManager.active;
		if (!!active) {
			active.close();
		}
	};

	$.fn.dialogManager.defaults = {
		dialog: {
			modal: true,
			resizable: false
		},
		autoopen: false,
		url: null
	};
})(jQuery);