package com.naver.landsearch.dto.vo;

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
public class LandRealDataVO {
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

	public void printData() {
		System.out.println(
			this.supplyArea+"\t"+this.pyeongName+"\t"+this.spacePrice+"\t"
			+this.realDealPrice+"("+this.realDealYear+"."+this.realDealMonth+", "+this.realDealFloor+"층)\t"
			+this.dealPriceMin+"\t"
			+this.realLeasePrice+"("+this.realLeaseYear+"."+this.realLeaseMonth+", "+this.realLeaseFloor+"층)\t"
			+this.leasePriceMin
		);
	}
}
