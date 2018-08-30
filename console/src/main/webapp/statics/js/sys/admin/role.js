$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/role/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true },
			{ label: '角色名称', name: 'roleName', index: 'role_name', width: 80 }, 			
			{ label: '备注', name: 'roleRemark', index: 'role_remark', width: 80 }, 			
			{ label: '', name: 'createTime', index: 'create_time', width: 80 }			
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
    check: {
        enable: true
    },
    data: {
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
			key:null
		},
		showList: true,
		title: null,
		sysRole: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.sysRole = {};
			vm.getMenu(-1);
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.getInfo(id)
			vm.getMenu(id);
		},
        getMenu: function(roleId){
            //加载菜单树
            $.gzGet({
				url:"../sys/menu/grantMenus/",
				success:function(r) {
                    ztree = $.fn.zTree.init($("#menuTree"), setting, r.menuList);
                    var nodes = ztree.getNodes();
                    for (var i = 0; i < nodes.length; i++) { //设置节点展开
                        ztree.expandNode(nodes[i], true, false, true);
                    }
                    if(roleId != -1 ){
                        vm.checkNode(roleId);
					}
                }
            });
        },
        checkNode: function (roleId) {
            $.gzGet({
                url:"../sys/menu/grantMenus/"+roleId,
                success:function(r) {
                    var sysRoleMenuList = r.sysRoleMenuList;
                    for(var i=0; i<sysRoleMenuList.length; i++) {
                        var node = ztree.getNodeByParam("id", sysRoleMenuList[i].menuId);
                        ztree.checkNode(node, true, false);
                    }
                }
            });
        },
		saveOrUpdate: function (event) {
			var url = vm.sysRole.id == null ? "../sys/role/save" : "../sys/role/update";
            var menuIds = [], nodes = ztree.getCheckedNodes(true);
            for(var i=0; i<nodes.length; i++) {
                menuIds.push(nodes[i].id);
            }
            vm.sysRole.menuIds = menuIds;
			$.gzAjax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.sysRole),
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
				    url: "../sys/role/delete",
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
				url:"../sys/role/info/"+id,
				success:function(r) {
					vm.sysRole = r.sysRole;
            	}
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'roleName': vm.q.key},
                page:page
            }).trigger("reloadGrid");
		}
	}
});