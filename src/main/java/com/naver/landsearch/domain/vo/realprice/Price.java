package com.naver.landsearch.domain.vo.realprice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naver.landsearch.domain.price.ComplexRealPrice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Price {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "real_price_id")
    private Long real_price_id;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_id")
    private ComplexRealPrice complexRealPrice;
}
