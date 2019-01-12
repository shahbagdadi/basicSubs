package com.netflix.subscription.subscriptionapplication;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProductTest {

	@BeforeClass
    public static void setup() {
        baseURI = "http://localhost:8087/v1/subscriptions/products";
    }

    @Test
    public void testProducts() {
        when().
            get("/").
        then().
            statusCode(200);
    }

    @Test
    public void testProduct() {
        when().
            get("/1").
        then().
            statusCode(200).
            body("id", equalTo(1));
    }

}
  
