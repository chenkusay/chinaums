/**
 * Created by Admin on 2018/3/27.
 */
var vm=new Vue({
    el:'#rrapp',
    data:{
        q:{
            key:null
        },
        title: null,
        configlist: {},

    },

    mounted:function () {
        var _this=this;
        this.getInfo();
    },
    methods:{
        getInfo:function(){
            $.gzGet({
                url:"../sysConfig/msglist",
                success:function(r){
                    var _list = r.list
                    _list[3].value=JSON.parse(_list[3].value)
                    _list[4].value=JSON.parse(_list[4].value)
                    vm.configlist=_list;
                }
            })
        },
        save:function(){
            $.gzAjax({
                type: "POST",
                url: "../sysConfig/saveupdate",
                data: JSON.stringify(vm.configlist),
                success: function(r){
                    alert('操作成功', function(index){
                        vm.reload();
                    });
                }
            });
        }


    },

})