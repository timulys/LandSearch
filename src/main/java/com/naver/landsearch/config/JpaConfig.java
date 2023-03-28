package com.naver.landsearch.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

/**
 * PackageName 	: com.naver.landsearch.config
 * FileName 	: JpaConfig
 * Author 		: jhchoi
 * Date 		: 2023-03-22
 * Description 	:
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2023-03-22			jhchoi				최초 생성
 */
@Configuration
public class JpaConfig {

	@Bean
	JPAQueryFactory jpaQueryFactory(EntityManager em) {
		return new JPAQueryFactory(em);
	}
}
