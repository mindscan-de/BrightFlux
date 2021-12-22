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
package de.mindscan.brightflux.viewer.parts.search;

import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.system.services.StartupParticipant;
import de.mindscan.brightflux.system.services.SystemServices;

/**
 * 
 */
public class SearchUIActivator implements StartupParticipant {

    /** 
     * {@inheritDoc}
     */
    @Override
    public void start( SystemServices systemservices ) {

        SearchUIProxyComponent searchUIComponent = new SearchUIProxyComponent();
        systemservices.registerService( searchUIComponent, SearchUIProxyComponent.class );

        ProjectRegistry projectRegistry = systemservices.getProjectRegistry();
        if (projectRegistry != null) {
            searchUIComponent.setProjectRegistry( projectRegistry );
        }
        else {

        }
    }

}
