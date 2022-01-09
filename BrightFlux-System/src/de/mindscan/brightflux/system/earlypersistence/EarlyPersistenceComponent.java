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
package de.mindscan.brightflux.system.earlypersistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import de.mindscan.brightflux.persistence.PersistenceModuleRegistry;

/**
 * TODO: we want for each component maybe its own configuration file. But we want to provide an index, where to find
 *       these component configuration files. The early persistence file may not have a property editor, and will be
 *       read only for the first implementations 
 */
public class EarlyPersistenceComponent {

    private static final String EARLY_PERSISTENCE_FILENAME = "early.persistence.ini";

    private Path currentDirectory;
    private Properties earlyProperties;
    private PersistenceModuleRegistry persistenceModuleRegistry;

    public EarlyPersistenceComponent() {
        this.currentDirectory = Paths.get( System.getProperty( "user.dir", "." ) );
    }

    public void initEarlyPersistence() {
        Path earlyPersistenceFilePath = this.currentDirectory.resolve( EARLY_PERSISTENCE_FILENAME );

        try (FileInputStream earlyPersistenceFileInputStream = new FileInputStream( earlyPersistenceFilePath.toFile() )) {
            Properties p = new Properties();
            p.load( earlyPersistenceFileInputStream );

            // TODO: evaluate the content of each Property entry and create a MAP for Path or String values.
            // lets see what we have to come up with...
            setEarlyProperties( p );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setEarlyProperties( Properties p ) {
        earlyProperties = p;

        // maybe split this as an initialization after 
        persistenceModuleRegistry = new PersistenceModuleRegistry( getPropertyAsPath( "persistence.dir" ) );
    }

    public boolean containsKey( String key ) {
        return earlyProperties.containsKey( key );
    }

    public Path getPropertyAsPath( String key ) {
        return evaluateAsPath( earlyProperties.getProperty( key ).trim() );
    }

    public Path evaluateAsPath( String value ) {
        if (value.startsWith( "{{user.dir}}/" )) {
            return this.currentDirectory.resolve( value.substring( "{{user.dir}}/".length() ) );
        }
        else {
            return Paths.get( value );
        }
    }

    public Path getCurrentDirectory() {
        return currentDirectory;
    }

    public BasePersistenceModule getBasePersistenceModule( String persistenceNamespace ) {
        // TODO add an evaluator, 
        // TODO such that a BasePersistence module is able to do replacements based on the content of the early persistence
        // TODO also only one instance of base persistence modules should exist for each persistenceNamespace 

        return new BasePersistenceModuleImpl( persistenceModuleRegistry.getPersistenceModule( persistenceNamespace ) );
    }

}
