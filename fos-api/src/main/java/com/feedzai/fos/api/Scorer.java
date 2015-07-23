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
package com.feedzai.fos.api;

import com.feedzai.fos.common.validation.NotNull;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.UUID;

/**
 * This class represents a scorer that is capable of scoring instances against a set of models.
 * <p/>
 * Each scorer can hold multiple models and MUST support multi-threaded usage.
 *
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
public interface Scorer  {
    /**
     * Score the <code>scorable</code> against the given <code>modelIds</code>.
     * <p/> The score must be between 0 and 1.
     * <p/> The resulting scores are returned by the same order as the <code>modelIds</code> (modelsIds(pos) » return(pos)).
     *
     * @param modelIds the list of models to score
     * @param scorable the instance data to score
     * @return a list of scores double[] where each list position contains the score for each classifier
     * @throws FOSException when scoring was not possible
     */
    @NotNull
    default List<double[]> score(List<UUID> modelIds, Object[] scorable) throws FOSException {
        ImmutableList.Builder<double[]> resultsBuilder = ImmutableList.builder();

        for (UUID modelId : modelIds) {
            resultsBuilder.add(score(modelId, scorable));
        }

        return resultsBuilder.build();
    }

    /**
     * Score all <code>scorables against</code> the given <code>modelId</code>.
     * <p/> The score must be between 0 and 1.
     * <p/> The resulting scores are returned in the same order as the <code>scorables</code> (scorables(pos) » return(pos)).
     *
     * @param modelId   the id of the model
     * @param scorables an array of instances to score
     * @return a list of scores double[] where each list position contains the score for each <code>scorable</code>.
     * @throws FOSException when scoring was not possible
     */
    @NotNull
    default List<double[]> score(UUID modelId, List<Object[]> scorables) throws FOSException {
        ImmutableList.Builder<double[]> resultsBuilder = ImmutableList.builder();

        for (Object[] scorable : scorables) {
            resultsBuilder.add(score(modelId, scorable));
        }

        return resultsBuilder.build();
    }

    /**
     * Score a single <code>scorable</code> against the given <code>modelId</code>.
     *
     * <p/> The score must be between 0 and 1.
     *
     * @param modelId   the id of the model
     * @param scorable  the instance to score
     * @return the scores
     * @throws FOSException when scoring was not possible
     */
    @NotNull
    double[] score(UUID modelId, Object[] scorable) throws FOSException;

    /**
     * Frees any resources allocated to this scorer.
     *
     * @throws FOSException when closing resources was not possible
     */
    void close() throws FOSException;
}
