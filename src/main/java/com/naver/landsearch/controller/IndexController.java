package com.naver.landsearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * PackageName 	: com.naver.landsearch.controller
 * FileName 	: IndexController
 * Author 		: jhchoi
 * Date 		: 2023-01-05
 * Description 	:
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2023-01-05			jhchoi				최초 생성
 */
@Controller
public class IndexController {
    @GetMapping(value = "/")
    public String getIndex() {
        return "home";
    }
}
