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
package de.mindscan.brightflux.system.videoannotator.commands;

import java.util.function.Consumer;

import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.videoannotation.VideoAnnotatorVideoObject;

/**
 * 
 */
public class LinkVideoAnnoationToDataFrameRowCommand implements BFCommand {

    /**
     * 
     */
    private static final long SECOND_IN_NANOSECONDS = 1000000000L;
    private VideoAnnotatorVideoObject videoObject;
    private DataFrameRow linkedDataFrameRow;
    private String[] linkedColumnNames;
    private long videoPositionInSec;

    /**
     * 
     */
    public LinkVideoAnnoationToDataFrameRowCommand( int videoPositionInSec, VideoAnnotatorVideoObject videoObject, String[] columnNames,
                    DataFrameRow linkedDataFrameRow ) {
        this.videoPositionInSec = videoPositionInSec;
        this.videoObject = videoObject;
        this.linkedColumnNames = columnNames;
        this.linkedDataFrameRow = linkedDataFrameRow;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void execute( Consumer<BFEvent> eventConsumer ) {

        // TODO Link the dataframerow 
        //  
        // The videoObject the linkedColumnNames, should be registered
        // 
        // * We will currently use our hard knowledge about the logs 

        for (String columnName : linkedColumnNames) {
            // TODO: check if column exists in dataFrameRow -> continue 
            // then
            // * add reference Point
            if (linkedDataFrameRow.get( columnName ) != null) {

                // Add reference point
                long referenceTimestamp = (Long) linkedDataFrameRow.get( columnName );
                addReferencePoint( videoPositionInSec, columnName, referenceTimestamp );

                // TODO: (Quick hack. 
                // For simplicity we will assume a starting point rather than calculating it by a second reference
                // if we have another reference point, we don't need this scaling thing, which is now hardcoded
                // as soon as we have two we can virtually calculate the starting positon

                // TODO: maybe this should move to the VideoObjectCalculation later
                // * add start point (0, columnname, as reference Point)
                referenceTimestamp -= ((long) videoPositionInSec) * SECOND_IN_NANOSECONDS;
                addReferencePoint( 0, columnName, referenceTimestamp );

                // * add stop point (videolength as reference Point)
                referenceTimestamp += ((long) videoObject.getVideoDurationInSeconds()) * SECOND_IN_NANOSECONDS;
                addReferencePoint( videoObject.getVideoDurationInSeconds(), columnName, referenceTimestamp );
            }
        }
    }

    private void addReferencePoint( long videoPositionInSec2, String columnName, long referenceTimestamp ) {
        // TODO implement a method to add reference points to the videoObject time stamp.
    }

}
