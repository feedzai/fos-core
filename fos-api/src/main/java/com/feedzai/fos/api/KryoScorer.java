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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.feedzai.fos.common.kryo.CustomUUIDSerializer;
import com.feedzai.fos.common.kryo.ScoringRequestEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class implements FOS Scorer interface that
 * uses the Kryo scoring backend for increased performance boost (~5x RMI performance)
 * in remote scoring
 *
 *
 * This class is thread safe. Multiple simultaneous scoring requests can be performed
 * from multiple threads. Each simultaneous scoring requests will run
 * on its own socket connection to the Kryo backend.
 *
 * Socket connections are pooled
 *
 * @author Miguel Duarte (miguel.duarte@feedzai.com)
 */
public class KryoScorer implements Scorer {
    private final static Logger logger = LoggerFactory.getLogger(KryoScorer.class);

    List<RemoteConnection> remoteConnections = new ArrayList<>();
    private final String host;
    private final int port;


    public KryoScorer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public List<double[]> score(List<UUID> modelIds, Object[] scorable) throws FOSException {
        RemoteConnection con = null;
        try {
            con = getConnection();
            List<double[]> scores = con.score(modelIds, scorable);
            return scores;
        } catch (Exception e) {
            throw new FOSException(e.getMessage(), e);
        } finally {
            releaseConnection(con);
        }
    }

    @Override
    public List<double[]> score(UUID modelId, List<Object[]> scorables) throws FOSException {
        RemoteConnection con = null;
        try {
            con = getConnection();
            List<double[]> scores = con.score(modelId, scorables);
            return scores;
        } catch (Exception e) {
            throw new FOSException(e.getMessage(), e);
        } finally {
            releaseConnection(con);
        }
    }

    @Override
    public double[] score(UUID modelId, Object[] scorable) throws FOSException {
        RemoteConnection con = null;
        try {
            con = getConnection();
            double[] scores = con.score(modelId, scorable);
            return scores;
        } catch (Exception e) {
            throw new FOSException(e.getMessage(), e);
        } finally {
            releaseConnection(con);
        }
    }


    @Override
    public void close() throws FOSException {
        try {
            for (RemoteConnection connection : remoteConnections) {
                connection.close();
            }
        } catch (Exception e) {
            throw new FOSException(e.getMessage(), e);
        }
    }

    /**
     * Gets a connection from the connection pool or creates a new connection
     * if needed
     * @return RemoteConnection
     * @throws IOException if unable to create a new connection
     */
    private RemoteConnection getConnection() throws IOException {
        synchronized (remoteConnections) {
            int size = remoteConnections.size();
            if (size  != 0) {
                return remoteConnections.remove(size - 1);
            }
        }
        return new RemoteConnection(host, port);
    }

    /**
     * Returns a connection back to the pool
     * to be reused later on.
     *
     * @param con RemoteConnection to be returned to the pool. Null values are ignored
     */
    private void releaseConnection(RemoteConnection con) {
        if (con == null) {
            return;
        }
        synchronized (remoteConnections) {
            remoteConnections.add(con);
        }
    }

    /**
     * This class implements the internal Kryo scoring backend
     */
    private static class RemoteConnection implements Scorer {
        /*
         * Buffer size in bytes to be used for Kryo i/o.
         * It should be noted that (beyond reasonable values)
         * this does not impose any limits to the size of objects to be read/written
         * if the internal buffer is exausted/underflows, kryo will flush or read
         * more data from the associated inputstream.
         *
         */
        public static final int BUFFER_SIZE = 1024;  // bytes
        final Socket s; // socket that represents client connection
        InputStream is;
        OutputStream os;
        Kryo kryo;
        Input input;
        Output output;


        RemoteConnection(String host, int port) throws IOException {
            s = new Socket(host, port);
            // Disable naggle Algorithm to decrease latency
            s.setTcpNoDelay(true);
            is = s.getInputStream();
            os = s.getOutputStream();
            kryo = new Kryo();
            kryo.addDefaultSerializer(UUID.class, new CustomUUIDSerializer());
            input = new Input(BUFFER_SIZE);
            output = new Output(BUFFER_SIZE);
            input.setInputStream(is);
            output.setOutputStream(os);
        }


        @Override
        public List<double[]> score(List<UUID> modelIds, Object[] scorable) throws FOSException {
            try {
                ScoringRequestEnvelope envelope = new ScoringRequestEnvelope(modelIds, scorable);
                kryo.writeObject(output, envelope);
                output.flush();
                os.flush();
                return kryo.readObject(input, ArrayList.class);
            } catch (Exception e) {
                throw new FOSException("Unable to score", e);
            }
        }

        @Override
        public List<double[]> score(UUID modelId, List<Object[]> scorables) throws FOSException {
            throw new FOSException("Not implemented");
        }

        @Override
        public double[] score(UUID modelId, Object[] scorabl) throws FOSException {
            throw new FOSException("Not implemented");
        }

        @Override
        public void close() throws FOSException {
            input.close();
            output.close();
            try {
                s.close();
            } catch (IOException e) {
                throw new FOSException(e.getMessage(), e);
            }
        }
    }
}
