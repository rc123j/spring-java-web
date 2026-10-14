# spring-java-web
Sprint Framework Boilerplate with: Spring Boot, Spring Data, Http Client, SpringMVC, Beans, Hibernate, Bootstrap CSS.

# Getting Started

Clone the repo
<pre>git clone https://github.com/elevatitech/spring-java-web.git </pre>

or just Download it
<pre>https://github.com/elevatitech/spring-java-web/archive/master.zip</pre>

### Prerequisites
The required tools are part for the project. (Maven, etc)

### Configuration
The mysql configuration can be updated in the src/main/resources/application.properties file. or you can use hbase by changing the spring.datasource url<br />
Create a database name "logistics" or change it in properties file.
<pre>$ vi src/main/resources/application.properties</pre>

### Installing
The dependencies are defined in the pom.xml file.
<pre>$ ./mvnw install  or  $ ./mvnw clean install</pre>

### Running
Once the dependencies are successfully installed and built, Compile and Run the project.
<pre>./mvnw spring-boot:run</pre>

### Output
This will compile and Start the web application. Check the Output in a web browser.
<pre>$ firefox http://localhost:8080/</pre>
        


### Shopify Order Sync
Click **Sync Orders** on the dashboard (or open `/order/sync`). The app fetches the orders, saves them in the `orders` table and redirects to the Orders page.

* The orders are fetched from the url in `shopify.orders.url` (application.properties).
* The test endpoint currently returns 404, so the app falls back to `src/main/resources/sample-orders.json`, which has the same format as the Shopify orders api.
* Running the sync again does not create duplicates, orders are matched by their Shopify id and updated.
* The Orders page shows the order number and the customer name. The search box in the top bar filters the rows while typing, by order number, customer name or email, e.g. `1003` or `alice`.
* **Export** on the Orders page downloads the orders as `orders.csv` (only the searched ones if something is typed in the search).
* The same orders are available as json at `/api/v1/orders`, `/api/v1/orders?search=alice` returns only the matching ones.

Main classes:
* `OrderController` / `OrderApiController` - web page and json api
* `OrderService` - fetch, save and search logic
* `OrderRepository` - database access (Spring Data JPA)
* `Order` - the database table, `ShopifyOrder` - the Shopify json

Note: Spring Boot 2.2 needs Java 8 or 11 to run.

### Tests
The tests use an in-memory H2 database and the sample json, MySQL is not needed.
<pre>./mvnw test</pre>

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/maven-plugin/)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#using-boot-devtools)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Rest Repositories](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#howto-use-exposing-spring-data-repositories-rest-endpoint)
* [Thymeleaf Templates](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-template-engines)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

