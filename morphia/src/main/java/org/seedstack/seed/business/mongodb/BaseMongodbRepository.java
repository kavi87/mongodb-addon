/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.business.mongodb;

import org.mongodb.morphia.Datastore;
import org.seedstack.business.api.domain.AggregateRoot;
import org.seedstack.business.api.domain.BaseRepository;
import org.seedstack.seed.core.api.Application;
import org.seedstack.seed.persistence.mongodb.internal.MorphiaPlugin;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * This class serves as inheritance base for the Mongodb repositories.
 *
 * @param <A>
 *            Mongodb Entity Type (DDD: Aggregate)
 * @param <K>
 *            key type
 * @author redouane.loulou@ext.mpsa.com Date: 20/10/2015
 */
public abstract class BaseMongodbRepository<A extends AggregateRoot<K>, K> extends BaseRepository<A, K> {

	private Datastore datastore;

	public Datastore getDatastore() {
		return datastore;
	}

	public void setDatastore(Datastore datastore) {
		this.datastore = datastore;
	}

	@Inject
	private void initDatastore(Application application, Injector injector) {
		datastore = injector.getInstance(Key.get(Datastore.class, MorphiaPlugin.getMongoDatastore(application, aggregateRootClass)));
	}

	@Override
	protected A doLoad(K id) {
		return datastore.get(aggregateRootClass, id);
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
		return datastore.get(aggregateRootClass, datastore.save(aggregate).getId());
	}

}