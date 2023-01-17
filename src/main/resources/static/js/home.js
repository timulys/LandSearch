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
        $("#dataContent").append(renderTemplate(data));
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
        $("#dataContent").append(renderTemplate(data));
    })
}

function renderTemplate(data) {
    var template = "";
    data.complexs.forEach(function(item) {
        template += "<div>"
        template += "<span style='font-weight: bold'>[" +  item.complexNo + "]" + item.complexName + "(" + item.updateAt + ")</span>";
        template += "<a href='" + item.landDataUrl + "' target='_blank'>[바]</a>";
        template += "<input type='button' onclick='dataSelectByCode(" + item.complexNo + ")' value='조'/>";
        template += "<input type='button' onclick='dataSendByCode(" + item.complexNo + ")' value='업'/>";
        item.complexPyeongVOList.forEach(function(pyeong) {
            template += "<span>:: " +  pyeong.pyeongName + "(" + pyeong.pyeongName2 + ")</span>";
            template += "<span>[" +  pyeong.dealPriceMin + "(" + pyeong.dealPricePerSpaceMin + ")/" +
                pyeong.leasePriceMin + "(" + pyeong.leasePricePerSpaceMin + ")]</span>";
        })
        template += "</div>"
    });
    return template;
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