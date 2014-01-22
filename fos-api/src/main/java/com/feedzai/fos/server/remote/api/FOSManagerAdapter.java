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

import com.feedzai.fos.api.*;
import com.feedzai.fos.common.validation.NotBlank;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * FOS Remote manager local adapter.
 *
 * @author Miguel Duarte (miguel.duarte@feedzai.com)
 * @since 0.3.0
 * @since 0.3.0
 */
public class FOSManagerAdapter implements Manager {
    private final KryoScorer kryoScorer;
    IRemoteManager manager;

    public FOSManagerAdapter(IRemoteManager manager, KryoScorer kryoScorer) {
        this.manager = manager;
        this.kryoScorer = kryoScorer;
    }

    @Override
    public UUID addModel(ModelConfig modelConfig, byte[] bytes) throws FOSException {
        try {
            return manager.addModel(modelConfig, bytes);
        } catch (RemoteException e) {
            throw new FOSException(e);
        }
    }

    @Override
    public UUID addModel(ModelConfig modelConfig, @NotBlank String s) throws FOSException {
        try {
            return manager.addModel(modelConfig, s);
        } catch (RemoteException e) {
            throw new FOSException(e);
        }
    }

    @Override
    public void removeModel(UUID uuid) throws FOSException {
        try {
            manager.removeModel(uuid);
        } catch (RemoteException e) {
            throw new FOSException(e);
        }
    }

    @Override
    public void reconfigureModel(UUID uuid, ModelConfig modelConfig) throws FOSException {
        try {
            manager.reconfigureModel(uuid, modelConfig);
        } catch (RemoteException e) {
            throw new FOSException(e);
        }
    }

    @Override
    public void reconfigureModel(UUID uuid, ModelConfig modelConfig, byte[] bytes) throws FOSException {
        try {
            manager.reconfigureModel(uuid, modelConfig, bytes);
        } catch (RemoteException e) {
            throw new FOSException(e);
        }
    }

    @Override
    public void reconfigureModel(UUID uuid, ModelConfig modelConfig, @NotBlank String s) throws FOSException {
        try {
            manager.reconfigureModel(uuid, modelConfig, s);
        } catch (RemoteException e) {
            throw new FOSException(e);
        }
    }

    @Override
    public Map<UUID, ? extends ModelConfig> listModels() throws FOSException {
        try {
            return manager.listModels();
        } catch (RemoteException e) {
            throw new FOSException(e);
        }
    }

    @Override
    public Scorer getScorer() throws FOSException {
        try {
            if (kryoScorer != null) {
                return kryoScorer;
            } else {
                return new FOSScorerAdapter(manager.getScorer());
            }
        } catch (RemoteException e) {
            throw new FOSException(e);
        }
    }

    @Override
    public UUID trainAndAdd(ModelConfig modelConfig, List<Object[]> objects) throws FOSException {
        try {
            return manager.trainAndAdd(modelConfig, objects);
        } catch (RemoteException e) {
            throw new FOSException(e);
        }
    }

    @Override
    public UUID trainAndAddFile(ModelConfig modelConfig, String s) throws FOSException {
        try {
            return manager.trainAndAddFile(modelConfig, s);
        } catch (RemoteException e) {
            throw new FOSException(e);
        }

    }

    @Override
    public byte[] train(ModelConfig modelConfig, List<Object[]> objects) throws FOSException {
        try {
            return manager.train(modelConfig, objects);
        } catch (RemoteException e) {
            throw new FOSException(e);
        }

    }

    @Override
    public byte[] trainFile(ModelConfig modelConfig, String s) throws FOSException {
        try {
            return manager.trainFile(modelConfig, s);
        } catch (RemoteException e) {
            throw new FOSException(e);
        }

    }

    @Override
    public void close() throws FOSException {
        try {
            manager.close();
        } catch (RemoteException e) {
            throw new FOSException(e);
        }
    }

    @Override
    public void save(UUID uuid, String savepath) throws FOSException {
        try {
            manager.save(uuid, savepath);
        } catch (RemoteException e) {
            throw new FOSException(e);
        }
    }

    public static FOSManagerAdapter create(String registryHostname, int registryPort) throws RemoteException, NotBoundException {
        IRemoteManager iRemoteManager = getiRemoteManager(registryHostname, registryPort);
        return new FOSManagerAdapter(iRemoteManager, null);
    }

    public static FOSManagerAdapter createKryo(String registryHostname, int registryPort, int kryoPort) throws RemoteException, NotBoundException {
        IRemoteManager iRemoteManager = getiRemoteManager(registryHostname, registryPort);
        return new FOSManagerAdapter(iRemoteManager, new KryoScorer(registryHostname, kryoPort));
    }

    private static IRemoteManager getiRemoteManager(String registryHostname, int registryPort) throws RemoteException, NotBoundException {
        Registry fosRegistry = LocateRegistry.getRegistry(registryHostname,
                registryPort);

        String managerClass = IRemoteManager.class.getSimpleName();

        return (IRemoteManager) fosRegistry.lookup(managerClass);
    }
}
