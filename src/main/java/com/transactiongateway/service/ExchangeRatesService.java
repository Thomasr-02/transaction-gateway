package com.transactiongateway.service;

import com.transactiongateway.entity.ExchangeRatesResponse;
import com.transactiongateway.entity.Transaction;
import com.transactiongateway.exception.ExchangeRateConvertCurrencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
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
    private static final String API_URL = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";

    @Autowired
    private RestTemplate restTemplate;


    private ExchangeRatesResponse fetchAllExchangeRates(String countryCurrency) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<ExchangeRatesResponse> initialResponse = fetchDataForPage(restTemplate, countryCurrency, 1);

        if (initialResponse.getStatusCode().is2xxSuccessful()) {
            ExchangeRatesResponse initialExchangeRatesResponse = initialResponse.getBody();
            int totalPages = initialExchangeRatesResponse != null ? initialExchangeRatesResponse.getMeta().getTotal_pages() : 0;

            ExchangeRatesResponse combinedResponse = initialExchangeRatesResponse;

            for (int page = 2; page <= totalPages; page++) {
                ResponseEntity<ExchangeRatesResponse> pageResponse = fetchDataForPage(restTemplate, countryCurrency, page);

                if (pageResponse.getStatusCode().is2xxSuccessful()) {
                    ExchangeRatesResponse pageExchangeRatesResponse = pageResponse.getBody();
                    combinedResponse.getData().addAll(pageExchangeRatesResponse != null ? pageExchangeRatesResponse.getData() : null);
                } else {
                    throw new RuntimeException("Failed to retrieve data from the API.");
                }
            }

            return combinedResponse;
        } else {
            throw new RuntimeException("Failed to retrieve data from the API.");
        }
    }

    private ResponseEntity<ExchangeRatesResponse> fetchDataForPage(RestTemplate restTemplate, String countryCurrency, int page) {
        String apiUrl = buildApiUrl(countryCurrency, page);
        return restTemplate.exchange(apiUrl, HttpMethod.GET, null, ExchangeRatesResponse.class);
    }

    private String buildApiUrl(String countryCurrency, int page) {
        return API_URL + "?sort=-record_calendar_year,-record_calendar_month,-record_calendar_day" +
                "&filter=country_currency_desc:eq:" + countryCurrency +
                "&page[number]=" + page;
    }

    public BigDecimal findExchangeRate(Transaction transaction, String countryCurrency) {
        try {
            List<ExchangeRatesResponse.ExchangeRateData> exchangeRatesResponse = fetchAllExchangeRates(countryCurrency).getData();
            if(!exchangeRatesResponse.isEmpty()){
                ExchangeRatesResponse.ExchangeRateData exchangeRate = findAppropriateExchangeRate(exchangeRatesResponse, transaction.getTransactionDate());
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

    public ExchangeRatesResponse.ExchangeRateData findAppropriateExchangeRate(List<ExchangeRatesResponse.ExchangeRateData> exchangeRates, LocalDateTime transactionDate) throws ExchangeRateConvertCurrencyException {
        ExchangeRatesResponse.ExchangeRateData rateUsed = null;
        long closestMonthsDifference = Long.MAX_VALUE;

        for (ExchangeRatesResponse.ExchangeRateData rateData : exchangeRates) {
            String recordDateString = rateData.getRecord_date();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate recordDate = LocalDate.parse(recordDateString, formatter);

            LocalDateTime midnightRecordDate = recordDate.atStartOfDay();

            long monthsDifference = transactionDate.until(midnightRecordDate, ChronoUnit.MONTHS);

            if (monthsDifference <= 6 && monthsDifference < closestMonthsDifference) {
                rateUsed = rateData;
                closestMonthsDifference = monthsDifference;
            }
        }

        if (rateUsed != null) {
            return rateUsed;
        } else {
            throw new ExchangeRateConvertCurrencyException("Purchase cannot be converted to the target currency.");
        }
    }

}
