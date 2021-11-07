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
package de.mindscan.brightflux.system.videoannotator;

import java.nio.file.Path;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameBuilder;
import de.mindscan.brightflux.dataframes.DataFrameSpecialColumns;
import de.mindscan.brightflux.dataframes.columntypes.ColumnTypes;
import de.mindscan.brightflux.system.videoannotator.impl.VideoAnnotatorVideoObjectImpl;

/**
 * 
 */
public class VideoAnnotatorVideoObjectFactory {

    public static VideoAnnotatorVideoObject createVideoObject( Path videoObjectPath ) {
        String filename = videoObjectPath.getFileName().toString();
    
        DataFrame df = new DataFrameBuilder( filename ) //
                        .addColumn( DataFrameSpecialColumns.INDEX_COLUMN_NAME, ColumnTypes.COLUMN_TYPE_SPARSE_INT ) //
                        // referenced Column id?
                        .addColumn( DataFrameSpecialColumns.ORIGINAL_INDEX_COLUMN_NAME, ColumnTypes.COLUMN_TYPE_SPARSE_INT ) //
                        // TODO: 64bit Timestamps / referenced Dataframe UUID? / referenced datacolumn?
                        // 
                        .addColumn( VideoAnnotatorComponent.ANNOTATION_COLUMN_NAME, ColumnTypes.COLUMN_TYPE_SPARSE_STRING ) //
                        .build();
    
        VideoAnnotatorVideoObject videoObject = new VideoAnnotatorVideoObjectImpl( df, videoObjectPath );
        return videoObject;
    }

}
