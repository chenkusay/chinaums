//全局配置
$.ajaxSetup({
    dataType: "json",
    contentType: "application/json",
    cache: false
});
(function($){
    window.alert = function(content, _callback){
        //信息框
        layer.open({
            content: content,
            btn: '确定',
            yes: function (index, layero) {
                layer.close(index)
                if (_callback){
                    _callback()
                }
            }
        });
    };

    window.confirm = function (content, _callback) {
        //询问框
        layer.open({
            content: content,
            btn: ['确认', '取消'],
            yes: function(index){
                layer.close(index);
                if (_callback){
                    _callback()
                }
            }
        });
    }

    $.extend({
        gzAjax:function(args) {
            var defaultArgs={
                type:"post",
                data:{},
                async:false,
                dataType:"json",
                timeout:"20000",
                needMask: true,
                cache:false,
                error:function(XMLHttpRequest, textStatus, errorThrown){
                    alert("网络异常，请稍后再试["+textStatus+"，"+errorThrown+"]");
                },
                success:function(data){}
            };
            var _args = $.extend({},defaultArgs,args);
            if(_args.needMask){
                //TODO loading
            }
            $.ajax(
                {
                    url: _args.url,
                    type:_args.type,
                    async:_args.async,
                    data:_args.data,
                    dataType:_args.dataType,
                    timeout:_args.timeout,
                    error: function(XMLHttpRequest, textStatus, errorThrown){
                        if(_args.needMask){
                            //TODO 关闭loading
                        }
                        _args.error(XMLHttpRequest, textStatus, errorThrown);
                    },
                    success: function(data){
                        if(_args.needMask){
                            //TODO 关闭loading
                        }
                        if(data.code===0){
                            _args.success(data);
                        }else if(data.code===300){
                            alert(data.msg);
                            return
                        }else if(data.code===500){
                            alert(data.msg);
                            return
                        }else if(data.code===403){
                            alert("您还未登录！请先登录", function () {
                                parent.location.href = '/login.html';
                            });
                            return
                        }else{
                            return;
                        }
                    }
                });
        },
        gzGet:function (args) {
            var defaultArgs={
                url:"",
                data:{},
                success:function(data){}
            };
            var _args = $.extend({},defaultArgs,args);

            $.get(_args.url,_args.data,function (data) {
                if(data.code===0){
                    _args.success(data);
                }else if(data.code===300){
                    alert(data.msg);
                    return
                }else if(data.code===500){
                    alert(data.msg);
                    return
                }else if(data.code===403){
                    alert("您还未登录！请先登录", function () {
                        parent.location.href = '/login.html';
                    });
                    return
                }else{
                    return;
                }
            });
        },
        showObj:function(obj){
            var objStr="";
            for(items in obj){
                objStr += items + "=" +obj[items] + '\n';
            }
            alert(objStr);
        }
    });

})(jQuery);

// 吐司提示
function toast(msg, second){
    if (!second) second = 2
    layer.open({
        content: msg,skin: 'msg',time: second //x秒后自动关闭
    });
}
function loading() {
    var index = layer.open({
        type: 2
    });
    return index;
}

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
        alert("请选择一条记录");
        return ;
    }

    var selectedIDs = grid.getGridParam("selarrrow");
    if(selectedIDs.length > 1){
        alert("只能选择一条记录");
        return ;
    }

    return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
        alert("请选择一条记录");
        return ;
    }

    return grid.getGridParam("selarrrow");
}
// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/**
 * 时间格式化
 * @param tm
 * @returns {string}
 */
function getFullTime(tm) {
    var date = new Date(tm); //转换成时间对象
    var year = date.getFullYear();
    var month = (date.getMonth()+1) < 10 ? "0" + (date.getMonth()+1) : (date.getMonth()+1);
    var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
    var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
    return year + "-" +month + "-" + day + " " + hours + ":" + minutes;
}
function getYMD(tm) {
    var date = new Date(tm); //转换成时间对象
    var year = date.getFullYear();
    var month = (date.getMonth()+1) < 10 ? "0" + (date.getMonth()+1) : (date.getMonth()+1);
    var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    return year + "-" +month + "-" + day;
}
function getTime(tm) {
    var date = new Date(tm); //转换成时间对象
    var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
    var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
    return hours + ":" + minutes;
}

// 获取url参数
function getParamVal(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r!=null) return unescape(r[2]); return null;
}
function replaceParamVal(oldUrl, paramName, replaceWith) {
    var re = eval('/(' + paramName + '=)([^&]*)/gi');
    var nUrl = oldUrl.replace(re, paramName + '=' + replaceWith);
    return nUrl;
}
// 验证url网址
function checkUrl(url) {
    if(url) {
        //判断URL地址的正则表达式为:http(s)?://([\w-]+\.)+[\w-]+(/[\w- ./?%&=]*)?
        //下面的代码中应用了转义字符"\"输出一个字符"/"
        var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
        var objExp=new RegExp(Expression);
        if(objExp.test(url) != true){
            return false;
        }
        return true
    }
    return false;
}
//正则表达式验证
//手机
var phoneReg = /^1(3|4|5|7|8)[0-9]\d{8}$/;
//邮箱
var mailReg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
//身份证
var idCardReg = /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/;
//金额
var moneyReg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;