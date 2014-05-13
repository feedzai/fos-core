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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.feedzai.fos.common.validation.NotBlank;

/**
 * A NumericAttribute will be mapped into the underlying implementation numeric type.
 */
public final class NumericAttribute extends Attribute {

    @JsonCreator
    public NumericAttribute(@NotBlank @JsonProperty("name") String name) {
        super(name);
    }

    @Override
    protected double parse(Object original) throws FOSException {
        if (original.getClass() == Double.class) {
            return (Double) original;
        }

        try {
            return Double.parseDouble(original.toString());
        } catch (Exception e) {
            throw new FOSException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "NumericAttribute{" + getName() + "}";
    }
}
