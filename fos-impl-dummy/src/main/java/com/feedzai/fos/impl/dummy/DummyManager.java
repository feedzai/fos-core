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
import com.feedzai.fos.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Null implementation of Manager.
 * <p/> Will return empty <code>Collections</code> or <code>0</code>(<code>integer</code>).
 * <p/> Logs every call (and parameters) with trace.
 *
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
public class DummyManager implements Manager {
    private final static Logger logger = LoggerFactory.getLogger(DummyManager.class);
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public UUID trainAndAdd(ModelConfig config, List<Object[]> instances) {
        try {
            logger.trace("config='{}', instances='{}'", mapper.writeValueAsString(config), mapper.writeValueAsString(instances));
        } catch (IOException e) {
            logger.error(e.getMessage(), e); 
        }
        return UUID.nameUUIDFromBytes(new byte[0]);
    }

    @Override
    public UUID trainAndAddFile(ModelConfig config, String path) throws FOSException {
        return  UUID.nameUUIDFromBytes(new byte[0]);
    }

    @Override
    public Model train(ModelConfig config, List<Object[]> instances)  {
        try {
            logger.trace("config='{}', instances='{}'", mapper.writeValueAsString(config), mapper.writeValueAsString(instances));
        } catch (IOException e) {
            logger.error(e.getMessage(), e); 
        }
        return new ModelBinary(new byte[0]);
    }

    @Override
    public Model trainFile(ModelConfig config, String path) throws FOSException {
        return new ModelBinary(new byte[0]);
    }

    @Override
    public UUID addModel(ModelConfig config, Model model) {
        try {
            logger.trace("config='{}', model='{}'", mapper.writeValueAsString(config), mapper.writeValueAsString(model));
        } catch (IOException e) {
            logger.error(e.getMessage(), e); 
        }
        return UUID.nameUUIDFromBytes(new byte[0]);
    }

    @Override
    public UUID addModel(ModelConfig config, ModelDescriptor descriptor) {
        try {
            logger.trace("config='{}', model='{}'", mapper.writeValueAsString(config), mapper.writeValueAsString(descriptor.getModelFilePath()));
        } catch (IOException e) {
            logger.error(e.getMessage(), e); 
        }
        return UUID.nameUUIDFromBytes(new byte[0]);
    }

    @Override
    public void removeModel(UUID modelId) {
        logger.trace("model='{}'", modelId);
    }

    @Override
    public void reconfigureModel(UUID modelId, ModelConfig config) {
        try {
            logger.trace("config='{}', model='{}'", mapper.writeValueAsString(config), mapper.writeValueAsString(modelId));
        } catch (IOException e) {
            logger.error(e.getMessage(), e); 
        }
    }

    @Override
    public void reconfigureModel(UUID modelId, ModelConfig config, Model model) {
        try {
            logger.trace("config='{}', model='{}'", mapper.writeValueAsString(config), mapper.writeValueAsString(modelId));
        } catch (IOException e) {
            logger.error(e.getMessage(), e); 
        }
    }

    @Override
    public void reconfigureModel(UUID modelId, ModelConfig config, ModelDescriptor descriptor) {
        try {
            logger.trace("config='{}', model='{}'", mapper.writeValueAsString(config), mapper.writeValueAsString(modelId));
        } catch (IOException e) {
            logger.error(e.getMessage(), e); 
        }
    }

    @Override
    public Map<UUID, ModelConfig> listModels() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public DummyScorer getScorer() {
        return new DummyScorer();
    }

    @Override
    public void close() throws FOSException {
    }

    @Override
    public void save(UUID uuid, String savepath) throws FOSException {
    }

    @Override
    public void saveAsPMML(UUID uuid, String savePath, boolean compress) throws FOSException {

    }
}
