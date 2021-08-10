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

import java.util.function.Consumer;

import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.events.CommandExecutionExceptionEvent;
import de.mindscan.brightflux.framework.events.CommandExecutionFinishedEvent;
import de.mindscan.brightflux.framework.events.CommandExecutionStartedEvent;
import de.mindscan.brightflux.system.commands.BFCommand;

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

        /*
         * TODO: Attention:
         * 
         * In a multi-threaded system this would now just queue this command into a deque 
         * or something less/non blocking. 
         * 
         * But I'm kind of lazy right now, and just want to execute each command at the 
         * time dispatched. A different Dispatcher can be implemented on a need-by-need
         * basis.
         * 
         * Also a dispatcher is not an executor...
         */

        executeCommand( command, eventConsumer );
    }

    // TODO: This code will be extracted in future to an execution thread
    private void executeCommand( BFCommand command, Consumer<BFEvent> eventConsumer ) {
        // (this execution should be part of the future worker threads, not of the dispatcher thread)
        // (but good enough for now)
        try {
            // a logger or multiple loggers can consume these events 
            // also other instances can subscribe to these events, like progress monitors and such
            eventConsumer.accept( new CommandExecutionStartedEvent( command ) );

            // execute the command
            // the command is executed on an aggregate (e.g. the parameters given at the creation of the command)
            // and this generates events
            // these events can then trigger a listener, 
            // which presents something to the user or invokes another command
            command.execute( eventConsumer );

            // a logger or multiple loggers can consume these events
            // also other instances can subscribe to these events, like progress monitors and such
            eventConsumer.accept( new CommandExecutionFinishedEvent( command ) );
        }
        catch (Exception ex) {
            ex.printStackTrace();
            // a logger or multiple loggers should consume these events
            eventConsumer.accept( new CommandExecutionExceptionEvent( command, ex ) );
        }
    }

}
