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

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author João Rafael (joao.rafael@feedzai.com)
 * @since 14.0.0
 */
public class NumericAttributeTest {

    private String name;
    private NumericAttribute field;
    private static final double EPS = 1e-6;

    @Before
    public void setup() {
        name = "a";
        field = new NumericAttribute(name);
    }

    @Test
    public void testSetIntegerValue() throws Exception {
        assertEquals("Faulty categorical value must be replaced", field.parseOrMissing("213"), 213, EPS);
    }

    @Test
    public void testSetDoubleValue() throws Exception {
        assertEquals("Faulty categorical value must be replaced", field.parseOrMissing("456.456"), 456.456, EPS);
    }

    @Test
    public void testSetNegativeValue() throws Exception {
        assertEquals("Faulty categorical value must be replaced", field.parseOrMissing("-213.3"), -213.3, EPS);
    }

    @Test
    public void testSetFaultyValue() throws Exception {
        assertTrue("Faulty categorical value must be replaced", Double.isNaN(field.parseOrMissing("NaN")));
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullValue() throws Exception {
        assertTrue("Null is not accepted", Double.isNaN(field.parseOrMissing(null)));
    }

    @Test
    public void testSetMissingValue() throws Exception {
        assertTrue("Missing value must be handled as missing", Double.isNaN(field.parseOrMissing(Attribute.MISSING_VALUE_STR)));
    }

}
