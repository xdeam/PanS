var checkedList = [] // 选中列表

arr123={}
// 节点单击事件
function domClick(e){
    // 选中子元素所有input框
    var subItem = e.parentNode.nextElementSibling
    var inputList = subItem.querySelectorAll(".to__item")
    for(var i = 0; i < inputList.length; i++){
        var item = inputList[i]
        item.querySelector("input").checked = !e.querySelector("input").checked
        var subName = item.querySelector(".to__name").innerHTML
    }

    // 选中当前input框
    e.querySelector("input").checked = !e.querySelector("input").checked
    select(e)
}

// 冒泡事件
function checkboxClick(e){
    e.checked = !e.checked
}

// checkedbox选中事件
function select(){
    // 筛选在右侧区域需要遍历的内容
    checkedList = []
    arr123=[]
    var cList = document.getElementsByName("cName")
    for( var i = 0; i < cList.length; i++){
        var classArr = (cList[i].parentNode.parentNode.className).split(' ')
        var className = classArr[classArr.length - 1]
        var level = parseInt(className.replace(/\w+-/g,'')) //

        // 设置显示的级别
        var levelArr = document.querySelectorAll("[class^='to__item level-']")
        var max = 1

        for (var a = 0; a < levelArr.length; a++) {
            var item = levelArr[a];
            var arr = item.className.split('-')
            if(parseInt(arr[1]) > max){
                max = parseInt(arr[1])
            }
        }
        if(level == max && cList[i].checked == true){
            checkedList.push(cList[i].value)
            arr123.push($(cList[i]).attr("data-idx"))
            //console.info(arr123)
        }
    }

    // 右侧区域添加选中内容
    var dom = document.getElementById("rightCont")
    dom.innerHTML = ""
    for (var i = 0; i < checkedList.length; i++) {
        var item = checkedList[i];
        var div = document.createElement("div")
        div.className = "to__item"
        div.innerHTML = '<span class="xlx" id="uid_'+arr123[i]+'" >' + item + '</span><span class="to__close" onclick="cancel(this)"><i>+</i></span>'
        dom.appendChild(div)
    }
}

// 取消选中事件
function cancel(dom){
    var cList = document.getElementsByName("cName")
    for (var i = 0; i < cList.length; i++) {
        var item = cList[i];
        if(item.value == dom.previousElementSibling.innerHTML)
        {
            item.checked = false
            select();
            break;
        }
    }
}

// 下拉框点击事件
function dropClick(dom){
    // 切换样式状态
    if(dom.className.indexOf("to__roate") > -1){
        dom.className = ""
    }
    else{
        dom.className = "to__roate"
    }

    // 显示隐藏内容
    var domShow = dom.parentNode.parentNode.nextElementSibling
    if(domShow.className.indexOf("to__show") > -1){
        domShow.className = "to__subItem"
    }
    else{
        domShow.className = "to__subItem to__show"
    }
}

function initData(){
    // 模拟数据
   /* var data = [
        {
            name : "xxx公司",
            child: [
                {
                    name : "综合部",
                    child : [
                        {
                            name : "小明",
                            child : []
                        },
                        {
                            name : "小红",
                            child : []
                        }
                    ]
                },
                {
                    name : "技术部",
                    child : [
                        {
                            name : "小白",
                            child : []
                        },
                        {
                            name : "小黑",
                            child : []
                        }
                    ]
                },
                {
                    name : "UI部",
                    child : []
                }
            ]
        }
    ]*/

    var endHtml = 0
    var html = ""
    var level = 1

    // 遍历树形结构
    function getData(data, dom){
        if(Object.prototype.toString.call(data) === '[object Array]'){
            for(var i = 0; i < data.length; i++){

                // 添加标题
                var item = document.createElement("div")
                var arrow = '<span class="to__dropdownList" ><i onclick="dropClick(this)"><svg t="1550632829702" class="icon" style="" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1783" xmlns:xlink="http://www.w3.org/1999/xlink" width="100%" height="100%"><defs><style type="text/css"></style></defs><path d="M959.52557 254.29773 511.674589 702.334953 63.824631 254.29773Z" p-id="1784"></path></svg></i></span>'

                // 设置显示级别
                if(level ==2){
                    arrow = ""
                }
                item.innerHTML = '<div class="to__item level-' + level + '">' + arrow + '<span onclick="domClick(this)"><input type="checkbox" name="cName" data-idx="'+data[i].uid+'"value="' +data[i].name+ '" onclick="checkboxClick(this)" /><div class="to__name">' +data[i].name+ '</div></span></div>'
                dom.appendChild(item)

                // 添加子元素
                var subItem = document.createElement("div")
                subItem.className = "to__subItem"
                item.appendChild(subItem)

                if(data[i].child.length > 0){
                    level++
                    getData(data[i].child,subItem)
                }
                else{
                    if(i == data.length - 1){
                        level--
                    }
                }
            }
        }
    }

    // 赋值
    var baseDom = document.createElement("div")
    //getData(data,baseDom)
    var index = layer.load(0, {shade: false});
    $.ajax({
        url:"/allDept",
        success:function (data) {
            layer.close(index);
            getData(data,baseDom)
            document.getElementById("leftCont").innerHTML =  baseDom.innerHTML
        }
    })


}


function shareTj() {
    dx=[]
    $(".xlx").each(function(){
      dx.push(parseInt($(this).attr('id').split("_")[1]))
    });

    $("#filesx").find("input:checkbox:not(':first'):checked").each(function(k,v){

        name=$(v).prop("id")
        ff=name.split("_")
        type=ff[0]
        if (dx.length>0){
            if(type=="file"){
                $.ajax({
                    url:"/pan/access/manage/"+getCate()+"/"+ff[1],
                    data:JSON.stringify(dx),
                    method:"post",
                    contentType: "application/json",
                    success:function () {
                        loadAccess(ff[0],ff[1])
                        layer.closeAll()
                    }
                })
            }else {
                $.ajax({
                    url:"/pan/access/manage/"+ff[1]+"/"+0,
                    data:JSON.stringify(dx),
                    method:"POST",
                    contentType: "application/json",
                    success:function () {
                        loadAccess(ff[0],ff[1])
                        layer.closeAll()
                    }
                })
            }
        }


    })



}