function dataSend() {
    var code = $("#complexCode").val();
    dataSendByCode(code);
}

function dataSendByCode(code) {
    var param = {
        complexCode : code
    };
    var url = "http://localhost:9090/api/landByCode";
    $.ajax({
        url: url,
        data: param,
        type: "GET",
    }).done((data) => {
        $("#dataContent").empty();
        var template = "";
        data.complexs.forEach(function(item) {
            template += "<p><span>" + item.complexName + "(" + item.updateAt + ")</span>";
            template += "<a href='" + item.landDataUrl + "'>[바로보기]</a>";
            template += "<input type='button' onclick='dataSendByCode(" + item.complexNo + ") value='업데이트' /></p>";
        })
        $("#dataContent").append(template);
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