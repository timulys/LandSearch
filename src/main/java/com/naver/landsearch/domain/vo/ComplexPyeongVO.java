package com.naver.landsearch.domain.vo;

import lombok.*;

/**
 * PackageName 	: com.naver.landsearch.domain.vo
 * FileName 	: ComplexPyeongVO
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
public class ComplexPyeongVO {
	// 평타입(제곱미터) 이름
	private String pyeongName;
	// 평타입(평) 이름
	private String pyeongName2;
	// 매매 최저가
	private String dealPriceMin;
	// 매매 최저가 평당가
	private String dealPricePerSpaceMin;
	// 전세 최저가
	private String leasePriceMin;
	// 전세 최저가 평당가
	private String leasePricePerSpaceMin;
	// 최저 전세가율
	private String leasePriceRateMin;
	// 최고 공시가격
	private String landPriceMaxByPtp;
}
