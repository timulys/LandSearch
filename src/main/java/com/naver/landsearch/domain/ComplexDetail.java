package com.naver.landsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * PackageName 	: com.naver.landsearch.domain
 * FileName 	: ComplexDetail
 * Author 		: jhchoi
 * Date 		: 2022-12-29
 * Description 	: 네이버 부동산 기본 정보 데이터 도메인
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
public class ComplexDetail {
	@Id
	@Column
	private String complexNo;                    // 단지 코드
	@Column
	private String landDataUrl;                  // 네이버 부동산 Direct URL
	@Column
	private String complexName;                  // 단지 명
	@Column
	private String address;                      // 단지 주소
	@Column
	private String roadAddressPrefix;			 // 단지 주소 Prefix
	@Column
	private String roadAddress;                  // 단지 도로명 주소
	@Column
	private String totalHouseholdCount;          // 단지 총 세대수
	@Column
	private String useApproveYmd;                // 단지 사용승인일(년월)
	@Column
	private String highFloor;                    // 단지 최고층
	@Column
	private String lowFloor;                     // 단지 최저층
	@Column
	private String totalDongCount;               // 단지 동 수
	@Column
	private String dealCount;                    // 단지 매매매물 숫자
	@Column
	private String leaseCount;                   // 단지 전세매물 숫자
	@Column
	private String rentCount;                    // 단지 월세매물 숫자
	@Column
	private String batlRatio;                    // 단지 용적률
	@Column
	private String btlRatio;                     // 단지 건폐율
	@Column
	private String parkingPossibleCount;         // 단지 총 주차대수
	@Column
	private String parkingCountByHousehold;      // 단지 주차 비율
	@Column
	private String constructionCompanyName;      // 단지 건설업체

	// 연관관계 매핑
	@OneToMany(mappedBy = "complexDetail")
	private List<ComplexPyeongDetail> complexPyeongDetailList;
}
