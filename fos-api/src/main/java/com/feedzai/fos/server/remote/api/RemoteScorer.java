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

import com.feedzai.fos.common.validation.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * New interface for @{Scorer} that also extends Remote (required for sharing with RMI clients).
 *
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
public interface RemoteScorer extends Remote {
    /**
     * Score the <code>scorable</code> against the given <core>modelIds</core>.
     * <p/> The score must be between 0 and 999.
     * <p/> The resulting scores are returned by the same order as the <code>modelIds</code> (modelsIds(pos) » return(pos)).
     *
     * @param modelIds the list of models to score
     * @param scorable the instance data to score
     * @return a list of scores (int between 0 and 999)
     * @throws RemoteException when scoring was not possible
     */
    @NotNull
    List<double[]> score(List<UUID> modelIds,Object[] scorable) throws RemoteException;

    /**
     * Score all <code>scorables agains</code> the given <code>modelId</code>.
     * <p/> The score must be between 0 and 999.
     * <p/> The resulting scores are returned in the same order as the <code>scorables</code> (scorables(pos) » return(pos)).
     *
     * @param modelId   the id of the model
     * @param scorables an array of instances to score
     * @return a map of scores where the key is the <code>modelId</code> (int between 0 and 999)
     * @throws RemoteException when scoring was not possible
     */
    @NotNull
    List<double[]> score(UUID modelId,List<Object[]> scorables) throws RemoteException;

    /**
     * Score a single <code>scorable</code> against the given <code>modelId</code>.
     *
     * <p/> The score must be between 0 and 999.
     *
     * @param modelId   the id of the model
     * @param scorable  the instance data to score
     * @return the scores
     * @throws RemoteException when scoring was not possible
     */
    @NotNull
    double[] score(UUID modelId, Object[] scorable) throws RemoteException;

    /**
     * Frees any resources allocated to this scorer.
     *
     * @throws RemoteException when closing resources was not possible
     */
    void close() throws RemoteException;

}
