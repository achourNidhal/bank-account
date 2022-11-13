package com.socgen.repositories;

import com.socgen.domain.Account;

public interface AccountRepository {

	Account findAccountById(long accountId);
}