package com.example.concurrency.currency.model;


import lombok.Getter;


public enum ExchangeRate {

    rubbleToUsd(0.4),
    rubbleToZl(2.0),

    usdToZl(4.5),
    usdToRubble(2.5),

    zlToRubble(0.5),
    zlToUsd(0.22);

    @Getter
    private double rate;

    ExchangeRate(double rate) {
        this.rate = rate;
    }

}
