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
     * @param config The configuration of the model.
     * @param model  A {@link Model representation of the model}.
     * @return the id of the new model
     * @throws FOSException when creating the classifier was not possible
     */
    UUID addModel(ModelConfig config, Model model) throws FOSException;

    /**
     * Adds a new model with the given configuration and using a local classifier source.
     *
     * @param config     The configuration of the model.
     * @param descriptor the {@link ModelDescriptor} describing the classifier
     * @return the id of the new models
     * @throws FOSException when creating the classifier was not possible
     */
    UUID addModel(ModelConfig config, @NotBlank ModelDescriptor descriptor) throws FOSException;

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
     * @param modelId The id of the model to update.
     * @param config  The new configuration of the model.
     * @param model   A {@link Model representation of the model}.
     * @throws FOSException when reconfiguring the model was not possible
     */
    void reconfigureModel(UUID modelId,ModelConfig config, Model model) throws FOSException;

    /**
     * Reconfigures the model identified by <code>modelId</code> with the given configuration.
     * <p/>
     * Reloads the model now using the classifier in the local file <code>localFileName</code> (does not delete previous classifier file).
     *
     * @param modelId    the id of the model to update
     * @param config     the new configuration of the model
     * @param descriptor the {@link ModelDescriptor} describing the classifier
     * @throws FOSException when reconfiguring the model was not possible
     */
    void reconfigureModel(UUID modelId,ModelConfig config, @NotBlank ModelDescriptor descriptor) throws FOSException;

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
    UUID trainAndAdd(ModelConfig config, List<Object[]> instances) throws FOSException;


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
    UUID trainAndAddFile(ModelConfig config, String path) throws FOSException;

    /**
     * Trains a new classifier with the given configuration and using the given <code>instances</code>.
     *
     * @param config    the model configuration
     * @param instances the training instances
     * @return A {@link Model representation of the model}.
     * @throws FOSException when training was not possible
     */
    @NotNull
    Model train(ModelConfig config, List<Object[]> instances) throws FOSException;


    /**
     * Trains a new classifier with the given configuration and using the given <code>instances</code>.
     *
     * @param config    the model configuration
     * @param path File with the training instances
     * @return A {@link Model representation of the model}.
     * @throws FOSException when training was not possible
     */
    @NotNull
    public Model trainFile(ModelConfig config, String path) throws FOSException;


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

    /**
     * Saves a UUID identified classifier as PMML to a given {@code filePath}.
     * </p>
     * The {@code filePath} will be the absolute file path where the model will be saved to.
     * <p/>
     * If the {@code compress} flag is set to {@code true}, the file will be saved in a GZipped compressed format.
     * Concrete implementations may differ on how they handle the compression.
     *
     * @param uuid     The classifier's UUID.
     * @param filePath The absolute file path  to which save the PMML representation of the classifier.
     * @param compress {@code true} to compress the resulting file to GZip, or {@code false} to save it as a raw PMML file.
     * @throws FOSException If if failed to read the classifier or export it to PMML.
     * @since @since 1.0.4
     */
    void saveAsPMML(UUID uuid, String filePath, boolean compress) throws FOSException;
}
