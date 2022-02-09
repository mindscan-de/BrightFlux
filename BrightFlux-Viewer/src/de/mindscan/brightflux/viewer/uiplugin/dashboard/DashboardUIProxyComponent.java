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
package de.mindscan.brightflux.viewer.uiplugin.dashboard;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.system.events.DataFrameEventListenerAdapter;
import de.mindscan.brightflux.viewer.parts.UIEvents;
import de.mindscan.brightflux.viewer.uievents.DataFrameRowSelectedListenerAdapter;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.persistence.DashboardUIPersistenceModule;

/**
 * 
 */
public class DashboardUIProxyComponent implements ProjectRegistryParticipant {

    private DashboardWindow dashboard;
    private ProjectRegistry projectRegistry;

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;

        registerDataFrameRowSelectionEvent( projectRegistry );
        registerDataFrameSelectedEvent( projectRegistry );
    }

    private void registerDataFrameRowSelectionEvent( ProjectRegistry projectRegistry ) {
        DataFrameRowSelectedListenerAdapter listener = new DataFrameRowSelectedListenerAdapter() {
            @Override
            public void handleDataFrameRowSelected( DataFrameRow selectedRow ) {
                delegateDataFrameRowSelectionToDashboard( selectedRow );
            }
        };

        projectRegistry.getEventDispatcher().registerEventListener( UIEvents.DataFrameRowSelectedEvent, listener );
    }

    private void registerDataFrameSelectedEvent( ProjectRegistry projectRegistry ) {
        DataFrameEventListenerAdapter listener = new DataFrameEventListenerAdapter() {
            @Override
            public void handleDataFrame( DataFrame selectedDataFrame ) {
                delegateDataFrameSelectionToDashboard( selectedDataFrame );
            }
        };

        projectRegistry.getEventDispatcher().registerEventListener( UIEvents.DataFrameSelectedEvent, listener );
    }

    public void registerCurrentActiveDashboardWindow( DashboardWindow dashboard ) {
        this.dashboard = dashboard;
    }

    public void unregisterCurrentActiveDashboardWindow() {
        this.dashboard = null;
    }

    public boolean hasCurrentActiveDashboardWindow() {
        return this.dashboard != null;
    }

    public void focusCurrentActiveDashboardWindow() {
        if (hasCurrentActiveDashboardWindow()) {
            this.dashboard.bringToTop();
        }
    }

    private void delegateDataFrameRowSelectionToDashboard( DataFrameRow selectedRow ) {
        if (hasCurrentActiveDashboardWindow()) {
            this.dashboard.dataFrameRowSelected( selectedRow );
        }
    }

    private void delegateDataFrameSelectionToDashboard( DataFrame selectedDataFrame ) {
        if (hasCurrentActiveDashboardWindow()) {
            this.dashboard.dataFrameSelected( selectedDataFrame );
        }

    }

    public void setPersistenceModule( DashboardUIPersistenceModule persistenceModule ) {
        // TODO Auto-generated method stub
    }

}
