$(function () {
    $("#jqGrid").jqGrid({
        url: '../member/memberlist',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true },
            { label: '微信头像', name: 'photo', index: 'photo', width: 70,
						formatter:function (value,options,row) {
        	return '<img src="'+value+'" width="80"  height="80"/>'
                        }

			},
			{ label: '推荐人昵称', name: 'aidName',sortable: false, index: 'aidName', width: 80 },
			{ label: '推荐人手机号', name: 'aidPhone',sortable: false, index: 'aidPhone', width: 80 },
			{ label: '微信openid', name: 'openid', index: 'openid', width: 80,hidden:true },
			{ label: '会员号', name: 'number', index: 'number', width: 80,hidden:true },
			{ label: '昵称', name: 'username', index: 'username', width: 80 }, 			
			{ label: '手机号', name: 'phone', index: 'phone', width: 70 },
            // { label: '当前积分', name: 'integral', index: 'integral', width: 65 },
            // { label: '累计积分', name: 'totalIntegral', index: 'totalIntegral', width: 65 },
            { label: '积分', name: 'integral', index: 'integral', width: 80,
				formatter:function (value,options,row) {
                    var result = "";
                    result+='当前积分:<span class="">'+row.integral+'</span><br><br>';
                    result+='累计积分:<span class="">'+row.totalIntegral+'</span>'
					return result;
                }
			},
            { label: '业绩', name: 'theMonthAchive', index: 'integral', width: 80,
                formatter:function (value,options,row) {
                    var result = "";
                    result+='当月业绩:<span class="">'+row.theMonthAchive+'</span><br><br>';
                    result+='总业绩:<span class="">'+row.totalAchive+'</span>'
                    return result;
                }
            },
            { label: '会员等级', name: 'level', index: 'level', width: 57,hidden:true},
			{ label: '会员等级', name: 'levelStr', index: 'levelStr', width: 57,
				formatter:function (value,options,row) {
                    if(row.level == 1){
                        return '<span class="label label-success">志愿者</span>';
                    }else {
                        return '<span class="label label-primary">健康使者</span>';
                    }
                }
			},
            { label: '粉丝总数',name:'totalFansNum',index:'totalFansNum',width:80,
            formatter:function (value) {
				if(value!=null){
					return value
				}else{
					return 0;
				}
            }
			},
            { label: '当月新增粉丝数',name:'theMonthFansNum',index:'theMonthFansNum',width:80,
                formatter:function (value) {
                    if(value!=null){
                        return value
                    }else{
                        return 0;
                    }
                }
            },
            { label: '成为消费会员时间',name:'consumeTime',index:'consume_time',width:80},
			{ label: '显示状态: 0隐藏 1显示', name: 'frozen', index: 'frozen', width: 80 ,hidden:true},
			{ label: '加入时间', name: 'createTime', index: 'create_time', width: 80 }

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
			level:0,
			times:null,
            username:null,
            aidPhone:null
        },
		showList: true,
		title: null,
		member: {}
	},
    mounted:function () {
        var _this = this;
        //时间段选择器
        laydate.render({
            elem: '#qtime',
            type: 'datetime',
            range: '到',
            done: function(value, date){
                _this.q.times = value;
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
			vm.member = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
            var rowData = $("#jqGrid").jqGrid('getRowData',id);
			if(rowData.level==null || rowData.aidPhone.length==0){
                vm.showList = false;
                vm.title = "绑定推荐人";
                vm.getInfo(id)
			}else {
                alert("已有推荐人,不能再绑定",null);
			}

		},
        upgrade:function () {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            var rowData = $("#jqGrid").jqGrid('getRowData',id);
            if(rowData.level==1){
                $.gzAjax({
                    type: "POST",
                    url:  "../member/upgrade/"+id,
                    data: {},
                    success: function(r){
                        alert('操作成功', function(index){
                            vm.reload();
                        });
                    }
                });
            }else {
                alert("已经是健康使者,不能重复升级",null);
            }
        },
		saveOrUpdate: function (event) {
			if(vm.member.aidPhone==null || vm.member.aidPhone.length<=0){
				alert("请输入手机号",null);
				return;
			}
			var url = "../member/bindAid";
			$.gzAjax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.member),
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
				    url: "../member/delete",
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
				url:"../member/info/"+id,
				success:function(r){
                	vm.member = r.member;
				}
            });
		},
		reload: function (event) {
			vm.showList = true;
			// var page = $("#jqGrid").jqGrid('getGridParam','page');
            var page = 1;
			$("#jqGrid").jqGrid('setGridParam',{
				postData:{'phone':vm.q.key,'level':vm.q.level,'timeScope':vm.q.times,'username':vm.q.username,'aidPhone':vm.q.aidPhone},
                page:page
            }).trigger("reloadGrid");
		}
	}
});