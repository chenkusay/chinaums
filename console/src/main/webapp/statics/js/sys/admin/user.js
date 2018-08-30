$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/user/list',
        datatype: "json",
        colModel: [
            { label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true },
            { label: '用户名', name: 'username', index: 'username', width: 80 },
            { label: '邮箱', name: 'email', index: 'email', width: 80 },
            { label: '手机号', name: 'mobile', index: 'mobile', width: 80 },
            { label: '状态', name: 'frozen', index: 'frozen', width: 80,
                formatter: function(value, options, row){
                    if(value === 0){
                        return '<span class="label label-primary">冻结</span>';
                    }
                    if(value === 1){
                        return '<span class="label label-success">正常</span>';
                    }

                }
            },
            { label: '创建者', name: 'createUserName', index: 'createUserName', width: 80 },
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
var setting = {
    check: {
        enable: true
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "id"
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
        sysUser: {}
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.sysUser = {};
            vm.sysUser.frozen=1;
            vm.getRoleTree(-1);
        },
        getRoleTree: function(userId){
            //加载角色树
            $.gzGet({
                url:"../sys/role/treeList",
                success:function(r) {
                    ztree = $.fn.zTree.init($("#roleTree"), setting, r.list);
                    var nodes = ztree.getNodes();
                    for (var i = 0; i < nodes.length; i++) { //设置节点展开
                        ztree.expandNode(nodes[i], true, false, true);
                    }
                    if(userId != -1 ){
                        vm.checkNode(userId);
                    }
                }
            });
        },
        checkNode:function (userId) {
            $.gzGet({
                url:"../sys/role/grantRoles/"+userId,
                success:function(r) {
                    var roleList = r.list;
                    for(var i=0; i<roleList.length; i++) {
                        var node = ztree.getNodeByParam("id", roleList[i].roleId);
                        ztree.checkNode(node, true, false);
                    }
                }
            });
        },
        update: function (event) {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id);
            vm.getRoleTree(id);
        },
        saveOrUpdate: function (event) {
            var url = vm.sysUser.id == null ? "../sys/user/save" : "../sys/user/update";
            var roleIds = [], nodes = ztree.getCheckedNodes(true);
            for(var i=0; i<nodes.length; i++) {
                roleIds.push(nodes[i].id);
            }
            vm.sysUser.roleIds = roleIds;
            $.gzAjax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.sysUser),
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
                    url: "../sys/user/delete",
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
                url:"../sys/user/info/"+id,
                success:function(r) {
                    vm.sysUser = r.user;
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'username': vm.q.key},
                page:page
            }).trigger("reloadGrid");
        }
    }
});