package com.netflix.subscription.repository;

import com.netflix.subscription.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>
{

}
