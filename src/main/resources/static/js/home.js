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

function dataUpdateAll() {
    if (confirm("시간이 다소 소요됩니다. 정말 업데이트 하시겠습니까?")) {
        $.ajax({
            url: "http://localhost:9090/api/allUpdate",
            type: "GET",
        }).done((data) => {
            console.log(data);
            $("#dataContent").empty();
        })
    } else {
        return false;
    }
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
    data.complexs.forEach(function(item, index) {
        template += "<div>"
        template += "<span style='font-weight: bold'>[" + (index+1) +  "." + item.address + "]" + item.complexName + "(" + item.updateAt + ")</span>";
        template += "<a href='" + item.landDataUrl + "' target='_blank'>[V]</a>";
        template += "<input type='button' onclick='dataSelectByCode(" + item.complexNo + ")' value='H'/>";
        template += "<input type='button' onclick='dataSendByCode(" + item.complexNo + ")' value='U'/>";
        item.complexPyeongVOList.forEach(function(pyeong) {
            template += "<span style='font-weight: bold'>:: " +  pyeong.pyeongName + "(" + pyeong.pyeongName2 + ")</span>";
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
    }).done((data) => {
        $("#dataContent").empty();
        var createdAt = "";
        var template = "";
        template += "<span sylte='font-weight: bold'>" + data.complexName + "</span>";
        template += "<a href='" + data.url + "' target='_blank'>[V]</a>";
        template += "<hr/>";
        data.articles.forEach(function(article) {
            if (createdAt == "") {
                createdAt = article.createdAt.substring(0, 16);
            }
            if (createdAt != article.createdAt.substring(0, 16)) {
                createdAt = article.createdAt.substring(0, 16);
                template += "<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>";
                template += "<hr/>"
            }
            template += "<div class='float'>"
            template += "<span>" + article.createdAt + "</span><br/>";
            template += "<span>" + article.pyeongName2 + " (" + article.dealCount
                + " / " + article.leaseCount + " / " + article.rentCount + ")</span><br/>";
            template += "<span>최저매매가 : " + article.dealPriceMin + "(" + article.dealPricePerSpaceMin + ")</span><br/>";
            template += "<span>최저전세가 : " + article.leasePriceMin + "(" + article.leasePricePerSpaceMin + ")</span><br/>";
            template += "<span>최고매매가 : " + article.dealPriceMax + "(" + article.dealPricePerSpaceMax + ")</span><br/>";
            template += "<span>최고전세가 : " + article.leasePriceMax + "(" + article.leasePricePerSpaceMax + ")</span><br/>";
            template += "<span>전세가율(최저/최고) : " + article.leasePriceRateMin + " / " + article.leasePriceRateMax + "</span><br/>";
            template += "<span>매매실거래 : " + article.realDealPrice + "(" + article.realDealDate + ")</span><br/>";
            template += "<span>전세실거래 : " + article.realLeasePrice + "(" + article.realLeaseDate + ")</span><br/>";
            template += "</div>";
        })
        $("#dataContent").append(template);
    })
}