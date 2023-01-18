package com.naver.landsearch.domain.vo;

import lombok.*;

/**
 * PackageName 	: com.naver.landsearch.domain.vo
 * FileName 	: ArticleVO
 * Author 		: jhchoi
 * Date 		: 2023-01-17
 * Description 	:
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2023-01-17			jhchoi				최초 생성
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVO {
	// 단지 코드
	private String complexNo;
	// 단지 이름
	private String complexName;
	// 네이버 부동산 Direct URL
	private String landDataUrl;
	// 해당 타입
	private String pyeongName;
	// 해당 타입(평)
	private String pyeongName2;
	// 해당 타입 매매매물 수
	private String dealCount;
	// 해당 타입 전세매물 수
	private String leaseCount;
	// 해당 타입 월세매물 수
	private String rentCount;
	// 해당 타입 매매 최저 금액
	private String dealPriceMin;
	// 해당 타입 매매 최저 평당가
	private String dealPricePerSpaceMin;
	// 해당 타입 매매 최고 금액
	private String dealPriceMax;
	// 해당 타입 매매 최고 평당가
	private String dealPricePerSpaceMax;
	// 해당 타입 전세 최저 금액
	private String leasePriceMin;
	// 해당 타입 전세 최저 평당가
	private String leasePricePerSpaceMin;
	// 해당 타입 전세 최고 금액
	private String leasePriceMax;
	// 해당 타입 전세 최고 평당가
	private String leasePricePerSpaceMax;
	// 해당 타입 전세가율 최저
	private String leasePriceRateMin;
	// 해당 타입 전세가율 최고
	private String leasePriceRateMax;
	// 최근 매매 실거래가
	private String realDealPrice;
	// 최근 매매 거래 일자
	private String realDealDate;
	// 최근 전세 실거래가
	private String realLeasePrice;
	// 최근 전세 거래 일자
	private String realLeaseDate;
	// 생성 일자
	private String createdAt;
}
