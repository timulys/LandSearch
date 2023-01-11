package com.naver.landsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * PackageName 	: com.naver.landsearch.domain
 * FileName 	: ArticleStatistics
 * Author 		: jhchoi
 * Date 		: 2022-12-29
 * Description 	: 평형 별 가격 정보 도메인
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2022-12-29			jhchoi				최초 생성
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table
public class ArticleStatistics extends BaseDomain {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "article_id")
	private Long id;
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
	// 해당 타입 최저 월세 보증금
	private String rentDepositPriceMin;
	// 해당 타입 최저 월세
	private String rentPriceMin;
	// 해당 타입 최고 월세 보증금
	private String rentDepositPriceMax;
	// 해당 타입 최고 월세
	private String rentPriceMax;
}
