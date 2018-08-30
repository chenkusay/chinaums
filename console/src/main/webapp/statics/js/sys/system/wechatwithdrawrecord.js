$(function () {
    $("#jqGrid").jqGrid({
        url: '../wechatwithdrawrecord/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true ,hidden:true},
			{ label: '会员编号', name: 'memberId', index: 'member_id', width: 80 ,hidden:true},
			{ label: '微信openid', name: 'openid', index: 'openid', width: 80 ,hidden:true},
			{ label: '微信昵称', name: 'username', index: 'username', width: 80},
			{ label: '手机号', name: 'phone', index: 'phone', width: 80},
			{ label: '交易单号', name: 'tradeNo', index: 'trade_no', width: 80},
			{ label: '提现金额', name: 'withdrawNum', index: 'withdraw_num', width: 80 }, 			
			{ label: '提现结果', name: 'flag', index: 'flag', width: 80 ,
                formatter:function (value,options,row) {
                    if(value == 1){
                        return '<span class="label label-success">成功</span>';
                    }else {
                        return '<span class="label label-primary">失败</span>';
                    }
                }
            },
			{ label: '失败消息', name: 'errorMsg', index: 'error_msg', width: 80 }, 			
			{ label: 'ip地址', name: 'ipaddr', index: 'ipaddr', width: 80 }, 			
			{ label: '提现时间', name: 'createTime', index: 'create_time', width: 80 }
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
            phone:null,
            tradeNo:null,
            flag:0
        },
		showList: true,
		title: null,
		wechatWithdrawRecord: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		getInfo: function(id){
			$.gzGet({
				url:"../wechatwithdrawrecord/info/"+id,
				success:function(r){
                	vm.wechatWithdrawRecord = r.wechatWithdrawRecord;
				}
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'phone': vm.q.phone,'flag':vm.q.flag,'tradeNo':vm.q.tradeNo},
                page:page
            }).trigger("reloadGrid");
		}
	}
});