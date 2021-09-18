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

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.system.commands.annotations.CreateAnnoationDataFrameCommand;
import de.mindscan.brightflux.system.commands.annotations.DataFrameAnnotateRowCommand;
import de.mindscan.brightflux.system.commands.dataframe.DataFrameFilterCommand;
import de.mindscan.brightflux.system.commands.dataframe.DataFrameQueryCommand;
import de.mindscan.brightflux.system.commands.dataframe.DataFrameSelectAndFilterCommand;
import de.mindscan.brightflux.system.commands.ingest.IngestCommand;
import de.mindscan.brightflux.system.commands.ingest.IngestSpecialRAW;
import de.mindscan.brightflux.system.commands.recipes.RecipeSaveCommand;

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

    public static BFCommand filterDataFrame( DataFrame inputDataFrame, DataFrameRowFilterPredicate predicate ) {
        return new DataFrameFilterCommand( inputDataFrame, predicate );
    }

    public static BFCommand selectAndFilterDataFrame( DataFrame inputDataFrame, String[] columnNames, DataFrameRowFilterPredicate predicate ) {
        return new DataFrameSelectAndFilterCommand( inputDataFrame, columnNames, predicate );
    }

    public static BFCommand queryDataFrame( DataFrame inputDataFrame, String queryString ) {
        return new DataFrameQueryCommand( inputDataFrame, queryString );
    }

    public static BFCommand applyRecipe( DataFrame inputDataFrame, Path recipe ) {
        return new RecipeExecuteCommand( inputDataFrame, recipe );
    }

    public static BFCommand saveRecipe( DataFrame inputDataFrame, Path targetFile ) {
        return new RecipeSaveCommand( inputDataFrame, targetFile );
    }

    public static BFCommand saveCSV( DataFrame inputDataFrame, Path targetFile ) {
        return new SaveAsCSVCommand( inputDataFrame, targetFile );
    }

    public static BFCommand alterDataframeAddColumn( DataFrame inputDataFrame, String newColumnName ) {
        return null;
    }

    public static BFCommand createSparseDataFrame() {
        return new CreateAnnoationDataFrameCommand();
    }

    public static BFCommand annotateRow( DataFrame inputDataFrame, int rowIndex, String annotation ) {
        return new DataFrameAnnotateRowCommand( inputDataFrame, rowIndex, annotation );

    }

}
