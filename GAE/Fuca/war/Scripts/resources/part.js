jQuery(function ($) {

    var partGrid = $('#partGrid').grid();
    var partGridControl = $('#partGrid').grid('control');
    $(document).on('open.dialog.utilis', '#parts_dialog_details', function (e, args) {
        args.url.search = { id: args.target.attr('data-item-id') };
    });
    //----------------------------------------Edit Position-----------------------------------------
    $(document).on('create.dialog.utilis', '#parts_dialog_details', function (event, args) {
        $(args.dialog).calculationPositionEditControl('control');
    });
    $(document).on('loaded.dialog.utilis', '#parts_dialog_details', function (event, args) {
        $(args.dialog).calculationPositionEditControl('control').reload(args.control.dialog, args.control);
    });
    $(document).on('close.dialog.utilis', '#parts_dialog_details', function (event, args) {
        partGridControl.refresh();
    });
});