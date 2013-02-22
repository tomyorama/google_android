/*------------------------------------*/
//	Utilis - jquery.grid.js
/*------------------------------------*/

(function ($) {

	var control = function (element, options) {
		this.options = options;
		this._element = $(element);
		this._pagination = this._element.find('.pagination').hide();
		this._state = {
			page: this.options.page,
			perpage: this.options.perpage,
			sort: this.options.sort || ''
		};

		// Find search form
		this.searchForm = null;
		var searchFormId = this.options.searchformid;
		if (!!searchFormId) {
			this.searchForm = $('#' + searchFormId);
			var formState = $.deparam(this.searchForm.serialize());
			$.extend(this._state, formState);
		}

		var currentState = $.bbq.getState();
		$.extend(this._state, currentState);

		this.init();
		this._initSorting();
		this._initPaging();

		if (this.options.dataonload) {
			this.refresh();
		} else
			this._element.hide();

		$.extend(true, this, this.options.model_extensions);
		ko.applyBindings(this, element);
	}

	control.prototype = {
		init: function () {
			this.rows = ko.observableArray();
			// Init search form
			if (this.searchForm) {
				// Search form init values
				this._setFormState();
				// Handle submit
				$(document).off('submit', this.searchForm.selector).on('submit', this.searchForm.selector, $.proxy(function (e) {
					var form$ = $(e.target);
					var formState = $.deparam(form$.serialize());
					$.extend(this._state, formState);
					// Go to page 0
					this._state.page = 0;
					if (this.options.usestate) {
						$.bbq.pushState(this._state, 2);
					}
					else {
						this.refresh();
					}
					return false;
				}, this));
				if (this.options.usestate) {
					// Handle changed hash
					$(window).bind('hashchange', $.proxy(function (e) {
						this._state = e.getState();
						this._setFormState();
						this.refresh();
					}, this));
				}
			}
			// Trigger get data
			if (!!this.options.trigger) {
				this._trigger = $(this.options.trigger);

				var getFormState = $.proxy(function () {
					this._state.page = 0;
					var formData = this._trigger.find('form').serialize();
					var formState = $.deparam(formData);
					return formState;
				}, this);

				this._trigger.on('loaded.ajax', $.proxy(function (e) {
					var isSearchValid = !!this._trigger.find('[data-u-grid-refresh="true"]').length;
					if (isSearchValid) {
						var formState = getFormState();
						this.refresh(formState);
					}
				}, this));

				// Init form state
				var formState = getFormState();
				$.extend(this._state, formState);
			}
			// Grid actions
			this.actionClick = $.proxy(function (action, item, e) {
				this._element.trigger($.format('{0}.grid.utilis', action), ko.toJS(item));
			}, this);
		},
		refresh: function (post) {
			$.extend(this._state, post);
			// Fix checkbox value
			_.each(this._state, function (val, key) {
				if (_.isArray(val)) {
					// Take first
					this._state[key] = val[0];
				}
			}, this);
			if (!!this._ajax) {
				this._ajax.abort();
			}
			this._ajax = $.ajax({
				url: this.options.url,
				dataType: 'json',
				type: 'POST',
				data: this._state
			})
			.success($.proxy(function (d) {
				if (d.data) {
					this.mappingRules = d.mappingRules || {};
					var mappedItems = $.map(d.data, $.proxy(function (item) {
						if (!item.highlighted) {
							item.highlighted = false;
						}
						var mappedItem = ko.mapping.fromJS(item, this.mappingRules);
						var extendedItem = $.extend(mappedItem, this.options.extensions);
						return extendedItem;
					}, this));
					this.rows(mappedItems);
					this._element.show();

					this._data = d.data;
					this._total = d.total;
					if (!!d.sort)
						this._state.sort = d.sort;
					this._setSortButton();
					this._paginate();
				}
			}, this));
		},
		addSingleRow: function (item) {
			this.rows.push(item);
		},
		removeSingleRow: function (item) {
			this.rows.remove(item);
		},

		addSingleRawItem: function (item) {
			if (!item.highlighted) {
				item.highlighted = false;
			}
			var mappedItem = ko.mapping.fromJS(item, this.mappingRules);
			var extendedItem = $.extend(mappedItem, this.options.extensions);
			this.rows.push(extendedItem);
		},
		removeSingleRowById: function (id) {
			$.each(this.rows(), $.proxy(function (index, value) {
				if (id == value.Id) {
					this.rows.remove(value);
					return false;
				}
			}, this));
		},

		// Sorting
		setSort: function (sortBy) {
			this._state.sort = sortBy;
		},
		_initSorting: function () {
			this._setSortButton();

			this._element.on('click mouseover mouseout', 'table > thead > tr > th > span[data-u-grid-sort]', $.proxy(function (e) {
				var btn = $(e.currentTarget);
				var sortField = btn.attr('data-u-grid-sort');
				if (sortField) {
					switch (e.type) {
						case 'click':
							e.preventDefault();
							e.stopPropagation();
							// Direction reset
							var sort = this._parseSort(this._state.sort);
							var direction = sort.direction;
							var isChangingField = sort.field != sortField;
							if (isChangingField)
								direction = 'asc';
							else
								direction = sort.direction == 'asc' ? 'desc' : 'asc';
							this._state.sort = $.format('{0} {1}', sortField, direction);
							this._setSortButton();
							this.refresh();
							break;
						case 'mouseover':
							btn.addClass('hover');
							break;
						case 'mouseout':
							btn.removeClass('hover');
							break;
					}
				}
			}, this));
		},
		_setSortButton: function () {
			var sort = this._parseSort(this._state.sort);
			if (!!sort.field) {
				var sortable = this._element.find('table > thead > tr > th:has(span[data-u-grid-sort])');
				sortable.removeClass("asc desc");
				var th = sortable.filter($.format(':has(span[data-u-grid-sort="{0}"])', sort.field));
				th.addClass(sort.direction);
			}
		},
		_parseSort: function (sortBy) {
			var result = { field: null, direction: null };
			if (!!sortBy) {
				var sortSplit = sortBy.split(' ');
				result.field = sortSplit[0];
				result.direction = sortSplit[1] == 'desc' ? 'desc' : 'asc';
			}
			return result;
		},

		// Paging
		_initPaging: function () {
			// Pagination events
			this._element.on('click', '.pagination a', $.proxy(function (e) {
				e.preventDefault();
				var btn = $(e.currentTarget);
				this._state.page = parseInt(btn.attr('data-u-grid-page')) - 1;
				this.refresh();
			}, this));
		},
		_paginate: function () {
			// Pagination
			var total = this._total;
			var itemsPerPage = this._state.perpage;
			var skip = this._state.page * this._state.perpage;
			var page = this._state.page + 1;
			if (itemsPerPage && total > itemsPerPage) {
				// Display pagination
				this._pagination.empty();
				// Measure size
				this._element.width('auto');
				var tbl = this._element.find('table');
				var size = { tableWidth: 300 };
				tbl.forceVisible($.proxy(function () {
					size.tableWidth = tbl.outerWidth(true);
				}, this));
				this._element.width(size.tableWidth);
				this._pagination.width(size.tableWidth);
				//overlay.width(size.tableWidth);
				var splitSize = size.tableWidth * .006;
				var totalPages = Math.ceil(total / itemsPerPage);
				if (totalPages != Infinity) {
					// Resize split size
					if (page <= splitSize) {
						splitSize += splitSize - page;
					} else if (page >= totalPages - splitSize) {
						splitSize += splitSize - (totalPages - page);
					}
					var pText = $('<span/>').html($.format('{0} {1}/{2}&nbsp;', 'Stranica', page, totalPages));
					this._pagination.append(pText);
					var pages = [];
					for (var i = 1; i < totalPages + 1; i++) {
						if (i <= splitSize || i >= totalPages - splitSize || (i <= page + splitSize && i >= page - splitSize)) {
							if (_.indexOf(pages, i) == -1) {
								pages.push(i);
							}
						}
					}
					$.each(pages, $.proxy(function (i, n) {
						var before = pages[i - 1];
						if (before != null && before < n - 1) {
							this._pagination.append('<span>...</span>');
						}
						var a = $('<a href="#"></a>').html(n).attr('data-u-grid-page', n);
						if (page == n) a.addClass('selected');
						this._pagination.append(a);
					}, this));
					this._pagination.css('display', 'block');
				}
			} else {
				// Hide pagination
				this._pagination.hide();
			}
		},
		_setFormState: function () {
			$.each(this._state, $.proxy(function (i, column) {
				this.searchForm.find($.format('[name="{0}"]', i)).val(column);
			}, this));
		}
	}

	$.fn.grid = function (options) {
		return $.utilis.pluginInit.call(this, 'data-u-grid', control, options, $.fn.grid.defaults);
	};

	$.fn.grid.defaults = {
		usestate: false,
		dataonload: true,
		perpage: 10, // server default
		page: 0, // server default
		sort: null, // server default
		trigger: null,

		extensions: null, // ??????
		model_extensions: null // ?????
	};

})(jQuery);