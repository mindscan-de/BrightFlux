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
package de.mindscan.brightflux.framework.dispatcher;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.events.BFEventListener;

/**
 * 
 */
public class SimpleEventDispatcher implements EventDispatcher {

    // Refactor this whole listenerMap thing and the registration and deregistration
    // to some kind of Registry
    // TODO: add handlers map, for simplicity we have one listener per event...
    private Map<Class<?>, Set<BFEventListener>> listenerMap = new HashMap<>();

    /**
     * 
     */
    public SimpleEventDispatcher() {
        listenerMap = new HashMap<>();
    }

    // TODO: register handler for certain EventType -> TODO: introduce event types for the Events, e.g multiple event types for one event?
    // TODO: register handler for EventClass.class itself
    // TODO: allow list of listeners / handlers, to allow multiple consume
    @Override
    public void registerEventListener( Class<?> eventType, BFEventListener listener ) {
        if (eventType == null || listener == null) {
            return;
        }

        listenerMap.computeIfAbsent( eventType, k -> new HashSet<>() ).add( listener );
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
        Collection<BFEventListener> bfEventListeners = listenerMap.get( eventClass );

        if (bfEventListeners != null) {
            for (BFEventListener bfEventListener : bfEventListeners) {
                // then call (all) the event handler(s).
                try {
                    bfEventListener.handleEvent( event );
                }
                catch (Exception ex) {
                    ex.printStackTrace();

                    // We should maybe dispatch some info, that there was an error while dispatching an event...
                    // Maybe we should have a failsafe-event dispatcher, which will never fail and consume errors into a log file.
                }
            }
        }
    }

}
