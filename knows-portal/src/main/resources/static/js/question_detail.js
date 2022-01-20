
/*
显示当前用户的问题
 */
let questionApp = new Vue({
    el:'#questionApp',
    data: {
        question:{},
    },
    methods: {
        loadQuestions:function () {
            let qid = location.search;
            if (!qid){
                return;
            }
            qid = qid.substring(1);
            axios({
                url: '/v1/questions/'+qid,
                method: "GET",
            }).then(function(r){
                questionApp.question=r.data;
                addDuration(questionApp.question);
            })
        }
    },
    created:function () {
        this.loadQuestions();
    }
});
let postAnswerApp = new Vue({
    el:"#postAnswerApp",
    data:{
        hasError:false,
        message:""
    },
    methods:{
        postAnswer:function () {
            //获取富文本编辑器中的内容
            let content = $("#summernote").val();
            if (!content){
                this.hasError = true;
                this.message="内容不能为空!";
                return;
            }
            //获取当前问题的id
            let qid = location.search;
            if (!qid){
                this.hasError = true;
                this.message = "问题id不能为空!";
                return;
            }
            qid = qid.substring(1);
            //构建表单
            let form = new FormData();
            form.append("questionId",qid);
            form.append("content",content);
            axios({
                url:"/v1/answers",
                method:"POST",
                data:form
            }).then(function (response) {
                console.log(response.data);
                response.data.duration = "刚刚";
                //将新增成功的回答添加至回答列表中
                answersApp.answers.push(response.data);
                //重置富文本编辑器
                $("summernote").summernote("reset");
                postAnswerApp.hasError = true;
                postAnswerApp.message="回答已提交!";
            })
        }
    }
});
let answersApp = new Vue({
    el:"#answersApp",
    data:{
        answers:[]
    },
    methods:{
        loadAnswers:function () {
            //获取url?之后的id
            let qid = location.search;
            if (!qid){
                return;
            }
            qid = qid.substring(1);
            axios({
                url:"/v1/answers/question/"+qid,
                method:"GET"
            }).then(function (response) {
                answersApp.answers = response.data;
                answersApp.updateDuration();
            })
        },
        updateDuration:function(){
            let answers = this.answers;
            for (let i=0;i<answers.length;i++){
                addDuration(answers[i]);
            }
        },
        postComment:function (answerId) {
            if (!answerId){
                return;
            }
            //获得当前提交评论的textarea对象
            let textarea = $("#addComment"+answerId+" textarea");
            //获取textarea对象评论的内容
            let content = textarea.val();
            if (!content){
                return;
            }
            //构建表单
            let form = new FormData();
            form.append("answerId",answerId);
            form.append("content",content);
            axios({
                url:"/v1/comments",
                method:"POST",
                data:form
            }).then(function (response) {
                let comment = response.data;
                //遍历当前回答数据所有回答
                let answers = answersApp.answers;
                for (let i=0;i<answers.length;i++){
                    //如果当前元素的id和本次评论回答的id一致
                    if (answers[i].id == answerId){
                        //就把新增的评论添加到当前回答的评论列表中
                        answers[i].comments.push(comment);
                        break;
                    }
                }
                //清空输入框
                textarea.val("");
            })
        },
        answerSolved:function (answerId) {
            axios({
                url:"/v1/answers/"+answerId+"/solved",
                method:"GET"
            }).then(function (response) {
                console.log(response.data);
            })
        }
    },
    created:function () {
        this.loadAnswers();
    }
})