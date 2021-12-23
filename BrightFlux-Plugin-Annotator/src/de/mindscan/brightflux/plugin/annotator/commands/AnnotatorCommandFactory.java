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
package de.mindscan.brightflux.plugin.annotator.commands;

import java.nio.file.Path;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.system.annotator.commands.CreateAnnoationDataFrameCommand;
import de.mindscan.brightflux.system.annotator.commands.DataFrameAnnotateRowCommand;
import de.mindscan.brightflux.system.annotator.commands.LoadAnnotationDataFrameCommand;
import de.mindscan.brightflux.system.annotator.commands.SaveAnnotationDataFrameCommand;

/**
 * 
 */
public class AnnotatorCommandFactory {

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

}