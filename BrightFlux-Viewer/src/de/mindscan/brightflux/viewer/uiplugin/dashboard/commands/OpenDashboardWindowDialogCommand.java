/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
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
package de.mindscan.brightflux.viewer.uiplugin.dashboard.commands;

import java.util.function.Consumer;

import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.system.services.SystemServices;
import de.mindscan.brightflux.viewer.uicommands.UIBFBackgroundCommand;
import de.mindscan.brightflux.viewer.uicommands.UIBFCommand;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.DashboardUIProxyComponent;

/**
 * 
 */
public class OpenDashboardWindowDialogCommand implements UIBFCommand, UIBFBackgroundCommand {

    /** 
     * {@inheritDoc}
     */
    @Override
    public void execute( Consumer<BFEvent> eventConsumer ) {
        SystemServices systemServices = SystemServices.getInstance();

        DashboardUIProxyComponent dashboardUIProxyComponent = systemServices.getService( DashboardUIProxyComponent.class );
        if (dashboardUIProxyComponent.hasCurrentActiveDashboardWindow()) {
            dashboardUIProxyComponent.focusCurrentActiveDashboardWindow();
        }
        else {
            // create a new instance
            // TODO: use the setter for the projectRegistry... just in case we need this?
            // do not use project registry, since it is putting this into the queue.
            // i might have to fix it, to not add to the queue, as soon as the activate method was called.
        }

    }

}
