package com.socgen.services.impl;

import java.util.List;
import java.util.Objects;

import com.socgen.domain.Account;
import com.socgen.domain.Operation;
import com.socgen.domain.Operation.OperationBuilder;
import com.socgen.enums.OperationType;
import com.socgen.exceptions.InsufficientFundException;
import com.socgen.exceptions.UnknowAccountException;
import com.socgen.exceptions.UnknowOperationTypeException;
import com.socgen.repositories.AccountRepository;
import com.socgen.repositories.OperationRepository;
import com.socgen.services.AccountOperationsService;

/**
 * Service handling different account operations.
 */
public class AccountOperationsServiceImpl implements AccountOperationsService {

	/** The account repository. */
	private AccountRepository accountRepository;

	/** The operation repository. */
	private OperationRepository operationRepository;

	/**
	 * Instantiates a new account operations service implementation.
	 *
	 * @param accountRepo   the account repository
	 * @param operationRepo the operation repository
	 */
	public AccountOperationsServiceImpl(AccountRepository accountRepo, OperationRepository operationRepo) {
		accountRepository = accountRepo;
		operationRepository = operationRepo;
	}

	/**
	 * Deposit an amount to the account.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 * @return the updated account
	 */
	@Override
	public Account deposit(long accountId, Double amount) {

		Account account = accountRepository.findAccountById(accountId);
		if (account == null)
			throw new UnknowAccountException();

		account = processOperation(OperationType.DEPOSIT, account, amount);
		saveOperationHistory(OperationType.DEPOSIT, account, amount);
		return account;
	}

	/**
	 * Withdraw an amount from the account.
	 *
	 * @param accountId the account id
	 * @param amount    the amount
	 * @return the updated account
	 */
	@Override
	public Account withdraw(long accountId, Double amount) {
		Objects.nonNull(amount);

		Account account = accountRepository.findAccountById(accountId);
		if (account == null)
			throw new UnknowAccountException();

		account = processOperation(OperationType.WITHDRAW, account, amount);
		saveOperationHistory(OperationType.WITHDRAW, account, amount);
		return account;
	}

	/**
	 * Operations history of the account.
	 *
	 * @param accountId the account id
	 * @return the list
	 */
	@Override
	public List<Operation> operationsHistory(long accountId) {
		return operationRepository.operationsHistory(accountId);
	}

	/**
	 * Process operation.
	 *
	 * @param operationType the operation type
	 * @param account       the account
	 * @param amount        the amount
	 * @return the updated account
	 */
	private synchronized Account processOperation(OperationType operationType, Account account, Double amount) {
		switch (operationType) {
		case DEPOSIT:
			account = processDeposit(account, amount);
			break;
		case WITHDRAW:
			account = processWithDrawal(account, amount);
			break;
		default:
			throw new UnknowOperationTypeException();
		}

		return account;
	}

	/**
	 * Save operation history.
	 *
	 * @param operationType the operation type
	 * @param account       the account
	 * @param amount        the amount
	 */
	private void saveOperationHistory(OperationType operationType, Account account, Double amount) {
		long accountId = account.getId();
		Operation operation = new OperationBuilder(operationType, amount, accountId).build();
		operationRepository.addOperation(accountId, operation);
	}

	/**
	 * Process deposit.
	 *
	 * @param account the account
	 * @param amount  the amount
	 * @return the updated account
	 */
	private Account processDeposit(Account account, double amount) {
		// accept money whenever deposited
		account.setBalance(account.getBalance() + amount);
		return account;
	}

	/**
	 * Process withdrawal.
	 *
	 * @param account the account
	 * @param amount  the amount
	 * @return the updated account
	 * @throws UnsupportedOperationException the unsupported operation exception
	 */
	private Account processWithDrawal(Account account, double amount) throws UnsupportedOperationException {
		// throw an exception if has not sufficient funds
		if (account.getBalance() < amount)
			throw new InsufficientFundException();

		account.setBalance(account.getBalance() - amount);
		return account;
	}

}
