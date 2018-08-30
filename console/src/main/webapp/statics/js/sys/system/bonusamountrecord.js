$(function () {
    $("#jqGrid").jqGrid({
        url: '../bonusamountrecord/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true },
			{ label: '年', name: 'year', index: 'year', width: 80 }, 			
			{ label: '月', name: 'month', index: 'month', width: 80 }, 			
			{ label: '总分红奖金', name: 'bonusTotal', index: 'bonus_total', width: 80 }, 			
			{ label: '更新时间', name: 'updateTime', index: 'update_time', width: 80 },
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
		bonusAmountRecord: {bonusTotal:""}
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
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.bonusAmountRecord = {};
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
			var url = vm.bonusAmountRecord.id == null ? "../bonusamountrecord/save" : "../bonusamountrecord/update";
			// var _this=this;
			confirm('确定设置总分红奖金为'+vm.bonusAmountRecord.bonusTotal+'吗？提交后不可修改!',function(){
                $.gzAjax({
                    type: "POST",
                    url: url,
                    data: JSON.stringify(vm.bonusAmountRecord),
                    success: function(r){
                        alert('操作成功', function(index){
                            vm.reload();
                        });
                    }
                });
			})

		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.gzAjax({
					type: "POST",
				    url: "../bonusamountrecord/delete",
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
				url:"../bonusamountrecord/info/"+id,
				success:function(r){
                	vm.bonusAmountRecord = r.bonusAmountRecord;
				}
            });
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