package com.tovelop.maphant.service

import com.tovelop.maphant.dto.BannerDTO
import com.tovelop.maphant.dto.GetBannerDTO
import com.tovelop.maphant.mapper.BannerMapper
import okhttp3.internal.format
import okhttp3.internal.wait
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.Banner
import org.springframework.boot.env.RandomValuePropertySource
import org.springframework.data.annotation.Id
import org.springframework.stereotype.Service
import org.w3c.dom.ranges.Range
import kotlin.random.Random

@Service
class BannerService(@Autowired val bannerMapper: BannerMapper) {
    fun insertBanner(bannerDTO: BannerDTO) {
        bannerMapper.insertBanner(bannerDTO)
    }
    fun deleteBanner(bannerId: Int) {
        bannerMapper.deleteBanner(bannerId)
    }
    fun findBannerByBannerId(bannerId: Int): BannerDTO {
        return bannerMapper.findBannerByBannerId(bannerId)
    }
    fun findBannerByCompany(company: String):List<BannerDTO> {
        return bannerMapper.findBannerByCompany(company)
    }
    fun updateTitleByBannerId(bannerId: Int, title: String) {
        bannerMapper.updateTitleByBannerId(bannerId, title)
    }
    fun updateImagesUrlByBannerId(bannerId: Int, imagesUrl: String) {
        bannerMapper.updateImagesUrlByBannerId(bannerId, imagesUrl)
    }
    fun updateUrlByBannerId(bannerId: Int, url: String) {
        bannerMapper.updateUrlByBannerId(bannerId, url)
    }
    fun findPercentageByBannerId(bannerId: Int): Double {
        return bannerMapper.findPayByBannerId(bannerId) / bannerMapper.sumAllPay().toDouble()
    }
    fun getBannerByBannerId(bannerId: Int): GetBannerDTO {
        val ramdomValue= Random.nextInt(1, bannerMapper.sumAllPay())
        var tmp = bannerMapper.findPayByBannerId(1)
        val payPercentageList= mutableListOf<Int>()
        payPercentageList.add(0)
        payPercentageList.add(tmp)
        for( i in 2 .. bannerMapper.findCountColumnOnBanner() - 1) {
            tmp = bannerMapper.findPayByBannerId(i)
            payPercentageList.add(payPercentageList[i - 1] + tmp)
        }
        for(i in 1 .. bannerMapper.findCountColumnOnBanner() - 1) {
            if(ramdomValue > payPercentageList[i - 1] && ramdomValue <= payPercentageList[i]) {
                return bannerMapper.getBannerByBannerId(i)
            }
        }
        return bannerMapper.getBannerByBannerId(bannerMapper.findBannerIdByMaxPay())
    }
}