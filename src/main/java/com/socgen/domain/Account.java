package com.socgen.domain;

import java.time.LocalDate;

public class Account {

	private final long id;
	private final Client client;
	private final LocalDate creationDate;
	private double balance;

	private Account(AccountBuilder builder) {
		this.id = builder.id;
		this.client = builder.client;

		this.balance = 0;
		this.creationDate = LocalDate.now();
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Client getClient() {
		return client;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public long getId() {
		return id;
	}

	public static class AccountBuilder {
		private final long id;
		private final Client client;

		public AccountBuilder(long id, Client client) {
			this.id = id;
			this.client = client;
		}

		public Account build() {
			Account account = new Account(this);
			return account;
		}
	}
}