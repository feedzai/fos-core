/*
 * $#
 * FOS Dummy
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
package com.feedzai.fos.impl.dummy;

import com.feedzai.fos.api.Manager;
import com.feedzai.fos.api.ManagerFactory;
import com.feedzai.fos.api.config.FosConfig;

/**
 * @author Marco Jorge (marco.jorge@feedzai.com)
 */
public class DummyManagerFactory implements ManagerFactory {

    @Override
    public Manager createManager(FosConfig configuration) {
        return new DummyManager();
    }
}
