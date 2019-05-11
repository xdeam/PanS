refreshTopList()
function initleft() {

}

function refreshTopList() {

    $.ajax({
        url:"/pan/category/",
        success:function (data) {
            var ss=""
            $.each(data.resultData,function (k,v) {
                ss+="<li><a id=\"cate"+v.categoryId+"\" href=\"#\" onclick=\"loadFiles('"+v.categoryId+"')\">"+v.categoryName+"</a></li>"
            })
            $("#entspace").html(ss)
        }
    })

}


function loadMine() {

    $.ajax({
        url:"/pan/file/mine",
        success:function (data) {

            document.querySelector("#upload").setAttribute("data-upload-method", "/fileUpload?cate="+cate);
            ss=""
            uri.arr=[]
            $.each(data.resultData.urls,function (k,v) {

                uri.arr.push(new Array(v.pid,v.pname));
            })
            uri.init()
            $.each(data.resultData.subCate,function (k,v) {
                v.createTime =new Date(v.createTime).format("yyyy-MM-dd hh:mm:ss")
                v.uri=uri.str;
                ss+=$("#catetempl").html().temp(v);
            })
            $.each(data.resultData,function (k,v) {
                v.uploadTime =new Date(v.uploadTime).format("yyyy-MM-dd hh:mm:ss")
                v.uri=uri.str;
                ss+=$("#templ").html().temp(v);
            })

            // uri.arr.empty();

            $("tbody").html(ss);
            minit()
            $("#accessPannel").html("")
            $("h5").html("")
            // loadAccess("cate",num)
        }
    })
}