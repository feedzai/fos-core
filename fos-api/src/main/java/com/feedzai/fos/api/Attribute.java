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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.feedzai.fos.common.validation.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import static org.apache.commons.lang3.Validate.notBlank;

/**
 * Represents a field (e.g. feature) that an instance of the model must have.
 *
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public abstract class Attribute implements Serializable {
    protected static final Logger logger = LoggerFactory.getLogger(Attribute.class);

    /**
     * The internal representation of a missing value.
     */
    @JsonIgnore
    public static final double MISSING_VALUE = Double.NaN;

    /**
     * The external representation of a missing value.
     */
    @JsonIgnore
    public static final String MISSING_VALUE_STR = "?";

    String name;

    protected Attribute(String name) {
        notBlank(name, "The name of the field cannot be blank");

        this.name = name;
    }

    /**
     * Gets the name of this field.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Parses the provided value for the current Attribute configuration.
     *
     * @param original The original value of the field.
     * @return The value in the correct representation for the classifier.
     * @throws FOSException If the value could not be parsed with the current Attribute properties.
     */
    protected abstract double parse(Object original) throws FOSException;

    /**
     * Parses the provided value for the current Attribute configuration.
     *
     * @param original The original value of the field.
     * @return The value in the correct representation for the classifier or missing value.
     */
    public double parseOrMissing(@NotNull Object original) {
        try {
            if (original.equals(MISSING_VALUE_STR)) {
                return MISSING_VALUE;
            }

            return this.parse(original);
        } catch (FOSException e) {
            logger.warn("Failed to parse {} using missing value instead", original);
            return MISSING_VALUE;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute attribute = (Attribute) o;

        if (name != null ? !name.equals(attribute.name) : attribute.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
