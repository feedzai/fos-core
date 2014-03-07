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
package com.feedzai.fos.api.util;

import com.feedzai.fos.api.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * Utility methods for {@link Manager} implementations.
 *
 * @author Ricardo Ferreira (ricardo.ferreira@feedzai.com)
 * @see 1.0.4
 */
public class ManagerUtils {

    /**
     * Obtain model UUID from ModelConfig if defined or generate a new random uuid
     *
     * @param config Model Configuration
     * @return new Model UUID
     * @throws com.feedzai.fos.api.FOSException
     */
    public static UUID getUuid(ModelConfig config) throws FOSException {
        String suuid = config.getProperty("UUID");

        return StringUtils.isBlank(suuid) ? UUID.randomUUID() : UUID.fromString(suuid);
    }

    /**
     * Creates a new {@link java.io.File} for the given {@link com.feedzai.fos.api.Model} and writes
     * the classifier representation to it.
     *
     * @param location The target location where the binary will be written to.
     * @param uuid     The UUID of the model.
     * @param model    The {@link com.feedzai.fos.api.Model} representation.
     * @return         The File where the model was written to.
     * @throws java.io.IOException Tf saving to disk was not possible.
     */
    public static File createModelFile(File location, UUID uuid, Model model) throws IOException {
        if (model instanceof ModelBinary) {
            return createModelBinaryFile(location, uuid, ((ModelBinary) model).getBinary());
        } else if (model instanceof ModelPMML) {
            return createModelPMMLFile(location, uuid, ((ModelPMML) model).getPMML());
        } else {
            throw new RuntimeException("Unknown Model instance " + model.getClass().getSimpleName());
        }
    }

    /**
     * Creates a file in {@code <location>/<id>.model} and serializes the given byte array to it.
     *
     * @param location The target location where the binary will be written to.
     * @param id       The UUID of the model.
     * @param model    The serialized classifier.
     * @return The File where the model was written to.
     * @throws java.io.IOException if saving to disk was not possible.
     */
    private static File createModelBinaryFile(File location, UUID id, byte[] model) throws IOException {
        File file = File.createTempFile(id.toString(), ".model", location);
        FileUtils.writeByteArrayToFile(file, model);
        return file;
    }

    /**
     * Creates a file in {@code <location>/<id>.xml} and serializes the given String to it.
     *
     * @param location The target location where the binary will be written to.
     * @param id       The UUID of the model.
     * @param pmml     The String containing the PMML representation.
     * @return The File where the model was written to.
     * @throws java.io.IOException if saving to disk was not possible.
     */
    private static File createModelPMMLFile(File location, UUID id, String pmml) throws IOException {
        File file = File.createTempFile(id.toString(), ".xml", location);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(pmml);
        }

        return file;
    }
}
