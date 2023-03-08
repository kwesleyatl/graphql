package com.buland.graphql.netflixdgs.springboot.repositories;

import com.buland.graphql.netflixdgs.springboot.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
