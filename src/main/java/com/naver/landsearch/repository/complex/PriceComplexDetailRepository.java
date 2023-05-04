package com.naver.landsearch.repository.complex;

import com.naver.landsearch.domain.complex.QComplexDetail;
import com.naver.landsearch.domain.complex.QComplexPyeongDetail;
import com.naver.landsearch.domain.price.QArticleStatistics;
import com.naver.landsearch.domain.price.QComplexRealPrice;
import com.naver.landsearch.domain.vo.PriceComplexVO;
import com.naver.landsearch.dto.SearchDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * PackageName 	: com.naver.landsearch.repository.complex
 * FileName 	: PriceComplexDetailRepository
 * Author 		: jhchoi
 * Date 		: 2023-03-22
 * Description 	:
 * ======================================================
 * DATE				    AUTHOR				NOTICE
 * ======================================================
 * 2023-03-22			jhchoi				최초 생성
 */
@Repository
@RequiredArgsConstructor
public class PriceComplexDetailRepository {
	private final JPAQueryFactory queryFactory;
	private QComplexDetail c = QComplexDetail.complexDetail;
	private QComplexPyeongDetail p =  QComplexPyeongDetail.complexPyeongDetail;
	private QArticleStatistics a = QArticleStatistics.articleStatistics;
	private QComplexRealPrice d = new QComplexRealPrice("realDealPrice");
	private QComplexRealPrice l = new QComplexRealPrice("realLeasePrice");

	public List<PriceComplexVO> findByDealPrice(String dealPrice) {
		List<PriceComplexVO> priceComplexList = queryFactory
			.select(Projections.constructor(PriceComplexVO.class, c.address, c.complexName, c.landDataUrl,
				c.useApproveYmd, p.pyeongName, p.pyeongName2, p.supplyArea, a.dealPriceMin, a.dealPricePerSpaceMin, d.formattedPrice,
				a.leasePriceMin, a.leasePricePerSpaceMin, l.formattedPrice, a.createdAt, p.entranceType))
			.from(c)
			.join(c.complexPyeongDetailList, p)
			.join(p.articleStatistics, a)
			.join(p.realDealPrice, d)
			.leftJoin(p.realLeasePrice, l)
			.where(a.dealPriceMin.like(dealPrice))
			.groupBy(c.complexName, p.pyeongName)
			.having(a.createdAt.max().isNotNull())
			.orderBy(a.dealPriceMin.length().asc(), a.dealPriceMin.asc(), c.complexName.asc()).fetch();

		return priceComplexList;
	}

	public List<PriceComplexVO> findDealPricePerSpaceByAddress(SearchDTO searchDTO) {
		List<PriceComplexVO> priceComplexList = queryFactory
			.select(Projections.constructor(PriceComplexVO.class, c.address, c.complexName, c.landDataUrl, c.useApproveYmd,
				p.pyeongName, p.pyeongName2, p.supplyArea, a.dealPriceMin, a.dealPricePerSpaceMin, d.formattedPrice,
				a.leasePriceMin, a.leasePricePerSpaceMin, l.formattedPrice, a.createdAt, p.entranceType))
			.from(c)
			.join(c.complexPyeongDetailList, p)
			.join(p.articleStatistics, a)
			.join(p.realDealPrice, d)
			.leftJoin(p.realLeasePrice, l)
			.where(this.builderAddress(searchDTO))
			.where(a.dealPricePerSpaceMin.isNotNull())
			.fetch();

		return priceComplexList;
	}

	public List<PriceComplexVO> findDealPricePerSpaceByAddressAndPyeong(SearchDTO searchDTO) {
		List<PriceComplexVO> priceComplexList = queryFactory
			.select(Projections.constructor(PriceComplexVO.class, c.address, c.complexName, c.landDataUrl, c.useApproveYmd,
				p.pyeongName, p.pyeongName2, p.supplyArea, a.dealPriceMin, a.dealPricePerSpaceMin, d.formattedPrice,
				a.leasePriceMin, a.leasePricePerSpaceMin, l.formattedPrice, a.createdAt, p.entranceType))
			.from(c)
			.join(c.complexPyeongDetailList, p)
			.join(p.articleStatistics, a)
			.join(p.realDealPrice, d)
			.leftJoin(p.realLeasePrice, l)
			.where(this.builderAddress(searchDTO))
			.where(p.exclusivePyeong.castToNum(Integer.class)
				.between(Integer.parseInt(searchDTO.getExPyeong()), Integer.parseInt(searchDTO.getExPyeong()) + 9))
			.where(a.dealPricePerSpaceMin.isNotNull())
			.fetch();

		return priceComplexList;
	}

	public List<PriceComplexVO> findRealDealPricePerSpaceByAddressAndPyeong(SearchDTO searchDTO) {
		List<PriceComplexVO> priceComplexList = queryFactory
			.select(Projections.constructor(PriceComplexVO.class, c.address, c.complexName, c.landDataUrl, c.useApproveYmd,
				p.pyeongName, p.pyeongName2, p.supplyArea, a.dealPriceMin, a.dealPricePerSpaceMin, d.formattedPrice,
				a.leasePriceMin, a.leasePricePerSpaceMin, l.formattedPrice, a.createdAt, p.entranceType))
			.from(c)
			.join(c.complexPyeongDetailList, p)
			.join(p.articleStatistics, a)
			.join(p.realDealPrice, d)
			.leftJoin(p.realLeasePrice, l)
			.where(this.builderAddress(searchDTO))
			.where(p.exclusivePyeong.castToNum(Integer.class)
				.between(Integer.parseInt(searchDTO.getExPyeong()), Integer.parseInt(searchDTO.getExPyeong()) + 9))
			.where(d.formattedPrice.isNotNull())
			.orderBy(d.formattedPrice.length().desc(), d.formattedPrice.desc())
			.fetch();

		return priceComplexList;
	}

	public List<PriceComplexVO> findRealDealPriceMinByAddress(SearchDTO searchDTO) {
		List<PriceComplexVO> priceComplexList = queryFactory
			.select(Projections.constructor(PriceComplexVO.class, c.address, c.complexName, c.landDataUrl, c.useApproveYmd,
				p.pyeongName, p.pyeongName2, p.supplyArea, a.dealPriceMin, a.dealPricePerSpaceMin, d.formattedPrice,
				a.leasePriceMin, a.leasePricePerSpaceMin, l.formattedPrice, a.createdAt, p.entranceType))
			.from(c)
			.join(c.complexPyeongDetailList, p)
			.join(p.articleStatistics, a)
			.join(p.realDealPrice, d)
			.leftJoin(p.realLeasePrice, l)
			.where(this.builderAddress(searchDTO))
			.where(d.formattedPrice.isNotNull())
			.orderBy(d.formattedPrice.length().desc(), d.formattedPrice.desc())
			.fetch();

		return priceComplexList;
	}

	private BooleanBuilder builderAddress(SearchDTO searchDTO) {
		BooleanBuilder builder = new BooleanBuilder();

		if (StringUtils.hasText(searchDTO.getAddress1())) {
			builder.and(c.address1.eq(searchDTO.getAddress1()));
		}
		if (StringUtils.hasText(searchDTO.getAddress2())) {
			builder.and(c.address2.eq(searchDTO.getAddress2()));
		}
		if (StringUtils.hasText(searchDTO.getAddress3())) {
			builder.and(c.address3.eq(searchDTO.getAddress3()));
		}
		if (StringUtils.hasText(searchDTO.getAddress4())) {
			builder.and(c.address4.eq(searchDTO.getAddress4()));
		}
		return builder;
	}
}
