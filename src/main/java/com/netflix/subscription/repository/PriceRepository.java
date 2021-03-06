package com.netflix.subscription.repository;

import com.netflix.subscription.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.*;
import org.springframework.stereotype.Repository;



@Repository
public interface PriceRepository extends JpaRepository<Price, Long>
{

    @Query("SELECT p FROM Price p WHERE p.status = ?1")
    List<Price> findAllByStatus(String status);

    @Query("SELECT p FROM Price p WHERE p.countryCode = ?1 and p.productId = ?2 and status = 'active'")
    Price findByCountryProduct(String countryCode, long productId);

    @Query("SELECT p FROM Price p WHERE p.status = 'scheduled' and p.effectiveDate < ?1")
    List<Price> findScheduledByCountryProduct(Date date);

    @Query("SELECT p FROM Price p WHERE p.status = 'active' and p.effectiveDate > ?1")
    List<Price> findUpdatedByCountryProduct(Date date);
}
