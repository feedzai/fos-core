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
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.feedzai.fos.common.kryo.CustomUUIDSerializer;
import com.feedzai.fos.common.kryo.ScoringRequestEnvelope;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

/**
 * This class should be used to perform scoring requests
 * to a remote FOS instance that suports kryo end points.
 * <p/>
 * It listens on a socket input stream for Kryo serialized {@link ScoringRequestEnvelope}
 * objects, extracts them and forwards them to the local scorer.
 * <p/>
 * The scoring result is then Kryo encoded on the socket output stream.
 *
 * @author Miguel Duarte (miguel.duarte@feedzai.com)
 * @since 1.0.6
 */
public class KryoScoringEndpoint implements Runnable {
    /**
     * The logger instance for the endpoint.
     */
    private final static Logger logger = LoggerFactory.getLogger(KryoScoringEndpoint.class);
    /**
     * The buffer size for Kryo buffers.
     */
    public static final int BUFFER_SIZE = 1024;
    /**
     * The client socket to read from and write to with Kryo serializer.
     */
    Socket client;
    /**
     * The {@link com.feedzai.fos.api.Scorer} to use for scoring messages.
     */
    Scorer scorer;
    /**
     * Flag to define if the
     */
    private volatile boolean running = true;

    /**
     * Creates a new instance of the {@link com.feedzai.fos.api.KryoScoringEndpoint} class.
     *
     * @param client The socket to use during communication.
     * @param scorer The scorer to score the messages that arrive in the socket.
     * @throws IOException When a problem occurs in the socket communication channels.
     */
    public KryoScoringEndpoint(Socket client, Scorer scorer) throws IOException {
        this.client = client;
        this.scorer = scorer;
    }

    @Override
    public void run() {
        Kryo kryo = new Kryo();
        kryo.addDefaultSerializer(UUID.class, new CustomUUIDSerializer());
        // workaround for java.util.Arrays$ArrayList missing default constructor
        kryo.register(Arrays.asList().getClass(), new CollectionSerializer() {
            protected Collection create(Kryo kryo, Input input, Class<Collection> type) {
                return new ArrayList();
            }
        });

        Input input = new Input(BUFFER_SIZE);
        Output output = new Output(BUFFER_SIZE);

        ScoringRequestEnvelope request = null;
        try (InputStream is = client.getInputStream();
             OutputStream os = client.getOutputStream()) {
            input.setInputStream(is);
            output.setOutputStream(os);
            while (running) {
                request = kryo.readObject(input, ScoringRequestEnvelope.class);
                List<double[]> scores = scorer.score(request.getUUIDs(),
                        request.getInstance());
                kryo.writeObject(output, scores);
                output.flush();
                os.flush();
            }
        } catch (Exception e) {
            if (request != null) {
                logger.error("Error scoring instance {} for models {}", Arrays.toString(request.getInstance()), Arrays.toString(request.getUUIDs().toArray()), e);
            } else {
                logger.error("Error scoring instance", e);
            }
        } finally {
            IOUtils.closeQuietly(client);
            running = false;
        }
    }

    /**
     * Closes the socket of the connection.
     */
    public void close() {
        running = false;
        IOUtils.closeQuietly(client);
    }
}
