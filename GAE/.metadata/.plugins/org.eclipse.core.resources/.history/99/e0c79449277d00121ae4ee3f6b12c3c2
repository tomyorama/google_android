
/*------------------------------------*/
//	Utilis - calculationPositionEditControl.js
/*------------------------------------*/

(function ($) {


    //----------------------Model-------------------------------------------------
    var control = function (element, options) {
        this.options = options;
        this.$element = $(element);
        this.init(options);
    }

    control.prototype = {
        init: function (options) {
            var self = this;
            $(self.$element).on('open.dialog.utilis', '#positondetails_dialog_edit, #position_dialog_addservice, #position_dialog_addmaterial, #position_dialog_addwork, #position_dialog_addcosts, #addaspart_dialog_edit', function (e, args) {
                args.url.search = { id: args.target.attr('data-position-id') };
            });
            $(self.$element).on('close.dialog.utilis', '#positondetails_dialog_edit,', function (e, args) {
                if (!!args.value && self.dialogControl) {
                    self.dialogControl.reload();
                }
            })
            $(self.$element).on('close.dialog.utilis', '#position_dialog_addservice, #position_dialog_addmaterial, #position_dialog_addwork, #position_dialog_addcosts', function (event, args) {
                if (!!args.value && !!args.value.item) {
                    if (self._positionModel) {
                        self._positionModel.add(args.value.item);
                    }
                }
            });
            $(self.$element).on('add.grid.utilis', '#search_positiong_grid', function (event, data) {
                $.ajax({
                    url: self._positionModel.options.transferurl + "?id=" + data.Id + "&itemId=" + self._positionModel.id,
                    type: "POST",
                    data: {},
                    success: function (res) {
                        if (res.success) {
                            if (self._positionModel) {
                                self._positionModel.add(res);
                                //positionModel.refresh();
                            }
                        }
                    }
                });
            });
            //------------------------------------Delete Item---------------------------------------
            $(self.$element).on('delete.positionGrid.utilis', function (e, data) {
                var dialog = $('#item_position_dialog_delete').dialogManager();
                var dialogControl = dialog.dialogManager('control');
                dialogControl.url.search = { id: data.id };
                dialogControl.open();
            });
            $(self.$element).on('open.dialog.utilis', '#item_position_dialog_delete', function (e, args) {
                if (!!args.target) {
                    args.url.search = { id: args.target.attr('data-item-id') };
                }
            });
            $(self.$element).on('close.dialog.utilis', '#item_position_dialog_delete', function (e, args) {
                if (!!args.value && !!args.value.deletedositionId) {
                    self._positionModel.removeItemById(args.value.deletedositionId);
                }
                else if (!!args.value && !!args.value.deletedItemId) {
                    if (self.dialogControl) {
                        self.dialogControl.close();
                    }
                }
            });
            //---------------------------------------------------------------------------

            self.reload = function (dialog, control) {
                self._positionGrid = $(dialog).find("#position_grid")[0];
                self._searchGrid = $(dialog).find('#search_positiong_grid')[0];
                if (!self._positionGrid || !self._searchGrid) {
                    self._positionGrid = $(document).find("#position_grid")[0];
                    self._searchGrid = $(document).find('#search_positiong_grid')[0];
                }
                //get Controls;
                self._positionModel = $(self._positionGrid).positionGrid("control");
                self._searchPositionModel = $(self._searchGrid).grid("control");
                self.dialogControl = control;
            };

        },
        callBack: function CallBack(item, dialog) {
            //temp solution!!
            var element = dialog;
            if (!dialog) {
                element = document;
            }
            var name = $(element).find('span#postition_name').html();
            var quantity = $(element).find('span#position_quantity').html();
            var drawingNo = $(element).find('span#position_dwno').html();
            var ordinal = $(element).find('span#position_ordinal').html();
            if (name)
                item.name(name);
            if (drawingNo)
                item.dwno(drawingNo);
            if (quantity)
                item.amount(Globalize.parseFloat(quantity));
            if (ordinal)
                item.ordinal(ordinal);
            item.price(Number(this._positionModel.grandTotal()));
            item.material(this._positionModel.getAllMaterials());
            item.materialprice(this._positionModel.totalMaterialItems());
        }
    }

    $.fn.calculationPositionEditControl = function (options) {
        return $.utilis.pluginInit.call(this, 'data-calcpositon-control', control, options, $.fn.calculationPositionEditControl.defaults);
    };

    $.fn.calculationPositionEditControl.defaults = {
        extensions: null, // ??????
        model_extensions: null // ?????
    };


})(jQuery);
