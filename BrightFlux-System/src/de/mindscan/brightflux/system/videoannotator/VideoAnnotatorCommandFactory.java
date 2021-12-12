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

import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.system.videoannotator.commands.LinkVideoAnnoationToDataFrameRowCommand;
import de.mindscan.brightflux.system.videoannotator.commands.LoadVideoAnnotationFromFileCommand;
import de.mindscan.brightflux.system.videoannotator.commands.LoadVideoForAnnotationCommand;
import de.mindscan.brightflux.system.videoannotator.commands.SaveVideoAnnotationToFileCommand;
import de.mindscan.brightflux.system.videoannotator.commands.UnlinkVideoAnnotationToDataFrameRowsCommand;
import de.mindscan.brightflux.videoannotation.VideoAnnotatorVideoObject;

/**
 * 
 */
public class VideoAnnotatorCommandFactory {

    public static BFCommand loadVideoForAnnotation( Path videoObjectPath, VideoObjectInformation videoObjectInfo ) {
        return new LoadVideoForAnnotationCommand( videoObjectPath, videoObjectInfo );
    }

    public static BFCommand linkVideoAnnotationToDataFrame( int videoPosInSeconds, VideoAnnotatorVideoObject videoObject, String[] linkedColumns,
                    DataFrameRow linkedDataFrameRow ) {
        return new LinkVideoAnnoationToDataFrameRowCommand( videoPosInSeconds, videoObject, linkedColumns, linkedDataFrameRow );
    }

    public static BFCommand unlinkVideoAnnotationFromDataFrame( VideoAnnotatorVideoObject videoObject ) {
        return new UnlinkVideoAnnotationToDataFrameRowsCommand( videoObject );
    }

    public static BFCommand loadVideoAnnotationFromFile( Path videoAnnotationPath ) {
        return new LoadVideoAnnotationFromFileCommand( videoAnnotationPath );
    }

    public static BFCommand saveVideoAnnotationToFile( VideoAnnotatorVideoObject videoObject, Path videoAnnotationTargetPath ) {
        return new SaveVideoAnnotationToFileCommand( videoObject, videoAnnotationTargetPath );
    }

}
