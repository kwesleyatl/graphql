package com.buland.graphql.netflixdgs.springboot.repositories;

import com.buland.graphql.netflixdgs.springboot.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
}
