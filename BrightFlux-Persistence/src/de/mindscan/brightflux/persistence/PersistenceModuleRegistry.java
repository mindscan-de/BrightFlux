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
package de.mindscan.brightflux.persistence;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import de.mindscan.brightflux.persistence.impl.PersistenceModuleImpl;

/**
 * 
 */
public class PersistenceModuleRegistry {

    private Path persistenceBasePath;
    private Map<String, PersistenceModule> persistenceModules;

    /**
     * @param persistenceBasePath Base path of the persistence data - registry and module data.
     */
    public PersistenceModuleRegistry( Path persistenceBasePath ) {
        this.persistenceBasePath = persistenceBasePath;
        this.persistenceModules = new LinkedHashMap<>();
    }

    public PersistenceModule getPersistenceModule( int persistenceId ) {
        // either create+load or get PersistenceModule from Runtime
        return null;
    }

    public PersistenceModule getPersistenceModule( String persistenceNamespaceName ) {
        if (persistenceModules.containsKey( persistenceNamespaceName )) {
            return persistenceModules.get( persistenceNamespaceName );
        }

        // load and initialize if not present...
        PersistenceModule persistenceModule = createModuleInsance( persistenceBasePath, persistenceNamespaceName );
        persistenceModules.put( persistenceNamespaceName, persistenceModule );
        return persistenceModule;
    }

    public static PersistenceModule createModuleInsance( Path persistenceBasePath2, String persistenceNamespaceName ) {
        // either create+load or get PersistenceModule from Runtime
        return new PersistenceModuleImpl( 0, persistenceNamespaceName );
    }

}
