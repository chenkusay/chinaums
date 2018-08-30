var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            key:null
        },
        uploadPic: {pic:null,baseimg: null,idx:-1,type:null},
        title: null,
        goods: {},
        errors: {pic:null}
    },
    created: function () {
        ue = UE.getEditor('myEditor')
    },
    methods: {
        saveOrUpdate: function (event) {
            var url = "../goodsrebuy/update";
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
        getInfo: function(){
            $.gzGet({
                url:"../goodsrebuy/info",
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
        choosePic: function(idx,type){
            vm.uploadPic.idx = idx;
            vm.uploadPic.pic = null;
            vm.uploadPic.type=type;
            if(type===1){
                if (idx != -1 ){
                    this.uploadPic.pic=this.goods.thumb;
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
                this.goods.thumb=this.uploadPic.pic;
            }else{
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
        this.getInfo();
        $(document).on("change", "#uploadFile", function () {
            if (this.files) {
                var fileLen = this.files.length;
                if(vm.uploadPic.type===1){//重消商品封面图
                    var FR= new FileReader();
                    FR.addEventListener("load", function (e) {
                        var base64Img = e.target.result;
                        vm.goods.thumb = base64Img;
                        $("#upFilePop").modal("hide");
                    });
                    FR.readAsDataURL(this.files[0]);

                }else{//重消商品主图上传
                    for (var idx=0; idx<fileLen; idx++){
                        var FR= new FileReader();
                        FR.addEventListener("load", function(e) {
                            var base64Img = e.target.result;
                            if (vm.uploadPic.idx == -1){
                                vm.goods.masterPics.push(base64Img)
                            }else {
                                vm.goods.masterPics[vm.uploadPic.idx] = base64Img
                            }
                            $("#upFilePop").modal("hide")
                        });
                        FR.readAsDataURL( this.files[idx] );
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