var origin = "http://localhost:9001/api/";
// var origin = "http://192.168.55.81:9090/api/";

function dataSend() {
    var code = $("#complexCode").val();
    dataSendByCode(code);
}

// 전체 데이터 조회
function dataSelectAll() {
    var url = origin + "allData";
    $.ajax({
        url: url,
        type: "GET",
    }).done((data) => {
        $("#dataContent").empty();
        $("#dataContent").append(renderTemplate(data));
    })
}

function dataAddress() {
    var url = origin + "selectByAddress";
    var param = {
        address1 : $("#address1").val(),
        address2 : $("#address2").val(),
        address3 : $("#address3").val(),
        address4 : $("#address4").val()
    };
    $.ajax({
        url: url,
        data: param,
        type: "GET"
    }).done((data) => {
        $("#dataContent").empty();
        $("#dataContent").append(renderTemplate(data));
    })
}

function dataSearchByDealPyeongRange() {
    var param = {
        address1 : $("#address1").val(),
        address2 : $("#address2").val(),
        address3 : $("#address3").val(),
        address4 : $("#address4").val(),
        exPyeong : $("#pyeongRange").val()
    };
    renderByPrice(param, origin + "selectByDealPricePyeongRange")
}

function dataSearchByRealDealPyeongRange() {
    var param = {
        address1 : $("#address1").val(),
        address2 : $("#address2").val(),
        address3 : $("#address3").val(),
        address4 : $("#address4").val(),
        exPyeong : $("#pyeongRange").val()
    };
    renderByPrice(param, origin + "selectByRealDealPricePyeongRange")
}

function showShortsForm() {
    var param = {
        address1 : $("#address1").val(),
        address2 : $("#address2").val(),
        address3 : $("#address3").val(),
        address4 : $("#address4").val(),
        exPyeong : $("#pyeongRange").val()
    };
    $.ajax({
        url: origin + "selectByRealDealPricePyeongRange",
        data: param,
        type: "GET",
    }).done((data) => {
        var template = "";
        $("#dataContent").empty();
        template += getToday() + "</br>";
        template += $("#address1").val() + " " + $("#address2").val();
        if ($("#address3").val())
            template += " " + $("#address3").val();
        if ($("#address4").val())
            template += " " + $("#address4").val();
        template +="  전용 " + $("#pyeongRange").val() + "평대</br>";
        template += "300세대 이상 아파트</br>실거래 평당가 TOP 10 입니다.</br>";
        template += "(가장 최근 반영된 실거래가 기준입니다.)</br></br>";
        data.complexs.forEach(function(item, index) {
            if (index < 10) {
                template += "<div>";
                template += "<span>" + (index + 1) + "위 " + item.complexName + ", " + item.pyeongName + "타입, 평당가 " + item.realDealPricePerSpaceToInt + "만원</span>"
                template += "</div>";
            }
        });

        $("#dataContent").append(template);
    });
}

function dataSelectByDealPricePerSpace() {
    var param = {
        address1 : $("#address1").val(),
        address2 : $("#address2").val(),
        address3 : $("#address3").val(),
        address4 : $("#address4").val()
    };
    renderByPrice(param, origin + "selectByDealPricePerSpace");
}

function dataSelectByRealDealPrice() {
    var param = {
        address1 : $("#address1").val(),
        address2 : $("#address2").val(),
        address3 : $("#address3").val(),
        address4 : $("#address4").val()
    };
    renderByPrice(param, origin + "selectByRealDealPrice");
}

function dataSelectByPriceRange() {
    var param = {
        address1 : $("#address1").val(),
        address2 : $("#address2").val(),
        address3 : $("#address3").val(),
        address4 : $("#address4").val(),
        searchPriceType : $("#priceType").val(),
        searchPriceRange : $("#priceRange").val()
    };
    renderByPrice(param, origin + "selectByPriceRange");
}

function renderByPrice(param, url) {
    $.ajax({
        url: url,
        data: param,
        type: "GET",
    }).done((data) => {
        var template = "";
        $("#dataContent").empty();
        data.complexs.forEach(function(item, index) {
            if ($("#onlyStairs").prop('checked') && item.entranceType == "복도식") {
                return;
            }
            if ($("#approveYear").val() != '' && (item.useApproveYear).indexOf($("#approveYear").val()) != 0) {
                return;
            }
            template += "<div>"
            if (item.gapPrice <= 5000) {
                template += "<span style='font-weight: bold; color: red'>[" + (index+1) +  "." + item.address + "]" + item.complexName + "(" + item.useApproveYear + ")</span>";
            } else {
                template += "<span style='font-weight: bold'>[" + (index + 1) + "." + item.address + "]" + item.complexName + "(" + item.useApproveYear + ")</span>";
            }
            template += "<span><b> " + item.pyeongName + "(" + item.pyeongName2 + ")[" + item.entranceType + "]</b></span>";
            template += "<a href='" + item.landDataUrl + "' target='_blank'>[V]</a>";
            template += "<span> : [[매호 - " + item.dealPriceMin + "(" + item.dealPricePerSpaceMinToInt + ") (매실 : " + item.realDealPrice + "(" + item.realDealPricePerSpaceToInt + "))]] // [[전호 - " +
                item.leasePriceMin + "(" + item.leasePricePerSpaceMin + ") (전실 : " + item.realLeasePrice + ")]]</span>";
            if (item.gapPrice <= 5000) {
                template += "<span style='font-weight: bold; color: red'> 갭 : " + item.gapPrice  + "(실갭:" + item.realGapPrice + ")</span>";
            } else {
                template += "<span> 갭 가격 : " + item.gapPrice + "(실갭:" + item.realGapPrice + ")</span>";
            }
        });
        $("#dataContent").append(template);
    });
}



