var companies = ["顺丰快递","申通快递","圆通快递","中通快递","百世快递","韵达快递","天天快递","中国邮政","邮政EMS","宅急送","德邦快递","全峰快递","其他快递"]
var companiesArr = new Array();
for(var i=0;i<companies.length;i++) {
    var obj = new Object();
    obj.id = i;
    obj.name = companies[i];
    companiesArr.push(obj);
}
$(function () {
    $("#jqGrid").jqGrid({
        url: '../orders/list',
        datatype: "json",
        colModel: [			
            { label: '商品信息', name: 'ordersItems', index: 'delivery_fee', width: 300,
				formatter:function (value, options, row) {
                    var html = '';
            		if(value!=null && value.length > 0){
                        for(var i=0;i<value.length;i++){
                            html += '<div style="">';
							html +='<div style="display: inline-block;float: left;overflow: hidden;width: 100px;margin-top: 10px;margin-right:20px;">';
                            html += '<img src="'+value[i].thumb+'" width="100" height="100"/>';
                            html +='</div>';
            				html += '<div style="display: inline-block;float: left; overflow:hidden;width: 100px;margin-top: 10px;margin-right:20px;word-wrap:break-word;word-break:break-all;word-wrap:break-word;word-break:break-all;white-space:normal ">';
                            html +=  '<div class="" >';
                            html +=  '<p class="" style="overflow: hidden;text-overflow: ellipsis;display: -webkit-box;word-break: break-all;-webkit-box-orient: vertical;-webkit-line-clamp: 2;">'+value[i].name+'</p>';
                            html +=  '</div>';
                            html +=  '<div class="">';
                            html +=  '<p>￥ '+value[i].amount+'</p>';
                            html +=	 '<p class="c959595 f14">x '+value[i].number+'</p>';
                            html +=	 '</div>';
                            html += '</div>';
                            html += '</div>';
                            html +='<div style="width:1px;white-space:normal">&nbsp;&nbsp;</div>';
						}

					}
					return html;
            	}
            },
            { label: '订单', name: 'orderNo', index: 'order_no', width: 150,
                formatter:function (value,options,row) {
                    var result = "";
                    result+='<span class="">'+value+'</span><br><br>';
                    result+='<span class="">'+row.orderTime+'</span>';
                    return result;
                }
            },
            { label: '价格信息', name: 'totalPrice', index: 'total_price', width: 120 ,
                formatter:function (value, options, row) {
                    var result = "";
                    result+='原总价:￥<span class="">'+row.totalPrice+'</span><br><br>';
                    result+='折后价格:￥<span class="">'+row.discountPrice+'</span><br><br>';
                    result+='支付价格:￥<span class="">'+row.price+'</span>';
                    return result;
                }
			},
			{ label: '收货地址',name: 'address', index: 'address', width: 150 ,
                formatter:function (value, options, row) {
                    var result = "";
                    result+='<span class="">'+row.username+'</span><br>';
                    result+='<span class="">'+row.phone+'</span><br>';
                    result+='<p><div style="width:100px;word-wrap:break-word;word-break:break-all;white-space:normal">'+row.address+'</div></p>';
                    return result;
                }
			},
            { label: '订单状态', name: 'status', index: 'status', width: 80 ,
				formatter:function (value,options,row) {
            	var status_arr = ["", "待付款","已付款","待收货","已完成","已评价","已关闭","已退款"];
                    if(value > 5){
                        return '<span class="label label-primary">'+status_arr[value]+'</span>';
                    }else {
                        return '<span class="label label-success">'+status_arr[value]+'</span>';
					}
                }
			},
			{ label: '物流', name: 'deliveryWayName', index: 'deliveryWayName', width: 100 ,
                formatter:function (value, options, row) {
                    var result = "";
                    var deliveryWayName = row.deliveryWayName==null?"":row.deliveryWayName;
                    var deliveryNo = row.deliveryNo==null?"":row.deliveryNo;
                    result+='<br><span class="">'+deliveryWayName+'</span><br>';
                    result+='<br><span class="">'+deliveryNo+'</span>';
                    return result;
                }
			},
            { label: '买家留言', name: 'message', index: 'message', width: 84,

            formatter:function (value, options, row) {
                var result="";
                if(value==null){
                    result=' ';
                }else {
                    result+='<div style="width:70px;word-wrap:break-word;word-break:break-all;white-space:normal">'+value+'</div>';
                }
                return result;
            }


            },
			{ label: '操作', name: '', index: '', width: 100,
				formatter:function (value,options,row) {
                    var result = "";
                    // var status_arr = ["", "待付款","已付款","待收货","已完成","已评价","已关闭","已退款"];
                    // if(row.status > 5){
                    //     result+='<span class="label label-primary">'+status_arr[row.status]+'</span>';
                    // }else {
                    //     result+='<span class="label label-success">'+status_arr[row.status]+'</span>';
                    // }
                    // result+='<a class="btn btn-default" style="margin-top: 10px" onclick="detail(\''+row.id+'\')">查看详情</a><br>';
                    if(row.status===2){
                        result+='<a class="btn btn-default" style="margin-top: 10px;" onclick="deliver(\''+row.id+'\')">确认发货</a><br>';
					}
                    if(row.status===3){
                        result+='<a class="btn btn-default" style="margin-top: 10px" onclick="upDeliver(\''+row.id+'\')">修改快递</a><br>';
					}
                    if(row.status==3){
                        result+='<a class="btn btn-default" style="margin-top: 10px" onclick="confirmDeliver(\''+row.id+'\')">确认收货</a><br>';
					}
                    return result;
                }
			},

        ],
		viewrecords: true,
        height: 470,
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
function detail(id) {
	vm.showList=false;
	vm.showDetail=true;
    vm.title = "订单详情";
	vm.getDetail(id,1);
}
function deliver(id) {
    vm.title = "发货";
    vm.showList=false;
    vm.showDetail=false;
	vm.showDeliver=true;
    ztree = $.fn.zTree.init($("#companyTree"), setting, companiesArr);
    vm.getDetail(id,1);
}
function upDeliver(id) {
    vm.title = "修改快递";
    vm.showList=false;
    vm.showDetail=false;
    vm.showDeliver=true;
    ztree = $.fn.zTree.init($("#companyTree"), setting, companiesArr);
    vm.getDetail(id,2);
}
function confirmDeliver(id) {
    confirm('确认货物已送达？', function(){
        $.gzAjax({
            type: "POST",
            url: "../orders/confirmDeliver/"+id,
            data: {},
            success: function(r){
                if(r.code === 0){
                    alert('操作成功', function(index){
                        vm.reload();
                    });
                }else {
                    alert(r.msg);
                }
            }
        });
    });
}
var setting = {
    check: {
        enable: false
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
            key:null,
            phone:null,
            orderNo:null,
            orderStatus:0
        },
        deliveryNo:"",
		showList: true,
		title: null,
		orders: {},
        showDeliver:false,
        showDetail:false,
        delivery: {search:"", idx: -1, list:[], showSearch:false, orders:{}, isEdit: false},
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
			vm.orders = {};
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

		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.gzAjax({
					type: "POST",
				    url: "../orders/delete",
				    data: JSON.stringify(ids),
				    success: function(r){
						alert('操作成功', function(index){
						    $("#jqGrid").trigger("reloadGrid");
						})
					}
				});
			});
		},
        getDetail: function(id,type){
			$.gzGet({
				url:"../orders/info/"+id,
				success:function(r){
                	vm.orders = r.orders;
                    if(type === 2){
                        var node = ztree.getNodeByParam("id",vm.orders.deliveryWay );
                        ztree.selectNode(node,true);
                    }
				}
            });
		},
        saveDeliver:function(){
            var node = ztree.getSelectedNodes();
            if(node==null || node.length==0){
                alert("请选择快递");
                return;
            }
            if(vm.orders.deliveryNo==null || vm.orders.deliveryNo==""){
                alert("请输入快递单号");
                return;
            }
            vm.orders.deliveryWayName =  node[0].name;
            vm.orders.deliveryWay =  node[0].id;
            $.gzAjax({
                type: "POST",
                url: "../orders/saveDeliver",
                data: JSON.stringify(vm.orders),
                success: function(r){
                    alert('操作成功', function(index){
                        window.location.reload();
                    })
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            vm.showDetail=false;
            vm.showDeliver=false;
            vm.q.orderStatus = vm.q.orderStatus==0?null:vm.q.orderStatus;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'orderNo': vm.q.orderNo,'status':vm.q.orderStatus,'phone':vm.q.phone,'timeScope':vm.q.key},
                page:page
            }).trigger("reloadGrid");
		}
	}
});