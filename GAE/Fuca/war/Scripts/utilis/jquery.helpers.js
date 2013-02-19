/*------------------------------------*/
//	Utilis - common.js
/*------------------------------------*/

(function ($, undefined) {

	$.fn.getAttributes = function (prefix) {
		var el = this[0];
		var result = null;
		if (!!el) {
			var searchAttr = $.format('{0}-', prefix);
			var re = new RegExp(searchAttr, 'i');
			var attrs = _.chain(el.attributes)
				.filter(function (x) {
					return x.name.search(re) == 0;
				})
				.map(function (x) {
					var isString = x.value.match(/^true$|^false$|^\d+[\.]?\d+$/) == null;
					var name = _.last(x.name.split(re));
					var formatValue = ' "{0}": "{1}" ';
					if (!isString)
						formatValue = ' "{0}": {1} ';
					return $.format(formatValue, name, x.value);
				})
				.value();
			if (!!attrs.length) {
				var attrsString = $.format('{{0}}', attrs.join(','));
				result = $.utilis.parseJS(attrsString);
			}
		}
		return result;
	};

	// Forsing render invisible elements to measure size
	$.fn.forceVisible = function (fun) {
		var hidders = this.filter(':hidden').parents().filter(function () {
			return $(this).css('display') == 'none';
		});
		hidders.addClass('visible_important');
		fun.call(this);
		hidders.removeClass('visible_important');

		return this;
	}

	if (window.ko) {
		// knockout custom bindings
		var MS_JSON_DATE_REGEX = /\/(Date\(\d*\))\/$/;
		ko.bindingHandlers.text = {
			update: function (element, valueAccessor, allBindingsAccessor, viewModel) {
				var allBindings = allBindingsAccessor();
				var format = allBindings.format;
				var value = ko.toJS(valueAccessor());
				var updateValue = value;
				// Check if date
				if (_.isString(value)) {
					var matchDate = value.match(MS_JSON_DATE_REGEX);
					if (matchDate) {
						updateValue = eval('new ' + matchDate[1]); // Create date
						if (!format)
							format = 'd';
					}
				}
				if (!!format) {
					updateValue = Globalize.format(updateValue, format);
				}
				$(element).text(updateValue || '');
			}
		};
		// knockout helpers
		ko.bindingHandlers.datepicker = {
			init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
				var options = allBindingsAccessor().datepicker || {};
				$(element).datepicker(options);
			}
		};
		ko.bindingHandlers.datetimepicker = {
			init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
				var options = {
					dateFormat: $.format('{0} 00:00', $.datepicker._defaults.dateFormat)
				};
				$.extend(options, allBindingsAccessor().datetimepicker || {});
				$(element).datepicker(options);
			}
		};
		ko.bindingHandlers.buttonset = {
			init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
				var options = allBindingsAccessor().buttonset || {};
				$(element).buttonset(options);
			}
		};
		ko.format = function (format) {
			var args = _.chain(arguments).tail().map(function (n) {
				return ko.toJS(n);
			}).value();
			return $.format.apply(this, [format].concat(args));
		}
	}
})(jQuery);


