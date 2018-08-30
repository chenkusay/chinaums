$(function () {
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		title: null,
        config: {}
	},
    created: function(){
        this.getConfig();
    },
	methods: {
		getConfig: function () {
            $.getJSON("../oss/config", function(r){
				vm.config = r.config;
            });
        },
		addConfig: function(){
			vm.showList = false;
			vm.title = "云存储配置";
		},
		saveOrUpdate: function () {
			var url = "../oss/saveConfig";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.config),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
        reload: function (event) {

        }
	}
});