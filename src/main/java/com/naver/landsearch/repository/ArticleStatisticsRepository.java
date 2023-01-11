package com.naver.landsearch.repository;

import com.naver.landsearch.domain.ArticleStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PackageName 	: com.naver.landsearch.repository
 * FileName 	: ArticleStatisticsRepository
 * Author 		: jhchoi
 * Date 		: 2023-01-05
 * Description 	:
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2023-01-05			jhchoi				최초 생성
 */
@Repository
public interface ArticleStatisticsRepository extends JpaRepository<ArticleStatistics, Long> {
}
