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
package de.mindscan.brightflux.system.videoannotator.preferences;

/**
 * TODO: this will be refactored into something which is more dynamic but currently supplies the "better" infrastructure  
 */
public class VideoAnnotatorPreferenceData {

    // TODO: read the snippets from configuration file from disk, such that it can be modified and adapted w/o compiling
    private static final String[] VIDEO_OBJECT_SNIPPETS = new String[] { //
                    "Tester clicks on home button", //
                    "HomeScreen opens (*SYNC*)", //
                    "View XYZ opens", //
                    "Time changes from X to Y", //
    };

    // Todo: isMap, isAtomic 
    public boolean isArray( String key ) {
        switch (key) {
            case VideoAnnotatorPreferenceKeys.VIDEO_OBJECT_SNIPPETS_KEY:
                return true;
            default:
                return false;
        }
    }

    public Object[] getArray( String key ) {
        switch (key) {
            case VideoAnnotatorPreferenceKeys.VIDEO_OBJECT_SNIPPETS_KEY:
                return VIDEO_OBJECT_SNIPPETS;
            default:
                return new Object[0];
        }
    }
}