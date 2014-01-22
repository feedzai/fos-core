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

import com.feedzai.fos.common.validation.NotBlank;
import com.feedzai.fos.common.validation.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class represents a manager that is capable of handling CRUD-type operations on the active models.
 * <p/>
 * The manager MUST support multi-threaded usage.
 *
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
public interface Manager {

    /**
     * Adds a new model with the given configuration and using the given serialized classifier.
     *
     * @param config the configuration of the model
     * @param model  the new serialized classifier
     * @return the id of the new model
     * @throws FOSException when creating the classifier was not possible
     */
    UUID addModel(ModelConfig config,byte[] model) throws FOSException;

    /**
     * Adds a new model with the given configuration and using a local classifier source.
     *
     * @param config        the configuration of the model
     * @param localFileName the local filename (absolute path) of the serialized classifier
     * @return the id of the new model
     * @throws FOSException when creating the classifier was not possible
     */
    UUID addModel(ModelConfig config, @NotBlank String localFileName) throws FOSException;

    /**
     * Removes the model identified by <code>modelId</code> from the list of active scorers (does not delete classifier file).
     *
     * @param modelId the id of the model to remove
     * @throws FOSException when removing the model was not possible
     */
    void removeModel(UUID modelId) throws FOSException;

    /**
     * Reconfigures the model identified by <code>modelId</code> with the given configuration.
     *
     * @param modelId the id of the model to update
     * @param config  the new configuration of the model
     * @throws FOSException when reconfiguring the model was not possible
     */
    void reconfigureModel(UUID modelId,ModelConfig config) throws FOSException;

    /**
     * Reconfigures the model identified by <code>modelId</code> with the given configuration.
     * <p/>
     * Reloads the model now using the classifier <code>model</code> (does not delete previous classifier file).
     *
     * @param modelId the id of the model to update
     * @param config  the new configuration of the model
     * @param model   the new serialized classifier
     * @throws FOSException when reconfiguring the model was not possible
     */
    void reconfigureModel(UUID modelId,ModelConfig config,byte[] model) throws FOSException;

    /**
     * Reconfigures the model identified by <code>modelId</code> with the given configuration.
     * <p/>
     * Reloads the model now using the classifier in the local file <code>localFileName</code> (does not delete previous classifier file).
     *
     * @param modelId       the id of the model to update
     * @param config        the new configuration of the model
     * @param localFileName the local filename (absolute path) of the serialized classifier
     * @throws FOSException when reconfiguring the model was not possible
     */
    void reconfigureModel(UUID modelId,ModelConfig config, @NotBlank String localFileName) throws FOSException;

    /**
     * Lists the models configured in the system.
     *
     * @return a map of <code>modelId</code> to model configuration
     * @throws FOSException when listing the maps was not possible
     */
    @NotNull
    Map<UUID, ? extends ModelConfig> listModels() throws FOSException;

    /**
     * Gets a scorer loaded with all the active models.
     *
     * @return a scorer
     * @throws FOSException when loading the scorer was not possible
     */
    @NotNull
    Scorer getScorer() throws FOSException;

    /**
     * Trains a new classifier with the given configuration and using the given <code>instances</code>.
     * <p/> Automatically add the new model to the existing scorer.
     *
     * @param config    the model configuration
     * @param instances the training instances
     * @return the id of the new model
     * @throws FOSException when training was not possible
     */
    UUID trainAndAdd(ModelConfig config,List<Object[]> instances) throws FOSException;


    /**
     * Trains a new classifier with the given configuration and using the <code>instances</code>.
     * in a CSV encoded file
     * <p/> Automatically add the new model to the existing scorer.
     *
     * @param config    the model configuration
     * @param path the training instances
     * @return the id of the new model
     * @throws FOSException when training was not possible
     */
    UUID trainAndAddFile(ModelConfig config,String path) throws FOSException;

    /**
     * Trains a new classifier with the given configuration and using the given <code>instances</code>.
     *
     * @param config    the model configuration
     * @param instances the training instances
     * @return a serialized representation of the model
     * @throws FOSException when training was not possible
     */
    @NotNull
    byte[] train(ModelConfig config,List<Object[]> instances) throws FOSException;


    /**
     * Trains a new classifier with the given configuration and using the given <code>instances</code>.
     *
     * @param config    the model configuration
     * @param path File with the training instances
     * @return a serialized representation of the model
     * @throws FOSException when training was not possible
     */
    @NotNull
    byte[] trainFile(ModelConfig config, String path) throws FOSException;

    /**
     * Frees any resources allocated to this manager.
     *
     * @throws FOSException when closing resources was not possible
     */
    void close() throws FOSException;


    /**
     * Saves a UUID identified classifier to a given savepath
     * @param uuid model uuid
     * @param savepath export model path
     */
    void save(UUID uuid, String savepath) throws FOSException;
}
