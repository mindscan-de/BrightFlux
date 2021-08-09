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
package de.mindscan.brightflux.system.registry;

import de.mindscan.brightflux.system.dispatcher.CommandDispatcher;
import de.mindscan.brightflux.system.dispatcher.EventDispatcher;
import de.mindscan.brightflux.system.dispatcher.SimpleCommandDispatcher;
import de.mindscan.brightflux.system.dispatcher.SimpleEventDispatcher;

/**
 * TODO: maybe ProjectRegistry is the wrong name...
 */
public class ProjectRegistryImpl implements ProjectRegistry {

    private EventDispatcher eventDispatcher;
    private SimpleCommandDispatcher commandDispatcher;

    static class Holder {
        static ProjectRegistry instance = new ProjectRegistryImpl();
    }

    public static ProjectRegistry getInstance() {
        return Holder.instance;
    }

    public ProjectRegistryImpl() {
        eventDispatcher = new SimpleEventDispatcher();
        commandDispatcher = new SimpleCommandDispatcher( eventDispatcher );
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

    // TODO: work on a mechanism that if the loaded files change, then we want to react on that....
    // 
    // addProjectListener
    // removeProjectListener
}
