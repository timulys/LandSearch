function dataSend() {
    var code = $("#complexCode").val();
    var param = {
        complexCode : code
    };
    var url = "http://localhost:9090/api/landByCode";
    $.ajax({
        url: url,
        data: param,
        type: "GET",
    }).done((response) => {
        // $("#dataContent").empty();
        $("#result").text(response.complexName + " 등록 완료");
        $("#link").attr("href", response.landDataUrl);
    })
}

function dataSelect() {
    var code = $("#complexCode").val();
    var param = {
        complexCode : code
    };
    var url = "http://localhost:9090/api/getByCode";
    $.ajax({
        url: url,
        data: param,
        type: "GET",
    }).done((landData) => {
        // $("#dataContent").empty();
        $("#result").text(landData);
        var obj = JSON.parse(landData);
        $("#link").attr("href", obj.landDataUrl);
    })
}