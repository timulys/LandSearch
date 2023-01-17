package com.naver.landsearch.domain.realprice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * PackageName 	: com.naver.landsearch.domain
 * FileName 	: RealPrice
 * Author 		: jhchoi
 * Date 		: 2022-12-29
 * Description 	: 네이버 부동산 실거래가 정보 도메인
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2022-12-29			jhchoi				최초 생성
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RealPrice {
	// 평 타입 번호
	private String areaNo;
	// 실거래가 목록
	private List<RealPriceOnMonth> realPriceOnMonthList;
}