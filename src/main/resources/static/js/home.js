function dataSend() {
    var code = $("#complexCode").val();
    var param = {
        complexCode : code
    };
    var url = "http://localhost:9090/api/landByCode"
    $.ajax({
        url: url,
        data: param,
        type: "GET",
    }).done(function (response) {
        console.log(response);
    })
}