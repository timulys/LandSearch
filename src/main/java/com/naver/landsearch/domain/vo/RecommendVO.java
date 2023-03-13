package com.naver.landsearch.domain.vo;

import lombok.*;

/**
 * PackageName 	: com.naver.landsearch.domain.vo
 * FileName 	: RecommendVO
 * Author 		: jhchoi
 * Date 		: 2023-03-06
 * Description 	:
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2023-03-06			jhchoi				최초 생성
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendVO {
	// 단지 코드
	private String complexNo;
	// 단지 명
	private String complexName;
	// 주소
	private String address;
	// 네이버 부동산 URL
	private String url;
	// 평형 정보
	private ComplexPyeongVO complexPyeongVo;
	// 추천값
	private Integer value;
}