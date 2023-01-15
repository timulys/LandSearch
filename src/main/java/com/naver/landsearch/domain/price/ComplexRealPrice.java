package com.naver.landsearch.domain.price;

import com.naver.landsearch.domain.BaseDomain;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table
public class ComplexRealPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long price_id;
    // 평 넘버(ID)
    private String areaNo;
    // 거래 타입
    private String tradeType;
    // 거래 년도
    private String tradeYear;
    // 거래 월
    private String tradeMonth;
    // 거래 일
    private String tradeDate;
    // 거래 금액
    private String formattedPrice;
    // 거래 층
    private String floor;
}
