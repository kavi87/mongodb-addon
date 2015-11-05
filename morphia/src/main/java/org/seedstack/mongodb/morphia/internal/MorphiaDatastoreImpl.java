/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.mongodb.morphia.internal;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import org.seedstack.mongodb.morphia.MorphiaDatastore;
/**
 * 
 * @author redouane.loulou@ext.mpsa.com
 *
 */
class MorphiaDatastoreImpl implements MorphiaDatastore, Serializable  {

	private static final long serialVersionUID = 3861460142806494075L;
	private String clientName;
	private String dbName;

	public MorphiaDatastoreImpl(String clientName, String dbName) {
		this.clientName = clientName;
		this.dbName = dbName;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return MorphiaDatastore.class;
	}

	@Override
	public String dbName() {
		return dbName;
	}

	@Override
	public String clientName() {
		return clientName;
	}

	@Override
	public int hashCode() {
		return ((127 * "clientName".hashCode()) ^ clientName.hashCode())
				+ ((127 * "dbName".hashCode()) ^ dbName.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MorphiaDatastore))
			return false;
		MorphiaDatastoreImpl other = (MorphiaDatastoreImpl) obj;
		if (clientName == null) {
			if (other.clientName != null)
				return false;
		} else if (!clientName.equals(other.clientName))
			return false;
		if (dbName == null) {
			if (other.dbName != null)
				return false;
		} else if (!dbName.equals(other.dbName))
			return false;
		return true;
	}
	

}