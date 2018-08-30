$(function () {
    $("#jqGrid").jqGrid({
        url: '../achieverecord/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '会员编号', name: 'memberId', index: 'member_id', width: 80 }, 			
			{ label: '上级会员编号', name: 'aid', index: 'aid', width: 80 }, 			
			{ label: '年', name: 'year', index: 'year', width: 80 }, 			
			{ label: '月', name: 'month', index: 'month', width: 80 }, 			
			{ label: '业绩', name: 'achieve', index: 'achieve', width: 80 }, 			
			{ label: '上月剩余业绩', name: 'preMonthSurplusAchieve', index: 'pre_month_surplus_achieve', width: 80 }, 			
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '会员等级', name: 'memberLevel', index: 'member_level', width: 80 }			
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
		achieveRecord: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.achieveRecord = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			var url = vm.achieveRecord.id == null ? "../achieverecord/save" : "../achieverecord/update";
			$.gzAjax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.achieveRecord),
			    success: function(r){
					alert('操作成功', function(index){
						vm.reload();
					});
				}
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.gzAjax({
					type: "POST",
				    url: "../achieverecord/delete",
				    data: JSON.stringify(ids),
				    success: function(r){
						alert('操作成功', function(index){
						    $("#jqGrid").trigger("reloadGrid");
						});
					}
				});
			});
		},
		getInfo: function(id){
			$.gzGet({
				url:"../achieverecord/info/"+id,
				success:function(r){
                	vm.achieveRecord = r.achieveRecord;
				}
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});