/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.mongodb.domain.user;

import org.mongodb.morphia.annotations.Embedded;
import org.seedstack.business.api.domain.ValueObject;

@Embedded
public class Address implements ValueObject{
	private String country;
	private String zipcode;
	private String city;
	private String street;
	private Integer number;
	
	
	
	public Address() {
		super();
	}
	public Address(String country, String zipcode, String city, String street, Integer number) {
		super();
		this.country = country;
		this.zipcode = zipcode;
		this.city = city;
		this.street = street;
		this.number = number;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	
	
}
