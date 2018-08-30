$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/menu/list',
        datatype: "json",
		type: "POST",
        colModel: [			
			{ label: '菜单ID', name: 'id', index: "id", width: 40, key: true ,hidden:true},
			{ label: '菜单名称', name: 'name', width: 100 },
			{ label: '上级菜单', name: 'pname', sortable: false, width: 100 },
			{ label: '菜单图标', name: 'icon', sortable: false, width: 40, formatter: function(value, options, row){
				return value == null ? '' : '<i class="'+value+' fa-lg"></i>';
			}},
			{ label: '图标颜色', name: 'iconColor', width: 40 },
			{ label: '菜单URL', name: 'url', width: 120 },
			{ label: '授权标识', name: 'perms', width: 110 },
			{ label: '类型', name: 'type', width: 30, formatter: function(value, options, row){
				if(value === 0){
					return '<span class="label label-primary">目录</span>';
				}
				if(value === 1){
					return '<span class="label label-success">菜单</span>';
				}
				if(value === 2){
					return '<span class="label label-warning">按钮</span>';
				}
			}},
			{ label: '排序号', name: 'sort', index: "sort", width: 40}
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

var setting = {
	data: {
		q:{
            name:null
		},
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "pid",
			rootPId: -1
		},
		key: {
			url:"nourl"
		}
	}
};
var ztree;

var vm = new Vue({
	el:'#rrapp',
	data:{
        q:{
            key: null
        },
		showList: true,
		title: null,
		menu:{
			pname:null,
			pid:0,
			type:1,
			sort:0
		}
	},
	methods: {
        query: function () {
            vm.reload();
        },
		getMenu: function(menuId){
			//加载菜单树
			$.gzGet({
				url:"../sys/menu/select",
				success:function(r) {
					ztree = $.fn.zTree.init($("#menuTree"), setting, r.menuList);
					var node = ztree.getNodeByParam("id", vm.menu.pid);
					ztree.selectNode(node);
					vm.menu.pname = node.name;
            	}
			})
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.menu = {pname:null,pid:0,type:1,sort:0};
			vm.getMenu();
		},
		update: function (event) {
			var menuId = getSelectedRow();
			if(menuId == null){
				return ;
			}
			
			$.gzGet({
                url:"../sys/menu/info/"+menuId,
                success:function(r){
                    vm.showList = false;
                    vm.title = "修改";
                    vm.menu = r.menu;

                    vm.getMenu();
                }
			});
		},
		del: function (event) {
			var menuIds = getSelectedRows();
			if(menuIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.gzAjax({
					type: "POST",
				    url: "../sys/menu/delete",
				    data: JSON.stringify(menuIds),
				    success: function(r){
						alert('操作成功', function(index){
							vm.reload();
						});
					}
				});
			});
		},
		saveOrUpdate: function (event) {
			var url = vm.menu.id == null ? "../sys/menu/save" : "../sys/menu/update";
			$.gzAjax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.menu),
			    success: function(r){
					alert('操作成功', function(index){
						vm.reload();
					});
				}
			});
		},
		menuTree: function(){
			layer.open({
				type: 1,
				offset: '50px',
				skin: 'layui-layer-molv',
				title: "选择菜单",
				area: ['300px', '450px'],
				shade: 0,
				shadeClose: false,
				content: jQuery("#menuLayer"),
				btn: ['确定', '取消'],
				btn1: function (index) {
					var node = ztree.getSelectedNodes();
					//选择上级菜单
					vm.menu.pid = node[0].id;
					vm.menu.pname = node[0].name;
					
					layer.close(index);
	            }
			});
		},
		reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'name': vm.q.key},
                page:page
            }).trigger("reloadGrid");
		}
	}
});