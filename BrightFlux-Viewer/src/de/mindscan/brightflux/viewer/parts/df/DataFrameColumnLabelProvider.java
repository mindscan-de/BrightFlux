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
package de.mindscan.brightflux.viewer.parts.df;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.plugin.highlighter.HighlighterComponent;
import de.mindscan.brightflux.viewer.uiplugin.highlighter.HighlighterUIComponent;

/**
 * TODO: the Label provider should be dependent of either configuration
 *       of presentation strategy and the internal column type.
 * TODO: we only use toString as the provider for the content, but this 
 *       is not sufficient, e.g. Values which are null / not available /
 *       or should be displayed in a different way. 
 * TODO: maybe a factory should take care of it.
 */
public class DataFrameColumnLabelProvider extends ColumnLabelProvider {

    // some predefined colors
    // TODO: get rid of these hard coded colors in the label provider.
    private static final Color YELLOW = new Color( 255, 255, 224 );
    private static final Color PINK = new Color( 255, 224, 255 );
    private static final Color RED = new Color( 255, 224, 224 );
    private static final Color GREEN = new Color( 224, 255, 224 );
    private static final Color BLUE = new Color( 224, 224, 255 );

    private final String columname;
    private HighlighterComponent highlighterComponent;
    private HighlighterUIComponent hilighterUIComponent;

    /**
     * @param columname The column name of the column determines, what column is shown from the data frame rows.
     */
    public DataFrameColumnLabelProvider( String columname ) {
        this.columname = columname;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public String getText( Object element ) {
        if (element == null) {
            return "";
        }

        DataFrameRow row = (DataFrameRow) element;

        Object object = row.get( columname );
        if (object == null) {
            return "";
        }

        return object.toString();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public Color getBackground( Object element ) {
        if (element == null) {
            return super.getBackground( element );
        }

        if (highlighterComponent == null) {
            return super.getBackground( element );
        }

        DataFrame logHighlightFrame = highlighterComponent.getLogHighlightFrame();

        if (logHighlightFrame == null) {
            return super.getBackground( element );
        }

        DataFrameRow row = (DataFrameRow) element;

        int originalRowIndex = row.getOriginalRowIndex();

        // check in the highlight frame, whether this rowindex contains a color
        if (logHighlightFrame.isPresent( HighlighterComponent.HIGHLIGHT_COLOR_VALUE_COLUMN_NAME, originalRowIndex )) {
            String color = (String) logHighlightFrame.getAt( HighlighterComponent.HIGHLIGHT_COLOR_VALUE_COLUMN_NAME, originalRowIndex );

            // TODO: ask the HighlighterUI to provide a Color for this colorname

            switch (color) {
                case "yellow":
                    return YELLOW;
                case "pink":
                    return PINK;
                case "red":
                    return RED;
                case "green":
                    return GREEN;
                case "blue":
                    return BLUE;
                default:
                    break;
            }
        }

        return super.getBackground( element );

    }

    public void setHighLightherComponent( HighlighterComponent highlighterComponent ) {
        this.highlighterComponent = highlighterComponent;
    }

    public void setHighlighterUIComponent( HighlighterUIComponent highlighterUIComponent ) {
        this.hilighterUIComponent = highlighterUIComponent;
    }

}