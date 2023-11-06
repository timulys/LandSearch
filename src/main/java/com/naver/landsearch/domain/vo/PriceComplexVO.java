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
public class PriceComplexVO implements Comparable<PriceComplexVO> {
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
	// 공급 평형
	private String supplyArea;
	// 매매 최저 금액
	private String dealPriceMin;
	// 매매 최저 금액(정렬용)
	private Integer dealPriceMinToInt;
	// 매매 최저 평당가
	private String dealPricePerSpaceMin;
	// 매매 최저 평당가(정렬용)
	private Integer dealPricePerSpaceMinToInt;
	// 실거래가 최근 매매 금액
	private String realDealPrice;
	// 실거래가 최근 매매 금액(정렬용)
	private Integer realDealPriceToInt;
	// 실거래가 최근 매매 금액 평당가(정렬용)
	private Integer realDealPricePerSpaceToInt;
	// 전세 최저 금액
	private String leasePriceMin;
	// 전세 최저 평당가
	private String leasePricePerSpaceMin;
	// 실거래가 최근 전세 금액
	private String realLeasePrice;
	// 실거래가 최근 전세 금액(정렬용)
	private Integer realLeasePriceToInt;
	// 업데이트 일시
	private LocalDateTime createAt;
	// 현관 타입
	private String entranceType;

	// 기본 사용 생성자
	public PriceComplexVO(String address, String complexName, String landDataUrl, String useApproveYear, String pyeongName,
		  String pyeongName2, String supplyArea, String dealPriceMin, String dealPricePerSpaceMin, String realDealPrice, String leasePriceMin,
		  String leasePricePerSpaceMin, String realLeasePrice, LocalDateTime createAt, String entranceType) {
		this.address = address;
		this.complexName = complexName;
		this.landDataUrl = landDataUrl;
		this.useApproveYear = useApproveYear;
		this.pyeongName = pyeongName;
		this.pyeongName2 = pyeongName2;
		this.supplyArea = supplyArea;
		this.dealPriceMin = dealPriceMin;
		if (dealPricePerSpaceMin != null)
			this.dealPricePerSpaceMin = dealPricePerSpaceMin;
		else
			this.dealPricePerSpaceMin = "0";
		if (realDealPrice != null)
			this.realDealPrice = realDealPrice;
		else
			this.realDealPrice = "없음";
		this.leasePriceMin = leasePriceMin;
		if (leasePricePerSpaceMin != null)
			this.leasePricePerSpaceMin = leasePricePerSpaceMin;
		else
			this.leasePricePerSpaceMin = "0";
		if (realLeasePrice != null)
			this.realLeasePrice = realLeasePrice;
		else
			this.realLeasePrice = "없음";
		this.createAt = createAt;
		this.entranceType = entranceType;
	}

	// 갭 가격(호가)
	private Integer gapPrice;
	// 갭 가격(실거래가)
	private Integer realGapPrice;
	// 최근 실거래 차액
	private Integer dealMargin;

	@Override
	public int compareTo(PriceComplexVO o) {
		return this.createAt.compareTo(o.createAt);
	}

	public void setRealDealPricePerSpace() {
		double supply = Math.round((Double.valueOf(this.supplyArea) * 30.25)) / 100.00; // 공급제곱미터 평형 변환 후 소수점 둘째자리까지(반올림)
		this.realDealPricePerSpaceToInt = ((int) Math.round(this.realDealPriceToInt / supply));
	}
}
