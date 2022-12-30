package com.naver.landsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naver.landsearch.domain.ComplexPyeongDetail;
import com.naver.landsearch.dto.LandDataDTO;
import com.naver.landsearch.dto.LandRealDataDTO;
import com.naver.landsearch.dto.vo.LandRealDataVO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
public class LandDataService {
	// 인증코드 상수 선언
	public static final String AUTH = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IlJFQUxFU1RBVEUiLCJpYXQiOjE2NzIyMTAwODYsImV4cCI6MTY3MjIyMDg4Nn0.Rjio48UHtENluaAJBF8LhYDD-Ud6YN3FPOaqdafwwAM";

	public void getLandData(String complexCode) {
		try {
			// 네이버 부동산 조회 URL
			String url = "https://new.land.naver.com/complexes/" + complexCode;
			// 네이버 부동산 데이터 조회 URL
			String complexUrl = "https://new.land.naver.com/api/complexes/" + complexCode + "?sameAddressGroup=true";
			Connection con = Jsoup.connect(complexUrl)
				.header("Authorization", AUTH)
				.method(Connection.Method.GET)
				.ignoreContentType(true);
			Document doc = con.get();
			Node body = doc.selectFirst("body").childNode(0);
			// 네이버 부동산 기본 정보 및 Deal 가격 DTO 생성
			LandDataDTO landDataDTO = new ObjectMapper().readValue(body.toString(), LandDataDTO.class);
			landDataDTO.setLandDataUrl(url); // 네이버 부동산 조회 URL

			// 부동산 기본 정보
			System.out.println(landDataDTO.getComplexDetail().getComplexNo() + "\t"
				+ landDataDTO.getComplexDetail().getComplexName() + "\t"
				+ landDataDTO.getComplexDetail().getAddress() + "\t"
				+ landDataDTO.getLandDataUrl());

			// 부동산 평형 및 타입
			for (ComplexPyeongDetail complexPyeongDetail : landDataDTO.getComplexPyeongDetailList()) {
				// 네이버 부동산 매매 실거래가 조회 URL
				String realDealUrl = "https://new.land.naver.com/api/complexes/" + complexCode + "/prices/real?complexNo="
					+ complexCode + "&tradeType=A1&year=5&areaNo=" + complexPyeongDetail.getPyeongNo() + "&type=table";

				con = Jsoup.connect(realDealUrl)
					.header("Authorization", AUTH)
					.method(Connection.Method.GET)
					.ignoreContentType(true);
				doc = con.get();
				body = doc.selectFirst("body").childNode(0);

				LandRealDataDTO realDealDTO = new ObjectMapper().readValue(body.toString(), LandRealDataDTO.class);

				// 네이버 부동산 전세 실거래가 조회 URL
				String realLeaseUrl = "https://new.land.naver.com/api/complexes/" + complexCode + "/prices/real?complexNo="
					+ complexCode + "&tradeType=B1&year=5&areaNo=" + complexPyeongDetail.getPyeongNo() + "&type=table";

				con = Jsoup.connect(realLeaseUrl)
					.header("Authorization", AUTH)
					.method(Connection.Method.GET)
					.ignoreContentType(true);
				doc = con.get();
				body = doc.selectFirst("body").childNode(0);

				LandRealDataDTO realLeaseDTO = new ObjectMapper().readValue(body.toString(), LandRealDataDTO.class);

				// 호가 및 실거래가 전용 VO
				LandRealDataVO vo = new LandRealDataVO();
				vo.setSupplyArea(complexPyeongDetail.getSupplyArea());
				vo.setPyeongName(complexPyeongDetail.getPyeongName());
				// 호가 정보
				if (complexPyeongDetail.getArticleStatistics() != null) {
					vo.setSpacePrice(complexPyeongDetail.getArticleStatistics().getDealPricePerSpaceMin());
					vo.setDealPriceMin(complexPyeongDetail.getArticleStatistics().getDealPriceMin());
					vo.setLeasePriceMin(complexPyeongDetail.getArticleStatistics().getLeasePriceMin());
				} else {
					vo.setSpacePrice("0");
					vo.setDealPriceMin("0");
					vo.setLeasePriceMin("0");
				}
				// 매매 실거래가 정보
				if (realDealDTO.getRealPriceOnMonthList().size() > 0) {
					vo.setRealDealPrice(realDealDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getFormattedPrice());
					vo.setRealDealYear(realDealDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getTradeYear());
					vo.setRealDealMonth(realDealDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getTradeMonth());
					vo.setRealDealFloor(realDealDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getFloor());
				} else {
					vo.setRealDealPrice("0");
					vo.setRealDealYear("0");
					vo.setRealDealMonth("0");
					vo.setRealDealFloor("0");
				}
				// 전세 실거래가 정보
				if (realLeaseDTO.getRealPriceOnMonthList().size() > 0) {
					vo.setRealLeasePrice(realLeaseDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getFormattedPrice());
					vo.setRealLeaseYear(realLeaseDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getTradeYear());
					vo.setRealLeaseMonth(realLeaseDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getTradeMonth());
					vo.setRealLeaseFloor(realLeaseDTO.getRealPriceOnMonthList().get(0).getRealPriceList().get(0).getFloor());
				} else {
					vo.setRealLeasePrice("0");
					vo.setRealLeaseYear("0");
					vo.setRealLeaseMonth("0");
					vo.setRealLeaseFloor("0");
				}
				vo.printData();
			}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
