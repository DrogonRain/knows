let taskApp = new Vue({
    el:'#taskApp',
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
                url: '/v1/questions/teacher',
                method: "GET",
                params:{
                    pageNum:pageNum
                }
            }).then(function(r){
                console.log("成功加载数据");
                console.log(r);
                if(r.status == OK){
                    taskApp.questions = r.data.list;
                    taskApp.pageinfo = r.data;
                    //为question对象添加持续时间属性
                    taskApp.updateDuration();
                }
            })
        },
        updateDuration:function () {
            let questions = this.questions;
            for(let i=0; i<questions.length; i++){
                addDuration(questions[i]);
            }
        }
    },
    created:function () {
        console.log("执行了方法");
        this.loadQuestions();
    }
});