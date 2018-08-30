$(function () {
    $("#jqGrid").jqGrid({
        url: '../bonustotalpermonth/list',
        datatype: "json",
        colModel: [			
			{ label: '年', name: 'year', index: 'year', width: 80 },
			{ label: '月', name: 'month', index: 'month', width: 80 }, 			
			{ label: '总分红点数', name: 'bonusSpotTotal', index: 'bonus_spot_total', width: 80 }, 			
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.pageNo",
            total: "page.totalPages",
            records: "page.totalRows"
        },
        prmNames : {
            page:"pageNo",
            rows:"pageSize",
            sort: "sidx",
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
        q:{
            key:null
        },
		showList: true,
		title: null,
		bonusTotalPermonth: {}
	},
    mounted:function () {
        var _this = this;
        //年月选择器
        laydate.render({
            elem: '#yearMonth',
            type: 'month',
            done: function(value, date){
                _this.bonusAmountRecord.yearMonth = value;
            }
        });
        laydate.render({
            elem: '#qkey',
            type: 'month',
            done: function(value, date){
                _this.q.key = value;
            }
        });
    },
	methods: {
		query: function () {
			vm.reload();
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'yearMonth':vm.q.key},
                page:page
            }).trigger("reloadGrid");
		}
	}
});