package com.netflix.subscription.core;


import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import com.netflix.subscription.repository.PriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.netflix.subscription.entity.Price;
import org.springframework.beans.factory.annotation.*;
import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class CacheManager {

	ConcurrentHashMap<String,Price> map;

	@Autowired
    private PriceRepository priceRepository;

    @Value("${fixedDelay.in.milliseconds}")
	private int delay;

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

    public void activateScheduledPrices()
    {
    	TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
    	List<Price> prices = priceRepository.findScheduledByCountryProduct(calendar.getTime());
        for(Price p : prices)
        {
        	log.info( "Picking up Scheduled price for " + p.getCountryCode() + "-" + p.getProductId(), p);
        	String key = p.getCountryCode() + "-" + p.getProductId();
        	Price old = map.get(key);
        	Price clone = (Price)((Price) old).clone();
        	clone.setStatus("inactive");
        	priceRepository.save(clone);
        	//map.put( key, p);
        	p.setStatus("active");
        	priceRepository.save(p);
        }
    }

    public void refreshUpdatedPrices()
    {
    	TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        // find all prices that were updated since last 2 runs to account for clock lags etc.
        // Being conservative to not miss a pricing update.
        calendar.add(Calendar.MILLISECOND, - (2 * delay));
    	List<Price> prices = priceRepository.findUpdatedByCountryProduct(calendar.getTime());
    	if (prices.size() == 0)
    		log.info( "No pricing updates ");
        for(Price p : prices)
        {
        	String key = p.getCountryCode() + "-" + p.getProductId();
        	log.info( "Refreshing price for " + key);
        	map.put( key, p);
        }
    }


}