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
package com.feedzai.fos.server.remote.api;

import com.feedzai.fos.api.FOSException;
import com.feedzai.fos.api.Scorer;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * FOS Scorer local adapter.
 *
 * @author Miguel Duarte (miguel.duarte@feedzai.com)
 * @since 13.1.0
 */
public class FOSScorerAdapter implements Scorer {

    private final RemoteScorer scorer;

    public FOSScorerAdapter(RemoteScorer scorer) {
        this.scorer = scorer;
    }

    @Override
    public List<double[]> score(List<UUID> uuids, Object[] objects) throws FOSException {
        try {
            return scorer.score(uuids, objects);
        } catch (RemoteException e) {
            throw new FOSException(e);
        }
    }

    @Override
    public Map<UUID, double[]> score(Map<UUID, Object[]> uuidMap) throws FOSException {
        try {
            return scorer.score(uuidMap);
        } catch (RemoteException e) {
            throw new FOSException(e);
        }
    }

    @Override
    public List<double[]> score(UUID uuid, List<Object[]> objects) throws FOSException {
        try {
            return scorer.score(uuid, objects);
        } catch (RemoteException e) {
            throw new FOSException(e);
        }
    }

    @Override
    public void close() throws FOSException {
        try {
            scorer.close();
        } catch (RemoteException e) {
            throw new FOSException(e);
        }
    }
}
