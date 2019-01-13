# Basic Subscription Platform

### Background Information: 	
Netflix offers customers 3 service plans (1S, 2S, & 4S) based on the number of concurrent streams and priced accordingly.As we are a global service, we have defined prices for each service plan for country we support.For each customer we know the service plan (1S, 2S, or 4S), the price for the chosen plan and the country of the customer.  

### Problem Statement:
Netflix rolls out price changes for our service periodically.  
In order to accurately and effective change the prices of 100M+ customers, we need to have a systematic solution for changing prices.  

### Objective:
Design and implement a system that hosts Netflix pricing which will enable us to systematically change prices across all our global customers.  
We want to see how your system supports price changes pushed by country.

### Assumptions
   1. A subscriber can only have 1 subscription active at a time.
   2. Limited number of sku’s/products (sku’s < 100) but large customer base 100M+.
   3. Customers are charged on their monthly billing anniversary. The 100M+ active subscribers are fairly well spread out through out the  month. Except that customers with anniversary on the 31st may be charged on 30th (or 28/29th in case of Feb).  
   4. No Proration when prices change. The new price will be effective at the end of the billing cycle.  
   5. The legal requirements around tracking new pricing/terms acceptance by the customer is out of scope of this system design.  
   6. A product is available for purchase in multiple currencies. Every country  has only 1 currency in which the product can be purchased. The system will provide an ability to change the price for specific country/countries for any active product.  
   7. Price changes can be scheduled in advance with an effective date. The new pricing will take effect on its effective date/time (UTC). Only 1 pricing will be active for a product-country combination at any given time. All time will be managed in UTC by the system.  
   8. The price change in scope for this exercise is limited to the listed price of a product. The actual pricing includes taxes (city/county/state taxes etc)based on their residence. The solution design only addresses listed price changes. Taxes will be calculated by a system outside the scope of this system design.  
   9. The users can upgrade/downgrade which will also impact their pricing. All such changes will be effective at the end of their billing cycle. 

