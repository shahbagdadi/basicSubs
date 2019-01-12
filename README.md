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