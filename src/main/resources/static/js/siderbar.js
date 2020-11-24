function sideOrgOnClick(){
    //orgDisplayTest()

    if($("#hidden_token").val() == ""){

        loginWinHandler = layer.open({
            type:1,
            title:"登录",
            area:["25%","50%"],
            content:$("#loginBox"),
            cancel: function (){
                document.getElementById("loginBox").style.display = "none"
            },
            end: function (){
                document.getElementById("loginBox").style.display = "none"
            }
        })
        layer.msg("请先登录")
        return
    }

}
function refreshSideBar() {
    $.ajax({
        url : '/user/tokenLogin',
        type: 'post',
        dataType: 'json',
        contentType: 'json',
        success(s){
            if(s.state == 0){
                $.ajax({
                    url: '/user/signout',
                    type: 'post',
                })
                location.reload()
            }
            console.log(s)
            var uIconUri = s.uIcon
            $("#img_userIcon").attr("src", '/db/usericon/' + uIconUri)
            $("#profile_userImg").attr("src", '/db/usericon/' + uIconUri)
            $("#profile_username").text(s.username);
            if(typeof (s.username) == undefined){
                $("#orgAsideDiv").html("")
            }
            sideOrgDisplay(s.onames, 0)
        },
        error(e){
            console.log(e)
        }
    })
}
function imgBoxMouseMove(){

    document.getElementById("profileDiv").style.visibility="visible";//显示
}

function imgBoxMouseLeave(){
    document.getElementById("profileDiv").style.visibility="hidden";//显示
}
function profileMouseIn(){
    document.getElementById("profileDiv").style.visibility="visible";//显示
}
function profileMouseLeave(){
    document.getElementById("profileDiv").style.visibility="hidden";//显示
}

function sideOrgDisplay(onames, activateIndex){
    var innerText = ''
    for(i in onames){
        innerText += '<a class="nav-item-link ' + ((i == activateIndex)?'aside_org_selected':'');
        innerText += '" href="#">'
        innerText += onames[i]
        innerText += "</a>"
        $("#orgAsideDiv").html(innerText)
    }
    console.log(innerText)
}

function sideProjMouseMove(){
    document.getElementById("orgAsideDiv").style.visibility="visible";//显示
}
function sideProjMouseLeave(){
    document.getElementById("orgAsideDiv").style.visibility="hidden";//显示
}