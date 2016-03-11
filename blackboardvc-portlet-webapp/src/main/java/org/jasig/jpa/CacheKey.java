/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.jpa;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * A object designed for use as a cache key. It assumes that all key values are immutable and pre-computes the hashCode.
 * <br/>
 * Tags can be added to a key which are used with the {@link TaggedCacheEntry} facilities. These tags ARE NOT included
 * in CacheKey comparison. 
 * 
 * @author Eric Dalquist
 */
public final class CacheKey implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final static ObjectWriter WRITER;
    static {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        WRITER = mapper.writerWithDefaultPrettyPrinter();
    }

    /**
     * Utility for building more complex cache keys
     */
    public static final class CacheKeyBuilder<K extends Serializable, V extends Serializable> {
        private final String source;
        private ArrayList<Serializable> keyList;
        private Map<Serializable, Serializable> keyMap; 
        
        private CacheKeyBuilder(String source) {
            this.source = source;
        }
        
        public CacheKeyBuilder<K, V> add(Serializable v) {
            checkKeyList();
            
            this.keyList.add(v);
            return this;
        }
        
        public CacheKeyBuilder<K, V> addAll(Serializable... vs) {
            checkKeyList();
            
            for (final Serializable v : vs) {
                this.keyList.add(v);
            }
            return this;
        }
        
        public CacheKeyBuilder<K, V> addAll(Collection<Serializable> vs) {
            checkKeyList();
            
            for (final Serializable v : vs) {
                this.keyList.add(v);
            }
            return this;
        }
        
        public CacheKeyBuilder<K, V> put(K k, V v) {
            checkKeyMap();
            
            this.keyMap.put(k, v);
            return this;
        }
        
        public CacheKeyBuilder<K, V> putAll(Properties p) {
            checkKeyMap();
            
            for (final Map.Entry<Object, Object> ve : p.entrySet()) {
                this.keyMap.put((String) ve.getKey(), (String) ve.getValue());
            }
            return this;
        }
        
        public CacheKeyBuilder<K, V> putAll(Map<? extends K, ? extends V> vm) {
            checkKeyMap();
            
            for (final Map.Entry<? extends K, ? extends V> ve : vm.entrySet()) {
                this.keyMap.put(ve.getKey(), ve.getValue());
            }
            return this;
        }
        
        public int size() {
            final int listLength = this.keyList != null ? this.keyList.size() : 0;
            final int mapLength = this.keyMap != null ? this.keyMap.size() : 0;
            return listLength + mapLength;
        }

        public CacheKey build() {
            final int listLength = this.keyList != null ? this.keyList.size() : 0;
            final int mapLength = this.keyMap != null ? this.keyMap.size() : 0;
            final int s = listLength + mapLength;
            final Serializable[] key = new Serializable[s];
            
            if (listLength > 0) {
                this.keyList.toArray(key);
            }
            
            if (mapLength > 0) {
                int mapIndex = listLength;
                for (final Map.Entry<? extends Serializable, ? extends Serializable> ve : this.keyMap.entrySet()) {
                    key[mapIndex++] = new Serializable[] { ve.getKey(), ve.getValue() };
                }
            }
            
            return new CacheKey(this.source, key);
        }
        
        private void checkKeyList() {
            if (this.keyList == null) {
                this.keyList = new ArrayList<Serializable>();
            }
        }
        
        private void checkKeyMap() {
            if (this.keyMap == null) {
                this.keyMap = new LinkedHashMap<Serializable, Serializable>();
            }
        }
    }
    
    public static <K extends Serializable, V extends Serializable> CacheKeyBuilder<K, V> builder(String source) {
        return new CacheKeyBuilder<K, V>(source);
    }
    
    public static CacheKey build(String source, Serializable... key) {
        return new CacheKey(source, key.clone());
    }
    
    public static CacheKey build(String source, Collection<? extends Serializable> key) {
        return new CacheKey(source, key.toArray(new Serializable[key.size()]));
    }
    
    public static CacheKey build(String source, Map<? extends Serializable, ? extends Serializable> keyData) {
        final Serializable[] key = new Serializable[keyData.size()];
        
        int mapIndex = 0;
        for (final Map.Entry<? extends Serializable, ? extends Serializable> ve : keyData.entrySet()) {
            key[mapIndex++] = new Serializable[] { ve.getKey(), ve.getValue() };
        }
        
        return new CacheKey(source, key);
    }

    private final String source;
    private final Serializable[] key;
    @JsonIgnore
    private final int hashCode;
    
    CacheKey(String source, Serializable[] key) {
        this.source = source;
        this.key = key;
        this.hashCode = this.internalHashCode();
    }

    public Serializable getKey() {
        return this.key;
    }
    
    public String getSource() {
        return this.source;
    }
    
    private int internalHashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(key);
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        return result;
    }
    
    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        if (hashCode() != obj.hashCode())
            return false;
        CacheKey other = (CacheKey) obj;
        if (!Arrays.deepEquals(key, other.key))
            return false;
        if (source == null) {
            if (other.source != null)
                return false;
        }
        else if (!source.equals(other.source))
            return false;
        return true;
    }

    @Override
    public String toString() {
        //Try to use a JSON formatter for generating the toString
        try {
            return WRITER.writeValueAsString(this);
        }
        catch (JsonGenerationException e) {
            //ignore
        }
        catch (JsonMappingException e) {
          //ignore
        }
        catch (IOException e) {
          //ignore
        }
        
        //Fall back on a simpler tostring
        return "CacheKey [source=" + source + ", key=" + Arrays.toString(key) + "]";
    }
}
