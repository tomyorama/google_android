jQuery(function ($) {

    var materialGrid = $('#materialGrid').grid();
    var materialGridControl = $('#materialGrid').grid('control');
    var workGrid = $('#workGrid').grid();
    var workGridControl = $('#workGrid').grid('control');
    var serviceGrid = $('#serviceGrid').grid();
    var serviceGridControl = $('#serviceGrid').grid('control');
    var unitGrid = $('#unitGrid').grid();
    var unitGridControl = $('#unitGrid').grid('control');
    var workTypeGrid = $('#workTypeGrid').grid();
    var workTypeGridControl = $('#workTypeGrid').grid('control');
    $(document).on('close.dialog.utilis', '#resources_dialog_details', function (e, args) {
        if (!!args.value) {
            materialGridControl.refresh();
            workGridControl.refresh();
        }
    });
    $(document).on('close.dialog.utilis', '#material_dialog_create', function (e, args) {
        if (!!args.value) {
            materialGridControl.refresh();
        }
    });
    $(document).on('close.dialog.utilis', '#work_dialog_create', function (e, args) {
        if (!!args.value) {
            workGridControl.refresh();
        }
    });
    $(document).on('close.dialog.utilis', '#service_dialog_create', function (e, args) {
        if (!!args.value) {
            serviceGridControl.refresh();
        }
    });
    $(document).on('close.dialog.utilis', '#unit_dialog_create', function (e, args) {
        if (!!args.value) {
            unitGridControl.refresh();
        }
    });
    $(document).on('close.dialog.utilis', '#worktype_dialog_create', function (e, args) {
        if (!!args.value) {
            workTypeGridControl.refresh();
        }
    });
    $(document).on('open.dialog.utilis', '#resources_dialog_details', function (e, args) {
        args.url.search = { ResourceId: args.target.attr('data-item-id') };
    });
    $(document).on('open.dialog.utilis', '#service_dialog_details, #unit_dialog_details, #worktype_dialog_details', function (e, args) {
        args.url.search = { id: args.target.attr('data-item-id') };
    });
    $(document).on('close.dialog.utilis', '#service_dialog_details', function (e, args) {
        if (!!args.value) {
            serviceGridControl.refresh();
        }
    });
    $(document).on('close.dialog.utilis', '#unit_dialog_details', function (e, args) {
        if (!!args.value) {
            unitGridControl.refresh();
        }
    });
    $(document).on('close.dialog.utilis', '#worktype_dialog_details', function (e, args) {
        if (!!args.value) {
            workTypeGridControl.refresh();
        }
    });
});