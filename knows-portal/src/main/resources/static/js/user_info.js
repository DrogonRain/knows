let userApp = new Vue({
    el:"#userApp",
    data:{
        user:{}
    },
    methods:{
        loadCurrentUser:function () {
            //this.xxx 这里的this指的是vue对象,即userApp
            axios({
                url:"/v1/users/me",
                method:"get"
            }).then(function (response) {
                //this.xxx 这里的值为axios不是userApp不能获取user
                userApp.user = response.data;
            })
        }
    },
    //created后面指定的代码会在页面加载完成后运行
    created:function () {
        this.loadCurrentUser();
    }
})