package com.transactiongateway.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class ExchangeRatesResponse {
    private List<ExchangeRateData> data;
    private Meta meta;
    private Links links;

    @Getter
    @Setter
    public static class ExchangeRateData {
        private String record_date;
        private String country;
        private String currency;
        private String country_currency_desc;
        private String exchange_rate;
        private String effective_date;
        private String src_line_nbr;
        private String record_fiscal_year;
        private String record_fiscal_quarter;
        private String record_calendar_year;
        private String record_calendar_quarter;
        private String record_calendar_month;
        private String record_calendar_day;

    }

    @Getter
    @Setter
    public static class Meta {
        private int count;
        private Labels labels;
        private DataTypes dataTypes;
        private DataFormats dataFormats;
        private int total_count;
        private int total_pages;


        @Getter
        @Setter
        public static class Labels {
            private String record_date;
            private String country;
            private String currency;
            private String country_currency_desc;
            private String exchange_rate;
            private String effective_date;
            private String src_line_nbr;
            private String record_fiscal_year;
            private String record_fiscal_quarter;
            private String record_calendar_year;
            private String record_calendar_quarter;
            private String record_calendar_month;
            private String record_calendar_day;

        }

        @Getter
        @Setter
        public static class DataTypes {
            private String record_date;
            private String country;
            private String currency;
            private String country_currency_desc;
            private String exchange_rate;
            private String effective_date;
            private String src_line_nbr;
            private String record_fiscal_year;
            private String record_fiscal_quarter;
            private String record_calendar_year;
            private String record_calendar_quarter;
            private String record_calendar_month;
            private String record_calendar_day;

        }

        @Getter
        @Setter
        public static class DataFormats {
            private String record_date;
            private String country;
            private String currency;
            private String country_currency_desc;
            private String exchange_rate;
            private String effective_date;
            private String src_line_nbr;
            private String record_fiscal_year;
            private String record_fiscal_quarter;
            private String record_calendar_year;
            private String record_calendar_quarter;
            private String record_calendar_month;
            private String record_calendar_day;

        }
    }

    @Getter
    @Setter
    public static class Links {
        private String self;
        private String first;
        private String prev;
        private String next;
        private String last;

    }
}
