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
package com.feedzai.fos.server.remote.impl;

import com.feedzai.fos.api.ManagerFactory;
import com.feedzai.fos.api.config.FosConfig;
import com.feedzai.fos.common.validation.NotNull;

/**
 * Factory for creating remote classifiers (then implement <code>Remote</code>).
 *
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
public class RemoteManagerFactory {

    /**
     * Creates a new RemoteManager.
     * <p/> Must be able to delegate the actual execution to an underlying implementation.
     * <p/> The configuration must specify a parameter with the factory class of the underlying configuration:
     * <ul>
     * <li>{@link FosConfig#FACTORY_NAME} the classname of the underlying {@link ManagerFactory} implementation</li>
     * </ul>
     * The <code>configuration</code> must also contain all required parameters for the underlying implementation.
     *
     * @param configuration the configuration parameters specific for an implementation
     * @return a <code>RemoteManager</code> that extends remote
     */
    @NotNull
    public RemoteManager createManager(FosConfig configuration) {
        ManagerFactory factory;
        try {
            factory = ManagerFactory.class.cast(Class.forName(configuration.getFactoryName()).newInstance());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Could not instantiate factory " + configuration.getFactoryName(), e);
        }

        try {
            return new RemoteManager(factory.createManager(configuration));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
