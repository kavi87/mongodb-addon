/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.mongodb.morphia;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import org.junit.Test;
import org.mongodb.morphia.mapping.MappingException;
import org.mongodb.morphia.query.UpdateException;
import org.seedstack.business.domain.Repository;
import org.seedstack.mongodb.morphia.fixtures.dummyobject.Dummy1;
import org.seedstack.mongodb.morphia.fixtures.dummyobject.Dummy2;
import org.seedstack.mongodb.morphia.fixtures.dummyobject.Dummy3;
import org.seedstack.mongodb.morphia.fixtures.dummyobject.Dummy4;
import org.seedstack.mongodb.morphia.fixtures.dummyobject.Dummy5;
import org.seedstack.mongodb.morphia.fixtures.dummyobject.Dummy6;
import org.seedstack.mongodb.morphia.fixtures.user.Address;
import org.seedstack.mongodb.morphia.fixtures.user.EntityStringId;
import org.seedstack.mongodb.morphia.fixtures.user.User;
import org.seedstack.mongodb.morphia.internal.MorphiaErrorCodes;
import org.seedstack.seed.SeedException;
import org.seedstack.seed.it.AbstractSeedIT;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class MorphiaRepositoryIT extends AbstractSeedIT {

    @Inject
    @Morphia
    private Repository<User, Long> userRepository;

    @Inject
    @Morphia
    private Repository<EntityStringId, String> entityStringIdRepository;

    @Inject
    private Injector injector;

    @Test
    public void repository_injection_test_no_client_for_aggregate() {
        try {

            injector.getInstance(
                    Key.get(TypeLiteral.get(Types.newParameterizedType(Repository.class, Dummy1.class, Long.class)),
                            Morphia.class));
        } catch (ProvisionException e) {
            assertThat(e.getCause().getMessage())
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
            assertThat(e.getCause().getMessage())
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
            assertThat(e.getCause().getMessage())
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
            assertThat(e.getCause().getMessage())
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
            assertThat(e.getCause().getMessage())
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
            assertThat(e.getCause().getMessage())
                    .isEqualTo(SeedException.createNew(MorphiaErrorCodes.ERROR_ASYNC_CLIENT).getMessage());
        }
    }


    @Test
    public void mongodb_repository_test() {
        assertThat(userRepository).isNotNull();
        User user1 = getUser(1L, "N°", "1");
        userRepository.persist(user1);
        User user2 = userRepository.load(user1.getEntityId());
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getEntityId()).isEqualTo(user2.getEntityId());
        userRepository.delete(user1);
        User user3 = userRepository.load(user1.getEntityId());
        assertThat(user3).isEqualTo(null);
        User user5 = getUser(2L, "N°", "2");
        userRepository.delete(user5);
        userRepository.persist(user5);
        User user6 = userRepository.load(user5.getEntityId());
        assertThat(user6.getId()).isEqualTo(user5.getId());
        userRepository.delete(user5);
        user6 = userRepository.load(user5.getEntityId());
        assertThat(user6).isEqualTo(null);
        userRepository.persist(user5);
        assertThat(userRepository.load(2L)).isNotEqualTo(null);
    }

    @Test(expected = MappingException.class)
    public void mongodb_repository_save_without_id() {
        EntityStringId saved = entityStringIdRepository.save(new EntityStringId(null));
        fail("should not have saved");
    }

    @Test(expected = UpdateException.class)
    public void mongodb_repository_save_with_inexistent_id() {
        userRepository.save(getUser(100L, "Robert", "SMITH"));
        fail("should not have saved");
    }

    @Test
    public void mongodb_repository_save() {
        userRepository.persist(getUser(200L, "Robert", "SMITH"));
        assertThat(userRepository.save(getUser(200L, "Jane", "SMITH")).getEntityId()).isEqualTo(200L);
    }

    @Test
    public void mongodb_repository_persist_load() {
        userRepository.persist(getUser(300L, "Robert", "SMITH"));
        assertThat(userRepository.load(300L).getEntityId()).isEqualTo(300L);
    }

    @Test
    public void mongodb_repository_clear() {
        userRepository.persist(getUser(400L, "Robert", "SMITH"));
        userRepository.persist(getUser(401L, "Jayne", "SMITH"));
        assertThat(userRepository.load(400L).getEntityId()).isEqualTo(400L);
        assertThat(userRepository.load(401L).getEntityId()).isEqualTo(401L);
        userRepository.clear();
        assertThat(userRepository.load(400L)).isNull();
        assertThat(userRepository.load(401L)).isNull();
    }

    public User getUser(long id, String firstname, String lastName) {
        return new User(id, firstname, lastName, new Address("France", "75001", "Paris", "Champ Elysee avenue", 1));
    }
}
