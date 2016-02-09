/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/**
 * 
 */
package org.seedstack.mongodb.morphia.internal;

import java.util.Collection;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.seedstack.mongodb.morphia.MorphiaDatastore;

import com.google.inject.AbstractModule;
import com.google.inject.Key;

/**
 * @author redouane.loulou@ext.mpsa.com
 *
 */
class MorphiaModule extends AbstractModule{

	private Collection<MorphiaDatastore> morphiaDatastoresAnnotation;
	private final Morphia morphia;
    
	public MorphiaModule(Collection<MorphiaDatastore> morphiaDatastoresAnnotation, Morphia morphia) {
		super();
		this.morphiaDatastoresAnnotation = morphiaDatastoresAnnotation;
		this.morphia = morphia;
	}

	@Override
	protected void configure() {
		if(morphiaDatastoresAnnotation!=null && !morphiaDatastoresAnnotation.isEmpty()){			
			for (MorphiaDatastore morphiaDatastore : morphiaDatastoresAnnotation) {
				DatastoreProvider datastoreProvider = new DatastoreProvider(morphiaDatastore, morphia);
				requestInjection(datastoreProvider);
				bind(Key.get(Datastore.class, morphiaDatastore)).toProvider(datastoreProvider);
			}
		}
	}
   

	 
}
