/*------------------------------------*/
//	Utilis - start.js
/*------------------------------------*/

jQuery(function ($) {
	// Validation groups
	$.utilis.addValidationGroupSupport();

	// Post buttons command/argument
	$.utilis.addPostCommandSupport();

	// Math.round(<number>, <decimals>)
	$.utilis.addBankersRoundingSupport();

	// jQuery UI defaults
	$.datepicker.setDefaults({
		constrainInput: false, // don't block key input
		showOtherMonths: true, // show days in prev/next months
		selectOtherMonths: true, // other months selectable
		showAnim: 'slideDown', // open with animation
		changeMonth: true, // combo month
		changeYear: true // combo year
	});

	// jQuery ajax setup
	$.ajaxSetup({
		cache: false, // Disable caching of AJAX responses 
		timeout: 60 * 1000 // 60 sec.
	});

	// Utilis unobtrusive
	var unobtrusiveInit = function (root) {
		var $el = $(root);
		// jquery.ui.datepicker
		var datepickers = $el.find('[data-u-datepicker="true"]');
		datepickers.datepicker();
		// jquery.upload
		var uploads = $el.find('[data-u-upload="true"]');
		uploads.upload();
		// jquery.tabs
		var tabs = $el.find('[data-u-tabs="true"]');
		tabs.tabs();
		// jquery.grid
		var grids = $el.find('[data-u-grid="true"]');
		grids.grid();

		var buttons = $el.find(':submit, :button');
		buttons.button();
	};
	unobtrusiveInit(document);

	// Ajax on load, bind plugins
	$(document).on('loaded.ajax', function (e) {
		unobtrusiveInit(e.target);
	});
});