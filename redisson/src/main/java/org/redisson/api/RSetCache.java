/**
 * Copyright 2016 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.redisson.api;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.redisson.api.mapreduce.RCollectionMapReduce;

/**
 * <p>Set-based cache with ability to set TTL for each object.
 * </p>
 *
 * <p>Current Redis implementation doesn't have set entry eviction functionality.
 * Thus values are checked for TTL expiration during any value read operation.
 * If entry expired then it doesn't returns and clean task runs asynchronous.
 * Clean task deletes removes 100 expired entries at once.
 * In addition there is {@link org.redisson.eviction.EvictionScheduler}. This scheduler
 * deletes expired entries in time interval between 5 seconds to 2 hours.</p>
 *
 * <p>If eviction is not required then it's better to use {@link org.redisson.api.RSet}.</p>
 *
 * @author Nikita Koksharov
 *
 * @param <V> value
 */
public interface RSetCache<V> extends Set<V>, RExpirable, RSetCacheAsync<V> {

    /**
     * Returns <code>RMapReduce</code> object associated with this map
     * 
     * @param <KOut> output key
     * @param <VOut> output value
     * @return MapReduce instance
     */
    <KOut, VOut> RCollectionMapReduce<V, KOut, VOut> mapReduce();
    
    /**
     * Stores value with specified time to live.
     * Value expires after specified time to live.
     *
     * @param value to add
     * @param ttl - time to live for key\value entry.
     *              If <code>0</code> then stores infinitely.
     * @param unit - time unit
     * @return <code>true</code> if value has been added. <code>false</code>
     *          if value already been in collection.
     */
    boolean add(V value, long ttl, TimeUnit unit);

    /**
     * Returns the number of elements in cache.
     * This number can reflects expired elements too
     * due to non realtime cleanup process.
     *
     * @return size of set
     */
    @Override
    int size();

    /**
     * Read all elements at once
     *
     * @return values
     */
    Set<V> readAll();

}
