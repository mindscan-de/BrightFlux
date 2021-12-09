/**
 * 
 * MIT License
 *
 * Copyright (c) 2021 Maxim Gansert, Mindscan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package de.mindscan.brightflux.videoannotation.impl;

import java.util.TreeMap;

/**
 * 
 */
public class VideoAnnotatorVideoObjectRefTimestampData {

    /**
     * Map of Map which contains the meta data, first level contains the Columnname, and second level is from video position to reference time stamp
     */
    private TreeMap<String, TreeMap<Long, Long>> referenceTimestamps = new TreeMap<>();

    /**
     * 
     */
    public VideoAnnotatorVideoObjectRefTimestampData() {
        // intentionally left blank.
    }

    public void registerReferenceTimestampForColumn( long videoPosition, String columnName, long referenceTimestamp ) {
        referenceTimestamps.computeIfAbsent( columnName, k -> new TreeMap<Long, Long>() ).put( videoPosition, referenceTimestamp );
    }

    public void clearReferenceTimestamps() {
        referenceTimestamps.clear();
    }

    public long videoPositionAt( long videoPosition, String columnName ) {
        if (referenceTimestamps.containsKey( columnName )) {
            return referenceTimestamps.get( columnName ).getOrDefault( videoPosition, 0L );
        }
        return 0L;
    }

    public boolean isColumnPredictable( String columnName ) {
        if (referenceTimestamps.containsKey( columnName )) {
            return referenceTimestamps.get( columnName ).size() >= 2;
        }
        return false;
    }

}
