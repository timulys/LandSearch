package com.naver.landsearch.domain.complex;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naver.landsearch.domain.price.ArticleStatistics;
import com.naver.landsearch.domain.BaseDomain;
import com.naver.landsearch.domain.price.ComplexRealPrice;
import com.naver.landsearch.domain.price.LandPriceMaxByPtp;
import lombok.*;

import javax.persistence.*;

/**
 * PackageName 	: com.naver.landsearch.domain
 * FileName 	: ComplexPyeongDetail
 * Author 		: jhchoi
 * Date 		: 2022-12-29
 * Description 	: 네이버 부동산 평형별 정보 도메인
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
public class ComplexPyeongDetail {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pyeong_id")
	private Long id;
	// 평 번호
	private String pyeongNo;
	// 공급면적(제곱미터)
	private String supplyArea;
	// 공급면적명(제곱미터) ex> 110A
	private String pyeongName;
	// 공급면적(평)
	private String supplyPyeong;
	// 공급면적명(평) ex> 34평형
	private String pyeongName2;
	// 전용면적(제곱미터)
	private String exclusiveArea;
	// 전용면적(평)
	private String exclusivePyeong;
	// 해당면적 세대수
	private String householdCountByPyeong;
	// 전용률
	private String exclusiveRate;
	// 입구 타입
	private String entranceType;
	// 방 개수
	private String roomCnt;
	// 화장실 개수
	private String bathroomCnt;

	// 연관관계 설정
	// 평형별 가격 정보 매핑
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "article_id")
	private ArticleStatistics articleStatistics;
	// 공시지가 정보 매핑
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "ptp_id")
	private LandPriceMaxByPtp landPriceMaxByPtp;
	// 매매 실거래가 정보 매핑
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "deal_price_id")
	private ComplexRealPrice realDealPrice;
	// 전세 실거래가 정보 매핑
	@OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "lease_price_id")
	private ComplexRealPrice realLeasePrice;

	// 연관관계 매핑
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "complex_no")
	@ToString.Exclude
	private ComplexDetail complexDetail;
}
