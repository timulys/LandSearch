package com.naver.landsearch.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RealPrice extends BaseDomain {
	@JsonAlias("realPriceList")
	private List<Price> realPriceList;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Price {
		// 거래 년도
		private String tradeYear;
		// 거래 월
		private String tradeMonth;
		// 거래 일
		private String tradeDate;
		// 거래 금액
		private String formattedPrice;
		// 거래 층
		private String floor;
	}
}