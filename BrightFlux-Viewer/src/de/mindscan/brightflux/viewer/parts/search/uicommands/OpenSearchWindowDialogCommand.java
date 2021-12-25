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
package de.mindscan.brightflux.viewer.parts.search.uicommands;

import java.util.function.Consumer;

import org.eclipse.swt.widgets.Shell;

import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.system.services.SystemServices;
import de.mindscan.brightflux.viewer.parts.search.ui.SearchWindowDialog;
import de.mindscan.brightflux.viewer.uicommands.UIBFCommand;

/**
 * 
 */
public class OpenSearchWindowDialogCommand implements UIBFCommand {

    private Shell shellMainApp;

    /**
     * 
     */
    public OpenSearchWindowDialogCommand( Shell shellMainApp ) {
        this.shellMainApp = shellMainApp;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void execute( Consumer<BFEvent> eventConsumer ) {
        ProjectRegistry projectRegistry = SystemServices.getInstance().getProjectRegistry();
        if (projectRegistry == null) {
            throw new NotYetImplemetedException( "" );
        }

        // TODO maybe locate another open instance and show it instead.

        // TODO: if not set, we open a new instance and then we must initialize
        SearchWindowDialog searchWindow = new SearchWindowDialog( shellMainApp, 0 );
        // TODO improve this using the project registry itself and then complete the registration.
        searchWindow.setProjectRegistry( projectRegistry );
        searchWindow.open();

    }

}
