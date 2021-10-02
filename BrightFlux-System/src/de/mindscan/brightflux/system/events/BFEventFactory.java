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
package de.mindscan.brightflux.system.events;

import java.nio.file.Path;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.system.annotator.events.AnnotationDataFrameCreatedEvent;
import de.mindscan.brightflux.system.annotator.events.DataFrameAnnotateRowEvent;
import de.mindscan.brightflux.system.events.dataframe.DataFrameClosedEvent;
import de.mindscan.brightflux.system.events.dataframe.DataFrameCreatedEvent;
import de.mindscan.brightflux.system.events.dataframe.DataFrameLoadedEvent;
import de.mindscan.brightflux.system.events.recipes.RecipeSaveResultEvent;
import de.mindscan.brightflux.system.highlighter.events.DataFrameClearHighlightRowEvent;
import de.mindscan.brightflux.system.highlighter.events.DataFrameHighlightRowEvent;
import de.mindscan.brightflux.system.highlighter.events.HighlighterDataFrameCreatedEvent;

/**
 * 
 */
public class BFEventFactory {

    public static BFEvent dataframeLoaded( DataFrame dataFrame ) {
        return new DataFrameLoadedEvent( dataFrame );
    }

    public static BFEvent dataframeCreated( DataFrame dataFrame ) {
        return new DataFrameCreatedEvent( dataFrame );
    }

    public static BFEvent dataframeClosed( DataFrame dataFrame ) {
        return new DataFrameClosedEvent( dataFrame );
    }

    public static BFEvent dataframeCreated( DataFrame dataFrame, DataFrame parentDataFrame ) {
        if (parentDataFrame == null) {
            return new DataFrameCreatedEvent( dataFrame );
        }

        return new DataFrameCreatedEvent( dataFrame, parentDataFrame.getUuid().toString() );
    }

    public static BFEvent annotationDataframeCreated( DataFrame dataFrame ) {
        return new AnnotationDataFrameCreatedEvent( dataFrame );
    }

    public static BFEvent recipeSaveSucceeded( Path targetFile ) {
        return new RecipeSaveResultEvent( targetFile, true );
    }

    public static BFEvent recipeSaveFailed( Path targetFile ) {
        return new RecipeSaveResultEvent( targetFile, false );
    }

    public static BFEvent recipeSaveResult( Path targetFile, boolean success ) {
        return new RecipeSaveResultEvent( targetFile, success );
    }

    public static DataFrameAnnotateRowEvent annotateDataFrameRow( DataFrame inputDataFrame, int row, String annotation ) {
        return new DataFrameAnnotateRowEvent( inputDataFrame, row, annotation );
    }

    public static BFEvent highlightDataframeCreated( DataFrame newDataFrame ) {
        return new HighlighterDataFrameCreatedEvent( newDataFrame );
    }

    public static BFEvent highlightDataFrameRow( DataFrame inputDataFrame, int row, String color ) {
        return new DataFrameHighlightRowEvent( inputDataFrame, row, color );
    }

    public static BFEvent clearHighlightDataFrameRow( DataFrame inputDataFrame, int row ) {
        return new DataFrameClearHighlightRowEvent( inputDataFrame, row );
    }

}
