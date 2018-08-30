$('.bg_load').hide();

var _header;

new Vue({
    el: "#_header",
    data: {
        no_read_num: 0,
        notice_list: [],
        time: 30,
        business: {photo:null},
        shop: {logo:null, openStat:0},
    },
    methods: {
        getShopInfo: function(){
            $.getJSON("/account/info?_"+$.now(), function(r){
                _header.shop = r.shop
            });
        },
        getBizInfo: function(){
            $.getJSON("/account/biz/info?_"+$.now(), function(r){
                _header.business = r.business
                _header.business.username = _header.business.username?_header.business.username:_header.business.mobile
                _header.business.photo = _header.business.photo
            });
        },
        getNoticeList: function () {
            $.post("/notice/newest?_"+$.now(), function(r){
                r = JSON.parse(r)
                _header.notice_list = r.list
                _header.no_read_num = _header.notice_list==null?0:_header.notice_list.length
            });
        },
        readAll: function(){
            $.post("/notice/signRead/all?_"+$.now(), function(r){
                if (r.code === 0){
                    _header.no_read_num = 0
                }else{
                    alert(r.msg)
                }
            });
        },
        logout: function(){
            if (confirm("确认退出当前账号？")){
                window.location='http://www.xsplus.cn/logout'
            }
        }
    },
    created: function(){
        _header = this
        _header.getShopInfo()
        _header.getNoticeList()
        _header.getBizInfo()
    },
    updated: function(){

    },
})