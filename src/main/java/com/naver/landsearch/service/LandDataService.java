package com.naver.landsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naver.landsearch.domain.complex.ComplexDetail;
import com.naver.landsearch.domain.complex.ComplexPyeongDetail;
import com.naver.landsearch.domain.price.ArticleStatistics;
import com.naver.landsearch.domain.price.ComplexRealPrice;
import com.naver.landsearch.domain.realprice.Price;
import com.naver.landsearch.domain.realprice.RealPrice;
import com.naver.landsearch.domain.vo.*;
import com.naver.landsearch.dto.SearchDTO;
import com.naver.landsearch.dto.LandDataDTO;
import com.naver.landsearch.repository.complex.ComplexDetailRepository;
import com.naver.landsearch.repository.complex.ComplexPyeongDetailRepository;
import com.naver.landsearch.repository.complex.PriceComplexDetailRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

	// File Load
	private final static String FILE_PATH = "D:/landdata_complex_detail.csv";
	private final ResourceLoader resourceLoader;

	// 수도권 추천 매물 List
	private final List<RecommendVO> recommendList = new ArrayList<>();

	// Dependency injection repositories
	private final ComplexDetailRepository complexDetailRepository;
	private final ComplexPyeongDetailRepository complexPyeongDetailRepository;

	private final PriceComplexDetailRepository priceComplexDetailRepository;

	// Service methods
	public List<String> selectAllComplexCode() {
		return complexDetailRepository.findAll().stream().map(ComplexDetail::getComplexNo).collect(Collectors.toList());
	}

	public List<String> selectAllComplexCodeByAddress(SearchDTO searchDTO) {
		if (searchDTO.getAddress4() != null && !searchDTO.getAddress4().isEmpty()) {
			return complexDetailRepository
				.findAllByAddress1AndAddress2AndAddress3AndAddress4OrderByAddressAscAddress2AscAddress3AscAddress4AscComplexNameAsc
					(searchDTO.getAddress1(), searchDTO.getAddress2(), searchDTO.getAddress3(), searchDTO.getAddress4())
				.stream().map(ComplexDetail::getComplexNo).collect(Collectors.toList());
		} else if (searchDTO.getAddress3() != null && !searchDTO.getAddress3().isEmpty()) {
			return complexDetailRepository
				.findAllByAddress1AndAddress2AndAddress3OrderByAddressAscAddress2AscAddress3AscComplexNameAsc
					(searchDTO.getAddress1(), searchDTO.getAddress2(), searchDTO.getAddress3())
				.stream().map(ComplexDetail::getComplexNo).collect(Collectors.toList());
		} else if (searchDTO.getAddress2() != null && !searchDTO.getAddress2().isEmpty()) {
			return complexDetailRepository
				.findAllByAddress1AndAddress2OrderByAddressAscAddress2AscAddress3AscComplexNameAsc
					(searchDTO.getAddress1(), searchDTO.getAddress2())
				.stream().map(ComplexDetail::getComplexNo).collect(Collectors.toList());
		}
		return complexDetailRepository
			.findAllByAddress1OrderByAddressAscAddress2AscAddress3AscComplexNameAsc(searchDTO.getAddress1())
			.stream().map(ComplexDetail::getComplexNo).collect(Collectors.toList());
	}

	public String codeSyncByCsv() {
		// Complex Code Sync by CSV
		String result = "";
		List<String> codeList = new ArrayList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(FILE_PATH));
			int i = 0;
			String code = "";
			while ((code = reader.readLine()) != null) {
				// 해당 code가 존재하면 무시
				boolean exists = complexDetailRepository.existsById(code);
				if (!exists) {
					i++;
					System.out.println(i + " 번째 단지 : " + code);
					saveLandData(code);
					result += code + " - 업데이트" + "</br>";
				} else {
					System.out.println(code + " - 있음");
					result += code + " - 있음" + "</br>";
				}
			}
			System.out.println("코드 입력 완료");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ComplexVO saveLandData(String complexCode) {
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
				// 네이버 부동산 매매 실거래가 저장
				String realDealUrl = LAND_API + complexCode + "/prices/real?complexNo="
					+ complexCode + "&tradeType=A1&year=5&areaNo=" + complexPyeongDetail.getPyeongNo() + "&type=table";
				ComplexRealPrice realDealPrice = realPriceData(realDealUrl);
				complexPyeongDetail.setRealDealPrice(realDealPrice);
				// 네이버 부동산 전세 실거래가 저장
				String realLeaseUrl = LAND_API + complexCode + "/prices/real?complexNo="
					+ complexCode + "&tradeType=B1&year=5&areaNo=" + complexPyeongDetail.getPyeongNo() + "&type=table";
				ComplexRealPrice realLeasePrice = realPriceData(realLeaseUrl);
				complexPyeongDetail.setRealLeasePrice(realLeasePrice);

				// 평형 정보 Save
				complexPyeongDetailRepository.save(complexPyeongDetail);
			}

			return ComplexVO.builder()
					.address(landDataDTO.getComplexDetail().getAddress())
					.complexName(landDataDTO.getComplexDetail().getComplexName())
					.complexNo(landDataDTO.getComplexDetail().getComplexNo())
					.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<ComplexVO> selectAllLandDataVO() {
		// UI 표현 가능하도록 변경
		List<ComplexDetail> complexDetailList = complexDetailRepository.findAll(Sort.by(Sort.Direction.ASC,
			"address", "address2", "address3", "complexName"));
		return selectComplex(complexDetailList);
	}

	public List<ComplexVO> selectAllLandDataByAddress(String address1, String address2, String address3, String address4) {
		List<ComplexDetail> complexDetailList = null;
		if (address4 != null && !address4.isEmpty()) {
			complexDetailList = complexDetailRepository.findAllByAddress1AndAddress2AndAddress3AndAddress4OrderByAddressAscAddress2AscAddress3AscAddress4AscComplexNameAsc(address1, address2, address3, address4);
		} else if (address3 != null && !address3.isEmpty()) {
			complexDetailList = complexDetailRepository.findAllByAddress1AndAddress2AndAddress3OrderByAddressAscAddress2AscAddress3AscComplexNameAsc(address1, address2, address3);
		} else if (address2 != null && !address2.isEmpty()) {
			complexDetailList = complexDetailRepository.findAllByAddress1AndAddress2OrderByAddressAscAddress2AscAddress3AscComplexNameAsc(address1, address2);
		} else {
			complexDetailList = complexDetailRepository.findAllByAddress1OrderByAddressAscAddress2AscAddress3AscComplexNameAsc(address1);
		}
		return selectComplex(complexDetailList);
	}

	// 금액대별 목록 조회
	public List<PriceComplexVO> selectAllLandDataByPrice(String dealPrice) {
		List<PriceComplexVO> complexDetailList = priceComplexDetailRepository.findByDealPrice(dealPrice + "억%");
		Map<String, PriceComplexVO> distinctMap = getPriceComplexList(complexDetailList);
		return new LinkedList<>(distinctMap.values());
	}

	// 평형대별 호가 금액 순 목록 조회
	public List<PriceComplexVO> selectByDealPricePyeongRange(SearchDTO searchDTO) {
		List<PriceComplexVO> priceComplexVOList = priceComplexDetailRepository.findDealPricePerSpaceByAddressAndPyeong(searchDTO);
		Map<String, PriceComplexVO> distinctMap = getPriceComplexList(priceComplexVOList);
		List<PriceComplexVO> complexDetailList = new ArrayList<>(distinctMap.values()).stream()
			.sorted(Comparator.comparing(PriceComplexVO::getDealPricePerSpaceMinToInt).reversed())
			.collect(Collectors.toList());
		return complexDetailList;
	}

	// 평형대별 실거래가 평당가 순 목록 조회
	public List<PriceComplexVO> selectByRealDealPricePyeongRange(SearchDTO searchDTO) {
		List<PriceComplexVO> priceComplexVOList = priceComplexDetailRepository.findRealDealPricePerSpaceByAddressAndPyeong(searchDTO);
		Map<String, PriceComplexVO> distinctMap = getPriceComplexList(priceComplexVOList);
		List<PriceComplexVO> complexDetailList = new ArrayList<>(distinctMap.values()).stream()
			.sorted(Comparator.comparing(PriceComplexVO::getRealDealPricePerSpaceToInt).reversed())
			.collect(Collectors.toList());
		return complexDetailList;
	}

	// 지역 주소별 호가 매매 평당가 내림차순 목록 조회
	public List<PriceComplexVO> selectByDealPricePerSpace(SearchDTO searchDTO) {
		List<PriceComplexVO> priceComplexVOList = priceComplexDetailRepository.findDealPricePerSpaceByAddress(searchDTO);
		Map<String, PriceComplexVO> distinctMap = getPriceComplexList(priceComplexVOList);
		List<PriceComplexVO> complexDetailList = new ArrayList<>(distinctMap.values()).stream()
			.sorted(Comparator.comparing(PriceComplexVO::getDealPricePerSpaceMinToInt).reversed())
			.collect(Collectors.toList());
		return complexDetailList;
	}

	// 지역 주소별 호가 갭 오름차순 목록 조회
	public List<PriceComplexVO> selectDealGapPrice(SearchDTO searchDTO) {
		List<PriceComplexVO> priceComplexVOList = new ArrayList<>();
		if (searchDTO.getExPyeong() == "")
			priceComplexVOList = priceComplexDetailRepository.findDealPricePerSpaceByAddress(searchDTO);
		else
			priceComplexVOList = priceComplexDetailRepository.findDealPricePerSpaceByAddressAndPyeong(searchDTO);

		Map<String, PriceComplexVO> distinctMap = getPriceComplexList(priceComplexVOList);
		List<PriceComplexVO> complexDetailList = new ArrayList<>(distinctMap.values()).stream()
			.filter(item -> item.getDealPriceMinToInt() != 0 && item.getLeasePriceMin() != null)
			.filter(item -> item.getGapPrice() <= searchDTO.getSearchGap())
			.sorted(Comparator.comparing(PriceComplexVO::getGapPrice))
			.collect(Collectors.toList());
		return complexDetailList;
	}

	// 지역 주소별 실거래 갭 오름차순 목록 조회
	public List<PriceComplexVO> selectRealDealGapPrice(SearchDTO searchDTO) {
		List<PriceComplexVO> priceComplexVOList = new ArrayList<>();
		if (searchDTO.getExPyeong() == "")
			priceComplexVOList = priceComplexDetailRepository.findDealPricePerSpaceByAddress(searchDTO);
		else
			priceComplexVOList = priceComplexDetailRepository.findDealPricePerSpaceByAddressAndPyeong(searchDTO);

		Map<String, PriceComplexVO> distinctMap = getPriceComplexList(priceComplexVOList);
		List<PriceComplexVO> complexDetailList = new ArrayList<>(distinctMap.values()).stream()
			.filter(item -> item.getRealDealPriceToInt() != 0 && item.getRealLeasePriceToInt() != 0)
			.filter(item -> item.getRealGapPrice() <= searchDTO.getSearchGap())
			.sorted(Comparator.comparing(PriceComplexVO::getRealGapPrice))
			.collect(Collectors.toList());
		return complexDetailList;
	}

	// 지역 주소별 실거래 평당가 내림차순 목록 조회
	public List<PriceComplexVO> selectByRealDealPrice(SearchDTO searchDTO) {
		List<PriceComplexVO> priceComplexVOList = priceComplexDetailRepository.findRealDealPriceMinByAddress(searchDTO);
		Map<String, PriceComplexVO> distinctMap = getPriceComplexList(priceComplexVOList);
		List<PriceComplexVO> complexDetailList = new ArrayList<>(distinctMap.values()).stream()
			.sorted(Comparator.comparing(PriceComplexVO::getRealDealPricePerSpaceToInt).reversed())
			.collect(Collectors.toList());
		return complexDetailList;
	}

	// 가격 타입별 금액대 조회
	public List<PriceComplexVO> selectPriceRange(SearchDTO searchDTO) {
		List<PriceComplexVO> priceComplexVOList = priceComplexDetailRepository.findAllByPriceRange(searchDTO);
		Map<String, PriceComplexVO> distinctMap = getPriceComplexList(priceComplexVOList);
		List<PriceComplexVO> complexDetailList = new ArrayList<>(distinctMap.values()).stream()
			.sorted(Comparator.comparing(PriceComplexVO::getDealPriceMin).reversed())
			.collect(Collectors.toList());
		return complexDetailList;
	}

	public List<ArticleVO> getLandData(String complexCode) {
		List<ArticleVO> complexArticleList = new ArrayList<>();
		// 데이터 조회
		ComplexDetail complexDetail = complexDetailRepository.findById(complexCode).orElseThrow(NullPointerException::new);
		for (ComplexPyeongDetail complexPyeongDetail : complexDetail.getComplexPyeongDetailList()) {
			if (complexPyeongDetail.getArticleStatistics() != null) {
				ArticleStatistics articleStatistics = complexPyeongDetail.getArticleStatistics();
				// TODO : 데이터 변환이 있으면 표시를 해야 함. (최근 실거래가? 호가변경? 매물건수?)
				complexArticleList.add(ArticleVO.builder()
						.complexNo(complexDetail.getComplexNo())
						.complexName(complexDetail.getComplexName())
						.landDataUrl(complexDetail.getLandDataUrl())
						.pyeongName(complexPyeongDetail.getPyeongName())
						.pyeongName2(complexPyeongDetail.getPyeongName2())
						.dealCount(articleStatistics.getDealCount())
						.leaseCount(articleStatistics.getLeaseCount())
						.rentCount(articleStatistics.getRentCount())
						.dealPriceMin(articleStatistics.getDealPriceMin() != null ?
							articleStatistics.getDealPriceMin() : "0")
						.dealPricePerSpaceMin(articleStatistics.getDealPricePerSpaceMin() != null ?
							articleStatistics.getDealPricePerSpaceMin() : "0")
						.dealPriceMax(articleStatistics.getDealPriceMax() != null ?
							articleStatistics.getDealPriceMax() : "0")
						.dealPricePerSpaceMax(articleStatistics.getDealPricePerSpaceMax() != null ?
							articleStatistics.getDealPricePerSpaceMax() : "0")
						.leasePriceMin(articleStatistics.getLeasePriceMin() != null ?
							articleStatistics.getLeasePriceMin() : "0")
						.leasePricePerSpaceMin(articleStatistics.getLeasePricePerSpaceMin() != null ?
							articleStatistics.getLeasePricePerSpaceMin() : "0")
						.leasePriceMax(articleStatistics.getLeasePriceMax() != null ?
							articleStatistics.getLeasePriceMax() : "0")
						.leasePricePerSpaceMax(articleStatistics.getLeasePricePerSpaceMax() != null ?
							articleStatistics.getLeasePricePerSpaceMax() : "0")
						.leasePriceRateMin(articleStatistics.getLeasePriceRateMin() != null ?
							articleStatistics.getLeasePriceRateMin() : "0")
						.leasePriceRateMax(articleStatistics.getLeasePriceRateMax() != null ?
							articleStatistics.getLeasePriceRateMax() : "0")
						.realDealPrice(complexPyeongDetail.getRealDealPrice().getFormattedPrice() != null ?
							complexPyeongDetail.getRealDealPrice().getFormattedPrice() : "0")
						.realDealDate(
							complexPyeongDetail.getRealDealPrice().getTradeYear() != null ?
								complexPyeongDetail.getRealDealPrice().getTradeYear() + "/" +
								complexPyeongDetail.getRealDealPrice().getTradeMonth() + "/" +
								complexPyeongDetail.getRealDealPrice().getTradeDate() : "0/0/0"
						)
						.realLeasePrice(complexPyeongDetail.getRealLeasePrice().getFormattedPrice() != null ?
							complexPyeongDetail.getRealLeasePrice().getFormattedPrice() : "0")
						.realLeaseDate(
							complexPyeongDetail.getRealLeasePrice().getTradeYear() != null ?
								complexPyeongDetail.getRealLeasePrice().getTradeYear() + "/" +
								complexPyeongDetail.getRealLeasePrice().getTradeMonth() + "/" +
								complexPyeongDetail.getRealLeasePrice().getTradeDate() : "0/0/0"
						)
						.createdAt(articleStatistics.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
						.build()
				);
			}
		}
		return complexArticleList;
	}

	public void clearRecommendList() {
		this.recommendList.clear();
	}

	public List<RecommendVO> getRecommendList() {
		return this.recommendList;
	}

	public Map<String, Object> getAddress(SearchDTO searchDTO) {
		Map<String, Object> result = new HashMap<>();
		List<String> address2 = complexDetailRepository.findAllByAddress2(searchDTO.getAddress1());
		result.put("address2", address2);
		if (searchDTO.getAddress2() != null) {
			List<String> address3 = complexDetailRepository.findAllByAddress3(searchDTO.getAddress1(),
				searchDTO.getAddress2());
			result.put("address3", address3);
		}
		if (searchDTO.getAddress3() != null) {
			List<String> address4 = complexDetailRepository.findAllByAddress4(searchDTO.getAddress1(),
				searchDTO.getAddress2(), searchDTO.getAddress3());
			result.put("address4", address4);
		}
		return result;
	}

	////////////////////// private methods //////////////////////
	// Complex Data Migration Method
	private List<ComplexVO> selectComplex(List<ComplexDetail> complexDetailList) {
		List<ComplexVO> resultList = new ArrayList<>();
		complexDetailList.forEach(complex -> {
			int pyeongTypes = complex.getPyoengNames().split(",").length;
			List<ComplexPyeongVO> pyeongList = new ArrayList<>();
			if (complex.getComplexPyeongDetailList() != null) {
				complex.getComplexPyeongDetailList().forEach(pyeong -> {
					String dealPriceMin = "0";
					if (pyeong.getArticleStatistics() != null && pyeong.getArticleStatistics().getDealPriceMin() != null) {
						dealPriceMin = pyeong.getArticleStatistics().getDealPriceMin();
						if (dealPriceMin.contains(" ")) {
							if (dealPriceMin.split("억").length > 1 && dealPriceMin.replaceAll("억 ", "").length() < 5) {
								dealPriceMin = dealPriceMin.replaceAll("억 ", "0").replace(",", "");
							} else {
								dealPriceMin = dealPriceMin.replaceAll("억 ", "").replace(",", "");
							}
						} else {
							if (dealPriceMin.contains(",") && dealPriceMin.contains("억"))
								dealPriceMin = dealPriceMin.replace(",", "").replace("억", "0000");
							else if (dealPriceMin.contains(","))
								dealPriceMin = dealPriceMin.replaceAll(",", "");
							else
								dealPriceMin = dealPriceMin.replaceAll("억", "0000");
						}
					} else {
						dealPriceMin = "0";
					}

					String leasePriceMin = "0";
					if (pyeong.getArticleStatistics() != null && pyeong.getArticleStatistics().getLeasePriceMin() != null) {
						leasePriceMin = pyeong.getArticleStatistics().getLeasePriceMin();
						if (leasePriceMin.contains(" ")) {
							if (leasePriceMin.split("억").length > 1 && leasePriceMin.replaceAll("억 ", "").length() < 5) {
								leasePriceMin = leasePriceMin.replaceAll("억 ", "0").replace(",", "");
							} else {
								leasePriceMin = leasePriceMin.replaceAll("억 ", "").replace(",", "");
							}
						} else {
							if (leasePriceMin.contains(",") && leasePriceMin.contains("억"))
								leasePriceMin = leasePriceMin.replace(",", "").replace("억", "0000");
							else if (leasePriceMin.contains(","))
								leasePriceMin = leasePriceMin.replaceAll(",", "");
							else
								leasePriceMin = leasePriceMin.replaceAll("억", "0000");
						}
					} else {
						leasePriceMin = "0";
					}

					String leasePriceRate = "0";
					Integer gapPrice = 0;
					if (!"0".equals(dealPriceMin)) {
						leasePriceRate = String.format("%.2f", (Double.valueOf(leasePriceMin) / Double.valueOf(dealPriceMin)) * 100);
						gapPrice = Integer.parseInt(dealPriceMin) - Integer.parseInt(leasePriceMin);
					}

					ComplexPyeongVO complexPyeongVO = ComplexPyeongVO.builder()
						.pyeongName(pyeong.getPyeongName())
						.pyeongName2(pyeong.getPyeongName2())
						.dealPriceMin(pyeong.getArticleStatistics() != null && pyeong.getArticleStatistics().getDealPriceMin() != null ? pyeong.getArticleStatistics().getDealPriceMin() : "0")
						.dealPricePerSpaceMin(pyeong.getArticleStatistics() != null && pyeong.getArticleStatistics().getDealPricePerSpaceMin() != null ? pyeong.getArticleStatistics().getDealPricePerSpaceMin() : "0")
						.leasePriceMin(pyeong.getArticleStatistics() != null && pyeong.getArticleStatistics().getLeasePriceMin() != null ? pyeong.getArticleStatistics().getLeasePriceMin() : "0")
						.leasePricePerSpaceMin(pyeong.getArticleStatistics() != null && pyeong.getArticleStatistics().getLeasePricePerSpaceMin() != null ? pyeong.getArticleStatistics().getLeasePricePerSpaceMin() : "0")
						.leasePriceRateMin(leasePriceRate)
						.gapPrice(gapPrice)
						.landPriceMaxByPtp(pyeong.getLandPriceMaxByPtp() != null && pyeong.getLandPriceMaxByPtp().getMaxPrice() != null ? pyeong.getLandPriceMaxByPtp().getMaxPrice() : "0")
						.entranceType(pyeong.getEntranceType())
						.build();

					pyeongList.add(complexPyeongVO);
				});
			}

			List<ComplexPyeongVO> splitPyeongList = new ArrayList<>();
			if (pyeongList.size() > pyeongTypes)
				splitPyeongList = pyeongList.subList(pyeongList.size() - pyeongTypes, pyeongList.size());
			else
				splitPyeongList = pyeongList;

			// 매물 추천 기능 : 갭이 일정 수준 이하(5000만원)인 매물
			for (ComplexPyeongVO pyeong : splitPyeongList) {
				if (pyeong.getGapPrice() != 0 && pyeong.getGapPrice() <= 5000) {
					RecommendVO recommend = RecommendVO.builder()
						.complexNo(complex.getComplexNo())
						.complexName(complex.getComplexName())
						.url(complex.getLandDataUrl())
						.address(complex.getAddress())
						.complexPyeongVo(pyeong)
						.value(1)
						.build();
					if (pyeong.getGapPrice() != 0 && pyeong.getGapPrice() <= 3000) {
						recommend.setValue(2);
					}
					recommendList.add(recommend);
				}
			}

			resultList.add(ComplexVO.builder()
				.complexNo(complex.getComplexNo())
				.complexName(complex.getComplexName())
				.address(complex.getAddress())
				.landDataUrl(complex.getLandDataUrl())
				.updateAt(complex.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.complexPyeongVOList(splitPyeongList)
				.useApproveYmd(complex.getUseApproveYmd() != null ? complex.getUseApproveYmd().substring(0, 4) : "정보없음")
				.build());
		});

		return resultList;
	}

	private ComplexRealPrice realPriceData(String url) {
		try {
			Connection con = Jsoup.connect(url)
					.header("Authorization", AUTH)
					.method(Connection.Method.GET)
					.ignoreContentType(true);
			Document doc = con.get();
			Node body = doc.selectFirst("body").childNode(0);
			RealPrice realPrice = new ObjectMapper().readValue(body.toString(), RealPrice.class);

			if (realPrice.getRealPriceOnMonthList() != null && realPrice.getRealPriceOnMonthList().size() > 0) {
				Price price = realPrice.getRealPriceOnMonthList().get(0).getRealPriceList().get(0);
				return ComplexRealPrice.builder()
						.areaNo(realPrice.getAreaNo())
						.tradeType(price.getTradeType())
						.tradeYear(price.getTradeYear())
						.tradeMonth(price.getTradeMonth())
						.tradeDate(price.getTradeDate())
						.formattedPrice(price.getFormattedPrice() != null ? price.getFormattedPrice() : "없음")
						.floor(price.getFloor())
						.build();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ComplexRealPrice();
	}

	private Map<String, PriceComplexVO> getPriceComplexList(List<PriceComplexVO> complexDetailList) {
		// 중복 제거 및 생성 일자로 오름차순 정렬
		Map<String, PriceComplexVO> distinctMap = new LinkedHashMap<>();
		complexDetailList.forEach(i -> {
			if (distinctMap.get(i.getComplexName() + i.getPyeongName()) != null) {
				PriceComplexVO compareVO = distinctMap.get(i.getComplexName() + i.getPyeongName());
				int compareValue = compareVO.compareTo(i);
				if (compareValue <= 0) {
					setPriceFormatted(i);
					distinctMap.put(i.getComplexName() + i.getPyeongName(), i);
				}
			} else {
				setPriceFormatted(i);
				distinctMap.put(i.getComplexName() + i.getPyeongName(), i);
			}
		});
		return distinctMap;
	}

	private void setPriceFormatted(PriceComplexVO i) {
		String dealPriceMin = "0";
		Integer dealPricePerSpaceMin = 0;
		String leasePriceMin = "0";
		Integer leasePricePerSpaceMin = 0;
		if (i.getDealPriceMin() != null) {
			if (i.getDealPriceMin().contains(" ")) {
				if (i.getDealPriceMin().split("억").length > 1 && i.getDealPriceMin().replaceAll("억 ", "").length() < 5) {
					dealPriceMin = i.getDealPriceMin().replaceAll("억 ", "0").replace(",", "");
				} else {
					dealPriceMin = i.getDealPriceMin().replaceAll("억 ", "").replace(",", "");
				}
			} else {
				if (i.getDealPriceMin().contains(",") && i.getDealPriceMin().contains("억"))
					dealPriceMin = i.getDealPriceMin().replace(",", "").replace("억", "0000");
				else if (i.getDealPriceMin().contains(","))
					dealPriceMin = i.getDealPriceMin().replaceAll(",", "");
				else
					dealPriceMin = i.getDealPriceMin().replaceAll("억", "0000");
			}
			i.setDealPriceMinToInt(Integer.parseInt(dealPriceMin));
		} else {
			i.setDealPriceMin("0");
		}

		if (i.getDealPricePerSpaceMin() != null) {
			if (i.getDealPricePerSpaceMin().contains("억 ") && i.getDealPricePerSpaceMin().contains(",")) {
				dealPricePerSpaceMin = Integer.valueOf(i.getDealPricePerSpaceMin().replaceAll("억 ", "").replace(",", ""));
			} else if (i.getDealPricePerSpaceMin().contains("억 ") && !i.getDealPricePerSpaceMin().contains(",")) {
				if (i.getDealPricePerSpaceMin().replaceAll("억 ", "").length() == 3) {
					dealPricePerSpaceMin = Integer.valueOf(i.getDealPricePerSpaceMin().replaceAll("억 ", "00"));
				} else if (i.getDealPricePerSpaceMin().replaceAll("억 ", "").length() == 2) {
					dealPricePerSpaceMin = Integer.valueOf(i.getDealPricePerSpaceMin().replaceAll("억 ", "000"));
				} else {
					dealPricePerSpaceMin = Integer.valueOf(i.getDealPricePerSpaceMin().replaceAll("억 ", "0"));
				}
			} else if (i.getDealPricePerSpaceMin().contains("억")) {
				dealPricePerSpaceMin = Integer.valueOf(i.getDealPricePerSpaceMin().replaceAll("억", "0000"));
			} else {
				dealPricePerSpaceMin = Integer.valueOf(i.getDealPricePerSpaceMin().replace(",", ""));
			}
			i.setDealPricePerSpaceMinToInt(dealPricePerSpaceMin);
		}

		// 전세
		if (i.getLeasePriceMin() != null) {
			if (i.getLeasePriceMin().contains(" ")) {
				if (i.getLeasePriceMin().split("억").length > 1 && i.getLeasePriceMin().replaceAll("억 ", "").length() < 5) {
					leasePriceMin = i.getLeasePriceMin().replaceAll("억 ", "0").replace(",", "");
				} else {
					leasePriceMin = i.getLeasePriceMin().replaceAll("억 ", "").replace(",", "");
				}
			} else {
				if (i.getLeasePriceMin().contains(",") && i.getLeasePriceMin().contains("억"))
					leasePriceMin = i.getLeasePriceMin().replace(",", "").replace("억", "0000");
				else if (i.getLeasePriceMin().contains(","))
					leasePriceMin = i.getLeasePriceMin().replaceAll(",", "");
				else
					leasePriceMin = i.getLeasePriceMin().replaceAll("억", "0000");
			}
		} else {
			i.setLeasePriceMin("0");
		}
		i.setGapPrice(Integer.parseInt(dealPriceMin) - Integer.parseInt(leasePriceMin));

		// 매매 실거래가
		if(i.getRealDealPrice() != null) {
			if (i.getRealDealPrice().equals("없음")) {
				i.setRealDealPriceToInt(0);
			} else {
				if (i.getRealDealPrice().contains(" ")) {
					if (i.getRealDealPrice().split("억").length > 1 && i.getRealDealPrice().replaceAll("억 ", "").length() < 5) {
						i.setRealDealPriceToInt(Integer.parseInt(i.getRealDealPrice().replaceAll("억 ", "0").replace(",", "")));
					} else {
						i.setRealDealPriceToInt(Integer.parseInt(i.getRealDealPrice().replaceAll("억 ", "").replace(",", "")));
					}
				} else {
					if (i.getRealDealPrice().contains(",") && i.getRealDealPrice().contains("억"))
						i.setRealDealPriceToInt(Integer.parseInt(i.getRealDealPrice().replace(",", "").replace("억", "0000")));
					else if (i.getRealDealPrice().contains(","))
						i.setRealDealPriceToInt(Integer.parseInt(i.getRealDealPrice().replaceAll(",", "")));
					else
						i.setRealDealPriceToInt(Integer.parseInt(i.getRealDealPrice().replaceAll("억", "0000")));
				}
			}
			// 실거래가 평당가 세팅
			i.setRealDealPricePerSpace();
		}
		// 전세 실거래가
		if (i.getRealLeasePrice() != null) {
			if (i.getRealLeasePrice().equals("없음")) {
				i.setRealLeasePriceToInt(0);
			} else {
				if (i.getRealLeasePrice().contains(" ")) {
					if (i.getRealLeasePrice().split("억").length > 1 && i.getRealLeasePrice().replaceAll("억 ", "").length() < 5) {
						i.setRealLeasePriceToInt(Integer.parseInt(i.getRealLeasePrice().replaceAll("억 ", "0").replace(",", "")));
					} else {
						i.setRealLeasePriceToInt(Integer.parseInt(i.getRealLeasePrice().replaceAll("억 ", "").replace(",", "")));
					}
				} else {
					if (i.getRealLeasePrice().contains(",") && i.getRealLeasePrice().contains("억"))
						i.setRealLeasePriceToInt(Integer.parseInt(i.getRealLeasePrice().replace(",", "").replace("억", "0000")));
					else if (i.getRealLeasePrice().contains(","))
						i.setRealLeasePriceToInt(Integer.parseInt(i.getRealLeasePrice().replaceAll(",", "")));
					else
						i.setRealLeasePriceToInt(Integer.parseInt(i.getRealLeasePrice().replaceAll("억", "0000")));
				}
			}
		}
		i.setRealGapPrice(i.getRealDealPriceToInt() - i.getRealLeasePriceToInt());
	}
}
