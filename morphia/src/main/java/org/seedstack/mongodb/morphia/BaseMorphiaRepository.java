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
import org.mongodb.morphia.Datastore;
import org.seedstack.business.domain.AggregateRoot;
import org.seedstack.business.domain.BaseRepository;
import org.seedstack.mongodb.morphia.internal.MorphiaUtils;
import org.seedstack.seed.Application;

/**
 * This class can serve as a base class for Morphia repositories. It provides methods for common CRUD operations as
 * well as access to the data store through the {@link #getDatastore()} ()} protected method.
 *
 * @param <A> Aggregate root class
 * @param <K> Key class
 * @author redouane.loulou@ext.mpsa.com
 */
public abstract class BaseMorphiaRepository<A extends AggregateRoot<K>, K> extends BaseRepository<A, K> {
    private Datastore datastore;

    /**
     * Provides access to the Morphia data store for implementing custom data access methods.
     *
     * @return the Morphia data store.
     */
    protected Datastore getDatastore() {
        return datastore;
    }

    @Inject
    private void initDatastore(Application application, Injector injector) {
        datastore = injector.getInstance(Key.get(Datastore.class, MorphiaUtils.getMongoDatastore(application, aggregateRootClass)));
    }

    @Override
    protected A doLoad(K id) {
        return datastore.get(aggregateRootClass, id);
    }

    @Override
    protected void doClear() {
        datastore.delete(datastore.createQuery(aggregateRootClass));
    }

    @Override
    protected void doDelete(K id) {
        datastore.delete(aggregateRootClass, id);
    }

    @Override
    protected void doDelete(A aggregate) {
        datastore.delete(aggregate);
    }

    @Override
    protected void doPersist(A aggregate) {
        datastore.save(aggregate);
    }

    @Override
    protected A doSave(A aggregate) {
        datastore.merge(aggregate);
        return aggregate;
    }
}