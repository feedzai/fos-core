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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * A descriptor for a model.
 * <p/>
 * Contains the path of the binary or the PMML file with the model representation, as well
 * as the format in which the model is represented.
 *
 * @author Ricardo Ferreira (ricardo.ferreira@feedzai.com)
 * @since 1.0.4
 */
public class ModelDescriptor implements Serializable {

    /**
     * The absolute path to the file with the model representation.
     */
    private String modelFilePath;

    /**
     * The format in which the model is represented (binary, PMML, etc).
     */
    private Format format;


    /**
     * Private constructor.
     *
     * @param format        The {@link Format} in which the model is represented (binary, PMML, etc).
     * @param modelFilePath The path to the file containing the model.
     */
    public ModelDescriptor(Format format, String modelFilePath) {
        this.format = format;
        this.modelFilePath = modelFilePath;
    }

    /**
     * Retrieves the path to the file with the model.
     *
     * @return The path to the file with the model.
     */
    public String getModelFilePath() {
        return modelFilePath;
    }

    /**
     * Retrieves the format in which the model is represented (binary, PMML, etc).
     *
     * @return The format in which the model is represented (binary, PMML, etc).
     */
    public Format getFormat() {
        return format;
    }


    /**
     * Possible model representation formats.
     */
    public enum Format {
        /**
         * Binary representation as a serialized byte array.
         */
        BINARY,

        /**
         * An XML file containing the PMML representation of the model.
         */
        PMML
    }
}
