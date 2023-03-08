package com.buland.graphql.netflixdgs.springboot.datafetchers;

import com.buland.graphql.netflixdgs.springboot.entities.Account;
import com.buland.graphql.netflixdgs.springboot.entities.Customer;
import com.buland.graphql.netflixdgs.springboot.repositories.AccountRepository;
import com.buland.graphql.netflixdgs.springboot.repositories.CustomerRepository;
import com.buland.graphqldgs.generated.types.AccountInput;
import com.buland.graphqldgs.generated.types.CustomerInput;
import com.netflix.graphql.dgs.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// @DgsComponent — It acts like @RestController annotation.
@DgsComponent
public class CustomerDataFetcher {

    CustomerRepository customerRepository;

    AccountRepository accountRepository;

    public CustomerDataFetcher(CustomerRepository customerRepository, AccountRepository accountRepository){
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    @DgsData(parentType = "Query", field = "customers")
    public List<Customer> customers() {
        return customerRepository.findAll();
    }

    @DgsData(parentType = "Customer", field = "accounts")
    public List<Account> accounts(DgsDataFetchingEnvironment dgsDataFetchingEnvironment) {
        Customer customer = dgsDataFetchingEnvironment.getSource();
        List<Account> accountList = new ArrayList<>();
        for (Account account : customer.getAccounts()) {
            Account accountResponse = accountRepository.findById(account.getAccountId()).get();
            accountList.add(accountResponse);
        }
        return accountList;
    }

    @DgsMutation
    public Customer customer(CustomerInput customerInput) {
        Customer customer = Customer.builder()
                .contact(customerInput.getContact())
                .name(customerInput.getName())
                .gender(customerInput.getGender())
                .mail(customerInput.getMail())
                .accounts(mapCustomerAccounts(customerInput.getAccounts()))
                .build();
        Customer customerResponse = customerRepository.save(customer);
        return customerResponse;
    }

    private List<Account> mapCustomerAccounts(List<AccountInput> accountIpnut) {
        List<Account> accountsList = accountIpnut.stream().map(accInput -> {
            Account account = Account.builder()
                    .accountBalance(accInput.getAccountBalance())
                    .accountNumber(accInput.getAccountNumber())
                    .accountStatus(accInput.getAccountStatus().name())
                    .build();
            return account;
        }).collect(Collectors.toList());
        return accountsList;
    }
}