function dataSync() {
    var url = origin + "codeSync";
    $.ajax({
        url: url,
        type: "GET"
    }).done((data) => {
        $("#dataContent").empty();
        $("#dataContent").append(data.resultMsg);
    })
}

function dataUpdateAll() {
    if (confirm("시간이 다소 소요됩니다. 정말 업데이트 하시겠습니까?")) {
        var url = origin + "allUpdate";
        $.ajax({
            url: url,
            type: "GET",
        }).done((data) => {
            console.log(data);
            $("#dataContent").empty();
        })
    } else {
        return false;
    }
}

function dataUpdateAllByAddress() {
    var address1 = $("#address1").val();
    var region = address1;
    var address2 = $("#address2").val();
    region += address2 != null ? address2 : "";
    var address3 = $("#address3").val();
    region += address3 != null ? address3 : "";
    var address4 = $("#address4").val();
    region += address4 != null ? address4 : "";

    var url = origin + "updateByAddress";
    var param = {
        address1 : address1,
        address2 : address2,
        address3 : address3,
        address4 : address4
    };
    if (confirm(region + " 업데이트를 시작합니다. 정말 업데이트 하시겠습니까?")) {
        $.ajax({
            url: url,
            type: "POST",
            dataType: "json",
            data: param
        }).done((data) => {
            console.log(data);
            $("#dataContent").empty();
        })
    } else {
        return false;
    }
}

function dataRecommend() {
    var url = origin + "recommend";
    var param = {
        address1 : $("#address1").val(),
        address2 : $("#address2").val(),
        address3 : $("#address3").val(),

    };
    $.ajax({
        url: url,
        data: param,
        dataType: "json",
        type: "GET"
    }).done((data) => {
        $("#dialog").empty();
        var template = "";
        data.recommendList.forEach(function(item, index) {
            if (item.value == 2) {
                template += "<span sylte='font-weight: bold; font-size: 8px'>★ " + (index+1)
            } else {
                template += "<span sylte='font-weight: bold; font-size: 8px'>" + (index+1)
            }
            template += ". " + item.complexName + "<a href='" + item.url + "' target='_blank'>[V]</a></span>";
            template += "<span>(" + item.complexPyeongVo.pyeongName + ") : " + item.complexPyeongVo.dealPriceMin + " / "
                + item.complexPyeongVo.leasePriceMin + " (" + item.complexPyeongVo.gapPrice + ") - " + item.address + "</span>";
            template += "<br/>";
        });

        $("#dialog").append(template);
        $("#dialog").dialog({
            title: '추천 매물 목록',
            modal: true,
            resizable: true,
            width: '2500',
            height: '1000'
        });
    });
}

// 데이터 저장 및 조회
function dataSendByCode(code) {
    var url = origin + "landByCode";
    var param = {
        complexCode : code
    };
    $.ajax({
        url: url,
        data: param,
        type: "GET",
    }).done((data) => {
        var complexVO = data.complexVO;
        $("#dataContent").append("<span style='font-weight: bold'>[" + complexVO.address + "]" + complexVO.complexName + "(" + complexVO.complexNo + ") 업데이트 완료</span></br>");
    })
}

