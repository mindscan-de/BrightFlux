/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
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
package de.mindscan.brightflux.system.earlypersistence;

import java.nio.file.Path;

import de.mindscan.brightflux.persistence.PersistenceModule;

/**
 * 
 */
public class BasePersistenceModuleImpl implements BasePersistenceModule {

    private PersistenceModule persistenceModule;
    private EarlyPersistenceComponent earlyPersistenceComponent;

    public BasePersistenceModuleImpl( PersistenceModule persistenceModule, EarlyPersistenceComponent earlyPersistenceComponent ) {
        this.persistenceModule = persistenceModule;
        this.earlyPersistenceComponent = earlyPersistenceComponent;
    }

    @Override
    public int getNamespaceId() {
        return persistenceModule.getNamespaceId();
    }

    @Override
    public String getNamespaceName() {
        return persistenceModule.getNamespaceName();
    }

    @Override
    public void resetToDefault( String key ) {
        persistenceModule.resetToDefault( key );
    }

    @Override
    public void resetAllToDefault() {
        persistenceModule.resetAllToDefault();
    }

    @Override
    public void setDefaultIntValue( String key, int defaultValue ) {
        persistenceModule.setDefaultIntValue( key, defaultValue );
    }

    @Override
    public void setIntValue( String key, int newValue ) {
        persistenceModule.setIntValue( key, newValue );
    }

    @Override
    public int getIntValue( String key ) {
        return persistenceModule.getIntValue( key );
    }

    @Override
    public void setDefaultStringValue( String key, String defaultValue ) {
        persistenceModule.setDefaultStringValue( key, defaultValue );
    }

    @Override
    public void setStringValue( String key, String newValue ) {
        persistenceModule.setStringValue( key, newValue );
    }

    @Override
    public String getStringValue( String key ) {
        return persistenceModule.getStringValue( key );
    }

    @Override
    public long getLongValue( String key ) {
        return persistenceModule.getLongValue( key );
    }

    @Override
    public void setLongValue( String key, long newValue ) {
        persistenceModule.setLongValue( key, newValue );
    }

    @Override
    public void setDefaultLongValue( String key, long defaultValue ) {
        persistenceModule.setDefaultLongValue( key, defaultValue );
    }

    @Override
    public String[] getStringArrayValue( String key ) {
        return persistenceModule.getStringArrayValue( key );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Path evaluateAsPath( String stringValue ) {
        return earlyPersistenceComponent.evaluateAsPath( stringValue );
    }
}
