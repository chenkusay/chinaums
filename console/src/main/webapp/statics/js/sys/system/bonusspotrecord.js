$(function () {
    $("#jqGrid").jqGrid({
        url: '../bonusspotrecord/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true},
			{ label: '会员编号', name: 'memberId', index: 'member_id', width: 80,hidden:true },
            { label: '会员昵称', name: 'userName', index: 'member_id', width: 80 },
            { label: '会员手机号', name: 'userPhone', index: 'userPhone', width: 80 },
			{ label: '分红点来至', name: 'fromId', index: 'from_id', width: 80 },
			{ label: '分红点数', name: 'record', index: 'record', width: 80 }, 			
			{ label: '来源类型', name: 'type', index: 'type', width: 80,
                formatter:function (value) {
                    var from_type=["直推5个消费会员","直推重消每5000元","3代内推广人所得"];
                    return '<span class="label label-primary">'+from_type[value]+'</span>';
                }
			},
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
            key:null,
            userPhone:null,
            orderNo:null,
            fromtype:-1
        },
		showList: true,
		title: null,
		bonusSpotRecord: {}
	},
    mounted:function () {
        var _this = this;
        //年月选择器
        // laydate.render({
        //     elem: '#yearMonth',
        //     type: 'month',
        //     done: function(value, date){
        //         _this.bonusAmountRecord.yearMonth = value;
        //     }
        // });
        laydate.render({
            elem: '#qkey',
            type: 'datetime',
            range: '到',
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
			vm.bonusSpotRecord = {};
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
			var url = vm.bonusSpotRecord.id == null ? "../bonusspotrecord/save" : "../bonusspotrecord/update";
			$.gzAjax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.bonusSpotRecord),
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
				    url: "../bonusspotrecord/delete",
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
				url:"../bonusspotrecord/info/"+id,
				success:function(r){
                	vm.bonusSpotRecord = r.bonusSpotRecord;
				}
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'orderNo':vm.q.orderNo,'userPhone':vm.q.userPhone,'type':vm.q.fromtype,'timess':vm.q.key},
                page:page
            }).trigger("reloadGrid");
		}
	}
});