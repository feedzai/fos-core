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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for categorical  attributes
 * @author Miguel Duarte (miguel.duarte@feedzai.com)
 */
public class CategoricalAttributeTest {

    private List<String> nominal_values;
    private String name;
    private CategoricalAttribute field;

    @Before
    public void setup() {
        nominal_values = Arrays.asList("1", "2");
        name = "a";
        field = new CategoricalAttribute(name, nominal_values);
    }

    @Test
    public void testCreateNominal() throws Exception {
        assertEquals("Nominal values were not set properly", nominal_values, field.getCategoricalInstances());
        assertEquals("Instance name not set properly", name, field.getName());

        int index = 0;
        for(String v : nominal_values) {
            assertEquals("Known value must be unchanged", index, (int) field.parseOrMissing(v));
            index++;
        }
    }

    @Test(expected = Exception.class)
    public void emptyStringNominal() throws Exception {
        nominal_values = Arrays.asList("1", "");
        field = new CategoricalAttribute(name, nominal_values);

    }

    @Test(expected = Exception.class)
    public void invalidNullNominal() throws Exception {
        nominal_values = Arrays.asList("1", null);
        field = new CategoricalAttribute(name, nominal_values);
    }

    @Test(expected = Exception.class)
    public void empty() throws Exception {
        nominal_values = Arrays.asList();
        field = new CategoricalAttribute(name, nominal_values);
    }


    @Test(expected = Exception.class)
    public void invalidName() throws Exception {
        nominal_values = Arrays.asList("1", "");
        field = new CategoricalAttribute("", nominal_values);

        nominal_values = Arrays.asList("1", null);
        field = new CategoricalAttribute(null, nominal_values);

    }

    @Test
    public void testSetFaultyValue() throws Exception {
        assertTrue("Faulty categorical value must be replaced", Double.isNaN(field.parseOrMissing("non_existant")));
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullValue() throws Exception {
        assertTrue("Null is not accepted", Double.isNaN(field.parseOrMissing(null)));
    }

    @Test
    public void testSetMissingValue() throws Exception {
        assertTrue("Missing value must be handled as missing", Double.isNaN(field.parseOrMissing(Attribute.MISSING_VALUE_STR)));
    }

    @Test
    public void testJackson() throws Exception {
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(field);

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Attribute>> typeReference = new TypeReference<List<Attribute>>() {
        };
        String result = mapper.writerWithType(typeReference).writeValueAsString(attributes);
        List<Attribute> deserialized = mapper.readValue(result, typeReference);
        assertEquals(attributes.size(), deserialized.size());
        CategoricalAttribute extracted = (CategoricalAttribute) deserialized.get(0);
        assertEquals(field.getName(), extracted.getName());
        assertEquals(field.getCategoricalInstances(), extracted.getCategoricalInstances());
    }
}
