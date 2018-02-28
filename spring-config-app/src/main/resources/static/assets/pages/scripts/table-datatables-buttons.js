var oTable;
var TableDatatablesButtons = function () {

    var initTable2 = function () {
        var table = $('#sample_2');

        oTable = table.dataTable({
            "language": {
                "aria": {
                    "sortAscending": ": 升序排序",
                    "sortDescending": ": 倒序排序"
                },
                "emptyTable": "没有找到记录",
                "info": "从 _START_ 到 _END_ 总共 _TOTAL_ 记录",
                "infoEmpty": "没有找到记录",
                "infoFiltered": "(查询 _TOTAL_ 从 _MAX_ 条记录)",
                "lengthMenu": "_MENU_ 条/页",
                "search": "查询:",
                "zeroRecords": "没有匹配的记录"
            },
            ajax: {
                url: $('#showProperties').attr('href')
            },
            "ordering": false,
            "bAutoWidth": false,
            columns: [
                {"data": "name"},
                {"data": "value"},
                {"data": "desc"},
            ],
            serverSide: true,
            buttons: [
                { extend: 'print', className: 'btn default' },
                { extend: 'pdf', className: 'btn default' },
                { extend: 'excel', className: 'btn default' },
                { extend: 'csv', className: 'btn default' },
            ],
            
            "lengthMenu": [
                [5, 10, 15, 20, -1],
                [5, 10, 15, 20, "All"] // change per page values here
            ],
            "pageLength": 10,

            "dom": "<'row' <'col-md-12'B>><'row'<'col-md-6 col-sm-12'l><'col-md-6 col-sm-12'f>r><'table-scrollable't><'row'<'col-md-5 col-sm-12'i><'col-md-7 col-sm-12'p>>", // horizobtal scrollable datatable

            // Uncomment below line("dom" parameter) to fix the dropdown overflow issue in the datatable cells. The default datatable layout
            // setup uses scrollable div(table-scrollable) with overflow:auto to enable vertical scroll(see: assets/global/plugins/datatables/plugins/bootstrap/dataTables.bootstrap.js). 
            // So when dropdowns used the scrollable div should be removed. 
            //"dom": "<'row' <'col-md-12'T>><'row'<'col-md-6 col-sm-12'l><'col-md-6 col-sm-12'f>r>t<'row'<'col-md-5 col-sm-12'i><'col-md-7 col-sm-12'p>>",
        });
    }

    return {

        //main function to initiate the module
        init: function () {

            if (!jQuery().dataTable) {
                return;
            }
            initTable2();
        }

    };

}();
$('#sample_2 tbody').on( 'click', 'tr', function () {
    if ( $(this).hasClass('selected') ) {
        $(this).removeClass('selected');
    } else {
        oTable.$('tr.selected').removeClass('selected');
        $(this).addClass('selected');
    }
});
function choseRow(){
    return oTable.$('.selected').length;
}
function showRow(){
    var nTrs = oTable.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr对象
    for(var i = 0; i < nTrs.length; i++){
        if($(nTrs[i]).hasClass('selected')){
            var objRow = oTable.fnGetData(nTrs[i]);
            return objRow;
        }
    }
}
function refreshTable(){
    oTable._fnReDraw();
}
jQuery(document).ready(function() {
    TableDatatablesButtons.init();
});