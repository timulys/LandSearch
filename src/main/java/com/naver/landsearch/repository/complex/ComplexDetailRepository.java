package com.naver.landsearch.repository.complex;

import com.naver.landsearch.domain.complex.ComplexDetail;
import com.naver.landsearch.domain.vo.PriceComplexVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
	List<ComplexDetail> findAllByAddress1OrderByAddressAscAddress2AscAddress3AscComplexNameAsc
		(String address1);
	List<ComplexDetail> findAllByAddress1AndAddress2OrderByAddressAscAddress2AscAddress3AscComplexNameAsc
		(String address1, String address2);
	List<ComplexDetail> findAllByAddress1AndAddress2AndAddress3OrderByAddressAscAddress2AscAddress3AscComplexNameAsc
		(String address1, String address2, String address3);
	List<ComplexDetail> findAllByAddress1AndAddress2AndAddress3AndAddress4OrderByAddressAscAddress2AscAddress3AscAddress4AscComplexNameAsc
		(String address1, String address2, String address3, String address4);

	@Query(value = "select m.address2 from ComplexDetail m where m.address1 = :address1 group by m.address2")
	List<String> findAllByAddress2(@Param("address1") String address1);

	@Query(value = "select m.address3 from ComplexDetail m where m.address1 = :address1 and m.address2 = :address2" +
		" group by m.address3")
	List<String> findAllByAddress3(@Param("address1") String address1, @Param("address2") String address2);

	@Query(value = "select m.address4 from ComplexDetail m where m.address1 = :address1 and m.address2 = :address2" +
		" and m.address3 = :address3 group by m.address4")
	List<String> findAllByAddress4(@Param("address1") String address1, @Param("address2") String address2, @Param("address3") String address3);
}
