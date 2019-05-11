function addCate(pid,catename) {
    $.ajax({
        url:"/pan/category/addCate",
        method:"POST",
        data:{"parentCateId":pid,"cateName":catename},
        success:function () {
            layer.msg('新分类创建成功');
            refreshTopList();
        }
    })
}
function upCate(catename,cid){

    d={"categoryId":cid,"parentId":0,"categoryName":catename}
    $.ajax({
        url:"/pan/category/"+cid,
        method:"PUT",
        contentType: "application/json",
        data:JSON.stringify(d),
        success:function () {
            layer.msg('分类修改成功');
            refreshTopList();
        }
    })
}

