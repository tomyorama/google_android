
/*------------------------------------*/
//	Utilis - positionGrid.js
/*------------------------------------*/

(function ($) {
    var SECTION_TYPE = 1, COMPONENT_TYPE = 2, POSITION_TYPE = 3, PART_TYPE = 4;
    var RESOURCE = "Resource", MATERIAL = "Material", EXTERNAL_COST = "ExternalService", ADDITIONAL_EXPENSES = "AdditionalExpenses";
    function PostionItem(positionGridControl, id, typeOfItem, name, unit, amount, total, isContainer) {
        var self = this;
        self.name = ko.observable(name);
        self.typeOfItem = typeOfItem;
        self.id = id;
        self.amountNumber = ko.observable(amount);
        self.amount = ko.observable(Globalize.format(amount, 'n2'));
        self.price = ko.observable(total);
        self.unit = unit;
        self.isContainer = isContainer ? true : false;
        self.totalPrice = ko.computed(function () {
            return Math.round(self.amountNumber() * self.price(), 2);
        });
        // --------In field editing-------
        self.editingField = ko.observable(false);
        self.editField = function (item) {
            self.editingField(true);
            self._oldNum = self.amount();
        }
        self.amount.subscribe(function (newValue) {
            var valueDecimal = Globalize.parseFloat(newValue);
            var isValidDecimal = !isNaN(valueDecimal);
            if (isValidDecimal && isValidDecimal > 0) {
                valueDecimal = Math.round(valueDecimal, 2);
                //save if changed!
                if (self._oldNum != valueDecimal) {
                    $.ajax({
                        url: positionGridControl.options.editpositionitemurl,
                        type: "POST",
                        data: { id: self.id, quantity: Globalize.format(valueDecimal, 'n2') },
                        success: function (res) {
                            if (res.success) {
                                self.amount(Globalize.format(valueDecimal, 'n2'));
                                self.amountNumber(valueDecimal);
                                self._oldNum = Globalize.format(valueDecimal, 'n2');
                            }
                            else {
                                //alert("Error while saving!");
                                self.amount(self._oldNum);
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            //alert("Error while saving!");
                            self.amount(self._oldNum);
                        }
                    });
                }
            } else {
                self.amount(self._oldNum);
            }
            self.editingField(false);
        });
        self.formattedPrice = ko.computed(function () {
            return Globalize.format(self.price(), 'n2');
        });
        self.formattedTotalPrice = ko.computed(function () {
            return Globalize.format(self.totalPrice(), 'n2');
        });
        //--------------------------------
    }

    var postion = function (element, options) {
        this.options = options;
        this.$element = $(element);
        this.init(options);
        ko.applyBindings(this, element);
        this.loadData(null, self);
    }

    postion.prototype = {
        init: function (options) {
            var self = this;

            self.id = options.positionid;
            self.postionResourceItems = ko.observableArray();
            self.postionExtServiceItems = ko.observableArray();
            self.postionMaterialItems = ko.observableArray();
            self.postionAddExpItems = ko.observableArray();
            self.add = function (itemParam) {
                var item = new PostionItem(this, itemParam.id, itemParam.type, itemParam.name, itemParam.unit, itemParam.quantity, itemParam.unitprice);
                if (item.typeOfItem == RESOURCE) {
                    self.postionResourceItems.push(item);
                    return;
                }
                else if (item.typeOfItem == MATERIAL) {
                    self.postionMaterialItems.push(item);
                    return;
                }
                else if (item.typeOfItem == EXTERNAL_COST) {
                    self.postionExtServiceItems.push(item);
                    return;
                }
                else if (item.typeOfItem == ADDITIONAL_EXPENSES) {
                    self.postionAddExpItems.push(item);
                    return;
                }
            }
            self.remove = function (item) {
                self.$element.trigger('delete.positionGrid.utilis', ko.toJS(item));
            }
            self._removeItem = function (item) {
                if (item.typeOfItem == RESOURCE) {
                    self.postionResourceItems.remove(item);
                    return;
                }
                else if (item.typeOfItem == MATERIAL) {
                    self.postionMaterialItems.remove(item);
                    return;
                }
                else if (item.typeOfItem == EXTERNAL_COST) {
                    self.postionExtServiceItems.remove(item);
                    return;
                }
                else if (item.typeOfItem == ADDITIONAL_EXPENSES) {
                    self.postionAddExpItems.remove(item);
                    return;
                }

            }
            self.removeItemById = function (id) {
                $.each(self.postionResourceItems(), function (index, data) {
                    if (data.id == id) {
                        self.postionResourceItems.remove(data);
                        return false;
                    }
                });
                $.each(self.postionMaterialItems(), function (index, data) {
                    if (data.id == id) {
                        self.postionMaterialItems.remove(data);
                        return false;
                    }
                });
                $.each(self.postionExtServiceItems(), function (index, data) {
                    if (data.id == id) {
                        self.postionExtServiceItems.remove(data);
                        return false;
                    }
                });
                $.each(self.postionAddExpItems(), function (index, data) {
                    if (data.id == id) {
                        self.postionAddExpItems.remove(data);
                        return false;
                    }
                });
            }
            self.loadData = function (data, calcItem) {

                $.ajax({
                    url: self.options.dataurl,
                    dataType: 'json',
                    type: 'POST',
                    data: data,
                    success: function (allData) {
                        $.map(allData.rows, function (item) {
                            self.add(item);
                        });
                    }
                });

            }
            self.getAllMaterials = function () {
                var retVal = "";
                var numOfMaterials = self.postionMaterialItems().length - 1;
                $.each(self.postionMaterialItems(), function (index, data) {
                    retVal += data.name();
                    if (!(index == numOfMaterials)) {
                        retVal += ", ";
                    }
                });
                return retVal;
            }
            self.refresh = function () {
                self.postionResourceItems.removeAll();
                self.postionExtServiceItems.removeAll();
                self.postionMaterialItems.removeAll();
                self.postionAddExpItems.removeAll();
                self.loadData(null, self);
            }
            self.totalResourceItems = ko.computed(function () {
                var array = self.postionResourceItems();
                var sum = 0;
                for (var i = 0; i < array.length; i++) {
                    sum += Math.round(array[i].totalPrice(), 2);
                }
                return sum;
            });
            self.formatedTotalResourceItems = ko.computed(function () {
                return Globalize.format(self.totalResourceItems(), 'n2');
            });
            self.totalExtServiceItems = ko.computed(function () {
                var array = self.postionExtServiceItems();
                var sum = 0;
                for (var i = 0; i < array.length; i++) {
                    sum += Math.round(array[i].totalPrice(), 2);
                }
                return sum;
            });
            self.formatedTotalExtServiceItems = ko.computed(function () {
                return Globalize.format(self.totalExtServiceItems(), 'n2');
            });
            self.totalMaterialItems = ko.computed(function () {
                var array = self.postionMaterialItems();
                var sum = 0;
                for (var i = 0; i < array.length; i++) {
                    sum += Math.round(array[i].totalPrice(), 2);
                }
                return sum;
            });
            self.formatedTotalMaterialItems = ko.computed(function () {
                return Globalize.format(self.totalMaterialItems(), 'n2');
            });
            self.totalAddExpItems = ko.computed(function () {
                var array = self.postionAddExpItems();
                var sum = 0;
                for (var i = 0; i < array.length; i++) {
                    sum += Math.round(array[i].totalPrice(), 2);
                }
                return sum;
            });
            self.formatedTotalAddExpItems = ko.computed(function () {
                return Globalize.format(self.totalAddExpItems(), 'n2');
            });
            self.grandTotal = ko.computed(function () {
                var sum = Math.round(self.totalMaterialItems(), 2) + Math.round(self.totalExtServiceItems(), 2) + Math.round(self.totalResourceItems(), 2) + Math.round(self.totalAddExpItems(), 2);
                return sum;
            });
            self.formatedGrandTotal = ko.computed(function () {
                return Globalize.format(self.grandTotal(), 'n2');
            });

        }
    }

    $.fn.positionGrid = function (options) {
        return $.utilis.pluginInit.call(this, 'data-position', postion, options, $.fn.positionGrid.defaults);
    };

    $.fn.positionGrid.defaults = {
        extensions: null, // ??????
        model_extensions: null // ?????
    };


})(jQuery);
