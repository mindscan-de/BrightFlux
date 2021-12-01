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
package de.mindscan.brightflux.persistence.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import de.mindscan.brightflux.persistence.PersistenceModule;

/**
 * 
 */
public class PersistenceModuleImpl implements PersistenceModule {

    private int namespaceId;
    private String namespaceName;

    private Map<String, Object> currentPersistenceData = new LinkedHashMap<>();
    private Map<String, Object> defaultPersistenceData = new LinkedHashMap<>();

    /**
     * 
     */
    public PersistenceModuleImpl( int namespaceId, String namespaceName ) {
        this.namespaceId = namespaceId;
        this.namespaceName = namespaceName;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int getNamespaceId() {
        return namespaceId;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceName() {
        return namespaceName;
    }

    // --------------------------

//    public Object getObjectValue( String key ) {
//        return null;
//    }
//
//    public void setObjectValue( String key, Object newValue ) {
//
//    }

    public String getStringValue( String key ) {
        return null;
    }

    public void setStringValue( String key, String newValue ) {
        // TODO:        
    }

    public int getIntValue( String key ) {
        if (currentPersistenceData.containsKey( key )) {
            return ((Integer) currentPersistenceData.get( key )).intValue();
        }

        return ((Integer) defaultPersistenceData.get( key )).intValue();
    }

    public void setIntValue( String key, int newValue ) {
        currentPersistenceData.put( key, Integer.valueOf( newValue ) );
    }

    public void setDefaultIntValue( String key, int defaultValue ) {
        defaultPersistenceData.put( key, Integer.valueOf( defaultValue ) );
    }

    public long getLongValue( String key ) {
        return 0L;
    }

    public void setLongValue( String key, long newValue ) {
        // TODO:
    }
}
