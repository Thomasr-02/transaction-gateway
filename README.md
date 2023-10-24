# Transaction Gateway

## Description
Application to store (persist using H2 Memory DB) a purchase transaction with a description, transaction
date, and a purchase amount in United States dollars. When the transaction is stored, it will be assigned a unique
identifier and you can list and convert purchase amount to others country currency, the conversion works using [Treasury Reporting Rates of Exchange
](https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange) API.

## Tools and Technologies Used

- **Spring Boot:** The core framework.

- **IDE:** IntelliJ.

- **Maven:** Build automation tools to manage dependencies and build projects.

- **Spring Initializr:** A web-based tool for generating Spring Boot projects with predefined configurations.

- **Git and Version Control Systems:** Git and GitHub for version control.

- **Database Tools:** H2 Database for database management.

- **API Documentation Tools:** README.MD for API documentation.

- **Test Application:** Postman

## Prerequisites

Before you can use the Spring Boot API with Maven, make sure you have the following prerequisites in place:

1. **Java Development Kit (JDK):** Ensure that you have the Java Development Kit (JDK) installed on your system. You can check if Java is installed by running `java -version` in your terminal. If it's not installed, download and install it from the [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html).

2. **Maven Build Tool:** Maven is the build tool for your project. Make sure you have Maven installed. You can check your Maven installation by running `mvn -version`. If it's not installed, follow the installation instructions for Maven at [Maven Installation Guide](https://maven.apache.org/install.html).

3. **Spring Boot Application:** You should have your Spring Boot application source code and configuration files ready. Ensure that your application is set up and ready to run with Maven.

4. **Terminal or IDE:** You will need access to a terminal to execute Maven commands for building and running your Spring Boot application or IDE like [IntelliJ](https://www.jetbrains.com/pt-br/idea/download/).

Make sure to meet these prerequisites before running and using your Spring Boot API with Maven.



## Installation
```bash 
git clone https://github.com/Thomasr-02/transaction-gateway
```
```bash 
mvn clean package 
```
```bash 
java -jar target/transaction-gateway-0.0.1-SNAPSHOT.jar
```

## **Features**
- ### Endpoint to create a new transaction.

**URL:** `http://localhost:8080/v1/transaction`
**Method:** `POST`
### Request Body

The request body should be in JSON format and include the following fields:

```json
{
    "description": "Potato",
    "purchaseAmount": 2
}
```

| Field            | Type   | Description                |
| ---------------- | ------ | -------------------------- |
| `description`    | string | Description of the purchase |
| `purchaseAmount` | number | Amount of the purchase     |

- ### Endpoint to retrieve a list of all transactions.

**URL:** `http://localhost:8080/v1/transaction`
**Method:** `GET`
### Response

```json
HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id": "baa31a8b-7f0f-41a8-83ac-ea9c5f93e00d",
        "description": "Potato",
        "purchaseAmount": 2,
        "timestamp": "2023-10-24T12:34:56Z"
    },
    {
        "id": "11c0df55-1829-4722-988e-aeafa7c020db",
        "description": "Tomato",
        "purchaseAmount": 3,
        "timestamp": "2023-10-24T13:45:30Z"
    }
]
```

| Field            | Type   | Description                     |
|------------------|--------|---------------------------------|
| `id`             | string | Unique identify of the purchase |
| `description`    | string | Description of the purchase     |
| `purchaseAmount` | number | Amount of the purchase          |
| `transactionDate` | string | Date of the purchase            |


- ### Endpoint to retrieve a specific transaction by its ID.

**URL:** `http://localhost:8080/v1/transaction/{id}`
**Method:** `GET`
### Response

```json
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": "baa31a8b-7f0f-41a8-83ac-ea9c5f93e00d",
    "description": "Potato",
    "transactionDate": "2023-03-31T11:35:02.661993",
    "purchaseAmount": 2
}
```

| Field            | Type   | Description                     |
|------------------|--------|---------------------------------|
| `id`             | string | Unique identify of the purchase |
| `description`    | string | Description of the purchase     |
| `purchaseAmount` | number | Amount of the purchase          |
| `transactionDate` | string | Date of the purchase            |


## **Errors Response**
404 Not Found: If a transaction with the specified ID does not exist.

500 Internal Server Error: If Purchase cannot be converted to the target currency.

### Contributing
Send a pull requests 😆💻
