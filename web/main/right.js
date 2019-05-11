function right_init(){
    
    $(document.body).append("<script language='javascript' src='../assert/verUpload/verUpload.js'></script>");

    new verUpload({
    files: "#upload",
    name: "files",
    method:"/fileUpload?cate="+cate,
    load_list: true,
    success: function (d) {
        //alert(d);
        loadFiles(cate)
    },
    fail: function (d) {
        alert(d)
    },
    size: 1024 * 4,
    ext: ['jpg', 'jpeg', 'png', 'gif','doc','docx','txt','ppt','pdf']
});


}
$("#ncate").click(function () {
    layer.prompt({title: '输入新建分类名，并确认', formType: 3}, function(text, index){
        layer.close(index);
        addCate(0,text);


    });
})
function createfolder() {
    layer.prompt({title: '输入新建文件夹名称，并确认', formType: 3}, function(text, index){
        layer.close(index);
        addFolder(cate,text);


    });


}
function addFolder(pid,catename) {
    $.ajax({
        url:"/pan/category/addCate",
        method:"POST",
        data:{"parentCateId":pid,"cateName":catename},
        success:function () {
            layer.msg('文件夹创建成功');
            loadFiles(cate)
        }
    })
}

function loadAccess(type,id){
        if (type=="file"){
           $.ajax({
               url:"/pan/access/viewFile/"+cate+"/"+id+"/",
               data:"",
               success:function (data) {
                   access_html=""
                   if (data.resultData!=undefined){
                        $.each(data.resultData,function (k,v) {
                            access_html+="  <ul class=\"icons-list\">\n" +
                                "                            <li>\n" +
                                "                                <i class=\"bg-primary img-circle\">"+v.user.dept.deptName.substr(0,1)+"</i>\n" +
                                "                                <div class=\"desc\">\n" +
                                "                                    <div class=\"title\">"+v.user.dept.deptName+" "+v.user.uname+"</div>\n" +
                                "                                    <small><a href='#' onclick=cancelAccess('"+v.type+"','"+v.fileId+"','"+v.accessId+"') >取消授权</a></small>\n" +
                                "                                </div>\n" +
                                "\n" +
                                "                            </li>\n" +
                                "\n" +
                                "                        </ul>"
                        })
                       $("#access_info").html(data.resultMsg+"文件权限")
                       $("#accessPannel").html(access_html)
                   }else   $("#access_info").html(data.resultMsg)
               }
           })
        }else {
            $.ajax({
                url:"/pan/access/viewCate/"+id+"/",
                data:"",
                success:function (data) {
                    access_html=""
                    if (data.resultData!=undefined){
                        $.each(data.resultData,function (k,v) {
                            access_html+="  <ul class=\"icons-list\">\n" +
                                "                            <li>\n" +
                                "                                <i class=\"bg-primary img-circle\">"+v.user.dept.deptName.substr(0,1)+"</i>\n" +
                                "                                <div class=\"desc\">\n" +
                                "                                    <div class=\"title\">"+v.user.dept.deptName+" "+v.user.uname+"</div>\n" +
                                "                                    <small><a href='#' onclick=cancelAccess('"+v.type+"','"+v.cateId+"','"+v.accessId+"') >取消授权</a></small>\n" +
                                "                                </div>\n" +
                                "\n" +
                                "                            </li>\n" +
                                "\n" +
                                "                        </ul>"
                        })
                        $("#access_info").html(data.resultMsg+" 权限信息")
                        $("#accessPannel").html(access_html)
                    }
                    else   $("#access_info").html(data.resultMsg)
                }
            })
        }
}

function cancelAccess(type,id,accessId) {
  // console.info(type+" "+id+" "+accessId);
    $.ajax({
        url:"/pan/access/cancelAccess/"+accessId+"/",
        success:function(data){
            loadAccess(type,id);
        }
    })
}
