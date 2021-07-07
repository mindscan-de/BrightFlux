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
package de.mindscan.brightflux.viewer.dispatcher;

import java.util.HashMap;
import java.util.Map;

import de.mindscan.brightflux.viewer.events.BFEvent;
import de.mindscan.brightflux.viewer.events.BFEventListener;

/**
 * 
 */
public class SimpleEventDispatcher implements EventDispatcher {

    // Refactor this whole listenerMap thing and the registration and deregistration
    // to some kind of Registry
    // TODO: add handlers map, for simplicity we have one listener per event...
    private Map<Class<?>, BFEventListener> listenerMap = new HashMap<>();

    /**
     * 
     */
    public SimpleEventDispatcher() {
        listenerMap = new HashMap<>();
    }

    // TODO: register handler for certain EventType -> TODO: introduce event types for the Events, e.g multiple event types for one event?
    // TODO: register handler for EventClass.class itself
    // TODO: allow list of listeners / handlers, to allow multiple consume
    public void registerEventListener( Class<?> eventType, BFEventListener listener ) {
        if (eventType == null || listener == null) {
            return;
        }
        listenerMap.put( eventType, listener );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public synchronized void dispatchEvent( BFEvent event ) {
        if (event == null) {
            return;
        }

        Class<? extends BFEvent> eventClass = event.getClass();

        // find event type / event class in map
        BFEventListener bfEventListener = listenerMap.get( eventClass );

        if (bfEventListener != null) {
            // then call all the event handlers.
            bfEventListener.handleEvent( event );
        }
    }

}
