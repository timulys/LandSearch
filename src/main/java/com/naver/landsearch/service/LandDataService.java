package com.naver.landsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naver.landsearch.domain.ComplexDetail;
import com.naver.landsearch.domain.ComplexPyeongDetail;
import com.naver.landsearch.dto.LandDataDTO;
import com.naver.landsearch.dto.LandRealDataDTO;
import com.naver.landsearch.dto.vo.LandViewDataVO;
import com.naver.landsearch.repository.ArticleStatisticsRepository;
import com.naver.landsearch.repository.ComplexDetailRepository;
import com.naver.landsearch.repository.ComplexPyeongDetailRepository;
import com.naver.landsearch.repository.LandPriceMaxByPtpRepository;
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

	// Service methods
	public LandViewDataVO saveLandData(String complexCode) {
		LandViewDataVO landViewDataVO = null;
		try {
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

			// ComplexDetail insert
			complexDetailRepository.save(landDataDTO.getComplexDetail());

			// 부동산 평형 및 타입
			for (ComplexPyeongDetail complexPyeongDetail : landDataDTO.getComplexPyeongDetailList()) {
				// ComplexDetail과의 연관관계 설정
				complexPyeongDetail.setComplexDetail(landDataDTO.getComplexDetail());

				// Land detail data insert
				// 호가 정보
				articleStatisticsRepository.save(complexPyeongDetail.getArticleStatistics());
				// 공시지가 정보
				landPriceMaxByPtpRepository.save(complexPyeongDetail.getLandPriceMaxByPtp());
				// 평형 정보
				complexPyeongDetailRepository.save(complexPyeongDetail);

				// 네이버 부동산 매매 실거래가 조회 URL
				String realDealUrl = LAND_API + complexCode + "/prices/real?complexNo="
					+ complexCode + "&tradeType=A1&year=5&areaNo=" + complexPyeongDetail.getPyeongNo() + "&type=table";

				con = Jsoup.connect(realDealUrl)
					.header("Authorization", AUTH)
					.method(Connection.Method.GET)
					.ignoreContentType(true);
				doc = con.get();
				body = doc.selectFirst("body").childNode(0);

				LandRealDataDTO realDealDTO = new ObjectMapper().readValue(body.toString(), LandRealDataDTO.class);

				// 네이버 부동산 전세 실거래가 조회 URL
				String realLeaseUrl = LAND_API + complexCode + "/prices/real?complexNo="
					+ complexCode + "&tradeType=B1&year=5&areaNo=" + complexPyeongDetail.getPyeongNo() + "&type=table";

				con = Jsoup.connect(realLeaseUrl)
					.header("Authorization", AUTH)
					.method(Connection.Method.GET)
					.ignoreContentType(true);
				doc = con.get();
				body = doc.selectFirst("body").childNode(0);

				LandRealDataDTO realLeaseDTO = new ObjectMapper().readValue(body.toString(), LandRealDataDTO.class);

				// 호가 및 실거래가 전용 VO
				landViewDataVO = new LandViewDataVO();
				landViewDataVO.setSupplyArea(complexPyeongDetail.getSupplyArea());
				landViewDataVO.setPyeongName(complexPyeongDetail.getPyeongName());
				// 호가 정보
				if (complexPyeongDetail.getArticleStatistics() != null) {
					landViewDataVO.setSpacePrice(complexPyeongDetail.getArticleStatistics().getDealPricePerSpaceMin());
					landViewDataVO.setDealPriceMin(complexPyeongDetail.getArticleStatistics().getDealPriceMin());
					landViewDataVO.setLeasePriceMin(complexPyeongDetail.getArticleStatistics().getLeasePriceMin());
				} else {
					landViewDataVO.setSpacePrice("0");
					landViewDataVO.setDealPriceMin("0");
					landViewDataVO.setLeasePriceMin("0");
				}
				// 매매 실거래가 정보
				if (realDealDTO.getRealPriceOnMonthList().size() > 0) {
					landViewDataVO.setRealDealPrice(realDealDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getFormattedPrice());
					landViewDataVO.setRealDealYear(realDealDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getTradeYear());
					landViewDataVO.setRealDealMonth(realDealDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getTradeMonth());
					landViewDataVO.setRealDealFloor(realDealDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getFloor());
				} else {
					landViewDataVO.setRealDealPrice("0");
					landViewDataVO.setRealDealYear("0");
					landViewDataVO.setRealDealMonth("0");
					landViewDataVO.setRealDealFloor("0");
				}
				// 전세 실거래가 정보
				if (realLeaseDTO.getRealPriceOnMonthList().size() > 0) {
					landViewDataVO.setRealLeasePrice(realLeaseDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getFormattedPrice());
					landViewDataVO.setRealLeaseYear(realLeaseDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getTradeYear());
					landViewDataVO.setRealLeaseMonth(realLeaseDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getTradeMonth());
					landViewDataVO.setRealLeaseFloor(realLeaseDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getFloor());
				} else {
					landViewDataVO.setRealLeasePrice("0");
					landViewDataVO.setRealLeaseYear("0");
					landViewDataVO.setRealLeaseMonth("0");
					landViewDataVO.setRealLeaseFloor("0");
				}
				landViewDataVO.printData();
			}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return landViewDataVO;
	}

	public LandViewDataVO getLandData(String complexCode) {
		Optional<ComplexDetail> complexDetail = complexDetailRepository.findById(complexCode);
		return null;
	}
}
