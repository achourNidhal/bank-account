package com.socgen.repositories.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.socgen.domain.Operation;
import com.socgen.repositories.OperationRepository;
import com.socgen.utilities.LongUtils;

/**
 * handle account operations history.
 */
public class OperationRepositoryImpl implements OperationRepository {

	Map<String, List<Operation>> operations = new HashMap<>();

	/**
	 * Adds the operation.
	 *
	 * @param accountId the account id
	 * @param operation the operation
	 */
	@Override
	public void addOperation(long accountId, Operation operation) {
		String accountIdAsString = LongUtils.toString(accountId);
		if (!operations.containsKey(accountIdAsString))
			operations.put(accountIdAsString, new ArrayList<>());

		operations.get(accountIdAsString).add(operation);
	}

	/**
	 * Retrieve operations history.
	 * 
	 * NOTICE: Here we should fetch only N previous operations, or could manage
	 * pagination
	 *
	 * @param accountId the account id
	 * @return the list operations
	 */
	@Override
	public List<Operation> operationsHistory(long accountId) {
		String accountIdAsString = LongUtils.toString(accountId);

		if (operations.containsKey(accountIdAsString))
			return operations.get(accountIdAsString);

		return new ArrayList<>();
	}

}