## System Design
 The high level system design for a basic subscription management platform is as shown below.  
 Note : The design mostly focuses on pricing to address the problem statement listed above. The other components as shown to demonstrate how the pricing module would fit with the rest of the subscription eco system.  

 ## Data Model
 ![ERD](https://github.com/shahbagdadi/basicSubs/blob/master/docs/subs.jpg)

 The DDL and DML based on the above data model for integration test can be found in [schema.sql](https://github.com/shahbagdadi/basicSubs/blob/master/src/main/db/schema.sql)

 * __Customer__ - Holds the customer's basic information like firstname , lastname , email etc. 
 * __Payment_Method__ - Holds basic payment information like payment_type (credit card, ACH, paypal etc. ), payment token, associated payment gateway etc. Note : You might want to consider PCI compliance impacts while storing payment information.
 * __Entitlements__ - 'Features' available in the system. Like STREAM_1 (single stream), STREAM_2 (2 streams), STREAM_HD_1 (single HD stream) , DVD_2_MONTH_UNLIMITED (2 DVD out , unlimited per month) etc.
 * __Entitlement_Set__ - The set of entitlements that will be granted when a customer purchases a particular subscription.
 * __Entitlement_set_mapping__ - Maps one or more entitlements to a particular set.
 * __Customer_grant__ - This table holds the entitlement set that are granted for a subscriber. The entitlement set is granted when a user subscribes and revoked on events like user cancels , upgrades/downgrades or payment cannot be collected etc.
 * __Product__ - Holds all the products/skus defined in the system like Basic, standard , Premium, Ultra, Basic DVD plan , Std DVD plan etc.
 * __Price__ - The price for each of the product. 
 	* Each country supported has an active price in the currency that is supported for that country.
 	* Only one price is active at a time.
 	* Prices can be scheduled in advance to be effective on a certain date/time (UTC). When that time arrives the current active price is set to "inactive" and the scheduled price is made "active".
* __Subscription__ - This table holds the subscription record for a customer. The subscription could be in different states like free trial, active, pending_cancellation, cancelled. The system will alllow a customer to have only 1 active subscription at a time.
* __Ammendment__ - This table tracks changes/ammendments to a subscription. Events like user cancellation, upgrade/downgrade , system cancellation for non payment, account suspension etc will be tracked as an ammendment with an effective date. Ammendments can change the state of a subscription. An upgrade / downgrade for example will cancel the current subscription at the end of the billing cycle and create a new subscription with the product selected by the customer.
* __Invoice / Invoice_line_items__ - Invoice line items are individual items in an invoice like base price, Taxes (state , city , county), discounts etc. An invoice contains one or more invoice line items. An invoice will be generated by the system on the customer's monthly billing anniversary. Once a payment is successfully collected the invoice balance will be 0. The payment system will attempt to collect for all invoices which have an amount > 0.
* __Payment__ - This table will keep track of payments collected by the system.

![Monthly Billing Anniversary](https://github.com/shahbagdadi/basicSubs/blob/master/docs/Billing_Anniversary.jpg)

## Price Retrieval and pricing Changes
### Back of envelop calculations
The basic pricing object consists of product id (int) , ISO 3166 country code (2 char) , ISO 4217 currency code (3 char), amount (double) , status (15 char) 
So a Price java oject is roughly 500 bytes.
Assuming there are 100 products and 200 countries in the world, the memory required to cache all active pricing is 500*100*200 = 10MB. Thus all the prices can easily fit in memory of the nodes serving the prices.  
So we will load all the active pricing in memory on the API service node on startup.  

### Caching Design. 
__How long should this be cached ? How do we update this cache when new prices are scheduled?__.  
__Note :__ New prices can be scheduled at any time with a future effective date.   
Keeping a high TTL means that price changes would not be reflected for a long time. Keeping a low TTL would cause frequent cache miss and also cause a spike of hits to the DB when the cache is invalidated.  
To mitigate this the approach we will take is as follows.    
1. Load all the prices in a ConcurrentHashMap on startup.
2. Schedule a refresh cache task which wakes up every 5 seconds (configurable) and checks if there are any scheduled prices which were effective in the last 10 seconds (buffer added to account for ntp time lags , network delays etc.).
3. For each of those prices set the current price as "inactive" and the "scheduled" price as "active" in the DB.
4. Update the ConcurrentHashMap for all prices which became "active" in the last 10 seconds.

### API Design

#### Get All Active products
API Request : List of all active products. 
Sample Request :  
```
curl -X GET \
  http://localhost:8087/v1/subscriptions/products 
```

#### Scheduled Price Changes
API Request :  POST a price change request
```
curl -X POST \
  http://localhost:8087/v1/subscriptions/prices \
  -H 'Content-Type: application/json' \
  -d '{
	"productId": 2,
    "amount": 11.99,
    "countryCode": "US",
    "currencyCode": "USD",
    "effectiveDate": "2019-06-15 10:45:00"
}' 
```

To get a list of all scheduled prices  
```
curl -X GET \
  'http://localhost:8087/v1/subscriptions/prices?status=scheduled'  
```

#### Fetch current price for a product in a particular country
API Request : GET current price for a product {product_id} in {country_code}.  
API Endpoint : http://localhost:8087/v1/subscriptions/prices/{country_code}/products/{product_id}. 
Sample Request :  
```
curl -X GET \
  http://localhost:8087/v1/subscriptions/prices/US/products/2  
 ```
Sample Response :  
```
{
    "amount": 11.99,
    "countryCode": "US",
    "currencyCode": "USD",
    "effectiveDate": "2019-01-12 22:23:06",
    "id": 11,
    "productId": 2,
    "status": "active"
}
```

## Deploy and Test

#### Pre-Requisites
 - JDK 8
 - Clone the repo

### Build
1. Clone the repo and in the top level folder run
2. Compile the code and create the jar file.  Note we will run all the tests later when the containers are up. 
``` ./mvnw clean install -DskipTests ```
3. Build and run the docker image. This will build the netflix-subscription-image and the run mysql and the subscription image.  
``` docker-compose up --build ```

### Test
1. Once the service is up you can run all the tests.  
``` ./mvnw test ```.  
This will call various integration test to check the APIs that we exposed.   
2. To run just the price change test  
``` ./mvnw -Dtest=PriceTest#testPriceChange test ```. 
This tests gets the price for product id=2 and countrycode = US and adds $1 to the amount and schedules it to be active in 5 seconds. The tests waits for a few seconds and validates if the price change is reflected.  

The details on the price tests conducted can be found [here](https://github.com/shahbagdadi/basicSubs/blob/master/src/test/java/com/netflix/subscription/subscriptionapplication/PriceTest.java#L79) 





