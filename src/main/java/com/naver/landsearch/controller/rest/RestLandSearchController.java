package com.naver.landsearch.controller.rest;

import com.naver.landsearch.domain.vo.ArticleVO;
import com.naver.landsearch.domain.vo.ComplexVO;
import com.naver.landsearch.domain.vo.PriceComplexVO;
import com.naver.landsearch.domain.vo.RecommendVO;
import com.naver.landsearch.dto.SearchDTO;
import com.naver.landsearch.service.LandDataService;
import com.naver.landsearch.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PackageName 	: com.naver.landsearch.controller
 * FileName 	: testController
 * Author 		: jhchoi
 * Date 		: 2022-12-28
 * Description 	:
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2022-12-28			jhchoi				최초 생성
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestLandSearchController {
	// Autowired
	public final LandDataService landDataService;
	public final TelegramService telegramService;

	@GetMapping("/codeSync")
	public ResponseEntity<Map<String, String>> registerByCsv() {
		String resultMsg = landDataService.codeSyncByCsv();

		Map<String, String> result = new HashMap<>();
		result.put("resultMsg", resultMsg);

		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/landByCode")
	public ResponseEntity<Map<String, Object>> saveComplexInfoByCode(@RequestParam("complexCode") String complexCode) {
		// ComplexCode를 통한 신규 네이버 부동산 데이터 Insert/Update
		Map<String, Object> result = new HashMap<>();
		ComplexVO complexVO = landDataService.saveLandData(complexCode);
		result.put("complexVO", complexVO);

		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/allData")
	public ResponseEntity<Map<String, Object>> allSelectComplexInfo() {
		// 등록된 전체 Complex 목록 조회
		landDataService.clearRecommendList();
		List<ComplexVO> complexVOList = landDataService.selectAllLandDataVO();
		
		Map<String, Object> result = new HashMap<>();
		result.put("complexs", complexVOList);
		if (complexVOList != null) {
			return ResponseEntity.ok().body(result);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/selectByAddress")
	public ResponseEntity<Map<String, Object>> allSelectComplexInfoByAddress(@RequestParam("address1") String address1,
		@RequestParam("address2") String address2, @RequestParam("address3") String address3, @RequestParam("address4") String address4) {
		// 지역별 Complex 목록 조회(시/도 단위)
		landDataService.clearRecommendList();
		List<ComplexVO> complexVOList = landDataService.selectAllLandDataByAddress(address1, address2, address3, address4);

		Map<String, Object> result = new HashMap<>();
		result.put("complexs", complexVOList);
		if (complexVOList != null) {
			return ResponseEntity.ok().body(result);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/selectByDealPricePyeongRange")
	public ResponseEntity<Map<String, Object>> selectByDealPricePyeongRange(SearchDTO searchDTO) {
		landDataService.clearRecommendList();
		List<PriceComplexVO> priceComplexVOList = landDataService.selectByDealPricePyeongRange(searchDTO);
		Map<String, Object> result = new HashMap<>();
		result.put("complexs", priceComplexVOList);

		if (result != null) {
			return ResponseEntity.ok().body(result);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/selectByRealDealPricePyeongRange")
	public ResponseEntity<Map<String, Object>> selectByRealDealPricePyeongRange(SearchDTO searchDTO) {
		landDataService.clearRecommendList();
		List<PriceComplexVO> priceComplexVOList = landDataService.selectByRealDealPricePyeongRange(searchDTO);
		Map<String, Object> result = new HashMap<>();
		result.put("complexs", priceComplexVOList);

		if (result != null) {
			return ResponseEntity.ok().body(result);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/selectByDealPricePerSpace")
	public ResponseEntity<Map<String, Object>> selectByDealPricePerSpace(SearchDTO searchDTO) {
		// 지역별 호가 매매 평당가 기준으로 정렬하여 조회
		landDataService.clearRecommendList();
		List<PriceComplexVO> priceComplexVOList = landDataService.selectByDealPricePerSpace(searchDTO);
		Map<String, Object> result = new HashMap<>();
		result.put("complexs", priceComplexVOList);

		if (result != null) {
			return ResponseEntity.ok().body(result);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/selectDealGapPrice")
	public ResponseEntity<Map<String, Object>> selectDealGapPrice(SearchDTO searchDTO) {
		// 지역별 호가 갭을 기준으로 정렬하여 조회
		landDataService.clearRecommendList();
		List<PriceComplexVO> priceComplexVOList = landDataService.selectDealGapPrice(searchDTO);
		Map<String, Object> result = new HashMap<>();
		result.put("complexs", priceComplexVOList);

		if (result != null) {
			return ResponseEntity.ok().body(result);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/selectRealDealGapPrice")
	public ResponseEntity<Map<String, Object>> selectRealDealGapPrice(SearchDTO searchDTO) {
		// 지역별 실거래 갑을 기준으로 정렬하여 조회
		landDataService.clearRecommendList();
		List<PriceComplexVO> priceComplexVOList = landDataService.selectRealDealGapPrice(searchDTO);
		Map<String, Object> result = new HashMap<>();
		result.put("complexs", priceComplexVOList);

		if (result != null) {
			return ResponseEntity.ok().body(result);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/selectByPriceRange")
	public ResponseEntity<Map<String, Object>> selectByPriceRange(SearchDTO searchDTO) {
		// 가격 타입별 금액대로 조회 (호가/실거래가)
		landDataService.clearRecommendList();
		List<PriceComplexVO> priceComplexVOList = landDataService.selectPriceRange(searchDTO);
		Map<String, Object> result = new HashMap<>();
		result.put("complexs", priceComplexVOList);

		if (result != null) {
			return ResponseEntity.ok().body(result);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/selectByRealDealPrice")
	public ResponseEntity<Map<String, Object>> selectByRealDealPrice(SearchDTO searchDTO) {
		landDataService.clearRecommendList();
		List<PriceComplexVO> priceComplexVOList = landDataService.selectByRealDealPrice(searchDTO);
		Map<String, Object> result = new HashMap<>();
		result.put("complexs", priceComplexVOList);

		if (result != null) {
			return ResponseEntity.ok().body(result);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/getByPrice")
	public ResponseEntity<Map<String, Object>> selectByPrice(@RequestParam("dealPrice") String dealPrice) {
		landDataService.clearRecommendList();
		List<PriceComplexVO> priceComplexVOList = landDataService.selectAllLandDataByPrice(dealPrice);
		Map<String, Object> result = new HashMap<>();
		result.put("complexs", priceComplexVOList);
		if (result != null) {
			return ResponseEntity.ok().body(result);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}


	@GetMapping("/getByCode")
	public ResponseEntity getComplexInfoByCode(@RequestParam("complexCode") String complexCode) {
		List<ArticleVO> articles = landDataService.getLandData(complexCode);

		Map<String, Object> result = new HashMap<>();
		result.put("complexName", articles.get(0).getComplexName());
		result.put("url", articles.get(0).getLandDataUrl());
		result.put("articles", articles);
		if (articles != null) {
			return ResponseEntity.ok().body(result);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/allUpdate")
	public ResponseEntity<Boolean> allUpdateComplexInfo() {
		// 전체 단지 목록 업데이트
		List<String> complexCodeList = landDataService.selectAllComplexCode();
		System.out.println("=============== 업데이트 시작 : " + System.currentTimeMillis() + "===============");
		for (int i = 0; i < complexCodeList.size(); i++) {
			System.out.println("=============== " + (complexCodeList.size() + 1) + " 중 " + (i + 1) + " 번째 단지 업데이트 시작 ===============");
			landDataService.saveLandData(complexCodeList.get(i));
		}
		System.out.println("=============== 업데이트 완료 : " + System.currentTimeMillis() + "===============");
		return ResponseEntity.ok().body(Boolean.TRUE);
	}

	@PostMapping("/updateByAddress")
	public ResponseEntity<Boolean> updateByAddress(SearchDTO searchDTO) {
		List<String> complexCodeList = landDataService.selectAllComplexCodeByAddress(searchDTO);
		System.out.println("=============== 업데이트 시작 : " + LocalDateTime.now() + "===============");
		for (int i = 0; i < complexCodeList.size(); i++) {
			System.out.println("=============== " + (complexCodeList.size() + 1) + " 중 " + (i + 1) + " 번째 단지 업데이트 시작 ===============");
			landDataService.saveLandData(complexCodeList.get(i));
		}
		System.out.println("=============== 업데이트 완료 : " + LocalDateTime.now() + "===============");
		return ResponseEntity.ok().body(Boolean.TRUE);
	}

	@GetMapping("/recommend")
	public ResponseEntity<Map<String, Object>> recommendComplexList(SearchDTO searchDTO) {
		List<RecommendVO> recommendList = landDataService.getRecommendList();
		Map<String, Object> result = new HashMap<>();
		result.put("recommendList", recommendList);
		if (recommendList != null) {
			telegramService.funcTelegram(searchDTO, recommendList);
			return ResponseEntity.ok().body(result);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/getAddress")
	public ResponseEntity getAddress(SearchDTO searchDTO) {
		// 주소 단계별 조회 후 리스트 리턴
		Map<String, Object> addressMap = landDataService.getAddress(searchDTO);
		if (addressMap.size() > 0) {
			return ResponseEntity.ok().body(addressMap);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
