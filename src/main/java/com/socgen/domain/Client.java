package com.socgen.domain;

public class Client {

	private final long id;
	private final String firstName;
	private final String lastName;

	private Client(ClientBuilder builder) {
		this.id = builder.id;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
	}

	public long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@Override
	public String toString() {
		return "Client: " + this.firstName + ", " + this.lastName;
	}

	public static class ClientBuilder {
		private final long id;
		private final String firstName;
		private final String lastName;

		public ClientBuilder(long id, String firstName, String lastName) {
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
		}

		public Client build() {
			Client client = new Client(this);
			return client;
		}
	}
}