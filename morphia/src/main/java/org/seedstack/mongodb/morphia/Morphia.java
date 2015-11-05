/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.mongodb.morphia;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This qualifier marks the use of the Morphia persistence.
 *
 * @author redouane.loulou@ext.mpsa.com
 *         Date: 20/10/2015
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Morphia {
}
