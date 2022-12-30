package com.naver.landsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplexDetail {
	// 단지 코드
	private String complexNo;
	// 단지 명
	private String complexName;
	// 단지 주소
	private String address;
	// 단지 도로명 주소
	private String roadAddress;
	// 단지 총 세대수
	private String totalHouseholdCount;
	// 단지 사용승인일(년월)
	private String useApproveYmd;
	// 단지 최고층
	private String highFloor;
	// 단지 최저층
	private String lowFloor;
	// 단지 동 수
	private String totalDongCount;
	// 단지 매매매물 숫자
	private String dealCount;
	// 단지 전세매물 숫자
	private String leaseCount;
	// 단지 월세매물 숫자
	private String rentCount;
	// 단지 용적률
	private String batlRatio;
	// 단지 건폐율
	private String btlRatio;
	// 단지 총 주차대수
	private String parkingPossibleCount;
	// 단지 주차 비율
	private String parkingCountByHousehold;
}
