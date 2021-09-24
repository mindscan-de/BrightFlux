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
package de.mindscan.brightflux.framework.registry;

import de.mindscan.brightflux.framework.dispatcher.CommandDispatcher;
import de.mindscan.brightflux.framework.dispatcher.EventDispatcher;
import de.mindscan.brightflux.framework.dispatcher.SimpleCommandDispatcher;
import de.mindscan.brightflux.framework.dispatcher.SimpleEventDispatcher;

/**
 * TODO: maybe ProjectRegistry is the wrong name...
 */
public class ProjectRegistryImpl implements ProjectRegistry {

    private EventDispatcher eventDispatcher;
    private CommandDispatcher commandDispatcher;

    public ProjectRegistryImpl() {
        this( new SimpleEventDispatcher() );
    }

    public ProjectRegistryImpl( EventDispatcher eventdispatcher ) {
        this( eventdispatcher, new SimpleCommandDispatcher( eventdispatcher ) );
    }

    public ProjectRegistryImpl( EventDispatcher eventdispatcher, CommandDispatcher commandDispatcher ) {
        this.eventDispatcher = eventdispatcher;
        this.commandDispatcher = commandDispatcher;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public CommandDispatcher getCommandDispatcher() {
        return commandDispatcher;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void registerParticipant( ProjectRegistryParticipant participant ) {
        // TODO put this element to a list
        // and then execute the initializion according to the list.
        participant.setProjectRegistry( this );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void completeParticipantRegistration() {
        // TODO implement using the queue.
    }

    // TODO: work on a mechanism that if the loaded files change, then we want to react on that....
    // 
    // addProjectListener
    // removeProjectListener
}
