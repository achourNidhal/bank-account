package com.socgen.repositories;

import java.util.List;

import com.socgen.domain.Operation;

/**
 * handle account operations history.
 */
public interface OperationRepository {

	/**
	 * Adds the operation.
	 *
	 * @param accountId the account id
	 * @param operation the operation
	 */
	void addOperation(long accountId, Operation operation);

	/**
	 * Retrieve operations history. 
	 * 
	 * NOTICE: Here we should fetch only N previous
	 * operations, or could manage pagination
	 *
	 * @param accountId the account id
	 * @return the list operations
	 */
	List<Operation> operationsHistory(long accountId);

}