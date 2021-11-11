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

import java.util.LinkedList;
import java.util.List;

import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.events.BFEventListener;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.system.events.BFEventListenerAdapter;
import de.mindscan.brightflux.system.videoannotator.events.VideoAnnotationVideoObjectClosedEvent;
import de.mindscan.brightflux.system.videoannotator.events.VideoAnnotationVideoObjectCreatedEvent;

/**
 * 
 */
public class VideoAnnotatorComponent implements ProjectRegistryParticipant {

    public static final String ANNOTATION_COLUMN_NAME = "videoAnnotation";

    private List<VideoAnnotatorVideoObject> videoAnnotationVideoObjects;

    private ProjectRegistry projectRegistry;

    // TODO how do we want to address the different videoAnnotationDataframes 

    /**
     * 
     */
    public VideoAnnotatorComponent() {
        this.videoAnnotationVideoObjects = new LinkedList<>();
    }

    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;

        registerVideoObjectCreatedHandler();
        registerVideoObjectClosedHandler();
    }

    private void registerVideoObjectCreatedHandler() {
        BFEventListener createdVideoObjectListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof BFVideoObjectEvent) {
                    registerVideoObject( ((BFVideoObjectEvent) event).getVideoObject() );
                }
            }
        };
        this.projectRegistry.getEventDispatcher().registerEventListener( VideoAnnotationVideoObjectCreatedEvent.class, createdVideoObjectListener );
    }

    private void registerVideoObjectClosedHandler() {
        BFEventListener closedVideoObjectListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof BFVideoObjectEvent) {
                    unregisterVideoObject( ((BFVideoObjectEvent) event).getVideoObject() );
                }
            }
        };
        this.projectRegistry.getEventDispatcher().registerEventListener( VideoAnnotationVideoObjectClosedEvent.class, closedVideoObjectListener );

    }

    public List<VideoAnnotatorVideoObject> getVideoAnnotationVideoObjects() {
        return videoAnnotationVideoObjects;
    }

    private void registerVideoObject( VideoAnnotatorVideoObject videoObject ) {
        videoAnnotationVideoObjects.add( videoObject );
    }

    private void unregisterVideoObject( VideoAnnotatorVideoObject videoObject ) {
        videoAnnotationVideoObjects.remove( videoObject );
    }
}
