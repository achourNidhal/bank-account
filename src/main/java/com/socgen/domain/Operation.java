package com.socgen.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.socgen.enums.OperationType;

public class Operation {
	private final OperationType type;
	private final Double amount;
	private final LocalDateTime date;
	private final long accountId;

	private Operation(OperationBuilder builder) {
		this.type = builder.type;
		this.amount = builder.amount;
		this.accountId = builder.accountId;
		this.date = LocalDateTime.now();
	}

	public OperationType getType() {
		return type;
	}

	public Double getAmount() {
		return amount;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public long getAccountId() {
		return accountId;
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

		return "Operation{" + "account=" + accountId + ", type=" + type + ", amount=" + amount + ", date="
				+ date.format(formatter) + "}";
	}

	public static class OperationBuilder {
		private final OperationType type;
		private final Double amount;
		private final long accountId;

		public OperationBuilder(OperationType type, Double amount, long accountId) {
			this.type = type;
			this.amount = amount;
			this.accountId = accountId;
		}

		public Operation build() {
			return new Operation(this);
		}
	}
}