/*
 * $#
 * FOS Common
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
package com.feedzai.fos.common.validation;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.Validate;

import java.io.File;

/**
 * Validation utils for retrieving values from a configuration object.
 *
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
public class ValidationUtils {
    /**
     * The message given when a string is blank but cannot be.
     *
     * param 1 the name of the parameter
     */
    public static final String NOT_BLANK = "Parameter '%s' is mandatory and cannot be blank";

    /**
     * The message given when a parameter does not extend a given class.
     *
     * param 1 the name of the parameter
     * param 2 that class that is must extend
     */
    public static final String NOT_CLASS = "Parameter '%s' is not a classname of '%s'";

    /**
     * The message given when an <code>Array</code> or <code>Collection</code> is empty but cannot be.
     *
     * param 1 the name of the parameter
     */
    public static final String NOT_EMPTY = "Parameter '%s' is mandatory and cannot be empty";

    /**
     * The message given when a parameter does not define an existing file.
     *
     * param 1 the name of the parameter
     * param 2 the current parameter value
     */
    public static final String NOT_FOUND = "Parameter '%s' is defining the file '%s' that does not exist";


    /**
     * The message given when a parameter does not define an an existing or creatable directory.
     *
     * param 1 the name of the parameter
     * param 2 the current parameter value
     */
    public static final String DIRECTORY_ERROR = "Parameter '%s' is defining the directory '%s' that does not exist and can't be created";


    /**
     * Gets a <code>String[]</code> from the given configuration.
     *
     * @param configuration the configuration where the parameter lies
     * @param parameterName the name of the parameter
     * @return the <code>String[]</code>
     * @throws IllegalArgumentException if the parameter is empty
     */
    @NotEmpty
    public static String[] getStringArrayNotEmpty(Configuration configuration, @NotBlank String parameterName) {
        return Validate.notEmpty(configuration.getStringArray(parameterName), NOT_EMPTY, parameterName);
    }

    /**
     * Gets a <code>String</code> from the given configuration.
     *
     * @param configuration the configuration where the parameter lies
     * @param parameterName the name of the parameter
     * @return the <code>String</code>
     * @throws IllegalArgumentException if the parameter is null or blank
     */
    @NotBlank
    public static String getStringNotBlank(Configuration configuration, @NotBlank String parameterName) {
        return Validate.notBlank(configuration.getString(parameterName), NOT_BLANK, parameterName);
    }

    /**
     * Gets a <code>File</code> from the given configuration.
     *
     * @param configuration the configuration where the parameter lies
     * @param parameterName the name of the parameter
     * @return the <code>File</code>
     * @throws IllegalArgumentException if the file does not exist
     */
    @NotNull
    public static <T> File getFile(Configuration configuration, @NotBlank String parameterName) {
        String fileName = getStringNotBlank(configuration, parameterName);
        File file = new File(fileName);

        if (file.exists()) {
            return file;
        } else {
            throw new IllegalArgumentException(String.format(NOT_FOUND, parameterName, fileName));
        }
    }

    /**
     * Gets a <code>Class</code> from the given configuration.
     *
     * @param configuration the configuration where the parameter lies
     * @param parameterName the name of the parameter
     * @param clazz         the class that the parameter represents
     * @return the <code>Class</code>
     * @throws IllegalArgumentException if the class was not found
     */
    @NotNull
    public static <T> Class<T> getClass(Configuration configuration, @NotBlank String parameterName,Class<T> clazz) {
        String classname = getStringNotBlank(configuration, parameterName);

        try {
            return clazz.getClass().cast(Class.forName(classname));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format(NOT_CLASS, classname, clazz.getName()), e);
        }
    }

    /**
     * Gets an <code>object instance</code> from the given configuration.
     *
     * @param configuration the configuration where the parameter lies
     * @param parameterName the name of the parameter
     * @param clazz         the class that the parameter represents
     * @return the <code>object</code> instantiation using the no-args constructor
     * @throws IllegalArgumentException if the class was not found, if the instantiation was illegal, or if there was illegal access
     */
    @NotNull
    public static <T> T getInstance(Configuration configuration, @NotBlank String parameterName,Class<T> clazz) {
        String classname = getStringNotBlank(configuration, parameterName);

        try {
            return clazz.cast(Class.forName(classname).newInstance());
        } catch (ClassNotFoundException|InstantiationException|IllegalAccessException  e) {
            throw new IllegalArgumentException(String.format(NOT_CLASS, classname, clazz.getName()), e);
        }
    }

    /**
     * Gets a <code>File</code> representing a directory, creating it if necessary from
     * the given configuration.
     *
     * @param configuration the configuration where the parameter lies
     * @param parameterName the name of the parameter
     * @return the <code>File</code>
     * @throws IllegalArgumentException if the file does not exist
     */
    public static <T> File getDir(Configuration configuration, String parameterName) {
        String fileName = getStringNotBlank(configuration, parameterName);
        File file = new File(fileName);

        if (file.isDirectory()) {
            return file;
        }

        if (file.mkdirs()) {
            return file;
        } else {
            throw new IllegalArgumentException(String.format(DIRECTORY_ERROR, parameterName, fileName));
        }
    }
}
