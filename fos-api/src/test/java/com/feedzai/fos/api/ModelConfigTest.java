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
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class ModelConfigTest {

    @Test
    public void testJacksonSerialization() throws IOException {
        CategoricalAttribute categorical = new CategoricalAttribute("categoric", Arrays.asList("abc", "def"));
        NumericAttribute numeric = new NumericAttribute("numeric");

        List<Attribute> attributes = Arrays.<Attribute>asList(categorical, numeric);

        Map<String, String> properties = new HashMap<>();
        properties.put("p1", "value1");
        properties.put("p2", "value2");

        ModelConfig config = new ModelConfig(attributes, properties);

        ObjectMapper mapper = new ObjectMapper();
        Assert.assertTrue(mapper.canSerialize(config.getClass()));
        String json = mapper.writeValueAsString(config);

        System.out.println(json);
        System.out.println(config);

        ModelConfig deserialized = mapper.readValue(json, config.getClass());
        assertEquals(config, deserialized);
    }
}
