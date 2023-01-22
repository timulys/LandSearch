package com.naver.landsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naver.landsearch.domain.complex.ComplexDetail;
import com.naver.landsearch.domain.complex.ComplexPyeongDetail;
import com.naver.landsearch.domain.price.ArticleStatistics;
import com.naver.landsearch.domain.price.ComplexRealPrice;
import com.naver.landsearch.domain.price.LandPriceMaxByPtp;
import com.naver.landsearch.domain.realprice.Price;
import com.naver.landsearch.domain.realprice.RealPrice;
import com.naver.landsearch.domain.vo.ArticleVO;
import com.naver.landsearch.domain.vo.ComplexPyeongVO;
import com.naver.landsearch.dto.LandDataDTO;
import com.naver.landsearch.domain.vo.ComplexVO;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
	public List<String> selectAllComplexCode() {
		return complexDetailRepository.findAll().stream().map(ComplexDetail::getComplexNo).collect(Collectors.toList());
	}

	public void saveLandData(String complexCode) {
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
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<ComplexVO> selectAllLandDataVO() {
		List<ComplexVO> resultList = new ArrayList<>();
		// UI 표현 가능하도록 변경
		List<ComplexDetail> complexDetailList = complexDetailRepository.findAll(Sort.by(Sort.Direction.ASC, "address", "address2", "address3", "complexName"));
		complexDetailList.forEach(complex -> {
			int pyeongTypes = complex.getPyoengNames().split(",").length;
			List<ComplexPyeongVO> splitPyeongList = new ArrayList<>();
			List<ComplexPyeongVO> pyeongList = new ArrayList<>();
			if (complex.getComplexPyeongDetailList() != null) {
				complex.getComplexPyeongDetailList().forEach(pyeong -> {
					pyeongList.add(ComplexPyeongVO.builder()
						.pyeongName(pyeong.getPyeongName())
						.pyeongName2(pyeong.getPyeongName2())
						.dealPriceMin(pyeong.getArticleStatistics() != null && pyeong.getArticleStatistics().getDealPriceMin() != null
							? pyeong.getArticleStatistics().getDealPriceMin() : "0")
						.dealPricePerSpaceMin(pyeong.getArticleStatistics() != null && pyeong.getArticleStatistics().getDealPricePerSpaceMin() != null ?
							pyeong.getArticleStatistics().getDealPricePerSpaceMin() : "0")
						.leasePriceMin(pyeong.getArticleStatistics() != null && pyeong.getArticleStatistics().getLeasePriceMin() != null ?
							pyeong.getArticleStatistics().getLeasePriceMin() : "0")
						.leasePricePerSpaceMin(pyeong.getArticleStatistics() != null && pyeong.getArticleStatistics().getLeasePricePerSpaceMin() != null ?
							pyeong.getArticleStatistics().getLeasePricePerSpaceMin() : "0")
						.build());
				});
			}

			if (pyeongList.size() > pyeongTypes)
				splitPyeongList = pyeongList.subList(pyeongList.size() - pyeongTypes, pyeongList.size());
			else
				splitPyeongList = pyeongList;

			resultList.add(ComplexVO.builder()
				.complexNo(complex.getComplexNo())
				.complexName(complex.getComplexName())
				.address(complex.getAddress())
				.landDataUrl(complex.getLandDataUrl())
				.updateAt(complex.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.complexPyeongVOList(splitPyeongList)
				.build());
		});

		return resultList;
	}

	public List<ArticleVO> getLandData(String complexCode) {
		// TODO : CreateDate별 조회 가능하도록 ViewVO List로 변환
		List<ArticleVO> complexArticleList = new ArrayList<>();
		// 데이터 조회
		ComplexDetail complexDetail = complexDetailRepository.findById(complexCode).orElseThrow(NullPointerException::new);
		for (ComplexPyeongDetail complexPyeongDetail : complexDetail.getComplexPyeongDetailList()) {
			if (complexPyeongDetail.getArticleStatistics() != null) {
				ArticleStatistics articleStatistics = complexPyeongDetail.getArticleStatistics();
				complexArticleList.add(ArticleVO.builder()
						.complexNo(complexDetail.getComplexNo())
						.complexName(complexDetail.getComplexName())
						.landDataUrl(complexDetail.getLandDataUrl())
						.pyeongName(complexPyeongDetail.getPyeongName())
						.pyeongName2(complexPyeongDetail.getPyeongName2())
						.dealCount(articleStatistics.getDealCount())
						.leaseCount(articleStatistics.getLeaseCount())
						.rentCount(articleStatistics.getRentCount())
						.dealPriceMin(articleStatistics.getDealPriceMin())
						.dealPricePerSpaceMin(articleStatistics.getDealPricePerSpaceMin())
						.dealPriceMax(articleStatistics.getDealPriceMax())
						.dealPricePerSpaceMax(articleStatistics.getDealPricePerSpaceMax())
						.leasePriceMin(articleStatistics.getLeasePriceMin())
						.leasePricePerSpaceMin(articleStatistics.getLeasePricePerSpaceMin())
						.leasePriceMax(articleStatistics.getLeasePriceMax())
						.leasePricePerSpaceMax(articleStatistics.getLeasePricePerSpaceMax())
						.leasePriceRateMin(articleStatistics.getLeasePriceRateMin())
						.leasePriceRateMax(articleStatistics.getLeasePriceRateMax())
								.realDealPrice(complexPyeongDetail.getRealDealPrice().getFormattedPrice())
								.realDealDate(
										complexPyeongDetail.getRealDealPrice().getTradeYear() + "/" +
										complexPyeongDetail.getRealDealPrice().getTradeMonth() + "/" +
										complexPyeongDetail.getRealDealPrice().getTradeDate()
								)
								.realLeasePrice(complexPyeongDetail.getRealLeasePrice().getFormattedPrice())
								.realLeaseDate(
										complexPyeongDetail.getRealLeasePrice().getTradeYear() + "/" +
										complexPyeongDetail.getRealLeasePrice().getTradeMonth() + "/" +
										complexPyeongDetail.getRealLeasePrice().getTradeDate()
								)
						.createdAt(articleStatistics.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
						.build()
				);
			}
		}
		return complexArticleList;
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
