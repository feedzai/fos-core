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

import com.feedzai.fos.server.remote.api.IRemoteManager;
import com.feedzai.fos.server.remote.api.RemoteScorer;
import org.junit.Test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
public class RunnerTest {
    /**
     * Epsilon for comparing doubles.
     */
    private final double EPS = 0.001;

    @Test
    public void mainTest() throws Exception {
        Runner.main("-c", "target/test-classes/ClassificationServerParameters.properties");

        UUID dummy = UUID.nameUUIDFromBytes(new byte[0]);

        Registry registry = LocateRegistry.getRegistry();

        IRemoteManager manager = (IRemoteManager) registry.lookup(IRemoteManager.class.getSimpleName());

        RemoteScorer scorer = (RemoteScorer) registry.lookup(RemoteScorer.class.getSimpleName());
        assertArrayEquals(manager.getScorer().score(dummy, new Object[0]), scorer.score(dummy, new Object[0]), EPS);
    }
}
