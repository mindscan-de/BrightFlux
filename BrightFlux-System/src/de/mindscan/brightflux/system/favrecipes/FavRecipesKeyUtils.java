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
package de.mindscan.brightflux.system.favrecipes;

/**
 * 
 */
public class FavRecipesKeyUtils {

    public final static String PATH_SEPARATOR = "::";
    public final static String ROOT = "";

    public static String[] splitKey( String key ) {
        return key.split( FavRecipesKeyUtils.PATH_SEPARATOR );
    }

    public static String buildKey( String[] splitKey ) {
        return String.join( PATH_SEPARATOR, splitKey );
    }

    public static String buildParentKey( String[] splitKey ) {
        String[] newSplitKey = new String[splitKey.length - 1];
        for (int i = 0; i < newSplitKey.length; i++) {
            newSplitKey[i] = splitKey[i];
        }

        return buildKey( newSplitKey );
    }

    public static String calculateParent( String key ) {
        if (key.isEmpty()) {
            return ROOT;
        }

        String[] splitKey = splitKey( key );
        if (splitKey.length == 1) {
            return ROOT;
        }

        return buildParentKey( splitKey );
    }

    public static String calculateName( String key ) {
        if (key.isEmpty()) {
            return ROOT;
        }

        String[] splitKey = splitKey( key );
        return splitKey[splitKey.length - 1];
    }
}
