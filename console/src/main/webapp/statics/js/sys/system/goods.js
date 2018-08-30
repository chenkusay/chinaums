$(function () {
    $("#jqGrid").jqGrid({
        url: '../goods/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true },
            { label: '缩略图', name: 'thumb', index: 'thumb', width: 80 ,
                formatter: function(value, options, row){
                    var result = "";
                    result+='<img src="'+row.thumb+'" width="80" height="80">';
                    return result;
                }
            },
            { label: '名称', name: 'name', index: 'name', width: 160 ,
                formatter: function(value, options, row){
                    var result = "";
                    result+='<span class="">'+value+'</span><br>';
                    // result+='<span class="">'+row.goodsSn+'</span>';
                    return result;
                }
            },
			{ label: '价格', name: 'purchasePrice', index: 'purchase_price', width: 60,
				formatter:function (value,options,row) {
                    var result = "";
                    result+='进价￥<span class="">'+value+'</span><br>';
                    result+='售价￥<span class="">'+row.price+'</span><br>';
                    result+='市场价￥<span class="">'+row.marketPrice+'</span>';
                    return result;
            	}
            },
			{ label: '优惠活动', name: 'discountPoint', index: 'discount_point', width: 160 ,
                formatter: function(value, options, row){
                    var result = "";
                    result+='优惠金额<span class="">'+value+'</span><br>';
                    result+='开始时间<span class="">'+row.startTime+'</span><br>';
                    result+='结束时间<span class="">'+row.endTime+'</span>';
                    return result;
                }
            },
			{ label: '排序', name: 'sort', index: 'sort', width: 80 },
			{ label: '状态', name: 'frozen', index: 'frozen', width: 40 ,
                formatter: function(value, options, row){
                    if(value === 0){
                        return '<span class="label label-primary">下架</span>';
                    }
                    if(value === 1){
                        return '<span class="label label-success">上架</span>';
                    }
                }
            },
            { label: '商品备注', name: 'sellerNote', index: 'seller_note', width: 80 }
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
        uploadPic: {pic:null,baseimg: null,idx:-1,type:null},
		showList: true,
		title: null,
		goods: {thumb:null,masterPics:[],frozen:null},
        errors: {pic:null}
	},
    created: function () {
        ue = UE.getEditor('myEditor')
    },
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
            vm.goods={thumb:null,masterPics:[],frozen:null};
			vm.goods.frozen=1;
            ue.setContent("");
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
			var url = "../goods/save";
            vm.goods.content = ue.getContent();
			$.gzAjax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.goods),
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
				    url: "../goods/delete",
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
				url:"../goods/info/"+id,
				success:function(r){
                	vm.goods = r.goods;
                    if (vm.goods.masterPics){
                        vm.goods.masterPics = JSON.parse(vm.goods.masterPics)
                    }else {
                        vm.goods.masterPics = [""]
                    }
                    ue.setContent(vm.goods.content?vm.goods.content:"");
				}
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
			    postData:{'name':vm.q.key},
                page:page
            }).trigger("reloadGrid");
		},
        choosePic: function(idx,type){
            vm.uploadPic.idx = idx;
            vm.uploadPic.pic = null;
            vm.uploadPic.type = type;

            if(type === 1){//上传商品封面图
                if (idx != -1 ){
                    this.uploadPic.pic = this.goods.thumb;
                }
                vm.uploadPic.baseimg = vm.uploadPic.pic;
            }else{
                if (idx != -1 ){
                    this.uploadPic.pic = this.goods.masterPics[idx];
                }
                vm.uploadPic.baseimg = vm.uploadPic.pic;
            }
            $("#upFilePop").modal("show");
        },
        delPicItem: function(idx){
            var _this = this;
            confirm('是否确认删除图片？', function(){
                _this.goods.masterPics.splice(idx, 1)
            });
        },
        getNetPic: function(){
            if (!checkUrl(this.uploadPic.pic)){
                this.errors.pic = "网络图片格式不正确";
                return;
            }
            this.errors.pic = "";
            this.uploadPic.baseimg = this.uploadPic.pic;
            if(this.uploadPic.type===1){
                this.goods.thumb = this.uploadPic.pic;
            }else {
                if (this.uploadPic.idx == -1){
                    this.goods.masterPics.push(this.uploadPic.pic)
                    this.uploadPic.idx = this.goods.masterPics.length-1
                }else {
                    this.goods.masterPics[this.uploadPic.idx] = this.uploadPic.pic;
                }
            }
        }
	},
    mounted: function () {
        var _this = this;
        // this.getInfo();
        $(document).on("change", "#uploadFile", function () {

            if (this.files) {
                var fileLen = this.files.length;
                if (vm.uploadPic.type === 1) {//商品封面上传
                    var FR = new FileReader();
                    FR.addEventListener("load", function (e) {
                        var base64Img = e.target.result;
                        vm.goods.thumb = base64Img;
                        $("#upFilePop").modal("hide")
                    });
                    FR.readAsDataURL(this.files[0]);
                } else {//商品主图上传
                    for (var idx = 0; idx < fileLen; idx++) {
                        var FR = new FileReader();
                        FR.addEventListener("load", function (e) {

                            var base64Img = e.target.result;
                            if (vm.uploadPic.idx == -1) {
                                vm.goods.masterPics.push(base64Img);
                            } else {
                                vm.goods.masterPics[vm.uploadPic.idx] = base64Img
                            }
                            $("#upFilePop").modal("hide")
                        });
                        FR.readAsDataURL(this.files[idx]);
                    }
                }
            }
        });
        //时间选择器
        laydate.render({
            elem: '#endTime',
            type: 'datetime',
            done: function(value, date){
                _this.goods.endTime = value;
            }
        });
        laydate.render({
            elem: '#startTime',
            type: 'datetime',
            done: function(value, date){
                _this.goods.startTime = value;
            }
        });
    }
});