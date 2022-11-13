package com.socgen.repositories.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.socgen.domain.Account;
import com.socgen.domain.Account.AccountBuilder;
import com.socgen.repositories.AccountRepository;

public class AccountRepositoryImpl implements AccountRepository {

	Collection<Account> accounts = List.of(new AccountBuilder(1, null).build(), new AccountBuilder(2, null).build(),
			new AccountBuilder(3, null).build());

	public Account findAccountById(long accountId) {
		Optional<Account> optAccount = accounts.stream().filter(a -> accountId == a.getId()).findAny();

		return optAccount.isPresent() ? optAccount.get() : null;
	}
}