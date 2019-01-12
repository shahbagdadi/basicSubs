package com.netflix.subscription.subscriptionapplication;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PriceTest {

    @BeforeClass
    public static void setup() {
        baseURI = "http://localhost:8087/v1/subscriptions/prices";
    }

    @Test
    public void testPrices() {
        when().
            get("/").
        then().
            statusCode(200);
    }

    @Test
    public void testPriceUS1() {
        when().
            get("/US/products/1").
        then().
            statusCode(200).
            body("productId", equalTo(1)).
            body("countryCode", equalTo("US")).
            body("status", equalTo("active"));
    }

    @Test
    public void testPriceUS2() {
        when().
            get("/US/products/2").
        then().
            statusCode(200).
            body("productId", equalTo(2)).
            body("countryCode", equalTo("US")).
            body("status", equalTo("active"));
    }


    @Test
    public void testPriceGB1() {
        when().
            get("/GB/products/1").
        then().
            statusCode(200).
            body("productId", equalTo(1)).
            body("countryCode", equalTo("GB")).
            body("status", equalTo("active"));
    }
}
  
