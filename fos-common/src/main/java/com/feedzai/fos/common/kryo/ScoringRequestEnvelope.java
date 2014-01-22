/*
 * $#
 * FOS Common
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
package com.feedzai.fos.common.kryo;

import java.util.List;
import java.util.UUID;

/**
 * This class should be used to wrap scoring requests
 * along with @{link KryoScorer}
 *
 * @author  Miguel Duarte (miguel.duarte@feedzai.com)
 */
public class ScoringRequestEnvelope {
    /**
     * List of classifier uuid to score
     */
    List<UUID> uuids;

    /**
     * Instance to score
     */
    Object[] instance;

    /**
     * Empty constructor to allow kryo to create new instances
     */
    public ScoringRequestEnvelope() {
    }

    /**
     * Sets the list of UUIDs to score
     * @param uuids list of uuids to score
     */
    public void setUuids(List<UUID> uuids) {
        this.uuids = uuids;
    }

    /**
     * Sets the instance to score
     * @param instance instance to score
     */
    public void setInstance(Object[] instance) {
        this.instance = instance;
    }

    /**
     * Creates a new scoring envelope
     *
     * @param uuids   List of classifier uuid to score
     * @param instance instance to score
     */
    public ScoringRequestEnvelope(List<UUID> uuids,
                                  Object[] instance) {
        this.uuids = uuids;
        this.instance = instance;
    }

    /**
     * Returns the list of UUID to score
     * @return list of UUID to score
     */
    public List<UUID> getUUIDs() {
        return uuids;
    }

    /**
     * Returns the instance to score
     * @return the instance to score
     */
    public Object[] getInstance() {
        return instance;
    }
}

