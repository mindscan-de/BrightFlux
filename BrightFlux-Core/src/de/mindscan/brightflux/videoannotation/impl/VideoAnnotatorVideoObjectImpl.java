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
package de.mindscan.brightflux.videoannotation.impl;

import java.nio.file.Path;
import java.util.UUID;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameSpecialColumns;
import de.mindscan.brightflux.videoannotation.VideoAnnotationColumns;
import de.mindscan.brightflux.videoannotation.VideoAnnotatorVideoObject;

/**
 * 
 */
public class VideoAnnotatorVideoObjectImpl implements VideoAnnotatorVideoObject {

    private DataFrame videoAnnotationDataFrame;
    private final VideoAnnotatorVideoObjectMetaData metaData;
    private final VideoAnnotatorVideoObjectRefTimestampData refTimestampData;

    public VideoAnnotatorVideoObjectImpl( DataFrame df, Path videoObjectPath ) {
        this.videoAnnotationDataFrame = df;

        int videoDurationInSeconds = 600;

        this.metaData = new VideoAnnotatorVideoObjectMetaData( videoObjectPath, videoDurationInSeconds );
        this.refTimestampData = new VideoAnnotatorVideoObjectRefTimestampData();
    }

    public VideoAnnotatorVideoObjectImpl( DataFrame df, Path videoObjectPath, UUID uuid ) {
        this.videoAnnotationDataFrame = df;

        int videoDurationInSeconds = 600;

        this.metaData = new VideoAnnotatorVideoObjectMetaData( videoObjectPath, videoDurationInSeconds, uuid );
        this.refTimestampData = new VideoAnnotatorVideoObjectRefTimestampData();
    }

    public VideoAnnotatorVideoObjectImpl( DataFrame df, VideoAnnotatorVideoObjectMetaData metaData,
                    VideoAnnotatorVideoObjectRefTimestampData refTimestampData ) {
        this.videoAnnotationDataFrame = df;
        this.metaData = metaData;
        this.refTimestampData = refTimestampData;
    }

    @Override
    public int getVideoDurationInSeconds() {
        return metaData.getVideoDurationInSeconds();
    }

    @Override
    public void setVideoDurationInSeconds( int durationInSeconds ) {
        metaData.setVideoDurationInSeconds( durationInSeconds );
    }

    @Override
    public Path getVideoObjectPath() {
        return metaData.getVideoObjectPath();
    }

    @Override
    public DataFrame getVideoAnnotationDataFrame() {
        return videoAnnotationDataFrame;
    }

    @Override
    public String getSimpleName() {
        return metaData.getVideoObjectPath().getFileName().toString();
    }

    @Override
    public UUID getUUID() {
        return metaData.getVideoObjectUUID();
    }

    @Override
    public boolean isAnnotationPresentForTimestamp( int timestamp ) {
        return videoAnnotationDataFrame.isPresent( VideoAnnotationColumns.ANNOTATION_COLUMN_NAME, timestamp );
    }

    @Override
    public void setAnnotationForTimestamp( int timestamp, String annotation ) {
        if (annotation != null && !annotation.isBlank()) {
            videoAnnotationDataFrame.setAt( DataFrameSpecialColumns.INDEX_COLUMN_NAME, timestamp, timestamp );
            videoAnnotationDataFrame.setAt( VideoAnnotationColumns.ANNOTATION_COLUMN_NAME, timestamp, annotation );
        }
        else {
            // clear that row - but beware if we add more information to this row... 
            videoAnnotationDataFrame.setNA( DataFrameSpecialColumns.INDEX_COLUMN_NAME, timestamp );
            videoAnnotationDataFrame.setNA( VideoAnnotationColumns.ANNOTATION_COLUMN_NAME, timestamp );
        }
    }

    @Override
    public String getAnnotationForTimestamp( int timestamp ) {
        if (videoAnnotationDataFrame.isPresent( DataFrameSpecialColumns.INDEX_COLUMN_NAME, timestamp )) {
            if (videoAnnotationDataFrame.isPresent( VideoAnnotationColumns.ANNOTATION_COLUMN_NAME, timestamp )) {
                return String.valueOf( videoAnnotationDataFrame.getAt( VideoAnnotationColumns.ANNOTATION_COLUMN_NAME, timestamp ) );
            }
        }
        return "";
    }

    /**
     * @return the metaData
     */
    @Override
    public VideoAnnotatorVideoObjectMetaData getMetaData() {
        return metaData;
    }

    /**
     * @return the refTimestamps
     */
    @Override
    public VideoAnnotatorVideoObjectRefTimestampData getRefTimestampData() {
        return refTimestampData;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void registerReferenceTimestampForColumn( long videoPosition, String columnName, long referenceTimestamp ) {
        refTimestampData.registerReferenceTimestampForColumn( videoPosition, columnName, referenceTimestamp );
    }

    @Override
    public void clearReferenceTimestamps() {
        refTimestampData.clearReferenceTimestamps();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public long predictTimestampForColumn( long videoPosition, String columnName ) {

        int videoLength = this.getVideoDurationInSeconds();

        // TODO: refactor this to the ref timestamp data class
        long tsStart = refTimestampData.videoPositionAt( 0, columnName );
        long tsEnd = refTimestampData.videoPositionAt( videoLength, columnName ) + 1;

        long predicted = tsStart + (((tsEnd - tsStart) * videoPosition) / Math.max( (long) videoLength, 1L ));

        return predicted;
    }
}
