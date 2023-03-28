package com.naver.landsearch.service;

import com.naver.landsearch.domain.vo.RecommendVO;
import com.naver.landsearch.dto.SearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * PackageName 	: com.naver.landsearch.service
 * FileName 	: TelegramService
 * Author 		: jhchoi
 * Date 		: 2023-03-06
 * Description 	:
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2023-03-06			jhchoi				최초 생성
 */
// TODO : 추후 텔레그램 메신저로 보낼 수 있는 방법을 고려해볼 것
@Service
@RequiredArgsConstructor
public class TelegramService {
	// Telegram Token
	private static final String TOKEN = "6267327734:AAH_9ygjsgbNuyrmbg8BcJaeCB6RMdWRzdo";
	private static final String CHAT_ID = "-1001793716640";

	public void funcTelegram(SearchDTO searchDTO, List<RecommendVO> recommendList) {
		String article = searchDTO.getAddress1() + searchDTO.getAddress2() +
			searchDTO.getAddress3() + searchDTO.getAddress4() + "%0A%0A";
		for (int i = 0; i < recommendList.size(); i++) {
			if (recommendList.get(i).getValue() == 2) {
				article += "★ ";
			}
			// 매물 기본 정보
			article += (i + 1) + ". [" + recommendList.get(i).getComplexName() + "]("
				+ recommendList.get(i).getUrl() + ") ("
				+ recommendList.get(i).getComplexPyeongVo().getPyeongName() + ") "
				+ recommendList.get(i).getComplexPyeongVo().getEntranceType() + " : ";
			// 매물 가격 정보
			article += recommendList.get(i).getComplexPyeongVo().getDealPriceMin() + " / "
				+ recommendList.get(i).getComplexPyeongVo().getLeasePriceMin() + "("
				+ recommendList.get(i).getComplexPyeongVo().getGapPrice() + ") - "
				+ recommendList.get(i).getAddress() + "%0A";
			
			if (i != 0 && i % 50 == 0) { // Sending 실패를 피해 50개씩 짤라서보냄
				sendMessage(article);
				article = "";
			} else if (i == recommendList.size() - 1) {
				sendMessage(article);
				article = "";
			}
		}
	}

	private void sendMessage(String article) {
		BufferedReader in = null;
		try {
			URL obj = new URL("https://api.telegram.org/bot" + TOKEN + "/sendmessage?chat_id=" + CHAT_ID +
				"&text=" + article + "&parse_mode=markdown");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		} catch (Exception e) {
			try {
				URL obj = new URL("https://api.telegram.org/bot" + TOKEN + "/sendmessage?chat_id=" + CHAT_ID +
					"&text=데이터가 너무 많습니다. 해당 지역의 경우 웹사이트를 통해 확인하세요.&parse_mode=markdown");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
