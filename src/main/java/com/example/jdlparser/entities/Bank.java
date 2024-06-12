package com.example.jdlparser.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Bank {

    @NotNull
    private String name;
    private String address;


    // getters and setters
}
