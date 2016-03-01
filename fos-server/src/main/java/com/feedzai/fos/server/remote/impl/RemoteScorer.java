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
import com.feedzai.fos.common.validation.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

/**
 * Remote scorer that encapsulates an underlying @{Scorer}.
 * <p/>
 * Encapsulates the underlying implementation exceptions in RemoteExceptions.
 *
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
public class RemoteScorer implements com.feedzai.fos.server.remote.api.RemoteScorer {
    private static Logger logger = LoggerFactory.getLogger(RemoteScorer.class);

    private Scorer scorer;

    /**
     * Creates a new @{Scorer} that delegates all calls to the underlying @{Scorer}.
     *
     * @param scorer the underlying scorer
     */
    public RemoteScorer(Scorer scorer) {
        this.scorer = scorer;
    }

    @Override
    @NotNull
    public List<double[]> score(List<UUID> modelIds,Object[] scorable) throws RemoteException {
        try {
            return this.scorer.score(modelIds, scorable);
        } catch (Exception e) {
            logger.error("Caught exception from underlying implementation",e);
            throw new RemoteException("Translated in RMI layer", e);
        }
    }

    @Override
    @NotNull
    public List<double[]> score(UUID modelId,List<Object[]> scorables) throws RemoteException {
        try {
            return this.scorer.score(modelId, scorables);
        } catch (Exception e) {
            logger.error("Caught exception from underlying implementation",e);
            throw new RemoteException("Translated in RMI layer", e);
        }
    }

    @Override
    @NotNull
    public double[] score(UUID modelId, Object[] scorable) throws RemoteException {
        try {
            return this.scorer.score(modelId, scorable);
        } catch (Exception e) {
            logger.error("Caught exception from underlying implementation",e);
            throw new RemoteException("Translated in RMI layer", e);
        }
    }


    @Override
    public void close() throws RemoteException {
        try {
            this.scorer.close();
        } catch (Exception e) {
            logger.error("Caught exception from underlying implementation",e);
            throw new RemoteException("Translated in RMI layer", e);
        }
    }
}
