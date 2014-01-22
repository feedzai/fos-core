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

import com.feedzai.fos.api.FOSException;
import com.feedzai.fos.api.Manager;
import com.feedzai.fos.api.ModelConfig;
import com.feedzai.fos.common.validation.NotBlank;
import com.feedzai.fos.common.validation.NotNull;
import com.feedzai.fos.server.remote.api.IRemoteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Remote manager that encapsulates an underlying @{Manager}.
 * <p/>
 * Encapsulates the underlying implementation exceptions in RemoteExceptions.
 *
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
public class RemoteManager implements IRemoteManager {
    private static final Logger logger = LoggerFactory.getLogger(RemoteManager.class);

    private Manager manager;
    private RemoteScorer remoteScorer;

    /**
     * Creates a new @{Manager} that delegates all calls to the underlying @{Manager}.
     *
     * @param manager the underlying manager
     * @throws Exception when retrieving the @{Scorer} from the underlying implementation
     */
    public RemoteManager(Manager manager) throws Exception {
        this.manager = manager;
        this.remoteScorer = new RemoteScorer(manager.getScorer());
    }


    @Override
    public UUID addModel(ModelConfig config, byte[] model) throws RemoteException, FOSException {
        return this.manager.addModel(config, model);
    }

    @Override
    public UUID addModel(ModelConfig config, @NotBlank String localFileName) throws RemoteException, FOSException {
        return this.manager.addModel(config, localFileName);
    }

    @Override
    public void removeModel(UUID modelId) throws RemoteException, FOSException {
        this.manager.removeModel(modelId);
    }

    @Override
    public void reconfigureModel(UUID modelId, ModelConfig config) throws RemoteException, FOSException {
        this.manager.reconfigureModel(modelId, config);
    }

    @Override
    public void reconfigureModel(UUID modelId, ModelConfig config, byte[] model) throws RemoteException, FOSException {
        this.manager.reconfigureModel(modelId, config, model);
    }

    @Override
    public void reconfigureModel(UUID modelId, ModelConfig config, String localFileName) throws RemoteException, FOSException {
        this.manager.reconfigureModel(modelId, config, localFileName);
    }

    @Override
    @NotNull
    public Map<UUID, ? extends ModelConfig> listModels() throws RemoteException, FOSException {
        return this.manager.listModels();

    }

    @Override
    @NotNull
    public RemoteScorer getScorer() throws RemoteException {
        return this.remoteScorer;
    }

    @Override
    public void close() throws RemoteException, FOSException {
        this.manager.close();

    }

    @Override
    public UUID trainAndAdd(ModelConfig config, List<Object[]> instances) throws RemoteException, FOSException {
        return this.manager.trainAndAdd(config, instances);

    }

    @Override
    public UUID trainAndAddFile(ModelConfig config, String path) throws RemoteException, FOSException {
        return this.manager.trainAndAddFile(config, path);
    }

    @Override
    public byte[] train(ModelConfig config, List<Object[]> instances) throws RemoteException, FOSException {
        return manager.train(config, instances);
    }

    @Override
    public byte[] trainFile(ModelConfig config, String path) throws RemoteException, FOSException {
        return manager.trainFile(config, path);
    }

    @Override
    public void save(UUID uuid, String savepath) throws RemoteException, FOSException {
        this.manager.save(uuid, savepath);
    }
}
