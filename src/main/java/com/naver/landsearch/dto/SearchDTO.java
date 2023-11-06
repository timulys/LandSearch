package com.naver.landsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PackageName 	: com.naver.landsearch.dto
 * FileName 	: AddressDTO
 * Author 		: jhchoi
 * Date 		: 2023-03-13
 * Description 	:
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2023-03-13			jhchoi				최초 생성
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {
	// 주소 검색 옵션
	private String address1;
	private String address2;
	private String address3;
	private String address4;

	// 평형대 검색 옵션
	private String exPyeong;

	// 갭 검색 옵션
	private Integer searchGap;

	// 금액대 검색 타입
	private Integer searchPriceType; // 0 : 호가, 1 : 실거래가
	// 금액대 검색 범위
	private Integer searchPriceRange; // x 억
}
