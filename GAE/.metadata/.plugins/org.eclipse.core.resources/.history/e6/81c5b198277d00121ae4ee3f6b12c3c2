/// <reference name="MicrosoftAjax.js"/>

/// UTILIS - Message box 1.0

(function ($, undefined) {
	$.utilisMsgBox = function (text, override) {
		var id = 'u-message-box';
		var loadingBox = $('#' + id);

		var setPosition = function () {
			loadingBox.css({ left: 'auto' });
			var leftPos = $(window).width() / 2 - loadingBox.outerWidth() / 2;
			loadingBox.css({ left: leftPos });
		};

		if (!loadingBox.length) {
			loadingBox = $('<div class="u-message-box ui-corner-bottom" id="' + id + '"><b></b></div>');
			loadingBox.appendTo(document.body).hide();
			$(window).resize(setPosition);
			// Click to hide
			loadingBox.click(function () {
				$.utilisMsgBox(null);
			});
		}

		var isVisible = text != null && $.trim(text) != '';
		if (isVisible) {
			if (!$.utilisMsgBox.isVisible || override) {
				var el = $('<div/>').addClass('u-message-box-container').text(text);
				loadingBox.empty().append(el);
				setPosition();
				loadingBox.stop(true, true).delay(250).fadeIn(500);
			}
		}
		else {
			loadingBox.stop(true, true).hide();
		}

		// Store current visibility
		$.utilisMsgBox.isVisible = isVisible;
	}

	$.utilisMsgBox.isVisible = false;

	// jQuery ajax event handlers
	var hasError = false;
	$(document)
		.ajaxSend(function (e, xhr) {
			xhr.fail(function (xhr, type) {
				if ((type === 'error' && xhr.status !== 0) || type == 'timeout') {
					hasError = true;
					var ex = null;
					try {
						ex = $.parseJSON(xhr.responseText);
					} catch (ee) { }
					var msg = 'Dogodila se neočekivana greška na serveru.';
					if (!!ex && !!ex.Message) {
						var msg = $.format('{0}', ex.Message);
						if (!!ex.StackTrace)
							msg = $.format('{0}\n\n{1}', msg, ex.StackTrace);
					}
					$.utilisMsgBox(msg, true);
				}
			});
		})
		.ajaxStart(function (e) {
			hasError = null;
			$.utilisMsgBox('Učitavanje. Molimo pričekajte...', true);
		})
		.ajaxStop(function (e) {
			if (!hasError) {
				$.utilisMsgBox(null);
			}
		});
})(jQuery);
