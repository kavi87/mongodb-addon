/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.mongodb.morphia.internal;

import com.mongodb.DBObject;
import org.mongodb.morphia.AbstractEntityInterceptor;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;
import org.seedstack.validation.api.VerboseConstraintViolationException;

import javax.validation.ValidatorFactory;
import java.util.Set;

class InternalValidationExtension extends AbstractEntityInterceptor {
    private final ValidatorFactory validatorFactory;

    InternalValidationExtension(final ValidatorFactory validatorFactory, final Morphia morphia) {
        this.validatorFactory = validatorFactory;
        morphia.getMapper().addInterceptor(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void prePersist(final Object ent, final DBObject dbObj, final Mapper mapper) {
        final Set result = validatorFactory.getValidator().validate(ent);
        if (!result.isEmpty()) {
            throw new VerboseConstraintViolationException(result);
        }
    }
}
