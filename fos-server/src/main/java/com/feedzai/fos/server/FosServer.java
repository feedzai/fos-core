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

import com.feedzai.fos.api.config.FosConfig;
import com.feedzai.fos.server.remote.api.IRemoteManager;
import com.feedzai.fos.server.remote.api.RemoteScorer;
import com.feedzai.fos.server.remote.impl.RemoteManager;
import com.feedzai.fos.server.remote.impl.RemoteManagerFactory;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Server that registers the classification implementation in the RMI registry.
 *
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
public class FosServer {
    private final static Logger logger = LoggerFactory.getLogger(FosServer.class);

    private FosConfig parameters;
    private final RemoteManager remoteManager;
    private Registry registry;

    /**
     * Creates a new server with the given parameters.
     * <p/> Creates a new @{RemoteManager} from the configuration file defined in the parameters.
     *
     * @param parameters a list of parameters
     * @throws ConfigurationException when the configuration file specified in the parameters cannot be open/read.
     */
    public FosServer(FosConfig parameters) throws ConfigurationException {
        this.parameters = parameters;
        this.remoteManager = new RemoteManagerFactory().createManager(parameters);
    }

    /**
     * Binds the Manager and Scorer to the RMI Registry.
     * <p/> Also registers a shutdown hook for closing and removing the items from the registry.
     *
     * @throws RemoteException when binding was not possible.
     */
    public void bind() throws RemoteException {
        registry = LocateRegistry.getRegistry(parameters.getRegistryPort());
        registry.rebind(IRemoteManager.class.getSimpleName(), UnicastRemoteObject.exportObject(remoteManager, parameters.getRegistryPort()));
        registry.rebind(RemoteScorer.class.getSimpleName(), UnicastRemoteObject.exportObject(remoteManager.getScorer(), parameters.getRegistryPort()));

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                unbind();
            }
        });
    }

    /**
     * Unbind and close the @{Manager} and @{Scorer].
     */
    public void unbind() {
        logger.info("Shutting down fos server");

        try {
            registry.unbind(IRemoteManager.class.getSimpleName());
        } catch (Exception e) {
            logger.trace("Could not unbind", e);
        }

        try {
            registry.unbind(RemoteScorer.class.getSimpleName());
        } catch (Exception e) {
            logger.trace("Could not unbind", e);
        }

        try {
            remoteManager.getScorer().close();
        } catch (Exception e) {
            logger.trace("Could not close", e);
        }

        try {
            remoteManager.close();
        } catch (Exception e) {
            logger.trace("Could not close", e);
        }
    }
}
