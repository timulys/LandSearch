package com.naver.landsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * PackageName 	: com.naver.landsearch.domain
 * FileName 	: LandPriceMaxByPtp
 * Author 		: jhchoi
 * Date 		: 2023-01-04
 * Description 	:
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2023-01-04			jhchoi				최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LandPriceMaxByPtp {
	// 공시지가 최저가
	private String minPrice;
	// 공시지가 최고가
	private String maxPrice;
	// 공시기준일
	private String stdYmd;
}
