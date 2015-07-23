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

import com.feedzai.fos.api.Scorer;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
@RunWith(PowerMockRunner.class)
public class RemoteClassifierScorerTest {
    @Mock
    private Scorer innerScorer;


    @Test
    public void testDelegate() throws Exception {
        UUID dummy = UUID.nameUUIDFromBytes(new byte[0]);

        EasyMock.expect(innerScorer.score(dummy, new ArrayList<>())).andReturn(null).once();
        EasyMock.expect(innerScorer.score(dummy, new Object[0])).andReturn(null).once();

        PowerMock.replay(innerScorer);

        RemoteScorer remote = new RemoteScorer(innerScorer);
        remote.score(dummy, new ArrayList<>());
        remote.score(dummy, new Object[0]);

        PowerMock.verifyAll();
    }
}
