/*
 * $#
 * FOS Server
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
package com.feedzai.fos.server;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.feedzai.fos.api.config.FosConfig;
import com.feedzai.fos.server.config.StartupConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.registry.LocateRegistry;

/**
 * Main class for launching the classification server.
 * See @{link StartupConfiguration} for command line switches.
 *
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
public class Runner {
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    /**
     * Launches the server.
     * See @{link StartupConfiguration} for command line switches.
     * <p/> Will start an embedded RMI registry if "-s" was specified.
     *
     * @param args the command line switches
     * @throws ConfigurationException
     */
    public static void main(String... args) throws ConfigurationException {
        long time = System.currentTimeMillis();
        StartupConfiguration parameters = new StartupConfiguration();
        JCommander jCommander = new JCommander(parameters);

        FosServer fosServer;

        try {
            jCommander.parse(args);
            logger.info("Starting fos server using configuration from {}", parameters.getConfiguration());


            FosConfig serverConfig = new FosConfig(new PropertiesConfiguration(parameters.getConfiguration()));

            if (serverConfig.isEmbeddedRegistry()) {
                LocateRegistry.createRegistry(serverConfig.getRegistryPort());
                logger.debug("RMI registry started in port {}", serverConfig.getRegistryPort());
            }

            fosServer = new FosServer(serverConfig);
            fosServer.bind();
            logger.info("FOS Server started in {}ms", (System.currentTimeMillis() - time));
        } catch (ParameterException e) {
            jCommander.usage();
        } catch (Exception e) {
            logger.error("Could not launch RMI service", e);
        }
    }
}
