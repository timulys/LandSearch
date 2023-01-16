function dataSend() {
    var code = $("#complexCode").val();
    dataSendByCode(code);
}

// 전체 데이터 조회
function dataSelectAll() {
    var url = "http://localhost:9090/api/allData";
    $.ajax({
        url: url,
        type: "GET",
    }).done((data) => {
        $("#dataContent").empty();
        var template = "";
        data.complexs.forEach(function(item) {
            template += "<p><span>" + "[" +  item.complexNo + "]" + item.complexName + "(" + item.updateAt + ")</span>";
            template += "<a href='" + item.landDataUrl + "'>[바로보기]</a>";
            template += "<input type='button' onclick='dataSelectByCode(" + item.complexNo + ")' value='조회하기'/>";
            template += "<input type='button' onclick='dataSendByCode(" + item.complexNo + ")' value='업데이트'/></p>";
        })
        $("#dataContent").append(template);
    })
}

// 데이터 저장 및 조회
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
            template += "<p><span>" + "[" +  item.complexNo + "]" + item.complexName + "(" + item.updateAt + ")</span>";
            template += "<a href='" + item.landDataUrl + "'>[바로보기]</a>";
            template += "<input type='button' onclick='dataSelectByCode(" + item.complexNo + ")' value='조회하기'/>";
            template += "<input type='button' onclick='dataSendByCode(" + item.complexNo + ")' value='업데이트'/></p>";
        })
        $("#dataContent").append(template);
    })
}

// 단건 조회
function dataSelect() {
    var code = $("#complexCode").val();
    dataSelectByCode(code);
}

function dataSelectByCode(code) {
    var param = {
        complexCode : code
    };
    var url = "http://localhost:9090/api/getByCode";
    $.ajax({
        url: url,
        data: param,
        type: "GET",
    }).done((landData) => {
        $("#dataContent").empty();
        $("#dataContent").append(landData);
        var obj = JSON.parse(landData);
        $("#dataContent").append("<br/><a href='" + obj.landDataUrl + "'>[바로보기]</a>");
        // $("#link").attr("href", obj.landDataUrl);
    })
}