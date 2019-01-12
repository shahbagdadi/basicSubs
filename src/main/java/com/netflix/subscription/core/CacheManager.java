package com.netflix.subscription.core;


import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import com.netflix.subscription.repository.PriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.netflix.subscription.entity.Price;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class CacheManager {

	ConcurrentHashMap<String,Price> map;

	@Autowired
    private PriceRepository priceRepository;

    public CacheManager()
    {
    	log.info("Initializing CacheManager");
    	map = new ConcurrentHashMap<String,Price>();
    	//loadAllActivePrices();
    }

    private static final Logger log = LoggerFactory.getLogger(CacheManager.class);

	@PostConstruct
    public void loadAllActivePrices() {
        log.info("Loading All active prices");
        List<Price> prices = priceRepository.findAllByStatus("active");
        for(Price p : prices)
        	map.put( p.getCountryCode() + "-" + p.getProductId(), p);
        
        log.info("Initialized cache with " + prices.size() + " prices from database.");
    }

    public Price getPrice(String countryCode, long productId)
    {
    	log.info(countryCode + "-" + productId);
    	return map.get(countryCode + "-" + productId);
    }
}