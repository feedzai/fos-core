/*
 * $#
 * FOS API
 *  
 * Copyright (C) 2013 Feedzai SA
 *  
 * This software is licensed under the Apache License, Version 2.0 (the "Apache License") or the GNU
 * Lesser General Public License version 3 (the "GPL License"). You may choose either license to govern
 * your use of this software only upon the condition that you accept all of the terms of either the Apache
 * License or the LGPL License.
 * 
 * You may obtain a copy of the Apache License and the LGPL License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Apache License
 * or the LGPL License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Apache License and the LGPL License for the specific language governing
 * permissions and limitations under the Apache License and the LGPL License.
 * #$
 */
package com.feedzai.fos.api.config;

import com.google.common.base.Objects;
import org.apache.commons.configuration.Configuration;

import java.io.File;
import java.rmi.registry.Registry;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Base configuration class for FOS server.
 * <p/>
 * Contains the basic configuration for FOS and the {@link Configuration} object containing all the non typed configuration provided in the configuration file.
 *
 * @author Diogo Guerra (diogo.guerra@feedzai.com)
 * @since 0.7
 */
public class FosConfig {
    /**
     * The config fqn for factory name.
     */
    public static final String FACTORY_NAME = "fos.factoryName";
    /**
     * The config fqn for registry port.
     */
    public static final String REGISTRY_PORT = "fos.registryPort";
    /**
     * The config fqn for scoring port.
     */
    public static final String SCORING_PORT = "fos.scoringPort";
    /**
     * The config fqn for embedded registry.
     */
    public static final String EMBEDDED_REGISTRY = "fos.embeddedRegistry";
    /**
     * The config fqn for the headers.
     */
    public static final String HEADER_LOCATION = "fos.headerLocation";
    /**
     * The config fqn for the size of the thread pool classifier.
     */
    public static final String THREADPOOL_SIZE = "fos.threadPoolSize";

    public static final int DEFAULT_SCORING_PORT = 2534;

    /**
     * The configuration object that contains all the configured properties in a key value format.
     */
    private Configuration config;

    /**
     * Defines if the current instance should start a RMI registry or connects to an existing one.
     */
    private boolean embeddedRegistry;
    /**
     * RMI registry port (for lookup or for bind if embeddedRegistry is true)
     */
    private int registryPort;
    /**
     * The factory fqn of the {@link com.feedzai.fos.api.ManagerFactory ManagerFactory} to use.
     */
    private String factoryName;
    /**
     * The location of the model headers.
     */
    private String headerLocation;
    /**
     * The size of the thread pool classifier.
     */
    private int threadPoolSize;
    /**
     * The port to use for fast scoring.
     */
    private int scoringPort;

    /**
     * Creates a new instance of the {@link FosConfig} class.
     *
     * @param configuration The base configuration to use.
     */
    public FosConfig(Configuration configuration) {
        checkNotNull(configuration, "Configuration cannot be null.");
        checkArgument(configuration.containsKey(FACTORY_NAME), "The configuration parameter " + FACTORY_NAME + " should be defined.");
        this.config = configuration;
        this.embeddedRegistry = configuration.getBoolean(EMBEDDED_REGISTRY, false);
        this.registryPort = configuration.getInt(REGISTRY_PORT, Registry.REGISTRY_PORT);
        this.factoryName = configuration.getString(FACTORY_NAME);
        this.headerLocation = configuration.getString(HEADER_LOCATION, "models");
        this.threadPoolSize = configuration.getInt(THREADPOOL_SIZE, 20);
        this.scoringPort = configuration.getInt(SCORING_PORT, DEFAULT_SCORING_PORT);
    }

    /**
     * Gets the configuration object that contains all the configured properties in a key value format.
     *
     * @return The configuration object that contains all the configured properties in a key value format.
     */
    public Configuration getConfig() {
        return config;
    }

    /**
     * Gets if the current instance should start a RMI registry or connects to an existing one.
     *
     * @return {@code true} if the current instance should start a RMI registry, {@code false} to connect to an existing one.
     */
    public boolean isEmbeddedRegistry() {
        return embeddedRegistry;
    }

    /**
     * Gets the RMI registry port (for lookup or for bind if embeddedRegistry is true)
     *
     * @return The RMI registry port (for lookup or for bind if embeddedRegistry is true)
     */
    public int getRegistryPort() {
        return registryPort;
    }

    /**
     * Gets the factory fqn of the {@link com.feedzai.fos.api.ManagerFactory ManagerFactory} to use.
     *
     * @return The factory fqn of the {@link com.feedzai.fos.api.ManagerFactory ManagerFactory} to use.
     */
    public String getFactoryName() {
        return factoryName;
    }

    /**
     * Gets the location of the model headers.
     *
     * @return The location of the model headers.
     */
    public File getHeaderLocation() {
        return new File(headerLocation);
    }

    /**
     * Gets the size of the thread pool classifier.
     *
     * @return The size of the thread pool classifier.
     */
    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    /**
     * Gets the port to use for fast scoring.
     *
     * @return The port to use for fast scoring.
     */
    public int getScoringPort() {
        return scoringPort;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("config", config).
                add("embeddedRegistry", embeddedRegistry).
                add("registryPort", registryPort).
                add("factoryName", factoryName).
                add("headerLocation", headerLocation).
                add("threadPoolSize", threadPoolSize).
                add("scoringPort", scoringPort).
                toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(config, embeddedRegistry, registryPort, factoryName, headerLocation);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FosConfig other = (FosConfig) obj;
        return Objects.equal(this.config, other.config) && Objects.equal(this.embeddedRegistry, other.embeddedRegistry) && Objects.equal(this.registryPort, other.registryPort) && Objects.equal(this.factoryName, other.factoryName) && Objects.equal(this.headerLocation, other.headerLocation);
    }
}
