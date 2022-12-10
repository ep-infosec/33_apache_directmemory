package org.apache.directmemory.serialization.protostuff;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import static com.dyuproject.protostuff.LinkedBuffer.allocate;
import static com.dyuproject.protostuff.ProtostuffIOUtil.mergeFrom;
import static com.dyuproject.protostuff.ProtostuffIOUtil.toByteArray;
import static com.dyuproject.protostuff.runtime.RuntimeSchema.getSchema;

import java.io.IOException;

import org.apache.directmemory.measures.Ram;
import org.apache.directmemory.serialization.Serializer;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.Schema;

public final class ProtoStuffSerializerV1
    implements Serializer
{

    static int serBufferSize = Ram.Kb( 3 );

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> byte[] serialize( T obj )
        throws IOException
    {
        @SuppressWarnings( "unchecked" ) // type should be safe since got directly from the obj
        final Class<T> clazz = (Class<T>) obj.getClass();
        final Schema<T> schema = getSchema( clazz );
        final LinkedBuffer buffer = allocate( serBufferSize );
        byte[] protostuff = null;

        try
        {
            protostuff = toByteArray( obj, schema, buffer );
        }
        finally
        {
            buffer.clear();
        }
        return protostuff;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T deserialize( byte[] source, Class<T> clazz )
        throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        final T object = clazz.newInstance();
        final Schema<T> schema = getSchema( clazz );
        mergeFrom( source, object, schema );
        return object;
    }

}
