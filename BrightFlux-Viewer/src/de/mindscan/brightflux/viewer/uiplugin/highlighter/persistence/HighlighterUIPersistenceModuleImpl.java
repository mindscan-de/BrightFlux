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
package de.mindscan.brightflux.viewer.uiplugin.highlighter.persistence;

import java.util.ArrayList;
import java.util.Collection;

import de.mindscan.brightflux.system.earlypersistence.BasePersistenceModule;

/**
 * 
 */
public class HighlighterUIPersistenceModuleImpl implements HighlighterUIPersistenceModule {

    private static final String COLOR_KEY_SUFFIX = ".color";

    private BasePersistenceModule persistenceModule;

    /**
     * 
     */
    public HighlighterUIPersistenceModuleImpl( BasePersistenceModule persistenceModule ) {
        this.persistenceModule = persistenceModule;
    }

    @Override
    public String[] getColorNames() {
        ArrayList<String> colorNames = new ArrayList<>();

        Collection<String> colorKeys = persistenceModule.enumerateKeys( key -> key.endsWith( COLOR_KEY_SUFFIX ) );
        colorKeys.stream().map( this::stripColorKeySuffix ).forEach( colorNames::add );

        return colorNames.toArray( new String[colorKeys.size()] );
    }

    private String stripColorKeySuffix( String colorKeyName ) {
        return colorKeyName.substring( 0, colorKeyName.indexOf( COLOR_KEY_SUFFIX ) );
    }

    @Override
    public String getColorHexCoding( String colorName ) {
        return persistenceModule.getStringValue( colorName + COLOR_KEY_SUFFIX ).trim();
    }
}
