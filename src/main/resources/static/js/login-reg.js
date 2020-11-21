function switchToReg(){
    layer.close(layer.index)
    registerWinHandler = layer.open({
        type:1,
        title:"注册",
        area:["25%","50%"],
        content:$("#registerBox"),
        cancel: function (){
            document.getElementById("registerBox").style.display = "none"
        },
        end: function (){
            document.getElementById("registerBox").style.display = "none"
        }
    })
}

function switchToLogin(){
    layer.close(layer.index)
    loginWinHandler = layer.open({
        type:1,
        title:"登入",
        area:["25%","50%"],
        content:$("#loginBox"),
        cancel: function (){
            document.getElementById("loginBox").style.display = "none"
        },
        end: function (){
            document.getElementById("loginBox").style.display = "none"
        }
    })
}
function onLoginClick(){
    var uName = $("#input_username").val();
    var pwd = $("#input_pwd").val()
    if(uName == "" || pwd == ""){
        layer.msg("账号或密码不能为空")
        return
    }
    var d = {
        'username': uName,
        'password': pwd
    }
    $.ajax({
        url: '/user/login',
        type: 'post',
        data: JSON.stringify(d),
        dataType:"json",
        contentType:"application/json;charset=utf-8",
        success:function (d){

            if(d.state == -1){
                layer.msg("登入失败")
            }else{
                layer.close(loginWinHandler)
                layer.msg("登入成功",{offset:'t'})
                console.log(d)
                document.getElementById("img_userIcon").src = ("/db/usericon/" + d.uri)
                document.getElementById("profile_userImg").src = ("/db/usericon/" + d.uri)
                location.reload()
            }
        },
        error:function (e){
            //layer.close(layer.index)
            layer.msg("登入失败")
            console.log(e)
        }
    })

}


var loginWinHandler = 0;
var registerWinHandler = 0;

function onUserImgClike(){
    var t = $("#hidden_token").val();
    console.log(t)
    if(t != ""){
        layer.msg("已登入")
        return
    }
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
}




$("#icon_choose").change(function () {//avatar_file  input[file]的ID
    // 获取上传文件对象
    var file = $(this)[0].files[0];
    // 读取文件URL
    var reader = new FileReader();
    reader.readAsDataURL(file);
    // 阅读文件完成后触发的事件
    reader.onload = function () {
        // 读取的URL结果：this.result
        $("#img_userIcon_reg").attr("src", this.result);//avatar_img  img标签的ID
    }
});


function onRegButtonClick(){
    var uName = $("#input_username_reg").val();
    var pwd = $("#input_pwd_reg").val()
    var nickname = $("#input_nickname_reg").val();
    if(uName == "" || pwd == "" || nickname == ""){
        layer.msg("注册信息错误")
        return
    }

    let formData = new FormData(document.getElementById('reg_form'))
    $.ajax({
        url : '/user/reg',
        type: 'post',
        data: formData,
        dataType: 'json',
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success(s){
            console.log(s)
            if(s.state == 200){
                layer.close(registerWinHandler)
                layer.msg("注册成功",{offset : "t"})
            }else if(s.state == -2){
                layer.msg("用户名已存在")
            }else if(s.state == -1){
                layer.msg("未知错误")
            }
        },
        error(e){
            console.log(e + "error")
            layer.msg("未知错误");
        }
    });
}