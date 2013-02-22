
/*------------------------------------*/
//	Utilis - calculationGrid.js
/*------------------------------------*/

(function ($) {

    //------------------------------------------Helper Functions----------------------------
    function addZeros(value) {
        var n = Number(value);
        return (n < 10) ? '00' + n : (n < 100) ? '0' + n : '' + n;
    }
    //--------------------------------------------------------------------------------------
    //----------------------------Knockoot--------------------------------------------------
    function CalculationItem(id, level, path, typeOfItem, name, ordinal, dwno, material, materialprice, amount, total, parent, allItems, options) {
        var self = this;
        //types-------
        self.SECTION_TYPE = 1;
        self.COMPONENT_TYPE = 2;
        self.POSITION_TYPE = 3;
        self.PART_TYPE = 4;
        //------------
        self.options = options;
        self.name = ko.observable(name);
        self.ordinal = ko.observable(ordinal);
        self.typeOfItem = typeOfItem;
        self.id = id;
        self.level = level;
        self.path = path;
        self.parentId = parent.id;
        self.padding = self.level * 40;
        self.isContainer = (typeOfItem == self.SECTION_TYPE || typeOfItem == self.COMPONENT_TYPE);
        self.loadedChildern = ko.observable(self.isContainer);
        self.expanded = ko.observable(false);
        self.dwno = ko.observable(dwno);
        self.material = ko.observable(material);
        self.materialprice = ko.observable(materialprice);
        self.amount = ko.observable(amount);
        self.price = ko.observable(total);

        self.childern = function () {
            var retList = [];
            var allCHierarhy = allItems();
            for (var i = 0; i < allCHierarhy.length; i++) {
                if (allCHierarhy[i].parentId == self.id) {
                    retList.push(allCHierarhy[i]);
                }
            }
            return retList;
        }
        self.isVisible = ko.computed(function () {
            return parent.expanded() && parent.isVisible();
        });
        self.fullName = ko.computed(function () {
            return addZeros(self.ordinal()) + " " + self.name();
        });
        self.totalPrice = ko.computed(function () {
            var price = 0;
            if (self.loadedChildern()) {
                var childern = self.childern();
                for (var i = 0; i < childern.length; i++) {
                    price += Math.round(childern[i].totalPrice(), 2);
                }
                return price;
            } else {
                price = Math.round(self.price(), 2) * Math.round(self.amount(), 2);
                return price;
            }
        });
        self.formattedPrice = ko.computed(function () {
            return Globalize.format(self.price(), 'n2') + " " + self.options.currency;
        });
        self.formattedMaterialprice = ko.computed(function () {
            return Globalize.format(self.materialprice(), 'n2') + " " + self.options.currency;
        });
        self.formattedTotalPrice = ko.computed(function () {
            return Globalize.format(self.totalPrice(), 'n2') + " " + self.options.currency;
        });
        self.formattedAmount = ko.computed(function () {
            return Globalize.format(self.amount(), 'n2');
        });

    }



    //----------------------Model-------------------------------------------------
    var calculation = function (element, options) {
        this.options = options;
        this.$element = $(element);
        this.init(options);
        ko.applyBindings(this, element);
        this.loadData(null, this);
    }

    calculation.prototype = {
        init: function (options) {
            var self = this;
            self.id = options.calculactionid;
            self.calculationItems = ko.observableArray();
            //types
            self.SECTION_TYPE = 1;
            self.COMPONENT_TYPE = 2;
            self.POSITION_TYPE = 3;
            self.PART_TYPE = 4;
            //on click Add new item
            self.add = function (calc) {
                self.$element.trigger('add.calculationgrid.utilis', ko.toJS(calc));
            }
            //helper function for add new Item
            self._addNewItem = function (res) {

                var parent = null;
                if (res.parent == self.id) {
                    parent = self;
                }
                else {
                    parent = self.find(res.parent);
                }
                if (!parent.loadedChildern()) {
                    //if not loaded --> load! 
                    self.expand(parent);
                }
                else {
                    //insert 
                    var newItem = new CalculationItem(res.id, res.level, res.path, res.rowtype, res.name, res.ordinal, res.dwgNo != null ? res.dwgNo : "", res.material, res.materialprice, res.pcs, res.price, parent, self.calculationItems, self.options);
                    self.calculationItems.splice(self.calculationItems.indexOf(parent) + 1, 0, newItem);

                    // expand if not expanded
                    if (!parent.expanded())
                        self.expand(parent);
                }

            }
            //On click add button
            self.addRootItem = function () {
                self.$element.trigger('add.calculationgrid.utilis', ko.toJS({ id: self.id }));
            }
            //EDIT
            self.edit = function (calc) {
                self.$element.trigger('edit.calculationgrid.utilis', ko.toJS(calc));
            }
            //remove item
            self._removeItem = function (calc) {
                var childern = calc.childern();
                $.each(childern, function (index, value) {
                    self.calculationItems.remove(value);
                });
                self.calculationItems.remove(calc);
            }
            self.removeItemById = function (id) {
                var calc = self.find(id);
                var childern = calc.childern();
                $.each(childern, function (index, value) {
                    self.calculationItems.remove(value);
                });
                self.calculationItems.remove(calc);
            }
            self.remove = function (calc) {
                self.$element.trigger('delete.calculationgrid.utilis', ko.toJS(calc));
            }
            //set expanded i visible to true for root!
            self.isVisible = function () {
                return true;
            }
            self.expanded = self.isVisible;
            self.loadedChildern = self.expanded;


            //find item by id
            self.find = function (id) {
                var retVal = null;
                $.each(self.calculationItems(), function (index, value) {
                    if (value.id == id) {
                        retVal = value;
                        return false;
                    }
                });
                return retVal;

            }
            //SUM of all elements!
            self.totalSum = ko.computed(function () {
                var price = 0;
                var tmpList = self.calculationItems();
                for (var i = 0; i < tmpList.length; i++) {
                    if (tmpList[i].parentId == self.id) {
                        price += Math.round(tmpList[i].totalPrice(), 2);
                    }

                }
                return price;
            });
            //SUM of all materials!
            self.totalSumMaterials = ko.computed(function () {
                var price = 0;
                var tmpList = self.calculationItems();
                for (var i = 0; i < tmpList.length; i++) {
                    price += Math.round(tmpList[i].materialprice(), 2);
                }
                return price;
            });
            //formated TotalSum
            self.grandTotal = ko.computed(function () {
                return Globalize.format(self.totalSum(), 'n2') + " " + self.options.currency;
            });
            //formated TotalSumMaterials
            self.grandTotalMaterials = ko.computed(function () {
                return Globalize.format(self.totalSumMaterials(), 'n2') + " " + self.options.currency;
            });
            //---expand,colapse ALL----------------------------------
            self.expandAll = function () {
                $.each(self.calculationItems(), function (index, value) {
                    if ((value.typeOfItem == self.SECTION_TYPE || value.typeOfItem == self.COMPONENT_TYPE) && !(value.expanded())) {
                        self.expand(value);
                    }
                });
            }
            self.colapseAll = function () {
                $.each(self.calculationItems(), function (index, value) {
                    if ((value.typeOfItem == self.SECTION_TYPE || value.typeOfItem == self.COMPONENT_TYPE) && value.expanded()) {
                        self.expand(value);
                    }
                });
            }
            //-------------------------------------------------------
            self.loadData = function (data, calcItem) {
                $.ajax({
                    url: self.options.dataurl,
                    dataType: 'json',
                    type: 'POST',
                    data: data,
                    success: function (allData) {
                        var fillItems = $.proxy(function (serverData, parent) {
                            var mappedItems = $.map(serverData.rows, $.proxy(function (item) {
                                var tmpItem = new CalculationItem(item.id, item.level, item.path, item.rowtype, item.name, item.ordinal, item.dwgNo, item.material, item.materialprice, item.pcs, item.price, parent, self.calculationItems, self.options);
                                this.calculationItems.push(tmpItem);
                                if (tmpItem.isContainer && !!item.rows) {
                                    fillItems(item, tmpItem);
                                }
                            }, this));
                            //if (parent.loadedChildern)
                            //    parent.loadedChildern(true);
                        }, self);
                        fillItems(allData, self);
                    }
                });
            }
            //on click expand!
            this.expand = $.proxy(function (calc) {
                //wait to load 
                if (calc.expanded() && !calc.loadedChildern()) {
                    //alert("Error while loading data!")
                    return;
                }
                //first load childern
                if (!calc.loadedChildern()) {
                    //this.loadData({ nodeid: calc.id, n_level: 1 }, calc);
                    alert('Error!');
                }

                //toggle visible
                if (calc.expanded()) {
                    calc.expanded(false);
                } else {
                    calc.expanded(true);
                }
            }, this)
        }
    }

    $.fn.calculationGrid = function (options) {
        return $.utilis.pluginInit.call(this, 'data-calculation', calculation, options, $.fn.calculationGrid.defaults);
    };

    $.fn.calculationGrid.defaults = {
        extensions: null, // ??????
        model_extensions: null // ?????
    };


})(jQuery);
