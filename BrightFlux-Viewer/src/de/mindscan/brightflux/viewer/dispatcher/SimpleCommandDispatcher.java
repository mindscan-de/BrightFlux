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

import java.util.function.Consumer;

import de.mindscan.brightflux.viewer.commands.BFCommand;
import de.mindscan.brightflux.viewer.events.BFEvent;
import de.mindscan.brightflux.viewer.events.CommandExecutionExceptionEvent;
import de.mindscan.brightflux.viewer.events.CommandExecutionFinishedEvent;
import de.mindscan.brightflux.viewer.events.CommandExecutionStartedEvent;

/**
 * 
 */
public class SimpleCommandDispatcher implements CommandDispatcher {

    private final EventDispatcher eventDispatcher;

    public SimpleCommandDispatcher() {
        this.eventDispatcher = new SimpleEventDispatcher();
    }

    public SimpleCommandDispatcher( EventDispatcher eventDispatcher ) {
        this.eventDispatcher = eventDispatcher;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public synchronized void dispatchCommand( BFCommand command ) {
        Consumer<BFEvent> eventConsumer = eventDispatcher::dispatchEvent;

        // TODO: an multi threaded system would now just queue this command into a deque
        // but I'm kind of lazy right now, and just want to execute one command at a time.

        // (this execution should be part of the future worker threads, not of the dispatcher thread)
        try {
            // a logger or multiple loggers can consume these events 
            eventConsumer.accept( new CommandExecutionStartedEvent( command ) );
            // execute the event
            command.execute( eventConsumer );
            // a logger or multiple loggers can consume these events
            eventConsumer.accept( new CommandExecutionFinishedEvent( command ) );
        }
        catch (Exception ex) {
            ex.printStackTrace();
            // a logger or multiple loggers should consume these events
            eventConsumer.accept( new CommandExecutionExceptionEvent( command, ex ) );
        }
    }

}
