$(function () {
    $("#jqGrid").jqGrid({
        url: '../customerservice/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true ,hidden:true},
            { label: '客服二维码', name: 'qrcode', index: 'qrcode', width: 80,
                formatter: function(value, options, row){
                    var result = "";
                    result+='<img src="'+row.qrcode+'" width="80" height="80">';
                    return result;
                }
			},
            { label: '客服名称', name: 'name', index: 'name', width: 80 },
			{ label: '联系电话', name: 'phone', index: 'phone', width: 80 },
			{ label: '排序', name: 'sort', index: 'sort', width: 80 }, 			
			// { label: '', name: 'createTime', index: 'create_time', width: 80 },
			{ label: '更新时间', name: 'updateTime', index: 'update_time', width: 80 }
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
        uploadPic: {pic: null, baseimg: null},
		showList: true,
		title: null,
		customerService: {},
        errors: {pic:null}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.customerService = {};
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
			var url = vm.customerService.id == null ? "../customerservice/save" : "../customerservice/update";
			$.gzAjax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.customerService),
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
				    url: "../customerservice/delete",
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
				url:"../customerservice/info/"+id,
				success:function(r){
                	vm.customerService = r.customerService;
				}
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData:{"name":vm.q.key},
                page:page
            }).trigger("reloadGrid");
		},
        choosePic: function(){
            this.uploadPic = {pic: this.customerService.qrcode, baseimg: this.customerService.qrcode}
            $("#upFilePop").modal("show")
        },
        getNetPic: function(){
            if (!checkUrl(this.uploadPic.pic)){
                this.errors.pic = "网络图片格式不正确"
                return
            }
            this.errors.pic = ""
            this.uploadPic.baseimg = this.uploadPic.pic
            this.customerService.qrcode = this.uploadPic.pic
        }
	},
    mounted: function () {
        var _this = this;
        $(document).on("change", "#uploadFile", function () {
            if (this.files && this.files[0]) {
                var FR= new FileReader();
                FR.addEventListener("load", function(e) {
                    var base64Img = e.target.result;
                    _this.uploadPic.baseimg = base64Img;
                    _this.uploadPic.pic = base64Img
                    _this.customerService.qrcode = _this.uploadPic.pic
                    $("#upFilePop").modal("hide")
                });
                FR.readAsDataURL( this.files[0] );
            }
        });
    }
});