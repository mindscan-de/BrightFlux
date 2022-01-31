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
package de.mindscan.brightflux.viewer.uiplugin.highlighter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;

import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.viewer.uiplugin.highlighter.persistence.HighlighterUIPersistenceModule;

/**
 * 
 */
public class HighlighterUIComponent implements ProjectRegistryParticipant {

    private final Map<String, Color> colorMap;
    private HighlighterUIPersistenceModule persistenceModule;

    public HighlighterUIComponent() {
        this.colorMap = new HashMap<>();

        initColors();
    }

    private void initColors() {
        // TODO: externalize this information into persistence.
        // Hard coded color map, which we may want to put into a persistence configuration file as well
        this.colorMap.put( "yellow", new Color( 255, 255, 224 ) );
        this.colorMap.put( "pink", new Color( 255, 224, 255 ) );
        this.colorMap.put( "red", new Color( 255, 224, 224 ) );
        this.colorMap.put( "green", new Color( 224, 255, 224 ) );
        this.colorMap.put( "blue", new Color( 224, 240, 255 ) );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        // intentionally left blank for now.
    }

    public Color getColor( String colorName ) {
        return colorMap.computeIfAbsent( colorName, this::calculateColor );
    }

    private Color calculateColor( String colorName ) {
        if (colorName.startsWith( "#" )) {
            return convertHexCodeColor( colorName );
        }

        return null;
    }

    public static Color convertHexCodeColor( String colorName ) {
        String reH = colorName.substring( 1, 3 );
        String grH = colorName.substring( 3, 5 );
        String blH = colorName.substring( 5, 7 );

        return new Color( Integer.parseInt( reH, 16 ), Integer.parseInt( grH, 16 ), Integer.parseInt( blH, 16 ) );
    }

    public void setPersistenceModule( HighlighterUIPersistenceModule persistenceModule ) {
        this.persistenceModule = persistenceModule;
    }

}
