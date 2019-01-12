package com.netflix.subscription.subscriptionapplication;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import io.restassured.http.*;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.netflix.subscription.entity.Price;
import java.util.*;

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
    public void testPriceUS3() {
        when().
            get("/US/products/3").
        then().
            statusCode(200).
            body("productId", equalTo(3)).
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

    @Test
    public void testPriceInvalidProduct() {
        when().
            get("/GB/products/101").
        then().
            statusCode(404);
    }

    @Test
    public void testPriceInvalidCountry() {
        when().
            get("/ZZ/products/1").
        then().
            statusCode(404);
    }

    @Test
    public void testPriceChange() {
        Price p = when().get("/US/products/2").as(Price.class);
        p.setAmount(p.getAmount() + 1);
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.add(Calendar.SECOND, 5);
        p.setEffectiveDate(calendar.getTime());
        p.setId(0);
        float amount = p.getAmount();
        System.out.println("New Amout set to = " + p.getAmount());
        given().body (p)
        .when ()
        .contentType (ContentType.JSON)
        .post ("/");

        try {
            System.out.println("Waiting 10 sec for price sync task on server to kick in and update cache ...");
            // sleep 10 seconds and verify if updated
            Thread.sleep(10000);
        } catch (Exception e)
        {
            System.out.println("Interrupted Exception");
        }

        when().
            get("/US/products/2").
        then().
            statusCode(200).
            body("productId", equalTo(2)).
            body("countryCode", equalTo("US")).
            body("status", equalTo("active")).
            body("amount", equalTo(amount));
    }
}
  
