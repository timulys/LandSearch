package com.naver.landsearch.dto.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@NoArgsConstructor
public class LandRealDataVO {
	private String supplyArea;
	private String pyeongName;
	private String spacePrice;
	private String dealPriceMin;
	private String leasePriceMin;

	private String realDealPrice;
	private String realDealYear;
	private String realDealMonth;
	private String realDealFloor;

	private String realLeasePrice;
	private String realLeaseYear;
	private String realLeaseMonth;
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
