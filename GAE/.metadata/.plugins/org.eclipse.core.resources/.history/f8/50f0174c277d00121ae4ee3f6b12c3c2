$(function () {
    //------------------------Tmp items----------------------------------------------------
    var openedPositionDialog = null;
    var tmpCalcItem = null;
    //-------------------------------------End---------------------------------------------
    //-----------------------------Handle Calculation Events-------------------------------
    var calcGrid = $("#calculation").calculationGrid();
    var calcControl = calcGrid.calculationGrid('control');
    calcGrid.on('add.calculationgrid.utilis', function (event, data) {
        var dialog = $('#calculation_dialog_add').dialogManager();
        var dialogControl = dialog.dialogManager('control');
        dialogControl.url.search = { parentId: data.id };
        dialogControl.open();
    });
    $(document).on('close.dialog.utilis', '#calculation_dialog_add', function (event, args) {
        if (!!args.value && !!args.value.item) {
            calcControl._addNewItem(args.value.item);
        }
    });
    calcGrid.on('edit.calculationgrid.utilis', function (event, data) {
        tmpCalcItem = calcControl.find(data.id);
        if (tmpCalcItem.typeOfItem == calcControl.POSITION_TYPE || tmpCalcItem.typeOfItem == calcControl.PART_TYPE) {
            var dialog = $('#calculation_dialog_pedit').dialogManager();
            var dialogControl = dialog.dialogManager('control');
            dialogControl.url.search = { id: tmpCalcItem.id };
            dialogControl.open();
            openedPositionDialog = dialogControl;
        }
        else {
            var dialog = $('#calculation_dialog_edit').dialogManager();
            var dialogControl = dialog.dialogManager('control');
            dialogControl.url.search = { id: tmpCalcItem.id };
            dialogControl.open();
        }
    });
    //-----------------position edit---------------
    $(document).on('create.dialog.utilis', '#calculation_dialog_pedit', function (event, args) {
        $(args.dialog).calculationPositionEditControl('control');
    });
    $(document).on('loaded.dialog.utilis', '#calculation_dialog_pedit', function (event, args) {
        $(args.dialog).calculationPositionEditControl('control').reload(args.control.dialog, args.control);
    });
    $(document).on('close.dialog.utilis', '#calculation_dialog_pedit', function (event, args) {
        $(args.control.dialog).calculationPositionEditControl('control').callBack(tmpCalcItem, args.control.dialog);
    });
    //---------------------------------------------------
    //-------------------Section edit--------------------
    $(document).on('close.dialog.utilis', '#calculation_dialog_edit', function (event, args) {
        if (!!args.value && !!args.value.item) {
            tmpCalcItem.name(args.value.item.name);
            tmpCalcItem.ordinal(args.value.item.ordinal);
        }
    });
    //-----------------------------------End-------------------------------------
    //----------------------------------Handle Dialog events---------------------
    $(document).on('close.dialog.utilis', '#calculationdetails_dialog_edit', function (e, args) {
        if (!!args.value) {
            window.location.href = window.location.href;
        }
    });
    $(document).on('open.dialog.utilis', '#parts_dialog_details', function (e, args) {
        var parentId = $(document).find('input#ParentId').val();
        args.url.search = { choosenId: args.target.attr('data-item-id'), parentId: parentId };


    });
    $(document).on('close.dialog.utilis', '#parts_dialog_details', function (event, args) {
        if (!!args.value && !!args.value.item) {
            calcControl._addNewItem(args.value.item);
            $('#calculation_dialog_add').dialogManager('control').close();
        }
    });
    //------------------------------------Delete Item---------------------------------------
    calcGrid.on('delete.calculationgrid.utilis', function (event, data) {
        var dialog = $('#item_dialog_delete').dialogManager();
        var dialogControl = dialog.dialogManager('control');
        dialogControl.url.search = { id: data.id };
        dialogControl.open();
    });
    $(document).on('open.dialog.utilis', '#item_dialog_delete', function (e, args) {
        if (!args.url.search || !args.url.search.id) {
            args.url.search = { id: args.target.attr('data-item-id') };
        }
    });
    $(document).on('close.dialog.utilis', '#item_dialog_delete, #item_position_dialog_delete', function (e, args) {
        if (!!args.value && !!args.value.returnurl) {
            window.location = args.value.returnurl;
        }
        else if (!!args.value && !!args.value.deletedItemId) {
            calcControl.removeItemById(args.value.deletedItemId);
        }
    });
    //---------------------Report-------------------------------------
    $(document).on('open.dialog.utilis', '#calculation_dialog_report', function (e, args) {
        args.url.search = { calculationId: args.target.attr('data-item-id') };
    });
    $(document).on('close.dialog.utilis', '#calculation_dialog_report', function (e, args) {
        if (!!args.value && !!args.value.reporturl) {
            window.open(args.value.reporturl);
        }
    });
});