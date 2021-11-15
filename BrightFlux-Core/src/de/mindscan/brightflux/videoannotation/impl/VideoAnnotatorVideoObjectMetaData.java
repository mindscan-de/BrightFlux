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

/**
 * Metadata for a Video Annotation Video Object. Data class was also extracted to be better able to
 * serialize and unserialize videoObjects to/from disk.
 */
public class VideoAnnotatorVideoObjectMetaData {

    /**
     * Path to the annotated video - may differ from system to system... unstable information ...  
     */
    private Path videoObjectPath;

    /**
     * Length (Time) of the video in seconds 
     */
    private int videoDurationInSeconds;

    /**
     * UUID of the Video Object Identifier - will be used to address the videos in the application or storage
     */
    private UUID videoObjectUUID;

    // ----

    public VideoAnnotatorVideoObjectMetaData( Path videoObjectPath, int videoDurationInSeconds ) {
        this( videoObjectPath, videoDurationInSeconds, UUID.randomUUID() );
    }

    public VideoAnnotatorVideoObjectMetaData( Path videoObjectPath, int videoDurationInSeconds, UUID uuid ) {
        this.videoObjectPath = videoObjectPath;
        this.videoDurationInSeconds = videoDurationInSeconds;
        this.videoObjectUUID = uuid;
    }

    public UUID getVideoObjectUUID() {
        return videoObjectUUID;
    }

    public int getVideoDurationInSeconds() {
        return videoDurationInSeconds;
    }

    public Path getVideoObjectPath() {
        return videoObjectPath;
    }

}