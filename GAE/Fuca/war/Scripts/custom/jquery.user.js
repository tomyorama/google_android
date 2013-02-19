/*------------------------------------*/
//	Utilis - jquery.user.js
/*------------------------------------*/

(function ($) {

	var control = function (element, options) {
		this.options = options;
		this._element = $(element);

		// Dialog OPEN events
		this._element.on('open.dialog.utilis', '.user_dialog_add, .user_dialog_edit', $.proxy(function (e, args) {
			var id = args.target.attr('data-id');
			args.url.search = { userId: id };
		}, this));

		// Dialog CREATE events
		this._element.on('create.dialog.utilis', '.user_dialog_add, .user_dialog_edit', $.proxy(function (e, args) {
			args.dialog.on('open.dialog.utilis', '.user_dialog_delete, .user_dialog_password_edit', $.proxy(function (e_1, args_1) {
				var assignmentId = $(args_1.target).attr('data-id');
				args_1.url.search = { userId: assignmentId };
			}, this));
		}, this));
	};

	control.prototype = {}

	$.fn.user = function (options) {
		return $.utilis.pluginInit.call(this, 'data-u-user', control, options, $.fn.document.defaults);
	};

	$.fn.document.defaults = {
		url: null
	};

})(jQuery);
