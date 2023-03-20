package com.naver.landsearch.domain.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * PackageName 	: com.naver.landsearch.domain.vo
 * FileName 	: PriceComplexVO
 * Author 		: jhchoi
 * Date 		: 2023-03-16
 * Description 	: 금액대별 단지 리스트 조회(데이터 표현 형식이 달라 ComplexVO를 사용하지 못함)
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2023-03-16			jhchoi				최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
public class PriceComplexVO {
	// 단지 주소
	private String address;
	// 단지 명
	private String complexName;
	// 네이버 부동산 Direct URL
	private String landDataUrl;
	// 사용승인일
	private String useApproveYear;
	// 평 타입 이름(제곱미터형)
	private String pyeongName;
	// 평 타입 이름(평형)
	private String pyeongName2;
	// 매매 최저 금액
	private String dealPriceMin;
	// 실거래가 최근 매매 금액
	private String realDealPrice;
	// 전세 최저 금액
	private String leasePriceMin;
	// 실거래가 최근 전세 금액
	private String realLeasePrice;
	// 업데이트 일시
	private LocalDateTime updateAt;
	// 현관 타입
	private String entranceType;

	// 기본 사용 생성자
	public PriceComplexVO(String address, String complexName, String landDataUrl, String useApproveYear,
		  String pyeongName, String pyeongName2, String dealPriceMin, String realDealPrice, String leasePriceMin,
		  String realLeasePrice, LocalDateTime updateAt, String entranceType) {
		this.address = address;
		this.complexName = complexName;
		this.landDataUrl = landDataUrl;
		this.useApproveYear = useApproveYear;
		this.pyeongName = pyeongName;
		this.pyeongName2 = pyeongName2;
		this.dealPriceMin = dealPriceMin;
		if (realDealPrice != null)
			this.realDealPrice = realDealPrice;
		else
			this.realDealPrice = "없음";
		this.leasePriceMin = leasePriceMin;
		if (realLeasePrice != null)
			this.realLeasePrice = realLeasePrice;
		else
			this.realLeasePrice = "없음";
		this.updateAt = updateAt;
		this.entranceType = entranceType;
	}

	// 갭 가격
	private Integer gapPrice;
}
