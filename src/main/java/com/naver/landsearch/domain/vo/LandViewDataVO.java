package com.naver.landsearch.domain.vo;

import lombok.*;

/**
 * PackageName 	: com.naver.landsearch.dto
 * FileName 	: LandRealDataVO
 * Author 		: jhchoi
 * Date 		: 2022-12-29
 * Description 	:
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2022-12-29			jhchoi				최초 생성
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LandViewDataVO {
	// 단지 코드
	private String complexNo;
	// 단지 명
	private String complexName;
	// 네이버 부동산 Direct URL
	private String landDataUrl;
	// 제곱미터 평형 면적
	private String supplyArea;
	// 평 타입 이름
	private String pyeongName;
	// 평당가
	private String spacePrice;
	// 매매 최저 금액
	private String dealPriceMin;
	// 전세 최저 금액
	private String leasePriceMin;

	// 실거래가 최근 매매 금액
	private String realDealPrice;
	// 실거래가 최근 매매 년도
	private String realDealYear;
	// 실거래가 최근 매매 월
	private String realDealMonth;
	// 실거래가 최근 매매 층
	private String realDealFloor;

	// 실거래가 최근 전세 금액
	private String realLeasePrice;
	// 실거래가 최근 전세 년도
	private String realLeaseYear;
	// 실거래가 최근 전세 월
	private String realLeaseMonth;
	// 실거래가 최근 전세 층
	private String realLeaseFloor;

	// 업데이트 일시
	private String updateAt;
}
