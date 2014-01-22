/*
 * $#
 * FOS Server
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
package com.feedzai.fos.server.remote.impl;

import com.feedzai.fos.server.remote.api.RemoteInterface;
import org.apache.commons.collections15.comparators.ComparatorChain;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * This test is a little unorthodox. It tests that Remote exception match with their targeted implementation.
 * It does so by checking {@link com.feedzai.fos.server.remote.api.RemoteInterface} .
 *
 * @author diogo.ferreira
 */
public class RemoteInterfacesTest {
    public RemoteInterfacesTest() {

    }

    @Test
    public void testRemoteInterfaces() throws Exception {
        for (Class klass : getClasses("com.feedzai.fos.server.remote.impl")) {
            if (klass.isAnnotationPresent(RemoteInterface.class)) {
                RemoteInterface implementation = (RemoteInterface) klass.getAnnotation(RemoteInterface.class);
                Class<?> matchingClass = implementation.of();
                testMatch(matchingClass, klass, 0);
            }
        }
    }

    private void testMatch(Class<?> api, Class<?> remote, int remoteFieldAdditions) {
        Method[] apiMethods = api.getMethods();
        Method[] remoteMethods = remote.getMethods();

        Arrays.sort(apiMethods, buildComparator());
        Arrays.sort(remoteMethods, buildComparator());

        // Checks if @RemoteInterface is different from the class/interface itself.
        assertNotSame(String.format("@RemoteInterface cannot be the same for the class name in %s", api.getSimpleName()), api, remote);
        // Checks if the remote implementation can be assigned to Remote (i.e. if in the hierarchy extends/implements Remote).
        assertTrue(String.format("'%s' does not implement '%s'", remote.getSimpleName(), Remote.class.getSimpleName()), Remote.class.isAssignableFrom(remote));

        assertEquals(desc(api, null, "Number of methods matches"), apiMethods.length, remoteMethods.length);

        for (int i = 0; i < apiMethods.length; i++) {
            // Check if the names match
            Method expected = apiMethods[i];
            Method actual = remoteMethods[i];

            assertEquals(desc(api, actual, "Names match"), expected.getName(), actual.getName());
            assertTrue(desc(api, actual, "Number of arguments matches"), expected.getParameterTypes().length - actual.getParameterAnnotations().length <= remoteFieldAdditions);

            boolean remoteOrException = false;
            for (Class klass : actual.getExceptionTypes()) {
                remoteOrException = Exception.class.equals(klass) || RemoteException.class.equals(klass);
                if (remoteOrException) {
                    break;
                }
            }

            // Checks if remote implementations throw Exception or RemoteException.
            assertTrue(desc(remote, actual, String.format("%s does not throw either RemoteException or Exception", actual.getName())), remoteOrException);
        }
    }

    private String desc(Class klass, Method method, String desc) {
        String methodName = method == null ? "none" : method.getName();

        return String.format("[%s#%s] %s", klass.getSimpleName(), methodName, desc);
    }

    private Comparator<Method> buildComparator() {
        ComparatorChain<Method> chain = new ComparatorChain<Method>();
        chain.addComparator(new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        chain.addComparator(new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                return o1.getParameterTypes().length - o2.getParameterTypes().length;
            }
        });

        return chain;
    }

    /**
     * What follows is a shameless adaptation of http://www.dzone.com/snippets/get-all-classes-within-package
     */

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws java.io.IOException
     */
    private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains("");
                classes.addAll(findClasses(file, packageName + "" + file.getName()));
            } else if (file.getName().contains("Remote")) {
                try {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                } catch (NullPointerException ex) {
                    // Some classes may fail their static initialization, we don't really care.
                }
            }
        }
        return classes;
    }
}
