package com.netflix.subscription.controller;

import com.netflix.subscription.entity.Price;
import com.netflix.subscription.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.netflix.subscription.core.CacheManager;
import java.util.List;

@RestController
@RequestMapping("/v1/subscriptions")
public class PriceController {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private CacheManager cacheManager;


    @PostMapping("/prices")
    public Price create(@RequestBody Price price)
    {
        price.setStatus("scheduled");
        return priceRepository.save(price);
    }


    @GetMapping("/prices")
    public List<Price> findAll(@RequestParam(value = "status", required = false) String status)
    {
        if (status == null || !status.equals("scheduled"))
            status = "active";
        return priceRepository.findAllByStatus(status);
    }


    @GetMapping("/prices/{country_code}/products/{product_id}")
    public Price findCountryProductPrice(@PathVariable("country_code") String countryCode, @PathVariable("product_id") long productId)
    {
        return cacheManager.getPrice(countryCode,productId);
        //return priceRepository.findByCountryProduct(countryCode,productId);
    }


    @PutMapping("/prices/{price_id}")
    public Price update(@PathVariable("price_id") Long priceId, @RequestBody Price priceObject)
    {
        Price price = priceRepository.findOne(priceId);
        price.setProductId(priceObject.getProductId());
        price.setCountryCode(priceObject.getCountryCode());
        price.setCurrencyCode(priceObject.getCurrencyCode());
        price.setAmount(priceObject.getAmount());
        price.setEffectiveDate(priceObject.getEffectiveDate());
        price.setStatus("scheduled");
        return priceRepository.save(price);
    }



    @DeleteMapping("/prices/{price_id}")
    public List<Price> delete(@PathVariable("price_id") Long priceId)
    {
        priceRepository.delete(priceId);
        return priceRepository.findAll();
    }



    @GetMapping("/prices/{price_id}")
    @ResponseBody
    public Price findByPriceId(@PathVariable("price_id") Long priceId)
    {
        return priceRepository.findOne(priceId);
    }
}
