/*
 * $#
 * FOS Dummy
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
package com.feedzai.fos.impl.dummy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedzai.fos.api.FOSException;
import com.feedzai.fos.api.Scorer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Dummy implementation of Scorer.
 * <p/> Will return empty <code>Collections</code> or <code>0</code>(<code>integer</code>).
 * <p/> Logs every call (and parameters) with trace.
 *
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
public class DummyScorer implements Scorer {
    private final static Logger logger = LoggerFactory.getLogger(DummyScorer.class);
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<double[]> score(List<UUID> modelIds, Object[] scorable) {
        try {
            logger.trace("modelIds='{}', scorable='{}'", mapper.writeValueAsString(modelIds), mapper.writeValueAsString(scorable));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<double[]> score(UUID modelId, List<Object[]> scorables) {
        try {
            logger.trace("modelId='{}', scorable='{}'",  mapper.writeValueAsString(modelId),  mapper.writeValueAsString(scorables));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public double[] score(UUID modelId, Object[] scorable) {
        try {
            logger.trace("modelId='{}', scorable='{}'",  mapper.writeValueAsString(modelId),  mapper.writeValueAsString(scorable));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return new double[0];
    }

    @Override
    public void close() throws FOSException {
    }
}
