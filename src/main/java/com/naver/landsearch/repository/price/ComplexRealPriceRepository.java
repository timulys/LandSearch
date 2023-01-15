package com.naver.landsearch.repository.price;

import com.naver.landsearch.domain.price.ComplexRealPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplexRealPriceRepository extends JpaRepository<ComplexRealPrice, Long> {
}
