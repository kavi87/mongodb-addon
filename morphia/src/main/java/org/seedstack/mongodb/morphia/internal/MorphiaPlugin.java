/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.mongodb.morphia.internal;

import com.google.common.collect.Lists;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.core.AbstractPlugin;
import org.kametic.specifications.Specification;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.seedstack.mongodb.morphia.MorphiaDatastore;
import org.seedstack.seed.Application;
import org.seedstack.seed.core.internal.application.ApplicationPlugin;
import org.seedstack.seed.core.utils.BaseClassSpecifications;
import org.seedstack.seed.core.utils.SeedReflectionUtils;
import org.seedstack.validation.internal.ValidationPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static org.seedstack.seed.core.utils.BaseClassSpecifications.classIsAbstract;

/**
 * This plugin manages the MongoDb Morphia object/document mapping library.
 */
public class MorphiaPlugin extends AbstractPlugin {
    private static final Logger logger = LoggerFactory.getLogger(MorphiaPlugin.class);

    private final Specification<Class<?>> MORPHIA_MAPPED_CLASSES_SPECS = morphiaSpecification();
    private final Collection<MorphiaDatastore> morphiaDatastores = new HashSet<MorphiaDatastore>();
    private final Morphia morphia = new Morphia();

    @Override
    public String name() {
        return "morphia";
    }

    @Override
    public Collection<Class<?>> requiredPlugins() {
        ArrayList<Class<?>> requiredPlugins = Lists.<Class<?>>newArrayList(ApplicationPlugin.class, MongoDbPlugin.class);
        if (isValidationPluginPresent()) {
            requiredPlugins.add(ValidationPlugin.class);
        }
        return requiredPlugins;
    }

    private boolean isValidationPluginPresent() {
        return SeedReflectionUtils.isClassPresent("org.seedstack.validation.internal.ValidationPlugin");
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {
        return classpathScanRequestBuilder().specification(MORPHIA_MAPPED_CLASSES_SPECS).build();
    }

    @Override
    public InitState init(InitContext initContext) {
        Application application = initContext.dependency(ApplicationPlugin.class).getApplication();

        if (isValidationPluginPresent()) {
            ValidatorFactory validatorFactory = initContext.dependency(ValidationPlugin.class).getValidatorFactory();
            if (validatorFactory != null) {
                new InternalValidationExtension(validatorFactory, morphia);
                logger.debug("Validation is enabled on Morphia entities");
            }
        }

        if (MORPHIA_MAPPED_CLASSES_SPECS != null) {
            Collection<Class<?>> morphiaScannedClasses = initContext.scannedTypesBySpecification().get(MORPHIA_MAPPED_CLASSES_SPECS);

            if (morphiaScannedClasses != null && !morphiaScannedClasses.isEmpty()) {
                morphia.map(new HashSet<Class>(morphiaScannedClasses));
                for (Class<?> morphiaClass : morphiaScannedClasses) {
                    MorphiaDatastore morphiaDatastore = MorphiaUtils.getMongoDatastore(application, morphiaClass);
                    if (!morphiaDatastores.contains(morphiaDatastore)) {
                        morphiaDatastores.add(morphiaDatastore);
                    }
                }
            }
        }
        return InitState.INITIALIZED;
    }

    @Override
    public Object nativeUnitModule() {
        return new MorphiaModule(morphiaDatastores, morphia);
    }

    @SuppressWarnings("unchecked")
    private Specification<Class<?>> morphiaSpecification() {
        Specification<Class<?>> specification;
        specification = BaseClassSpecifications.and(
                BaseClassSpecifications.or(BaseClassSpecifications.classAnnotatedWith(Entity.class),
                        BaseClassSpecifications.classAnnotatedWith(Embedded.class)),
                BaseClassSpecifications.not(classIsAbstract()));

        return specification;
    }
}