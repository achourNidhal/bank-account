package com.socgen.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.socgen.domain.Operation;

/**
 * Test Helper
 */
public final class TestHelper {

	/**
	 * Adds the mocked operation.
	 *
	 * @param accountId  the account id
	 * @param operation  the operation
	 * @param operations the operations
	 */
	public static void addMockedOperation(String accountId, Operation operation,
			Map<String, List<Operation>> operations) {
		if (!operations.containsKey(accountId))
			operations.put(accountId, new ArrayList<>());

		operations.get(accountId).add(operation);
	}

	/**
	 * Gets the mocked operations history.
	 *
	 * @param accountId  the account id
	 * @param operations the operations
	 * @return the mocked operations history
	 */
	public static List<Operation> getMockedOperationsHistory(String accountId,
			Map<String, List<Operation>> operations) {
		if (operations.containsKey(accountId))
			return operations.get(accountId);

		return Lists.newArrayList();
	}
}
