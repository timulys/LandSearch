package com.naver.landsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naver.landsearch.domain.complex.ComplexDetail;
import com.naver.landsearch.domain.complex.ComplexPyeongDetail;
import com.naver.landsearch.domain.price.ArticleStatistics;
import com.naver.landsearch.domain.price.ComplexRealPrice;
import com.naver.landsearch.domain.price.LandPriceMaxByPtp;
import com.naver.landsearch.domain.vo.realprice.Price;
import com.naver.landsearch.domain.vo.realprice.RealPrice;
import com.naver.landsearch.dto.LandDataDTO;
import com.naver.landsearch.domain.vo.LandViewDataVO;
import com.naver.landsearch.repository.complex.ComplexDetailRepository;
import com.naver.landsearch.repository.complex.ComplexPyeongDetailRepository;
import com.naver.landsearch.repository.price.ArticleStatisticsRepository;
import com.naver.landsearch.repository.price.ComplexRealPriceRepository;
import com.naver.landsearch.repository.price.LandPriceMaxByPtpRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

/**
 * PackageName 	: com.naver.landsearch.service
 * FileName 	: LandDataService
 * Author 		: jhchoi
 * Date 		: 2022-12-29
 * Description 	:
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2022-12-29			jhchoi				최초 생성
 */
@Service
@RequiredArgsConstructor
public class LandDataService {
	// 인증코드
	public static final String AUTH = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IlJFQUxFU1RBVEUiLCJpYXQiOjE2NzIyMTAwODYsImV4cCI6MTY3MjIyMDg4Nn0.Rjio48UHtENluaAJBF8LhYDD-Ud6YN3FPOaqdafwwAM";
	// 네이버 부동산 URL
	public static final String LAND = "https://new.land.naver.com/complexes/";
	// 네이버 부동산 API URL
	public static final String LAND_API = "https://new.land.naver.com/api/complexes/";

	// Dependency injection repositories
	private final ComplexDetailRepository complexDetailRepository;
	private final ComplexPyeongDetailRepository complexPyeongDetailRepository;

	private final ArticleStatisticsRepository articleStatisticsRepository;
	private final LandPriceMaxByPtpRepository landPriceMaxByPtpRepository;

	private final ComplexRealPriceRepository complexRealPriceRepository;

	// Service methods
	public LandViewDataVO saveLandData(String complexCode) {
		LandViewDataVO landViewDataVO = null;
		try { // TODO : 각 서비스 모듈 간 메소드 분리 작업 진행
			// 네이버 부동산 조회 URL
			String url = LAND + complexCode;
			// 네이버 부동산 데이터 조회 URL
			String complexUrl = LAND_API + complexCode + "?sameAddressGroup=true";
			Connection con = Jsoup.connect(complexUrl)
				.header("Authorization", AUTH)
				.method(Connection.Method.GET)
				.ignoreContentType(true);
			Document doc = con.get();
			Node body = doc.selectFirst("body").childNode(0);
			// 네이버 부동산 기본 정보 및 Deal 가격 DTO 생성
			LandDataDTO landDataDTO = new ObjectMapper().readValue(body.toString(), LandDataDTO.class);
			landDataDTO.getComplexDetail().setLandDataUrl(url); // 네이버 부동산 조회 URL
			landDataDTO.getComplexDetail().splitAddress();
			// ComplexDetail insert
			complexDetailRepository.save(landDataDTO.getComplexDetail());

			// 부동산 평형 및 타입
			for (ComplexPyeongDetail complexPyeongDetail : landDataDTO.getComplexPyeongDetailList()) {
				// ComplexDetail과의 연관관계 설정
				complexPyeongDetail.setComplexDetail(landDataDTO.getComplexDetail());

				// Land detail data insert
				// 호가 정보
				if (complexPyeongDetail.getArticleStatistics() != null)
					articleStatisticsRepository.save(complexPyeongDetail.getArticleStatistics());
				// 공시지가 정보
				if (complexPyeongDetail.getLandPriceMaxByPtp() != null)
					landPriceMaxByPtpRepository.save(complexPyeongDetail.getLandPriceMaxByPtp());

				// 네이버 부동산 매매 실거래가 저장
				String realDealUrl = LAND_API + complexCode + "/prices/real?complexNo="
					+ complexCode + "&tradeType=A1&year=5&areaNo=" + complexPyeongDetail.getPyeongNo() + "&type=table";
				ComplexRealPrice realDealPrice = realPriceData(realDealUrl);
				complexRealPriceRepository.save(realDealPrice);
				complexPyeongDetail.setRealDealPrice(realDealPrice);

				// 네이버 부동산 전세 실거래가 저장
				String realLeaseUrl = LAND_API + complexCode + "/prices/real?complexNo="
					+ complexCode + "&tradeType=B1&year=5&areaNo=" + complexPyeongDetail.getPyeongNo() + "&type=table";
				ComplexRealPrice realLeasePrice = realPriceData(realLeaseUrl);
				complexRealPriceRepository.save(realLeasePrice);
				complexPyeongDetail.setRealLeasePrice(realLeasePrice);

				// 평형 정보
				complexPyeongDetailRepository.save(complexPyeongDetail);

				// 호가 및 실거래가 전용 VO
				landViewDataVO = new LandViewDataVO();
				landViewDataVO.setComplexName(landDataDTO.getComplexDetail().getComplexName());
				landViewDataVO.setLandDataUrl(url);
				landViewDataVO.setSupplyArea(complexPyeongDetail.getSupplyArea());
				landViewDataVO.setPyeongName(complexPyeongDetail.getPyeongName());

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return landViewDataVO;
	}

	public ComplexDetail getLandData(String complexCode) {
		ComplexDetail complexDetail = complexDetailRepository.findById(complexCode).orElseThrow(NullPointerException::new);
		for (ComplexPyeongDetail complexPyeongDetail : complexDetail.getComplexPyeongDetailList()) {
			ArticleStatistics articleStatistics = complexPyeongDetail.getArticleStatistics();
			LandPriceMaxByPtp landPriceMaxByPtp = complexPyeongDetail.getLandPriceMaxByPtp();
			ComplexRealPrice realDealPrice = complexPyeongDetail.getRealDealPrice();
			System.out.println(complexPyeongDetail.getPyeongName2() + " 매매 : " + realDealPrice.getFormattedPrice());
			ComplexRealPrice realLeasePrice = complexPyeongDetail.getRealLeasePrice();
			System.out.println(complexPyeongDetail.getPyeongName2() + " 전세 : " + realLeasePrice.getFormattedPrice());
		}
		return complexDetail;
	}

	////////////////////// private methods //////////////////////
	private ComplexRealPrice realPriceData(String url) {
		try {
			Connection con = Jsoup.connect(url)
					.header("Authorization", AUTH)
					.method(Connection.Method.GET)
					.ignoreContentType(true);
			Document doc = con.get();
			Node body = doc.selectFirst("body").childNode(0);
			RealPrice realPrice = new ObjectMapper().readValue(body.toString(), RealPrice.class);

			if (realPrice.getRealPriceOnMonthList().size() > 0) {
				Price price = realPrice.getRealPriceOnMonthList().get(0).getRealPriceList().get(0);
				return ComplexRealPrice.builder()
						.areaNo(realPrice.getAreaNo())
						.tradeType(price.getTradeType())
						.tradeYear(price.getTradeYear())
						.tradeMonth(price.getTradeMonth())
						.tradeDate(price.getTradeDate())
						.formattedPrice(price.getFormattedPrice())
						.floor(price.getFloor())
						.build();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ComplexRealPrice();
	}
}
