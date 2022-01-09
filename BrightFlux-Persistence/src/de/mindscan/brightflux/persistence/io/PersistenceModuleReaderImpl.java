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
package de.mindscan.brightflux.persistence.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import de.mindscan.brightflux.persistence.PersistenceModule;

/**
 * 
 */
public class PersistenceModuleReaderImpl {

    /**
     * 
     */
    public PersistenceModuleReaderImpl() {
    }

    public void loadFile( PersistenceModule persistenceModule, Path fullpath ) {

        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream( fullpath.toFile() )) {
            properties.load( fis );

            PropertiesAccesorSpy allProperties = new PropertiesAccesorSpy( properties );

            Set<String> allPropertyNames = allProperties.stringPropertyNames();
            for (String entry : allPropertyNames) {
                System.out.println( String.valueOf( entry ) );
            }

            allProperties.resetReadProperiesRead();
            handleArrays( allProperties, persistenceModule );
            handleTypedPrimitives( allProperties, persistenceModule );
            handleNonTypedPrimitives( allProperties, persistenceModule );
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleArrays( PropertiesAccesorSpy properties, PersistenceModule persistenceModule ) {
        Set<String> allPropertyNames = properties.remainingPropertyNames();

        List<String> allArrays = allPropertyNames.stream() //
                        .map( keyname -> keyname.trim() )//
                        .filter( keyname -> keyname.endsWith( "." + "array.type" ) ) //
                        .collect( Collectors.toList() );

        // we want to extract the array and its key...
        for (String arrayKeyTypeName : allArrays) {
            String keyBaseName = arrayKeyTypeName.substring( 0, arrayKeyTypeName.indexOf( "array.type" ) ).trim();

            String arrayType = properties.getProperty( arrayKeyTypeName ).trim().toLowerCase();
            String arrayLengthAsString = properties.getProperty( keyBaseName + "array.length" ).trim();
            int arrayLength = Integer.valueOf( arrayLengthAsString ).intValue();

            switch (arrayType) {
                case "string":
                    persistenceModule.setIntValue( keyBaseName + "array.length", arrayLength );

                    for (int i = 0; i < arrayLength; i++) {
                        String ithKey = keyBaseName + Integer.toString( i );
                        String ithValue = properties.getProperty( ithKey, "" );
                        persistenceModule.setStringValue( ithKey, ithValue );
                    }
                    break;

                default:
                    // unknown Type...
                    // just ignore
                    break;
            }
        }
    }

    private void handleTypedPrimitives( PropertiesAccesorSpy properties, PersistenceModule persistenceModule ) {
        Set<String> allPropertyNames = properties.remainingPropertyNames();

        List<String> allTypedPrimitives = allPropertyNames.stream() //
                        .map( keyname -> keyname.trim() )//
                        .filter( keyname -> keyname.endsWith( "." + "type" ) ) //
                        .collect( Collectors.toList() );

        for (String primitiveKeyTypeName : allTypedPrimitives) {
            String keyBaseName = primitiveKeyTypeName.substring( 0, primitiveKeyTypeName.indexOf( "type" ) ).trim();
            String primitiveType = properties.getProperty( primitiveKeyTypeName ).trim().toLowerCase();

            switch (primitiveType) {
                case "string": {
                    String primitiveValue = properties.getProperty( keyBaseName, "" );
                    persistenceModule.setStringValue( primitiveKeyTypeName, primitiveValue );
                    break;
                }
                case "int": {
                    String primitiveValue = properties.getProperty( keyBaseName, "0" );
                    int primitiveIntValue = Integer.parseInt( primitiveValue );
                    persistenceModule.setIntValue( primitiveKeyTypeName, primitiveIntValue );
                    break;
                }
                case "long": {
                    String primitiveValue = properties.getProperty( keyBaseName, "0" );
                    long primitiveLongValue = Long.parseLong( primitiveValue );
                    persistenceModule.setLongValue( primitiveKeyTypeName, primitiveLongValue );
                    break;
                }
                default:
                    // unknown Type...
                    // just ignore
                    break;
            }
        }
    }

    private void handleNonTypedPrimitives( PropertiesAccesorSpy properties, PersistenceModule persistenceModule ) {
        Set<String> allPropertyNames = properties.remainingPropertyNames();

        for (String keyBaseName : allPropertyNames) {
            String primitiveValue = properties.getProperty( keyBaseName, "" );
            persistenceModule.setStringValue( keyBaseName, primitiveValue );
        }
    }

}
