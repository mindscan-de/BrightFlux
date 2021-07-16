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
package de.mindscan.brightflux.ingest.tokenizers;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * 
 */
public class DataTokenizerFactory {

    static class Holder {
        static DataTokenizerFactory factoryInstance = new DataTokenizerFactory();
    }

    public static DataTokenizer create( String tokenizerType ) {
        return getInstance().buildTokenizerInstance( tokenizerType );
    }

    public static DataTokenizerFactory getInstance() {
        return Holder.factoryInstance;
    }

    public DataTokenizer buildTokenizerInstance( String tokenizerType ) {
        if ("CSVTokenizer".equals( tokenizerType )) {
            return new CSVTokenizerImpl();
        }

        try {
            // use classloader and check if it implements DataTokenizer, for DataTokenizers in Classpath... 
            ClassLoader classLoader = this.getClass().getClassLoader();
            Class<?> loadedClass = classLoader.loadClass( tokenizerType );
            Class<?>[] interfaces = loadedClass.getInterfaces();

            if (Arrays.asList( interfaces ).contains( DataTokenizer.class )) {
                return (DataTokenizer) loadedClass.getDeclaredConstructor().newInstance();
            }
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        throw new IllegalArgumentException();
    }

}
