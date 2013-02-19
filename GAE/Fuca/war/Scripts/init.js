/*------------------------------------*/
//	Tomy - init.js
/*------------------------------------*/

jQuery(function($) {

	function refresh() {
		var datepickers = $(document).find('[data-u-datepicker="true"]');
		datepickers.datepicker({
			dateFormat : 'dd.mm.yy'
		});
		var buttons = $(document).find(
				':submit, :button,[data-u-button="true"]');
		buttons.button();
	}
	refresh();
});