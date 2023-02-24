package com.naver.landsearch.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.naver.landsearch.domain.complex.ComplexDetail;
import com.naver.landsearch.domain.vo.ArticleVO;
import com.naver.landsearch.domain.vo.ComplexVO;
import com.naver.landsearch.service.LandDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Paths;
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

	@GetMapping("/codeSync")
	public ResponseEntity registerByCsv() {
		landDataService.codeSyncByCsv();
		return new ResponseEntity(HttpStatus.OK);
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
		List<ComplexVO> complexVOList = landDataService.selectAllLandDataVO();
		
		Map<String, Object> result = new HashMap<>();
		result.put("complexs", complexVOList);
		if (complexVOList != null) {
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
}
