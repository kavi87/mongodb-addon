/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.mongodb.morphia.fixtures.dummyobject;

import org.seedstack.business.domain.BaseAggregateRoot;

public class Dummy1 extends BaseAggregateRoot<Long> {

	@Override
	public Long getEntityId() {
		return null;
	}

}