/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.mongodb.morphia.fixtures.user;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.seedstack.business.domain.AggregateRoot;

@Entity
public class EntityStringId implements AggregateRoot<String> {
    @Id
    private String id;

    public EntityStringId() {
    }

    public EntityStringId(String id) {
        this.id = id;
    }

    @Override
    public String getEntityId() {
        return id;
    }
}
