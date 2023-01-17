package com.naver.landsearch.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * PackageName 	: com.naver.landsearch.domain
 * FileName 	: CreateDomain
 * Author 		: jhchoi
 * Date 		: 2023-01-17
 * Description 	:
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2023-01-17			jhchoi				최초 생성
 */
@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class CreateDomain {
	@CreatedDate
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;
}
