package com.naver.landsearch.repository;

import com.naver.landsearch.domain.LandPriceMaxByPtp;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PackageName 	: com.naver.landsearch.repository
 * FileName 	: LandPriceMaxByPtpRepository
 * Author 		: jhchoi
 * Date 		: 2023-01-05
 * Description 	:
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2023-01-05			jhchoi				최초 생성
 */
public interface LandPriceMaxByPtpRepository extends JpaRepository<LandPriceMaxByPtp, Long> {
}
