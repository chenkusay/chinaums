$(function () {
    $("#jqGrid").jqGrid({
        url: '../qrcodebgt/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true },
			{ label: '标题', name: 'title', index: 'title', width: 80 }, 			
			{ label: '模板图片', name: 'pic', index: 'pic', width: 80,
                formatter: function(value, options, row){
                    var result = "";
                    result+='<img src="'+row.pic+'" width="80" height="80">';
                    return result;
                }
			},
			{ label: '二维码X轴坐标', name: 'x', index: 'sort', width: 80 },
			{ label: '二维码Y轴坐标', name: 'y', index: 'sort', width: 80 },
            { label: '头像X轴坐标', name: 'iconX', index: 'iconX', width: 80 },
            { label: '头像Y轴坐标', name: 'iconY', index: 'iconY', width: 80 },
            { label: '昵称X轴坐标', name: 'nicknameX', index: 'nicknameX', width: 80 },
            { label: '昵称Y轴坐标', name: 'nicknameY', index: 'nicknameY', width: 80 },
            { label: '字体大小', name: 'fontSize', index: 'fontSize', width: 80 },
            { label: '排序', name: 'sort', index: 'sort', width: 80 },
			{ label: '冻结状态', name: 'frozen', index: 'frozen', width: 80 ,
                formatter: function(value, options, row){
                    if(value === 0){
                        return '<span class="label label-primary">冻结</span>';
                    }
                    if(value === 1){
                        return '<span class="label label-success">启用</span>';
                    }
                }
			},
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
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
		qrcodeBgt: {},
        errors: {pic:null}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.qrcodeBgt = {};
            vm.qrcodeBgt.frozen=1;
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
			var url = vm.qrcodeBgt.id == null ? "../qrcodebgt/save" : "../qrcodebgt/update";
			$.gzAjax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.qrcodeBgt),
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
				    url: "../qrcodebgt/delete",
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
				url:"../qrcodebgt/info/"+id,
				success:function(r){
                	vm.qrcodeBgt = r.qrcodeBgt;
				}
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'title': vm.q.key},
                page:page
            }).trigger("reloadGrid");
		},
        choosePic: function(){
            this.uploadPic = {pic: this.qrcodeBgt.pic, baseimg: this.qrcodeBgt.pic}
            $("#upFilePop").modal("show")
        },
        getNetPic: function(){
            if (!checkUrl(this.uploadPic.pic)){
                this.errors.pic = "网络图片格式不正确"
                return
            }
            this.errors.pic = ""
            this.uploadPic.baseimg = this.uploadPic.pic
            this.qrcodeBgt.pic = this.uploadPic.pic
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
                    _this.qrcodeBgt.pic = _this.uploadPic.pic
                    $("#upFilePop").modal("hide")
                });
                FR.readAsDataURL( this.files[0] );
            }
        });
    }
});