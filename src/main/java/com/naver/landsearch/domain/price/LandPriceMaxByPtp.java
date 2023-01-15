package com.naver.landsearch.domain.price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naver.landsearch.domain.BaseDomain;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table
public class LandPriceMaxByPtp {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ptp_id")
	private Long id;
	// 공시지가 최저가
	private String minPrice;
	// 공시지가 최고가
	private String maxPrice;
	// 공시기준일
	private String stdYmd;
}