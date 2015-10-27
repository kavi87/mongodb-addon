/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.persistence.mongodb.morphia;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.seedstack.seed.it.AbstractSeedIT;
import org.seedstack.seed.mongodb.domain.user.Address;
import org.seedstack.seed.mongodb.domain.user.User;
import org.seedstack.seed.persistence.mongodb.api.MorphiaDatastore;

import com.google.inject.Inject;


public class MorphiaIT extends AbstractSeedIT{
	@Inject
	@MorphiaDatastore(clientName = "client1",dbName="db1")
	private Datastore datastore; 
	
	@Test
	public void datastore_test(){
		User user = new User(1L,"Gerard", "menvuça", new Address("France", "78300", "Poissy", "avenue de l'europe", 1));
		Key<User> keyUser = datastore.save(user);
		Assertions.assertThat(keyUser).isNotNull();
	}
}
