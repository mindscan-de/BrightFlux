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
package de.mindscan.brightflux.system.commands;

import java.nio.file.Path;
import java.util.Map;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.DataFrameRowQueryCallback;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.system.annotator.commands.CreateAnnoationDataFrameCommand;
import de.mindscan.brightflux.system.annotator.commands.DataFrameAnnotateRowCommand;
import de.mindscan.brightflux.system.annotator.commands.LoadAnnotationDataFrameCommand;
import de.mindscan.brightflux.system.annotator.commands.SaveAnnotationDataFrameCommand;
import de.mindscan.brightflux.system.commands.dataframe.DataFrameFilterCommand;
import de.mindscan.brightflux.system.commands.dataframe.DataFrameQueryCBCommand;
import de.mindscan.brightflux.system.commands.dataframe.DataFrameQueryCommand;
import de.mindscan.brightflux.system.commands.dataframe.DataFrameSelectAndFilterCommand;
import de.mindscan.brightflux.system.commands.ingest.IngestCommand;
import de.mindscan.brightflux.system.commands.ingest.IngestSpecialHXX;
import de.mindscan.brightflux.system.commands.ingest.IngestSpecialRAW;
import de.mindscan.brightflux.system.commands.io.ExpandProprietaryZipStreamCommand;
import de.mindscan.brightflux.system.commands.io.SaveAsCSVCommand;
import de.mindscan.brightflux.system.commands.recipes.RecipeExecuteCommand;
import de.mindscan.brightflux.system.commands.recipes.RecipeSaveCommand;
import de.mindscan.brightflux.system.highlighter.commands.CreateHighlightDataFrameCommand;
import de.mindscan.brightflux.system.highlighter.commands.DataFrameClearHighlightRowCommand;
import de.mindscan.brightflux.system.highlighter.commands.DataFrameHighlightRowCommand;
import de.mindscan.brightflux.system.highlighter.commands.LoadHighlightDataFrameCommand;
import de.mindscan.brightflux.system.highlighter.commands.SaveHighlightDataFrameCommand;
import de.mindscan.brightflux.system.videoannotator.commands.LoadVideoAnnotationFromFileCommand;
import de.mindscan.brightflux.system.videoannotator.commands.LoadVideoForAnnotationCommand;
import de.mindscan.brightflux.system.videoannotator.commands.SaveVideoAnnotationToFileCommand;
import de.mindscan.brightflux.videoannotation.VideoAnnotatorVideoObject;

/**
 * This class provides commands around DataFrames 
 */
public class DataFrameCommandFactory {

    /**
     * This method will create an {@link IngestCommand} which when executed will provide a DataFrame.
     * @param filePath The path to the file containing the data to ingest.
     * @return a command
     */
    public static BFCommand ingestFile( Path filePath ) {
        return new IngestCommand( filePath );
    }

    public static BFCommand ingestSpecialRaw( Path filePath ) {
        return new IngestSpecialRAW( filePath );
    }

    public static BFCommand ingestSpecialHXX( DataFrame inputDataFrame, String inputColumn ) {
        return new IngestSpecialHXX( inputDataFrame, inputColumn );
    }

    public static BFCommand filterDataFrame( DataFrame inputDataFrame, DataFrameRowFilterPredicate predicate ) {
        return new DataFrameFilterCommand( inputDataFrame, predicate );
    }

    public static BFCommand selectAndFilterDataFrame( DataFrame inputDataFrame, String[] columnNames, DataFrameRowFilterPredicate predicate ) {
        return new DataFrameSelectAndFilterCommand( inputDataFrame, columnNames, predicate );
    }

    public static BFCommand queryDataFrame( DataFrame inputDataFrame, String queryString ) {
        return new DataFrameQueryCommand( inputDataFrame, queryString );
    }

    public static BFCommand queryCBDataFrame( DataFrame inputDataFrame, String queryString, Map<String, DataFrameRowQueryCallback> callbacks ) {
        return new DataFrameQueryCBCommand( inputDataFrame, queryString, callbacks );
    }

    public static BFCommand applyRecipe( DataFrame inputDataFrame, Path recipe ) {
        return new RecipeExecuteCommand( inputDataFrame, recipe );
    }

    public static BFCommand applyRecipe( DataFrame inputDataFrame, Path recipe, Map<String, DataFrameRowQueryCallback> callbacks ) {
        return new RecipeExecuteCommand( inputDataFrame, recipe, callbacks );
    }

    public static BFCommand saveRecipe( DataFrame inputDataFrame, Path targetFile ) {
        return new RecipeSaveCommand( inputDataFrame, targetFile );
    }

    public static BFCommand saveCSV( DataFrame inputDataFrame, Path targetFile ) {
        return new SaveAsCSVCommand( inputDataFrame, targetFile );
    }

    public static BFCommand createSparseDataFrame() {
        return new CreateAnnoationDataFrameCommand();
    }

    public static BFCommand saveAnnotationDataFrame( DataFrame annotationDataFrame, Path targetFilePath ) {
        return new SaveAnnotationDataFrameCommand( annotationDataFrame, targetFilePath );
    }

    public static BFCommand loadAnnotationDataFrame( Path annotationFilePath ) {
        return new LoadAnnotationDataFrameCommand( annotationFilePath );
    }

    public static BFCommand annotateRow( DataFrame inputDataFrame, int rowIndex, String annotation ) {
        return new DataFrameAnnotateRowCommand( inputDataFrame, rowIndex, annotation );
    }

    public static BFCommand createHighlightDataFrame() {
        return new CreateHighlightDataFrameCommand();
    }

    public static BFCommand saveHighlightDataFrame( DataFrame highlightDataFrame, Path targetFilePath ) {
        return new SaveHighlightDataFrameCommand( highlightDataFrame, targetFilePath );
    }

    public static BFCommand loadHighlightDataFrame( Path highlightFilePath ) {
        return new LoadHighlightDataFrameCommand( highlightFilePath );
    }

    public static BFCommand highlightRow( DataFrame inputDataFrame, int rowIndex, String color ) {
        return new DataFrameHighlightRowCommand( inputDataFrame, rowIndex, color );
    }

    public static BFCommand clearHighlightRow( DataFrame ingestedDF, int rowIndex ) {
        return new DataFrameClearHighlightRowCommand( ingestedDF, rowIndex );
    }

    public static BFCommand expandFile( Path filePath ) {
        return new ExpandProprietaryZipStreamCommand( filePath );
    }

    public static BFCommand loadVideoForAnnotation( Path videoObjectPath ) {
        return new LoadVideoForAnnotationCommand( videoObjectPath );
    }

    public static BFCommand loadVideoAnnotationFromFile( Path videoAnnotationPath ) {
        return new LoadVideoAnnotationFromFileCommand( videoAnnotationPath );
    }

    public static BFCommand saveVideoAnnotationToFile( VideoAnnotatorVideoObject videoObject, Path videoAnnotationTargetPath ) {
        return new SaveVideoAnnotationToFileCommand( videoObject, videoAnnotationTargetPath );
    }

}
