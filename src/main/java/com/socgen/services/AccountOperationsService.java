package com.socgen.services;

import java.util.List;

import com.socgen.domain.Account;
import com.socgen.domain.Operation;

/**
 * Service handling different account operations.
 */
public interface AccountOperationsService {

	/**
	 * Deposit an amount to the account.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 * @return the account
	 */
	Account deposit(long accountId, Double amount);

	/**
	 * Withdraw an amount from the account.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 * @return the account
	 */
	Account withdraw(long accountId, Double amount);

	/**
	 * Operations history of the account.
	 *
	 * @param accountId the account id
	 * @return the list
	 */
	List<Operation> operationsHistory(long accountId);

}