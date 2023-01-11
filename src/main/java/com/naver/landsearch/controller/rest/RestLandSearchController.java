package com.naver.landsearch.controller.rest;

import com.naver.landsearch.dto.vo.LandViewDataVO;
import com.naver.landsearch.service.LandDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<LandViewDataVO> saveComplexInfoByCode(@RequestParam("complexCode") String complexCode) {
		// TODO : DB에 complexCode로 조회를 하여 존재 여부 먼저 확인
		// TODO : landDataService.getLandData(complexCode)
		// TODO : 존재한다면 update를 해야하는가 select를 해야 하는가?
		// TODO : 이후 DB에 해당 단지 데이터가 없으면 saveLandData 호출
		LandViewDataVO landViewDataVO = landDataService.saveLandData(complexCode);
		if (landViewDataVO != null) {
			return new ResponseEntity<>(landViewDataVO, HttpStatus.OK);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/getByCode")
	public ResponseEntity<LandViewDataVO> getComplexInfoByCode(@RequestParam("complexCode") String complexCode) {
		LandViewDataVO landData = landDataService.getLandData(complexCode);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
