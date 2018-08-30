$(function () {
    $("#jqGrid").jqGrid({
        url: '../orderscomment/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true ,hidden:true},
            { label: '缩略图', name: 'thumb', index: 'thumb', width: 120 ,
                formatter: function(value, options, row){
                    var result = "";
                    result+='<img src="'+row.thumb+'" width="80" height="80">';
                    return result;
                }
            },
            { label: '商品名称', name: 'goodsName', index: 'goodsName', width: 160 ,
                formatter: function(value, options, row){
                    var result = "";
                    result+='<div style="width: 150px;white-space:normal"><span class="">'+value+'</span></div>';
                    return result;
                }
            },
            { label: '订单号', name: 'ordersNo', index: 'orders_no', width: 140 },
            { label: '评价', name: 'content', index: 'content', width: 160 ,
                formatter: function(value, options, row){
                    var result = "";
                    result+=row.createTime;
                    result+="<br>";
                    result+='<div style="width: 150px;white-space:normal">'+value+'</div>';
                    return result;
                }
            },
            { label: '买家信息', name: 'username', index: 'username', width: 120,
                formatter: function(value, options, row){
					var result = "";
                    result+='<span>'+value+'</span><br>';
                    result+='<span>'+row.phone+'</span>';
                    return result;
                }
			},
			{ label: '回复内容', name: 'reply', index: 'reply', width: 120,
            formatter:function (value, options, row) {
                var result="";
                if(value==null){
                    result='';
                }else {
                    result+='<div style="width: 110px;white-space:normal">'+value+'</div>';
                }
                return result;
            }



            },
            { label: '回复时间', name: 'replyTime', index: 'reply_time', width: 80 },
			{ label: '是否显示', name: 'frozen', index: 'frozen', width: 80 ,
                formatter: function(value, options, row){
                    if(value === 0){
                        return '<span class="label label-primary">待审核</span>';
                    }
                    if(value === 1){
                        return '<span class="label label-success">显示</span>';
                    }
                    if(value === 2){
                        return '<span class="label label-success">不显示</span>';
                    }
                }
            },
			{ label: '点赞数', name: 'totalZan', index: 'total_zan', width: 80 }
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
		ordersComment: {},
        updateType:1 //1回复 2审核
	},
	methods: {
		query: function () {
			vm.reload();
		},
		update: function (type) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			if(type==1){
                vm.title = "回复";
            }else {
                vm.title = "审核";
            }
            vm.showList = false;
            vm.updateType=type;
            vm.getInfo(id,type)
		},
        replyOrfrozen: function () {
			var url = vm.updateType==1?"../orderscomment/reply":"../orderscomment/frozen";
			if(vm.updateType==1 && (vm.ordersComment.reply==null || vm.ordersComment.reply=="")){
			    alert("回复内容不能为空");
            }
            if(vm.updateType==2 && (vm.ordersComment.frozen!=1 && vm.ordersComment.reply!=2)){
                alert("请选择是否展示");
            }
			$.gzAjax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.ordersComment),
			    success: function(r){
					alert('操作成功', function(index){
						vm.reload();
					});
				}
			});
		},
		getInfo: function(id,type){
			$.gzGet({
				url:"../orderscomment/info/"+id,
				success:function(r){
                	vm.ordersComment = r.ordersComment;
                    if(type==1){
                        if(typeof(vm.ordersComment.reply) != undefined && vm.ordersComment.reply!=null && vm.ordersComment.reply!=""){
                            vm.showList = true;
                            alert("不能重复回复");

                        }
                    }else {
                        if(vm.ordersComment.frozen != 0){
                            vm.showList = true;
                            alert("不能重复审核");
                        }
                    }
				}
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
			    postData:{'ordersNo':vm.q.key},
                page:page
            }).trigger("reloadGrid");
		}
	}
});