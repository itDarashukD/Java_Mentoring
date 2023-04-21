package com.example.concurrency.currency.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserAccount {

    private BigDecimal usd;
    private BigDecimal rubble;
    private BigDecimal zl;

}
