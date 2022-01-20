/*
显示热门问题
 */
let hotQuestion = new Vue({
    el:'#hotQuestion',
    data: {
        questions:[],
        pageinfo:{},
    },
    methods: {
        loadQuestions:function (pageNum) {
            console.log(pageNum);
            if(! pageNum){
                pageNum=1;
            }
            axios({
                url: '/v1/questions/hot',
                method: "GET",
                params:{
                    pageNum:pageNum
                }
            }).then(function(r){
                console.log("成功加载数据");
                console.log(r);
                if(r.status == OK){
                    hotQuestion.questions = r.data.list;
                    hotQuestion.pageinfo = r.data;
                }
            })
        }
    },
    created:function () {
        console.log("执行了方法");
        this.loadQuestions();
    }
});
