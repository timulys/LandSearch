package com.naver.landsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class ComplexPyeongDetail extends BaseDomain {
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
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "article_id")
	private ArticleStatistics articleStatistics;
	// 공시지가 정보 매핑
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ptp_id")
	private LandPriceMaxByPtp landPriceMaxByPtp;
	// 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "complex_no")
	private ComplexDetail complexDetail;
}
