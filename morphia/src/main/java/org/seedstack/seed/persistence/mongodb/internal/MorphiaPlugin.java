/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.persistence.mongodb.internal;

import static org.seedstack.seed.core.utils.BaseClassSpecifications.classIsAbstract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.configuration.Configuration;
import org.kametic.specifications.Specification;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.seedstack.seed.core.api.Application;
import org.seedstack.seed.core.api.SeedException;
import org.seedstack.seed.core.internal.application.ApplicationPlugin;
import org.seedstack.seed.core.utils.BaseClassSpecifications;
import org.seedstack.seed.persistence.mongodb.api.MongoDbErrorCodes;
import org.seedstack.seed.persistence.mongodb.api.MorphiaDatastore;
import org.seedstack.seed.persistence.mongodb.api.MorphiaErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.PluginException;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.core.AbstractPlugin;

/**
 * 
 * @author redouane.loulou@ext.mpsa.com
 *
 */
public class MorphiaPlugin extends AbstractPlugin {
	public static final String MONGO_PLUGIN_CONFIGURATION_PREFIX = "org.seedstack.seed.persistence.mongodb";

	private static final Logger LOGGER = LoggerFactory.getLogger(MorphiaPlugin.class);

	private static final Specification<Class<?>> MORPHIA_MAPPED_CLASSES_SPECS = morphiaSpecification();

	private Collection<Class<?>> morphiaScannedClasses;

	private Collection<MorphiaDatastore> morphiaDatastores = new HashSet<MorphiaDatastore>();
	
	private final Morphia morphia = new Morphia();


	@Override
	public String name() {
		return "seed-persistence-morphia-plugin";
	}

	@Override
	public InitState init(InitContext initContext) {
		Application application = null;
		for (Plugin plugin : initContext.pluginsRequired()) {
			if (plugin instanceof ApplicationPlugin) {
				application = ((ApplicationPlugin) plugin).getApplication();
			}
		}

		if (application == null) {
			throw new PluginException("Unable to find application plugin");
		}
		
        if(MORPHIA_MAPPED_CLASSES_SPECS!=null){        	
        	morphiaScannedClasses = initContext.scannedTypesBySpecification().get(MORPHIA_MAPPED_CLASSES_SPECS);
        	
        	if(morphiaScannedClasses != null && !morphiaScannedClasses.isEmpty()){
        		morphia.map(new HashSet<Class>(morphiaScannedClasses));
        		for (Class<?> morphiaClass : morphiaScannedClasses) {
        			MorphiaDatastore morphiaDatastore = getMongoDatastore(application, morphiaClass);
    				if(!morphiaDatastores.contains(morphiaDatastore)){
    					morphiaDatastores.add(morphiaDatastore);
    				}
        		}
        	}
        }
		return InitState.INITIALIZED;
	}

	/**
	 * if the morphia configuration is ok, Return an instance of the annatation MorphiaDatastore
	 * @param application Application
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
		checkMongoClient(application, morphiaClass, clientName, dbName);
		MorphiaDatastore morphiaDatastore = new MorphiaDatastoreImpl(clientName, dbName);
		return morphiaDatastore;
	}

	 private static void checkMongoClient(Application application, Class<?> mappedClass, String clientName, String dbName) {
		Configuration configurationClientMongodb = application.getConfiguration().subset(MongoDbPlugin.MONGO_PLUGIN_CONFIGURATION_PREFIX + ".client." + clientName);
		if (configurationClientMongodb.isEmpty()) {
			throw SeedException.createNew(MongoDbErrorCodes.UNKNOWN_CLIENT_SPECIFIED)
					.put("aggregate", mappedClass.getName()).put("clientName", clientName).put("dbName", dbName);
		} 
		boolean async = configurationClientMongodb.getBoolean("async", false);
		if(async){
			throw SeedException.createNew(MorphiaErrorCodes.ERROR_ASYNC_CLIENT)
			.put("aggregate", mappedClass.getName()).put("clientName", clientName).put("dbName", dbName);
		}
		String[] dbNames = configurationClientMongodb.getStringArray("databases");
		if (dbNames != null && dbNames.length>0 &&  !Arrays.asList(dbNames).contains(dbName)) {
			throw SeedException.createNew(MorphiaErrorCodes.UNKNOW_DATABASE_NAME)
					.put("aggregate", mappedClass.getName()).put("clientName", clientName).put("dbName", dbName);
		}
	}
	
	@Override
	public Collection<Class<? extends Plugin>> requiredPlugins() {
		Collection<Class<? extends Plugin>> plugins = new ArrayList<Class<? extends Plugin>>();
		plugins.add(ApplicationPlugin.class);
		plugins.add(MongoDbPlugin.class);
		return plugins;
	}

	@Override
	public Collection<ClasspathScanRequest> classpathScanRequests() {
		return classpathScanRequestBuilder().specification(MORPHIA_MAPPED_CLASSES_SPECS).build();
	}

	@SuppressWarnings("unchecked")
	private static Specification<Class<?>> morphiaSpecification() {
		Specification<Class<?>> specification = null;
		specification = BaseClassSpecifications.and(
				BaseClassSpecifications.or(BaseClassSpecifications.classAnnotatedWith(Entity.class),
						BaseClassSpecifications.classAnnotatedWith(Embedded.class)),
				BaseClassSpecifications.not(classIsAbstract()));

		return specification;
	}
	
	@Override
	public Object nativeUnitModule() {
		return new MorphiaModule(morphiaDatastores, morphia);
	}

	
}