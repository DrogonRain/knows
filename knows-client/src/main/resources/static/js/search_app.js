let searchApp=new Vue({
    el:"#searchApp",
    data:{
        key:""
    },
    methods:{
        search:function(){
            //encodeURI防中文乱码
            //"/search.html?区别"
            let url="/search.html?"+encodeURI(this.key);
            location.href=url;
        }
    }
})