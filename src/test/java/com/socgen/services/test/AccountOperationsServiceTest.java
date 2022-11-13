package com.socgen.services.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.socgen.domain.Account;
import com.socgen.domain.Account.AccountBuilder;
import com.socgen.domain.Operation;
import com.socgen.enums.OperationType;
import com.socgen.exceptions.InsufficientFundException;
import com.socgen.repositories.AccountRepository;
import com.socgen.repositories.OperationRepository;
import com.socgen.services.impl.AccountOperationsServiceImpl;
import com.socgen.utilities.LongUtils;
import com.socgen.utilities.TestHelper;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AccountOperationsServiceTest {

	@Mock
	AccountRepository accountRepo;
	@Mock
	OperationRepository operationRepo;

	@InjectMocks
	AccountOperationsServiceImpl operationsService;

	static List<Account> mockedAccounts;
	Map<String, List<Operation>> mockedOperations = Maps.newHashMap();

	private static final double firstAmount = 100, secondAmount = 10, thirdAmount = 50, fourthAmount = 20;

	@BeforeEach
	void init() {
		mockedOperations = Maps.newHashMap();
		mockedAccounts = Lists.newArrayList();
	}

	@Test
	public void testDepositsFromSameAccount() {
		Account account = new AccountBuilder(0, null).build();
		mockedAccounts.add(account);
		when(accountRepo.findAccountById(eq((long) 0))).thenReturn(mockedAccounts.get(0));

		operationsService.deposit(0, firstAmount);
		operationsService.deposit(0, secondAmount);
		operationsService.withdraw(0, thirdAmount);
		account = operationsService.withdraw(0, fourthAmount);

		double expectedBalance = firstAmount + secondAmount - thirdAmount - fourthAmount;
		assertTrue(expectedBalance == account.getBalance());
	}

	@Test()
	public void testWithdrawalFromAccountWithoutSufficientFunds() {
		Account account = new AccountBuilder(0, null).build();
		mockedAccounts.add(account);
		when(accountRepo.findAccountById(eq((long) 0))).thenReturn(mockedAccounts.get(0));

		assertDoesNotThrow(() -> {
			operationsService.deposit(0, secondAmount);
		});

		InsufficientFundException exception = assertThrows(InsufficientFundException.class, () -> {
			operationsService.withdraw(0, firstAmount);
		});
		assertEquals("Insufficient balance", exception.getMessage());
	}

	@Test
	public void testDepositsAndWithDrawalsFromSameAccount() {
		Account account = new AccountBuilder(0, null).build();
		mockedAccounts.add(account);
		when(accountRepo.findAccountById(eq((long) 0))).thenReturn(mockedAccounts.get(0));

		operationsService.deposit(0, firstAmount);
		operationsService.deposit(0, secondAmount);
		operationsService.withdraw(0, thirdAmount);
		account = operationsService.withdraw(0, fourthAmount);

		double expectedBalance = firstAmount + secondAmount - thirdAmount - fourthAmount;
		assertTrue(expectedBalance == account.getBalance());
	}

	@Test
	public void testDepositsAndWithDrawalsInMultihreadedContext() {

		LongStream.rangeClosed(0, 9).forEach(i -> {
			Account account = new AccountBuilder(i, null).build();
			mockedAccounts.add(account);
			when(accountRepo.findAccountById(eq(i))).thenReturn(account);
		});

		double expectedBalance = firstAmount - secondAmount - thirdAmount + fourthAmount;

		ExecutorService executor = Executors.newFixedThreadPool(10);

		LongStream.rangeClosed(0, 9).forEach(i -> {
			Runnable worker = () -> {

				operationsService.deposit(i, firstAmount);
				operationsService.withdraw(i, secondAmount);
				operationsService.withdraw(i, thirdAmount);
				Account account = operationsService.deposit(i, fourthAmount);

				assertTrue(expectedBalance == account.getBalance());
			};

			executor.execute(worker);
		});

		executor.shutdown();
	}

	@Test
	public void testFetchingHistoryAfterDepositsAndWithDrawals() {

		// the id of the account used for different operations
		long accountId = 0;

		// stub the save of an operation history
		doAnswer(inv -> {
			String accountIdAsString = LongUtils.toString(inv.getArgument(0));
			Operation operation = inv.getArgument(1);

			TestHelper.addMockedOperation(accountIdAsString, operation, mockedOperations);
			return null;
		}).when(operationRepo).addOperation(anyLong(), any(Operation.class));

		Account account = new AccountBuilder(accountId, null).build();
		mockedAccounts.add(account);
		when(accountRepo.findAccountById(eq((long) accountId))).thenReturn(mockedAccounts.get(0));

		operationsService.deposit(accountId, firstAmount);
		operationsService.deposit(accountId, secondAmount);
		operationsService.withdraw(accountId, thirdAmount);
		account = operationsService.withdraw(accountId, fourthAmount);

		List<Operation> history = TestHelper.getMockedOperationsHistory(LongUtils.toString(accountId),
				mockedOperations);

		// check that history contains 4 operations
		assertTrue(history.size() == 4);

		// check first operation history
		assertEquals(accountId, history.get(0).getAccountId());
		assertEquals(OperationType.DEPOSIT, history.get(0).getType());
		assertEquals(firstAmount, history.get(0).getAmount());

		// check second operation history
		assertEquals(accountId, history.get(1).getAccountId());
		assertEquals(OperationType.DEPOSIT, history.get(1).getType());
		assertEquals(secondAmount, history.get(1).getAmount());

		// check third operation history
		assertEquals(accountId, history.get(2).getAccountId());
		assertEquals(OperationType.WITHDRAW, history.get(2).getType());
		assertEquals(thirdAmount, history.get(2).getAmount());

		// check fourth operation history
		assertEquals(accountId, history.get(3).getAccountId());
		assertEquals(OperationType.WITHDRAW, history.get(3).getType());
		assertEquals(fourthAmount, history.get(3).getAmount());
	}

}
