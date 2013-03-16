$(document)
		.ready(

				function() {
					var makeGrid = function(gridSelector, searchSelector,
							pagerSelector, choose) {
						function dateFmatter(cellvalue, options, rowObject) {
							// do something here
							return new Date(cellvalue).toLocaleFormat();
						}
						var gridColNames = [ 'Nadimak', 'Ime', 'Prezime',
								'email' ];
						var gridColModel = [ {
							name : 'nickname',
							index : 'nickname',
							width : 200,
							sortable : false
						}, {
							name : 'name',
							index : 'name',
							width : 200,
							sortable : false
						}, {
							name : 'lastname',
							index : 'lastname',
							width : 200,
							sortable : false
						}, {
							name : 'email',
							index : 'email',
							width : 200,
							sortable : false
						} ];
						if (!choose) {
							gridColNames.push('Akcije');
							gridColModel.push({
								name : 'act',
								index : 'act',
								width : 250,
								align : 'center',
								sortable : false
							});
						}
						// Search Grid
						jQuery(gridSelector)
								.jqGrid(
										{
											url : $(searchSelector).attr(
													"data-url")
											// + "?"+
											// $("#search_patients_form").serialize()
											,
											datatype : "json",
											height : 255,
											autowidth : true,
											mtype : 'POST',
											colNames : gridColNames,
											colModel : gridColModel,

											jsonReader : {
												repeatitems : false
											},
											rowNum : 20,
											rowList : [ 10, 20, 30 ],
											pager : pagerSelector,
											// pgbuttons: false,
											// pgtext: false,
											// pginput: false,
											sortname : 'name',
											viewrecords : true,
											multiselect : choose,
											sortorder : "asc",
											gridComplete : function() {
												if (choose)
													return;
												var ids = jQuery(gridSelector)
														.jqGrid('getDataIDs');
												for ( var i = 0; i < ids.length; i++) {
													var id = ids[i];
															edit = "<a href=\"/Users/EdiUser/"
																	+ id
																	+ "\" >Pregledaj</a>",
															details = "<a href=\"/Users/Details/"
																	+ id
																	+ "\" data-u-button=\"true\" style=\"padding-left: 10px;\">Detalji</a>",
															deleteTermin = "<a href=\"/Users/Delete/"
																	+ id
																	+ "\" style=\"padding-left: 10px;\">Obriši</a>";
													jQuery(gridSelector)
															.jqGrid(
																	'setRowData',
																	ids[i],
																	{
																		act : details

																	});
												}
											}

										});
					};
					makeGrid("#user_search_grid", "#search_users", '#user_nav',
							false);
					makeGrid("#user_search_grid1", "#search_users1",
							'#user_nav1', true);
					makeGrid("#user_search_grid2", "#search_users2",
							'#user_nav2', true);
				});