function renderTemplate(data) {
    var template = "";
    data.complexs.forEach(function(item, index) {
        template += "<div>"
        template += "<span style='font-weight: bold'>[" + (index+1) +  "." + item.address + "]" + item.complexName + "(" + item.useApproveYmd + ")</span>";
        template += "<a href='" + item.landDataUrl + "' target='_blank'>[V]</a>";
        template += "<input type='button' onclick='dataSelectByCode(" + item.complexNo + ")' value='H'/>";
        template += "<input type='button' onclick='dataSendByCode(" + item.complexNo + ")' value='U'/>";
        template += "<span style='font-size: 8px'>(" + item.updateAt + ")</span>"
        item.complexPyeongVOList.forEach(function(pyeong) {
            var entrance = "";
            pyeong.entranceType == "계단식" ? entrance = "계" : entrance = "복";
            if ($("#onlyStairs").prop('checked') && entrance == "복") {
                return;
            }
            if (pyeong.leasePriceRateMin * 1 >= 70 || (pyeong.gapPrice <= 5000 && pyeong.gapPrice > 0)) {
                if (pyeong.gapPrice > 3000 && pyeong.gapPrice <= 5000) {
                    template += "<span style='font-weight: bold; color: red;'>:: [★]" +  pyeong.pyeongName + "(" + pyeong.pyeongName2 + "[" + entrance + "])</span>";
                } else if (pyeong.gapPrice <= 3000) {
                    template += "<span style='font-weight: bold; color: red;'>:: [★★★]" +  pyeong.pyeongName + "(" + pyeong.pyeongName2 + "[" + entrance + "])</span>";
                } else {
                    template += "<span style='font-weight: bold; color: red;'>:: " + pyeong.pyeongName + "(" + pyeong.pyeongName2 + "[" + entrance + "])</span>";
                }
                template += "<span style='font-weight: bold; color: red; font-size: 8px'>[" +  pyeong.dealPriceMin + "(" + pyeong.dealPricePerSpaceMin + ")/" +
                    pyeong.leasePriceMin + "(" + pyeong.leasePricePerSpaceMin + ") : " + pyeong.gapPrice + "만 (" + pyeong.leasePriceRateMin + ")]</span>";
            } else {
                template += "<span style='font-size: 8px'>:: " + pyeong.pyeongName + "(" + pyeong.pyeongName2 + "[" + entrance + "])</span>";
                template += "<span style='font-size: 8px'>[" + pyeong.dealPriceMin + "(" + pyeong.dealPricePerSpaceMin + ")/" +
                    pyeong.leasePriceMin + "(" + pyeong.leasePricePerSpaceMin + ")]</span>";
            }
            if (pyeong.landPriceMaxByPtp * 1 > 0 && pyeong.landPriceMaxByPtp * 1 <= 100000000) {
                template += "<span style='font-weight: bold;'>[공]</span>";
            }
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
    var url = origin + "getByCode";
    var param = {
        complexCode : code
    };
    $.ajax({
        url: url,
        data: param,
        type: "GET",
    }).done((data) => {
        $("#dialog").empty();
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
                template += "<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>";
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
        $("#dialog").append(template);
        $("#dialog").dialog({
            title: '단지 조회',
            modal: true,
            resizable: true,
            width: '2500',
            height: '1000'
        });
    })
}

// 갭검색
function selectDealGapPrice() { // 호가갭 검색
    var param = { // 주소 검색 param
        address1 : $("#address1").val(),
        address2 : $("#address2").val(),
        address3 : $("#address3").val(),
        address4 : $("#address4").val(),
        exPyeong : $("#pyeongRange").val(),
        searchGap : $("#searchGap").val()
    };
    renderByPrice(param, origin + "selectDealGapPrice")
}
function selectRealDealGapPrice() { // 실거래가갭 검색
    var param = { // 주소 검색 param
        address1 : $("#address1").val(),
        address2 : $("#address2").val(),
        address3 : $("#address3").val(),
        address4 : $("#address4").val(),
        exPyeong : $("#pyeongRange").val(),
        searchGap : $("#searchGap").val()
    };
    renderByPrice(param, origin + "selectRealDealGapPrice")
}

function getToday() {
    var today = new Date();

    var year = today.getFullYear();
    var month = ('0' + (today.getMonth() + 1)).slice(-2);
    var day = ('0' + today.getDate()).slice(-2);

    return year + '년 ' + month  + '월 ' + day + '일';
}

$(function() {
    var url = origin + "getAddress";

    $("#address1").change(function() {
        $("#address2").empty();
        $("#address3").empty();
        $("#address4").empty();
        var addressDTO = {
            address1 : $("#address1").val()
        };
        $.ajax({
            url: url,
            dataType: 'json',
            data: addressDTO,
            type: "POST"
        }).done((data) => {
            if (data.address2 != null) {
                $("#address2").append("<option value=''>전체</option>")
                for (let i = 0; i < data.address2.length; i++) {
                    $("#address2").append("<option value='" + data.address2[i] + "'>" + data.address2[i] + "</option>")
                }
            }
        })
    });

    $("#address2").change(function() {
        $("#address3").empty();
        $("#address4").empty();
        var addressDTO = {
            address1 : $("#address1").val(),
            address2 : $("#address2").val()
        };
        $.ajax({
            url: url,
            dataType: 'json',
            data: addressDTO,
            type: "POST"
        }).done((data) => {
            if (data.address3 != null) {
                $("#address3").append("<option value=''>전체</option>")
                for (let i = 0; i < data.address3.length; i++) {
                    $("#address3").append("<option value='" + data.address3[i] + "'>" + data.address3[i] + "</option>")
                }
            }
        })
    });

    $("#address3").change(function() {
        $("#address4").empty();
        var addressDTO = {
            address1 : $("#address1").val(),
            address2 : $("#address2").val(),
            address3 : $("#address3").val()
        };
        $.ajax({
            url: url,
            dataType: 'json',
            data: addressDTO,
            type: "POST"
        }).done((data) => {
            if (data.address4 != null && data.address4[0] != null) {
                $("#address4").append("<option value=''>전체</option>")
                for (let i = 0; i < data.address4.length; i++) {
                    $("#address4").append("<option value='" + data.address4[i] + "'>" + data.address4[i] + "</option>")
                }
            }
        })
    });
})