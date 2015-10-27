/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.business.mongodb.internal;

import javax.inject.Inject;

import org.seedstack.business.api.domain.AggregateRoot;
import org.seedstack.business.spi.GenericImplementation;
import org.seedstack.seed.business.mongodb.BaseMongodbRepository;
import org.seedstack.seed.core.utils.SeedCheckUtils;
import org.seedstack.seed.persistence.mongodb.api.Morphia;

import com.google.inject.assistedinject.Assisted;

/**
 * Default Morphia implementation for Repository. Used only when no implementation is provided for an aggregate.
 *
 * To inject this implementation you have to use {@link org.seedstack.business.api.domain.Repository} as follows:
 * <pre>
 * {@literal @}Inject
 * Repository{@literal <MyAggregateRoot, MyKey>} myAggregateRepository;
 * </pre>
 *
 * @param <AGGREGATE> the aggregate root
 * @param <KEY>       the aggregate key
 * @author redouane.loulou@ext.mpsa.com
 * @see org.seedstack.business.api.domain.Repository
 * @see org.seedstack.business.mongodb.BaseMongodbRepository
 */
@Morphia
@GenericImplementation
public class DefaultMongodbRepository<AGGREGATE extends AggregateRoot<KEY>, KEY> extends BaseMongodbRepository<AGGREGATE, KEY> {

    /**
     * Constructs a DefaultMongodbRepository.
     *
     * @param genericClasses the resolved generics for the aggregate root class and the key class
     */
    @SuppressWarnings("unchecked")
    @Inject
    public DefaultMongodbRepository(@Assisted Object[] genericClasses) {
        Object[] clonedClasses = genericClasses.clone();
        SeedCheckUtils.checkIfNotNull(clonedClasses);
        SeedCheckUtils.checkIf(clonedClasses.length == 2);
        this.aggregateRootClass = (Class) clonedClasses[0];
        this.keyClass = (Class) clonedClasses[1];
    }
}