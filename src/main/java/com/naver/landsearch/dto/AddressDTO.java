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
public class AddressDTO {
	private String address1;
	private String address2;
	private String address3;
	private String address4;
}
