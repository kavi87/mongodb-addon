/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.mongodb.morphia.internal;

import org.apache.commons.configuration.Configuration;
import org.seedstack.mongodb.morphia.MorphiaDatastore;
import org.seedstack.seed.Application;
import org.seedstack.seed.SeedException;

import java.util.Arrays;

public final class MorphiaUtils {
    private MorphiaUtils() {

    }

    /**
     * Returns an instance of the annotation MorphiaDatastore if the morphia configuration is ok.
     *
     * @param application  Application
     * @param morphiaClass persistent morphia object
     * @return MorphiaDatastore
     */
    public static MorphiaDatastore getMongoDatastore(Application application, Class<?> morphiaClass) {
        Configuration morphiaEntityConfiguration = application.getConfiguration(morphiaClass).subset("morphia");
        if (morphiaEntityConfiguration.isEmpty()) {
            throw SeedException.createNew(MorphiaErrorCodes.UNKNOW_DATASTORE_CONFIGURATION).put("aggregate",
                    morphiaClass.getName());
        }
        String clientName = morphiaEntityConfiguration.getString("clientName");
        String dbName = morphiaEntityConfiguration.getString("dbName");
        if (clientName == null) {
            throw SeedException.createNew(MorphiaErrorCodes.UNKNOW_DATASTORE_CLIENT)
                    .put("aggregate", morphiaClass.getName()).put("clientName", clientName);
        }
        if (dbName == null) {
            throw SeedException.createNew(MorphiaErrorCodes.UNKNOW_DATASTORE_DATABASE)
                    .put("aggregate", morphiaClass.getName()).put("clientName", clientName).put("dbName", dbName);
        }
        checkMongoClient(application.getConfiguration(), morphiaClass, clientName, dbName);
        MorphiaDatastore morphiaDatastore = new MorphiaDatastoreImpl(clientName, dbName);
        return morphiaDatastore;
    }

    private static void checkMongoClient(Configuration configuration, Class<?> mappedClass, String clientName, String dbName) {
        Configuration configurationClientMongodb = configuration.subset(MongoDbPlugin.CONFIGURATION_PREFIX + ".client." + clientName);
        if (configurationClientMongodb.isEmpty()) {
            throw SeedException.createNew(MongoDbErrorCodes.UNKNOWN_CLIENT_SPECIFIED)
                    .put("aggregate", mappedClass.getName()).put("clientName", clientName).put("dbName", dbName);
        }
        boolean async = configurationClientMongodb.getBoolean("async", false);
        if (async) {
            throw SeedException.createNew(MorphiaErrorCodes.ERROR_ASYNC_CLIENT)
                    .put("aggregate", mappedClass.getName()).put("clientName", clientName).put("dbName", dbName);
        }
        String[] dbNames = configurationClientMongodb.getStringArray("databases");
        if (dbNames != null && dbNames.length > 0 && !Arrays.asList(dbNames).contains(dbName)) {
            throw SeedException.createNew(MorphiaErrorCodes.UNKNOW_DATABASE_NAME)
                    .put("aggregate", mappedClass.getName()).put("clientName", clientName).put("dbName", dbName);
        }
    }
}
