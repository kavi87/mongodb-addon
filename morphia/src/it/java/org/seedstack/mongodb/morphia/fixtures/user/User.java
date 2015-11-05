/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.mongodb.morphia.fixtures.user;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.seedstack.business.domain.AggregateRoot;

@Entity
public class User implements AggregateRoot<Long>{
	
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(long id, String name, String lastname, Address address) {
		super();
		this.id = id;
		this.name = name;
		this.lastname = lastname;
		this.address = address;
	}

	@Id
	private long id;
	
	private String name;
	
	private String lastname;

	private Address address;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public Long getEntityId() {
		return id;
	}
	
	
}
