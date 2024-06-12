package com.example.jdlparser.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Account {

    @NotNull
    private String number;
    @NotNull
    private BigDecimal balance;
    @NotNull
    private Instant createdDate;


    // getters and setters
}
