/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.mongodb.morphia.internal;

import org.seedstack.seed.ErrorCode;

/**
 * @author redouane.loulou@ext.mpsa.com
 */
public enum MorphiaErrorCodes implements ErrorCode {
    UNKNOW_DATASTORE_CONFIGURATION,
    UNKNOW_DATASTORE_CLIENT,
    UNKNOW_DATASTORE_DATABASE,
    UNKNOW_DATABASE_NAME,
    ERROR_ASYNC_CLIENT
}
