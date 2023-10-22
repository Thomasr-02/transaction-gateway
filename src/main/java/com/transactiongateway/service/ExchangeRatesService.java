package com.transactiongateway.service;

import com.transactiongateway.entity.ExchangeRatesResponse;
import com.transactiongateway.entity.Transaction;
import com.transactiongateway.exception.ExchangeRateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ExchangeRatesService {

    @Autowired
    private RestTemplate restTemplate;

    private ExchangeRatesResponse fetchExchangeRates(String countryCurrency) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange?sort=-record_calendar_year,-record_calendar_month,-record_calendar_day&filter=country_currency_desc:eq:" + countryCurrency;
        try {
            ResponseEntity<ExchangeRatesResponse> response = restTemplate.getForEntity(apiUrl, ExchangeRatesResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new Exception("Failed to retrieve data from the API.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BigDecimal findExchangeRate(Transaction transaction, String countryCurrency) {
        try {
            List<ExchangeRatesResponse.ExchangeRateData> exchangeRatesResponse = fetchExchangeRates(countryCurrency).getData();
            if(!exchangeRatesResponse.isEmpty()){
                ExchangeRatesResponse.ExchangeRateData exchangeRate = findAppropriateExchangeRate(exchangeRatesResponse.get(0), transaction.getTransactionDate());
                return new BigDecimal(exchangeRate.getExchange_rate());
            }
            else {
                throw new Exception(countryCurrency + "does not exist.");
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public BigDecimal convertPurchaseAmount(BigDecimal purchaseAmount, BigDecimal exchangeRateValue){
        BigDecimal convertedAmount = purchaseAmount.multiply(exchangeRateValue);
        convertedAmount = convertedAmount.setScale(2, RoundingMode.HALF_UP);
        return convertedAmount;
    }

    private ExchangeRatesResponse.ExchangeRateData findAppropriateExchangeRate(ExchangeRatesResponse.ExchangeRateData exchangeRate, LocalDateTime transactionDate) {

        String recordDateString = exchangeRate.getRecord_date();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate recordDate = LocalDate.parse(recordDateString, formatter);

        LocalDateTime midnightRecordDate = recordDate.atStartOfDay();

        long monthsDifference = transactionDate.until(midnightRecordDate, ChronoUnit.MONTHS);

        if (monthsDifference >= 6) {
            throw new ExchangeRateNotFoundException("Purchase cannot be converted to the target currency.");
        }
        return exchangeRate;
    }

}
