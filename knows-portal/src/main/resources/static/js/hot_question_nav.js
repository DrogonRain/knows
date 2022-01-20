Vue.component("hot-question",{
    props:["questions"],
    template:`
   <div class="list-group list-group-flush" >
          <a href="question/detail.html" class="list-group-item list-group-item-action" v-for="question in questions">
            <div class="d-flex w-100 justify-content-between">
              <h6 class="mb-1 text-dark" v-text="question.title">equals和==的区别是啥？</h6>
            </div>
            <div class="row">
              <div class="col-6">
                <small class="mr-2">1条回答</small>
                <small class="text-warning" style="display: none" v-show="question.status==0">未回复</small>
                <small class="text-info" style="display: none" v-show="question.status==1">未解决</small>
                <small class="text-success" v-show="question.status==2">已解决</small>
              </div>
              <div class="col-6 text-right">
                <small v-text="question.pageViews">10浏览</small>
              </div>
            </div>
          </a>
        </div>
    `
})