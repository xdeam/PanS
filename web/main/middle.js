$(function () {
   // loadFiles(1)

})
function createcate () {
    layer.prompt({title: '输入新建分类名，并确认', formType: 3}, function(text, index){
        layer.close(index);
        addCate(0,text);})


    }


function Uri() {
    this.arr=new Array();
    this.str=""
    this.ss1=""
    that=this;
    this.init=function () {
        ss1=""
        this.str=""
        arr=uri.arr;
        for(i=0;i<arr.length;i++){
            if (i!=arr.length-1)
                ss1+="<li><a href=\"#\" onclick=\"nagtive('"+arr[i][0]+"')\"  >"+arr[i][1]+"</a></li>"
            else
                ss1+="<li class=\"active\">"+arr[i][1]+"</li>"
            this.str+="/"+arr[i][1];
            that.ss1=ss1;
        }

    }
    this.show=function () {
        $("#ng1").html(that.ss1);
    }
}
uri=new Uri();
function Funx() {
    this.success;
    this.index=0;
}
function init() {
    $(document.body).append("  <script src=\"temp.js\"></script>\n" +
        "   <script src=\"time.js\"></script>");

   $("#sall").click(function(){
        //    console.info("2222")
        if($(this).prop('checked')==true)
        {
            $("input:checkbox:not(':first')").prop('checked',true)
        }
        else{
            $("input:checkbox:not(':first')").prop('checked',false)
        }
    })

    $("#deletefile").click(function(){
        ss=""
        ls=$("input:checkbox:not(':first'):checked").length
        fun=new Funx();
        fun.success=function () {
            this.index+=1;
            if (this.index==ls){
                loadFiles(cate)
                layer.msg("删除成功")
            }
        }
        $("input:checkbox:not(':first'):checked").each(function(k,v){

            name=$(v).prop("id")
            ff=name.split("_")
            type=ff[0]

            if(type=="file"){
                delfile(ff[1],fun)
            }else {
                delfolder(ff[1],fun)
            }

            //ss+=$(v).prop("id")
            // ss+="No."+k+" :"+$(v).prop('checked')
        })


        //alert(ss)
    })


}
function minit(){

   //initx();
    $("#filesx tr:not(':first')").click(function(){

        ck=$(this).children(":first").children("input:checkbox")
        name=ck.prop("id")
        ff=name.split("_")
        type=ff[0]
        loadAccess(ff[0],ff[1])
/*
        if(ck.prop('checked'))
        {
            ck.prop('checked',false)
        }else{
            ck.prop('checked',true)
        }*/


    })
    $("#filesx tr:not(':first')").hover(function(){


        $(this).children().eq(1).children(".options").css("visibility","visible")
    },function(){
        $(this).children().eq(1).children(".options").css("visibility","hidden")
    })
    $("#filesx input").click(function(event){
        event.stopPropagation();
    })
    $("#filesx button").click(function(event){
        event.stopPropagation();
    })
    uri.show()
    initUserP()

}
function initUserP() {
    $.ajax({
        url:"/userInfo",
        success:function (data) {
            if(data.resultData=='null'){
                window.location.href='/'
            }else {

                username=data.resultData.uname
                dName=data.resultData.dept.deptName;
                $("#user_logo").html(username.substring(0,1))
                $("#tuname").html(dName+" : "+username+"<span class=\"caret\">")
            }
        }
    })
}
function loadFiles(num) {

    $.ajax({
        url:"/pan/category/"+num,
        success:function (data) {
            cate=num
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
            $.each(data.resultData.files,function (k,v) {
                v.uploadTime =new Date(v.uploadTime).format("yyyy-MM-dd hh:mm:ss")
               v.uri=uri.str;
                ss+=$("#templ").html().temp(v);
            })

           // uri.arr.empty();

            $("tbody").html(ss);
            minit()
            loadAccess("cate",num)
        }
    })
   // return ss;
}
function searchFile() {
    str=$("#search_strs").val()
    $.ajax({
        url:"/pan/file/search",
        data:{"fileName":str},
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
function folderClick(folderId,folderName) {
   // console.info(folderId)
    loadFiles(folderId)

}
function nagtive(cateId) {
  //  console.info(cateId)
    loadFiles(cateId)
/*    for(i=0;i<uri.arr.length;i++){
        item=uri.arr.pop()
        if (item[0]==cateId)
            arr.push(item)
            break;
    }
    uri.show();*/


}
function delfile(fileId,fun) {
    $.ajax({
        url:"/pan/file/del/"+cate+"/"+fileId,
        method:"DELETE",
        success:function () {
            fun.success()
           // loadFiles(cate)
        }
    })
}
function delfolder(foldId,fun) {
    $.ajax({
        url:"/pan/category/"+foldId,
        method:"DELETE",
        success:function () {
            fun.success()
            //loadFiles(cate)

        }
    })
}


//点击下载按钮
function downButton() {
    ls=$("input:checkbox:not(':first'):checked").length

    if (ls==1){
        name=$("input:checkbox:not(':first'):checked").get(0).id
        ff=name.split("_")
        type=ff[0]
        if (type=="file")
        window.location.href="/download/"+ff[1]
        else
        window.location.href="/downloadFolder/"+ff[1]
    }else {
            downloadx()
    }
}

//批量下载
function downloadx(){
    ss=""
    ls=$("input:checkbox:not(':first'):checked").length

    arr=new Array();
    folderarr=new Array();

    $("input:checkbox:not(':first'):checked").each(function(k,v){

        name=$(v).prop("id")
        ff=name.split("_")
        type=ff[0]

        if(type=="file"){

            arr.push(ff[1])

            // delfile(ff[1],fun)
        }else {
            folderarr.push(ff[1])
            //delfolder(ff[1],fun)
        }

        //ss+=$(v).prop("id")
        // ss+="No."+k+" :"+$(v).prop('checked')
    })

    window.location.href="/mutiDownload?json="+JSON.stringify(arr)+"&folders="+JSON.stringify(folderarr);


    //alert(ss)
}