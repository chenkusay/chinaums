$(function () {
    $("#jqGrid").jqGrid({
        url: '../banner/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true },
			{ label: '标题', name: 'title', index: 'title', width: 80 },
			{ label: '图片', name: 'pic', index: 'pic', width: "160",
				formatter:function (value,options,row) {
				return '<img src="'+value+'" width="100%"  height="20%"/>';
            }},
			{ label: '调转链接', name: 'link', index: 'link', width: 80 }, 			
			{ label: '排序', name: 'sort', index: 'sort', width: 40 },
			{ label: '冻结状态', name: 'frozen', index: 'frozen', width: 40 ,
                formatter: function(value, options, row){
                    if(value === 0){
                        return '<span class="label label-primary">冻结</span>';
                    }
                    if(value === 1){
                        return '<span class="label label-success">启动</span>';
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
		banner: {},
        errors: {pic:null}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.banner = {};
			vm.banner.frozen=1;
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
			var url = vm.banner.id == null ? "../banner/save" : "../banner/update";
			$.gzAjax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.banner),
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
				    url: "../banner/delete",
				    data: JSON.stringify(ids),
				    success: function(r){
						alert('操作成功', function(index){
						    $("#jqGrid").trigger("reloadGrid");
						})
					}
				});
			});
		},
		getInfo: function(id){
			$.gzGet({
				url:"../banner/info/"+id,
				success:function(r){
                	vm.banner = r.banner;
				}
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData:{'title':vm.q.key},
                page:page
            }).trigger("reloadGrid");
		},
        choosePic: function(){
            this.uploadPic = {pic: this.banner.pic, baseimg: this.banner.pic}
            $("#upFilePop").modal("show")
        },
        getNetPic: function(){
            if (!checkUrl(this.uploadPic.pic)){
                this.errors.pic = "网络图片格式不正确"
                return
            }
            this.errors.pic = ""
            this.uploadPic.baseimg = this.uploadPic.pic
            this.banner.pic = this.uploadPic.pic
        }
	},
    mounted: function () {
        var _this = this
        $(document).on("change", "#uploadFile", function () {
            if (this.files && this.files[0]) {
                var FR= new FileReader();
                FR.addEventListener("load", function(e) {
                    var base64Img = e.target.result;
                    _this.uploadPic.baseimg = base64Img;
                    _this.uploadPic.pic = base64Img
                    _this.banner.pic = _this.uploadPic.pic
                    $("#upFilePop").modal("hide")
                });
                FR.readAsDataURL( this.files[0] );
            }
        })
    }
});