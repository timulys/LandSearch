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

	@GetMapping("/landByCode")
	public ResponseEntity<Map<String, Object>> saveComplexInfoByCode(@RequestParam("complexCode") String complexCode) {
		// ComplexCode를 통한 신규 네이버 부동산 데이터 Insert/Update
		landDataService.saveLandData(complexCode);
		// 등록된 전체 Complex 목록 조회
		List<ComplexVO> complexVOList = landDataService.selectAllLandDataVO();

		Map<String, Object> result = new HashMap<>();
		result.put("complexs", complexVOList);
		if (complexVOList != null) {
			return ResponseEntity.ok().body(result);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
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
}
