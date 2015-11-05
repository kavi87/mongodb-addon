/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.mongodb.morphia;

import com.google.inject.*;
import com.google.inject.util.Types;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.seedstack.business.domain.Repository;
import org.seedstack.mongodb.morphia.internal.MorphiaErrorCodes;
import org.seedstack.seed.SeedException;
import org.seedstack.seed.it.AbstractSeedIT;
import org.seedstack.mongodb.morphia.fixtures.dummyobject.*;
import org.seedstack.mongodb.morphia.fixtures.user.Address;
import org.seedstack.mongodb.morphia.fixtures.user.User;

public class MongodbRepositoryIT extends AbstractSeedIT {

	@Inject
	@Morphia
	private Repository<User, Long> userRepository;

	@Inject
	private Injector injector;

	@Test
	public void repository_injection_test_no_client_for_aggregate() {
		try {

			injector.getInstance(
					Key.get(TypeLiteral.get(Types.newParameterizedType(Repository.class, Dummy1.class, Long.class)),
							Morphia.class));
		} catch (ProvisionException e) {
			Assertions.assertThat(e.getCause().getMessage())
					.isEqualTo(SeedException.createNew(MorphiaErrorCodes.UNKNOW_DATASTORE_CLIENT).getMessage());
		}
	}

	@Test
	public void repository_injection_test_no_dbName_for_aggregate() {
		try {
			injector.getInstance(
					Key.get(TypeLiteral.get(Types.newParameterizedType(Repository.class, Dummy2.class, Long.class)),
							Morphia.class));
		} catch (ProvisionException e) {
			Assertions.assertThat(e.getCause().getMessage())
					.isEqualTo(SeedException.createNew(MorphiaErrorCodes.UNKNOW_DATASTORE_DATABASE).getMessage());
		}
	}

	@Test
	public void repository_injection_test_no_mongoDb_client() {
		try {
			injector.getInstance(
					Key.get(TypeLiteral.get(Types.newParameterizedType(Repository.class, Dummy3.class, Long.class)),
							Morphia.class));
		} catch (ProvisionException e) {
			Assertions.assertThat(e.getCause().getMessage())
					.isEqualTo(SeedException.createNew(MorphiaErrorCodes.UNKNOW_DATASTORE_CLIENT).getMessage());
		}
	}

	@Test
	public void repository_injection_test_no_mongoDb_database() {
		try {
			injector.getInstance(
					Key.get(TypeLiteral.get(Types.newParameterizedType(Repository.class, Dummy4.class, Long.class)),
							Morphia.class));
		} catch (ProvisionException e) {
			Assertions.assertThat(e.getCause().getMessage())
					.isEqualTo(SeedException.createNew(MorphiaErrorCodes.UNKNOW_DATABASE_NAME).getMessage());
		}
	}

	@Test
	public void repository_injection_test_no_mongodb_for_aggregate() {
		try {
			injector.getInstance(
					Key.get(TypeLiteral.get(Types.newParameterizedType(Repository.class, Dummy5.class, Long.class)),
							Morphia.class));
		} catch (ProvisionException e) {
			Assertions.assertThat(e.getCause().getMessage())
					.isEqualTo(SeedException.createNew(MorphiaErrorCodes.UNKNOW_DATASTORE_CONFIGURATION).getMessage());
		}
	}

	@Test
	public void repository_injection_async_client() {
		try {
			injector.getInstance(
					Key.get(TypeLiteral.get(Types.newParameterizedType(Repository.class, Dummy6.class, Long.class)),
							Morphia.class));
		} catch (ProvisionException e) {
			Assertions.assertThat(e.getCause().getMessage())
					.isEqualTo(SeedException.createNew(MorphiaErrorCodes.ERROR_ASYNC_CLIENT).getMessage());
		}
	}


	@Test
	public void mongodb_repository_test() {
		Assertions.assertThat(userRepository).isNotNull();
		User user1 = getUser(1L, "N°", "1");
		userRepository.persist(user1);
		User user2 = userRepository.load(user1.getEntityId());
		Assertions.assertThat(user1.getId()).isEqualTo(user2.getId());
		Assertions.assertThat(user1.getEntityId()).isEqualTo(user2.getEntityId());
		userRepository.delete(user1);
		User user3 = userRepository.load(user1.getEntityId());
		Assertions.assertThat(user3).isEqualTo(null);
		User user5 = getUser(2L, "N°", "2");
		userRepository.delete(user5);
		userRepository.save(user5);
		User user6 = userRepository.load(user5.getEntityId());
		Assertions.assertThat(user6.getId()).isEqualTo(user5.getId());
		userRepository.delete(user5);
		user6 = userRepository.load(user5.getEntityId());
		Assertions.assertThat(user6).isEqualTo(null);
		userRepository.persist(user5);
		Assertions.assertThat(userRepository.load(2L)).isNotEqualTo(null);
	}
	
	public User getUser(long id, String firstname, String lastName) {
		return new User(id, firstname, lastName, new Address("France", "75001", "Paris", "Champ Elysee avenue", 1));
	}

}
