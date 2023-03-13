package com.naver.landsearch.repository.complex;

import com.naver.landsearch.domain.complex.ComplexDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PackageName 	: com.naver.landsearch.repository
 * FileName 	: LandDataRepository
 * Author 		: jhchoi
 * Date 		: 2022-12-29
 * Description 	:
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2022-12-29			jhchoi				최초 생성
 */
@Repository
public interface ComplexDetailRepository extends JpaRepository<ComplexDetail, String> {
	List<ComplexDetail> findAllByAddress1OrderByAddressAscAddress2AscAddress3AscComplexNameAsc(String address1);
}
