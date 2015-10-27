/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.persistence.mongodb.internal;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.seedstack.seed.core.api.Application;
import org.seedstack.seed.persistence.mongodb.api.MorphiaDatastore;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import com.mongodb.MongoClient;
/**
 * 
 * @author redouane.loulou@ext.mpsa.com
 *
 */
class DatastoreProvider implements Provider<Datastore> {

	@Inject
	private Injector injector;

	@Inject
	private Application application;

	private Class<?> mappedclass;

	private MorphiaDatastore morphiaDatastore;

	private final Morphia morphia;

	@Override
	public Datastore get() {
		MongoClient mongoClient = injector
				.getInstance(Key.get(MongoClient.class, Names.named(morphiaDatastore.clientName())));
		return morphia.createDatastore(mongoClient, morphiaDatastore.dbName());
	}

	public DatastoreProvider(MorphiaDatastore morphiaDatastore, Morphia morphia) {
		super();
		this.morphiaDatastore = morphiaDatastore;
		this.morphia = morphia;
	}

}
