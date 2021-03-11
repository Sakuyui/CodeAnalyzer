function onFileSearchClick(){
    d = {
        file : "aaa"
    }
    $.ajax({
        url: '/search/byFile',
        type: 'post',
        data: JSON.stringify(d),
        dataType: "html",
        success:function (msg){
            console.log(msg)
        }
        ,
        error:function (e){
            console.log(e)
        }
    });
}