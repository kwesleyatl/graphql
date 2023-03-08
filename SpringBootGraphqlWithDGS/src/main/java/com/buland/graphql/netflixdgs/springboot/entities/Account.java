package com.buland.graphql.netflixdgs.springboot.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private String accountId;
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer accountId;

    private Integer accountNumber;
    private String accountStatus;
    private Double accountBalance;
}
