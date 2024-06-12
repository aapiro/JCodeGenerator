package com.example.jdlparser.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Branch {

    @NotNull
    private String name;
    private String code;


    // getters and setters
}
