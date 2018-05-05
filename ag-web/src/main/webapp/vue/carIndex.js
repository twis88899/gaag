/**
 * 首页carIndexJs
 * index.js
 * 2018.04.11
 */
$(function () {
	var $test = $("#test");
	var data = { id: 5, name: "张三" };
    var Index={
        init:function(){
            console.info("init..");
        },

        //事件注册
        bindEvents: function (_this) {
            var _data = {
                "orgId":"44"
            }
        	$test.click(function(){
        		$.ajax({
                    url: 'http://localhost:8083/car-web/carInfo',
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify(_data),
                    contentType : 'application/json;charset=UTF-8',
                    //contentType:"application/x-www-form-urlencoded;charset=UTF-8",
                    beforeSend: function(){

                    },
                    success: function(response){

                    },
                    complete : function(XMLHttpRequest,status){ //请求完成后最终执行参数
                        
                    },
                    error: function(){

                    }
                });
        	});
        }
    };



    $(function ($) {
        Index.init();
        Index.bindEvents();
    });
});