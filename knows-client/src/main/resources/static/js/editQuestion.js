Vue.component('v-select', VueSelect.VueSelect);
let editQuestionApp = new Vue({
    el:"#editQuestionApp",
    data:{
        title:'',
        selectedTags:[],
        tags:[],
        selectedTeachers:[],
        teachers:[]
    },
    methods:{
        loadTags:function () {
            console.log("loadTags");
            axios({
                url:'http://localhost:9000/v2/tags',
                method: 'GET',
                params:{
                    accessToken:token
                }
            }).then(function(r){
                console.log(r);
                if(r.status == OK){
                    let list=r.data;
                    let tags = [];
                    for (let i=0;i<list.length; i++) {
                        tags.push(list[i].name);
                    }
                    editQuestionApp.tags = tags;
                }
            })
        },
        loadTeachers:function () {
            console.log("loadTeachers");
            axios({
                url:'http://localhost:9000/v1/users/master',
                method: 'GET',
                params:{
                    accessToken:token
                }
            }).then(function(r){
                console.log(r);
                if(r.status == OK){
                    let list=r.data;
                    let teachers = [];
                    for (let i=0;i<list.length; i++) {
                        teachers.push(list[i].nickname);
                    }
                    editQuestionApp.teachers= teachers;
                }
            })
        },
        loadOldSelected:function () {
            console.log("loadOldSelected");
            let qid = location.search;
            if (!qid){
                return;
            }
            qid=qid.substring(1);
            axios({
                url:'http://localhost:9000/v2/questions/questionVo/'+qid,
                method:'GET'
            }).then(function (response) {
                if (response.status == OK){
                    editQuestionApp.title=response.data.title;
                    editQuestionApp.selectedTags=response.data.tagNames;
                    editQuestionApp.selectedTeachers=response.data.teacherNames;
                    //为id为summernote的富文本编辑器设置值
                    $('#summernote').summernote('code',response.data.content);
                }
            })
        }
    },
    created:function () {
        this.loadTags();
        this.loadTeachers();
        this.loadOldSelected();
    }
})