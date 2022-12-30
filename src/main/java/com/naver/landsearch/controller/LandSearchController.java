package com.naver.landsearch.controller;

import com.naver.landsearch.service.LandDataService;
import lombok.RequiredArgsConstructor;
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
public class LandSearchController {
	// Autowired
	public final LandDataService landDataService;

	@GetMapping("/landByCode")
	public void getComplexInfoByCode(@RequestParam("complexCode") String complexCode) {
		landDataService.getLandData(complexCode);
	}
}
