$(document).ready(function () {
    //Search Grid
    jQuery("#patient_search_grid").jqGrid({
        url: $("#search_patients").attr("data-url") + "?" + $("#search_patients_form").serialize(), //"?q=*",
        datatype: "json",
        height: 255,
        autowidth: true,
        mtype: 'POST',
        colNames: ['Ime', 'Prezime', 'Mjesto', 'Vrijeme', 'Pregledan', 'Akcije'],
        colModel: [
      { name: 'name', index: 'name', width: 80, sortable: false },
      { name: 'surname', index: 'surname', width: 80, sortable: false },
      { name: 'place', index: 'place', width: 100, align: 'center', sortable: false },
      { name: 'time', index: 'time', width: 50, align: 'center', sortable: false },
      { name: 'examined', index: 'examined', width: 30, align: 'center', sortable: false },
      { name: 'act', index: 'act', width: 75, align: 'center', sortable: false }
      ],

        jsonReader: {
            repeatitems: false
        },
        rowNum: 20,
        rowList: [10, 20, 30],
        pager: jQuery('#patient_nav'),
        //pgbuttons: false,
        //pgtext: false,
        //pginput: false,
        sortname: 'name',
        viewrecords: true,
        sortorder: "asc",
        gridComplete: function () {
            var ids = jQuery("#patient_search_grid").jqGrid('getDataIDs');
            for (var i = 0; i < ids.length; i++) {
                var id = ids[i];
                edit = "<a href=\"/Doctor/EditPatient/" + id + "\" >Pregledaj</a>";
                details = "<a href=\"/Doctor/Details/" + id + "\" style=\"padding-left: 10px;\">Detalji</a>";
                deletePatient = "<a href=\"/Doctor/Delete/" + id + "\" style=\"padding-left: 10px;\">Obriši</a>";
                jQuery("#patient_search_grid").jqGrid('setRowData', ids[i], { act: edit + details + deletePatient });
            }
        }

    });

    //-----------fix for ie7------------
    if ($.browser.msie && parseInt($.browser.version, 10) == 7) {
        $("#search_patients_button").on("click", function () {
            jQuery("#patient_search_grid").jqGrid('setGridParam', { url: $("#search_patients_form").attr("action") + "?" + $("#search_patients_form").serialize(), page: 1 }).trigger("reloadGrid");
            return false;
        });
    }
    //----------------------------------
    $("#search_patients_form").on("submit", function () {

        jQuery("#patient_search_grid").jqGrid('setGridParam', { url: this.action + "?" + $(this).serialize(), page: 1 }).trigger("reloadGrid");
        return false;
    });


    $("#print_patients_button").on("click", function () {
        document.location = $(this).attr("data-url") + '?' + $("#search_patients_form").serialize();
        //$.get($(this).attr("data-url") + '?' + $("#search_patients_form").serialize());
        return false;
    });

    $("#Towns").on("change", function () {
        var url = $("#changeable_places_list").attr("data-url");

        $.ajax({
            type: 'GET',
            url: url,
            data: { town: $(":selected").val() },
            success: function (data) {
                $("#changeable_places_list").html(data); // updating
            },
            dataType: 'html'
        });
    })

});