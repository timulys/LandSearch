package com.naver.landsearch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naver.landsearch.domain.RealPrice;
import lombok.Data;

import java.util.List;

/**
 * PackageName 	: com.naver.landsearch.dto
 * FileName 	: LandRealDataDTO
 * Author 		: jhchoi
 * Date 		: 2022-12-29
 * Description 	: 네이버 부동산 실거래가 정보
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2022-12-29			jhchoi				최초 생성
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LandRealDataDTO {
	private String areaNo;
	private List<RealPrice> realPriceOnMonthList;
}
