var cate=1;
function lf() {
    refreshTopList()
    $("#entspace").children(":first-child").children("a:first-child").click();

}
function getCate(){
    return cate;
}
function delcate() {
    $.ajax({
        url:"/pan/category/"+cate,
        method:"delete",
        success:function (da) {
            console.info(da);
            lf()
            layer.msg('分类删除成功');
        }
    })
}
function renamecate() {

        coname=$("#cate"+cate).html()
        layer.prompt({title: '输入新的分类名，并确认', formType: 3, value: coname,}, function(text, index){
            layer.close(index);
            upCate(text,cate)

        });
}
