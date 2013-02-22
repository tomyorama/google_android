/*------------------------------------*/
//	Utilis - utilis.js
/*------------------------------------*/

(function (utilis, $, undefined) {

	var dataFormValidationGroupKey = '__dataFormValidation__';

	$.extend(utilis, {
		// Init jquery plugin
		// $.fn.<plugin name> = function (options) {
		//		return $.utilis.pluginInit.call(this, 'data-u-<unobtrusive name>', <control class>, options, $.fn.<plugin name>.defaults);
		// };
		pluginInit: function (unobtrusiveName, control, options, defaults) {
			var dataAttrOptions = $.format('{0}-options', unobtrusiveName);
			var init = function (el, opt) {
				var $el = $(el);
				var controlInstance = $el.data(unobtrusiveName);
				if (!controlInstance) {
					var attrOptions = $el.getAttributes(dataAttrOptions);
					var attrObjectOptions = utilis.parseJS($el.attr(dataAttrOptions));
					var mergedOptions = $.extend(true, {}, defaults, attrOptions, attrObjectOptions, opt);
					controlInstance = new control(el, mergedOptions);
					$el.data(unobtrusiveName, controlInstance);
				}
				return controlInstance;
			};
			if (typeof (options) == 'string' && options == 'control') {
				var controls = this.map(function (i, n) { return init(n, null); });
				return controls[0];
			} else {
				var controls = this.map(function (i, n) { return init(n, options); });
			}
			return this;
		},
		addValidationGroupSupport: function () {
			var orginalCheck = $.validator.prototype.check;
			$.extend(true, $.validator, {
				prototype: {
					// extend jquery validation check function
					check: function (element) {
						// Find validation group
						var $form = $(this.currentForm);
						var info = $form.data(dataFormValidationGroupKey);
						var validSelector = ':not([data-val-group])';
						if (!!info && !!info.group) {
							// Validate group only
							validSelector = $.format('[data-val-group~={0}]', info.group);
						}
						if (!$(element).is(validSelector)) {
							this.successList.push(element);
							return true;
						}
						return orginalCheck.call(this, element);
					}
				}
			});

			// Save validation group
			var onSubmit = function (e) {
				var $form = $(this).parents('form:first');
				var valGroup = $(this).attr('data-val-group');
				var info = { group: valGroup };
				$form.data(dataFormValidationGroupKey, info);
				$.validator.unobtrusive.parse($form);
			};
			$(document).on('click', 'form :submit', onSubmit);
			$(document).on('change', 'form [data-u-autopostback="true"][data-val-group]', onSubmit);
		},
		// Unobtrusive: add 'submitCommand' and 'submitArgument' to post data
		// input[type=submit][data-u-button-command]
		// input[type=submit][data-u-button-argument]
		addPostCommandSupport: function () {
			var onSubmit = function (e) {
				var btn = $(this);
				var command = btn.attr('data-u-button-command');
				var argument = btn.attr('data-u-button-argument');

				// Find/create hidden field
				var form = btn.parents('form:first');
				var addRemoveHidden = function (hiddenName, value) {
					var inputArg = form.find($.format(':hidden[name={0}]', hiddenName));
					if (!inputArg.length)
						inputArg = $('<input type="hidden" />').attr('name', hiddenName).insertAfter(btn);
					inputArg.val(value);
				};

				if (!!command) {
					addRemoveHidden('submitCommand', command);
				}
				if (!!argument) {
					addRemoveHidden('submitArgument', argument);
				}
				if (!!e.data && !!e.data.submit)
					form.submit();
			};

			$(document).on('click', 'form :submit[data-u-button-command], form :submit[data-u-button-argument]', onSubmit);
			$(document).on('change', 'form [data-u-autopostback="true"]', { submit: true }, onSubmit);
		},
		// Math.round(<number>, <decimals>)
		addBankersRoundingSupport: function () {
			var orginalRound = Math.round;
			function evenRound(num, decimalPlaces) {
				var result = null;
				if (decimalPlaces == null) {
					result = orginalRound(num);
				} else {
					var d = decimalPlaces || 0;
					var m = Math.pow(10, d);
					var n = +(d ? num * m : num).toFixed(8); // Avoid rounding errors
					var i = Math.floor(n), f = n - i;
					var r = (f == 0.5) ? ((i % 2 == 0) ? i : i + 1) : orginalRound(n);
					result = d ? r / m : r;
				}
				return result;
			}
			Math.round = evenRound;
		},
		// Url helpers
		// Get url info(search & hash values)
		parseUrl: function (url) {
			var parseObj = function (str) {
				var r = null, re = /([^&\?#=]+)=([^&]*)/g, m;
				if (str) {
					r = {};
					var s = str[0];
					while (m = re.exec(s)) {
						r[m[1]] = m[2];
					}
				}
				return r;
			}
			var urlDecoded = decodeURIComponent(url || '');
			var hostPathMatch = urlDecoded.match(/[^\?#]+/);
			var hostPath = hostPathMatch != null ? hostPathMatch[0] : null;
			var searchMatch = urlDecoded.match(/(?=\?)[^#]+/);
			var hashMatch = urlDecoded.match(/(?=#).+/);
			return {
				search: parseObj(searchMatch),
				hash: parseObj(hashMatch),
				path: hostPath,
				combine: function () {
					var r = '';
					if (this.path)
						r = r + this.path;
					if (this.search)
						r = r + '?' + $.param(this.search);
					if (this.hash)
						r = r + '#' + $.param(this.hash);
					return r;
				}
			};
		},
		// Parse string as javascript
		parseJS: function (JSstring) {
			var result = null;
			try {
				result = eval($.format('({0})', JSstring));
			} catch (ex) { }
			return result;
		},
		// Get property by name dot separated
		getObject: function (obj, name) {
			var names = name.split('.');
			$.each(names, function (i, n) {
				obj = obj[n];
				if (obj == null)
					return false; // break
			});
			return obj;
		}
	});
})(jQuery.utilis = jQuery.utilis || {}, jQuery);