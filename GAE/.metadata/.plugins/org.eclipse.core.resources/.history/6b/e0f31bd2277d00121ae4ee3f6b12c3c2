$(document)
		.ready(

				function() {
					function dateFmatter(cellvalue, options, rowObject) {
						// do something here
						return new Date(cellvalue).toLocaleFormat();
					}
					// Search Grid
					jQuery("#patient_search_grid")
							.jqGrid(
									{
										url : $("#search_patients").attr(
												"data-url")
										// + "?"+
										// $("#search_patients_form").serialize()
										,
										datatype : "json",
										height : 255,
										autowidth : true,
										mtype : 'POST',
										colNames : [ 'Ime', 'team1', 'team2',
												'date', 'result', 'Akcije' ],
										colModel : [ {
											name : 'name',
											index : 'name',
											width : 200,
											sortable : false
										}, {
											name : 'team1',
											index : 'team1',
											width : 200,
											sortable : false
										}, {
											name : 'team2',
											index : 'team2',
											width : 200,
											align : 'center',
											sortable : false
										}, {
											name : 'date',
											index : 'date',
											width : 200,
											align : 'center',
											formatter : dateFmatter,
											sortable : false
										}, {
											name : 'result',
											index : 'result',
											width : 150,
											align : 'center',
											sortable : false
										}, {
											name : 'act',
											index : 'act',
											width : 250,
											align : 'center',
											sortable : false
										} ],

										jsonReader : {
											repeatitems : false
										},
										rowNum : 10,
										rowList : [ 5, 10, 20 ],
										pager : jQuery('#patient_nav'),
										// pgbuttons: false,
										// pgtext: false,
										// pginput: false,
										sortname : 'name',
										viewrecords : true,
										sortorder : "asc",
										gridComplete : function() {
											var ids = jQuery(
													"#patient_search_grid")
													.jqGrid('getDataIDs');
											for ( var i = 0; i < ids.length; i++) {
												var id = ids[i];
														edit = "<a href=\"/Termin/EditPatient/"
																+ id
																+ "\" >Pregledaj</a>",
														details = "<a href=\"/Termin/Details/"
																+ id
																+ "\" data-u-button=\"true\" style=\"padding-left: 10px;\">Detalji</a>",
														deleteTermin = "<a href=\"/Termin/Delete/"
																+ id
																+ "\" style=\"padding-left: 10px;\">Obriši</a>";
												jQuery("#patient_search_grid")
														.jqGrid(
																'setRowData',
																ids[i],
																{
																	act : details

																});
											}
										}

									});
				});