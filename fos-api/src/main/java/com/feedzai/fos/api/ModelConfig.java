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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedzai.fos.common.validation.NotBlank;
import com.feedzai.fos.common.validation.NotEmpty;
import com.feedzai.fos.common.validation.NotNull;
import com.feedzai.fos.common.validation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.Validate.notEmpty;

/**
 * Represents the configuration of a classifier.
 *
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
public final class ModelConfig implements Serializable {
    private List<Attribute> attributes = new ArrayList<>();
    private Map<String, String> properties = new HashMap<>();
    /**
     * Flag to indicate if the model should be stored by FOS.
     */
    private boolean storeModel = true;

    public ModelConfig() {
    }

    /**
     * Creates a new configuration with the given <code>attributes</code> and <code>properties</code>.
     *
     * @param attributes the list of instances this model supports
     * @param properties a list of custom properties to send to the concrete implementation )
     */
    public ModelConfig(@NotEmpty List<Attribute> attributes, Map<String, String> properties) {
        checkNotNull(properties, "Custom properties cannot be null");
        notEmpty(attributes, "Instance fields cannot be empty");

        this.attributes.addAll(attributes);
        this.properties.putAll(properties);
    }

    /**
     * Gets the instance fields of this configuration (unmodifiable).
     *
     * @return the list of fields
     */
    @NotEmpty
    public List<Attribute> getAttributes() {
        if (attributes == null) {
            this.attributes = new ArrayList<>();
        }
        return attributes;
    }

    /**
     * Gets the custom properties of this configuration (unmodifiable).
     *
     * @return a map from custom property name to custom property value
     */
    @NotNull
    public Map<String, String> getProperties() {
        if (this.properties == null) {
            this.properties = new HashMap<>();
        }
        return properties;
    }

    /**
     * Updates this model with inputs from the given <code>ModelConfig</code>.
     * <p/>
     * For the <code>InstanceFields</code>, the internal state is clean and the provided <code>ModelConfig.attributes</code> are copied over.
     * For the <code>Properties</code>, the provided <code>ModelConfig.properties</code> overwrite matching existing values (non matching values are kept as is).
     *
     * @param modelConfig the model config that has the information to update this instance
     */
    public void update(ModelConfig modelConfig) {
        checkNotNull(modelConfig, "Model config cannot be null");

        if (this.equals(modelConfig)) {
            // nothing to update!
            return;
        }

        // if the new model instances is empty then do not update
        if (modelConfig.getAttributes().size() != 0) {
            this.attributes.clear();
            this.attributes.addAll(modelConfig.attributes);
        }

        /* does not clear properties, only adds */
        this.properties.putAll(modelConfig.getProperties());
    }

    /**
     * Add the given property to the current custom properties.
     *
     * @param key   the key of the property
     * @param value the value of the property
     * @return the already existing value in the map (or null if it doesn't exist).
     */
    @Nullable
    public String setProperty(@NotBlank String key, String value) {
        return this.properties.put(key, value);
    }

    /**
     * Remove the given property from the current custom properties.
     *
     * @param key   the key of the property
     * @return the already existing value in the map (or null if it doesn't exist).
     */
    @Nullable
    public String removeProperty(@NotBlank String key) {
        return this.properties.remove(key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes, properties, storeModel);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ModelConfig other = (ModelConfig) obj;
        return Objects.equals(this.attributes, other.attributes) && Objects.equals(this.properties, other.properties) && Objects.equals(this.storeModel, other.storeModel);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("attributes", attributes)
                .add("properties", properties)
                .add("storeModel", storeModel)
                .toString();
    }

    /**
     * Returns properties as an integer value
     *
     * @param name property name
     * @return property value
     */
    public int getIntProperty(String name, int defaultValue) {
        try {
            return getIntProperty(name);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Returns properties as an integer value
     *
     * @param name property name
     * @return property value
     * @throws FOSException if property name is invalid or if it wasn't possible to parse an integer from the value
     */
    public int getIntProperty(String name) throws FOSException {
        checkNotNull(name, "Configuration option must be defined");
        notEmpty(name, "Configuration option must not be blank");
        String svalue = properties.get(name);
        checkNotNull(svalue, "Configuration option '" + name + "' does not exist");
        notEmpty(name, "Configuration option '" + name + "' must not be blank");

        int value = 0;
        try {
            value = Integer.parseInt(svalue);
        } catch (NumberFormatException e) {
            throw new FOSException(e.getMessage(), e);
        }

        return value;
    }

    /**
     * Returns a model property value
     *
     * @param name property name
     * @return property value
     * @throws FOSException if property name is null or empty
     */
    public String getProperty(String name) throws FOSException {
        checkNotNull(name, "Configuration option must be defined");
        notEmpty(name, "Configuration option must not be blank");
        String svalue = properties.get(name);
        return svalue;
    }


    /**
     * Gets flag to indicate if the model should be stored by FOS.
     *
     * @return {@code true} if FOS is storing the model, {@code false} otherwise.
     */
    public boolean isStoreModel() {
        return storeModel;
    }

    /**
     * Sets flag to indicate if the model should be stored by FOS.
     *
     * @param storeModel {@code true} if FOS should the model, {@code false} otherwise.
     */
    public void setStoreModel(boolean storeModel) {
        this.storeModel = storeModel;
    }


    /**
     * Reads a model configuration from a file.
     *
     * @param path configuration file path
     * @return ModelConfig read from a file
     * @throws FOSException if it wasn't possible to parse a file
     */
    public static ModelConfig fromFile(String path) throws FOSException {
        ObjectMapper mapper = new ObjectMapper();
        ModelConfig deserialized;

        try {
            deserialized = mapper.readValue(new File(path), ModelConfig.class);
        } catch (IOException e) {
            throw new FOSException(e.getMessage(), e);
        }

        return deserialized;

    }

}
