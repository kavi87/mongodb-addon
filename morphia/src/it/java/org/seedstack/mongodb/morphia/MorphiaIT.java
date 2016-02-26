/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.mongodb.morphia;

import com.google.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.seedstack.mongodb.morphia.fixtures.user.Address;
import org.seedstack.mongodb.morphia.fixtures.user.User;
import org.seedstack.seed.it.AbstractSeedIT;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Fail.fail;

public class MorphiaIT extends AbstractSeedIT {
    @Inject
    @MorphiaDatastore(clientName = "client1", dbName = "db1")
    private Datastore datastore;

    @Test
    public void datastore_test() {
        User user = new User(1L, "Gerard", "menvuça", new Address("France", "78300", "Poissy", "avenue de l'europe", 1));
        Key<User> keyUser = datastore.save(user);
        Assertions.assertThat(keyUser).isNotNull();
    }

    @Test(expected = ConstraintViolationException.class)
    public void validation_is_working() {
        User user = new User(1L, null, "menvuça", new Address("France", "78300", "Poissy", "avenue de l'europe", 1));
        datastore.save(user);
        fail("should not have saved");
    }
}
