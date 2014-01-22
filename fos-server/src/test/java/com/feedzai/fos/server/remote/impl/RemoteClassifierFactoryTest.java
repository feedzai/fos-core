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

import com.feedzai.fos.api.config.FosConfig;
import com.feedzai.fos.impl.dummy.DummyManager;
import com.feedzai.fos.impl.dummy.DummyManagerFactory;
import com.feedzai.fos.impl.dummy.DummyScorer;
import junit.framework.Assert;
import org.apache.commons.configuration.BaseConfiguration;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

/**
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
public class RemoteClassifierFactoryTest {
    @Test
    public void testCreation() throws Exception {
        BaseConfiguration configuration = new BaseConfiguration();
        configuration.setProperty(FosConfig.FACTORY_NAME, DummyManagerFactory.class.getName());
        configuration.setProperty(FosConfig.EMBEDDED_REGISTRY, true);
        configuration.setProperty(FosConfig.REGISTRY_PORT, 5959);
        final FosConfig fosConfig = new FosConfig(configuration);
        RemoteManagerFactory remoteClassifierFactory = new RemoteManagerFactory();

        Assert.assertTrue(Whitebox.getInternalState(remoteClassifierFactory.createManager(fosConfig),"manager") instanceof DummyManager);
        Assert.assertTrue(Whitebox.getInternalState(remoteClassifierFactory.createManager(fosConfig).getScorer(),"scorer") instanceof DummyScorer);
    }
}
