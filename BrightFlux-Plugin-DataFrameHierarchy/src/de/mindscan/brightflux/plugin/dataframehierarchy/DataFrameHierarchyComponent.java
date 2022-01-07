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
package de.mindscan.brightflux.plugin.dataframehierarchy;

import java.util.UUID;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.dataframehierarchy.events.DataFrameHierarchyUpdatedEvent;
import de.mindscan.brightflux.plugin.dataframehierarchy.impl.DataFrameHierarchyImpl;
import de.mindscan.brightflux.system.events.DataFrameCreatedEventListenerAdapter;
import de.mindscan.brightflux.system.events.DataFrameEventListenerAdapter;
import de.mindscan.brightflux.system.events.dataframe.DataFrameClosedEvent;
import de.mindscan.brightflux.system.events.dataframe.DataFrameCreatedEvent;
import de.mindscan.brightflux.system.events.dataframe.DataFrameLoadedEvent;

/**
 * 
 */
public class DataFrameHierarchyComponent implements ProjectRegistryParticipant {

    private DataFrameHierarchyImpl dataframeHierarchy;

    private ProjectRegistry projectRegistry;

    /**
     * 
     */
    public DataFrameHierarchyComponent() {
        dataframeHierarchy = new DataFrameHierarchyImpl();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;

        registerDataFrameCreatedListener( this.projectRegistry );
        registerDataFrameLoadedListener( this.projectRegistry );
        registerDataFrameClosedListener( this.projectRegistry );
    }

    private void registerDataFrameCreatedListener( ProjectRegistry projectRegistry ) {
        // register to (create events / created from a parent frame) -  these do contain a hierarchy
        DataFrameCreatedEventListenerAdapter createdListener = new DataFrameCreatedEventListenerAdapter() {
            @Override
            public void handleDataFrameCreated( DataFrame dataFrame, String parentUUID ) {
                dataframeHierarchy.addLeafNode( dataFrame, UUID.fromString( parentUUID ) );

                projectRegistry.getEventDispatcher().dispatchEvent( new DataFrameHierarchyUpdatedEvent( dataframeHierarchy ) );
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( DataFrameCreatedEvent.class, createdListener );
    }

    private void registerDataFrameLoadedListener( ProjectRegistry projectRegistry ) {
        // register to (load events) -  these do not contain a hierarchy
        DataFrameEventListenerAdapter loadedListener = new DataFrameEventListenerAdapter() {
            @Override
            public void handleDataFrame( DataFrame dataFrame ) {
                dataframeHierarchy.addRootNode( dataFrame );

                projectRegistry.getEventDispatcher().dispatchEvent( new DataFrameHierarchyUpdatedEvent( dataframeHierarchy ) );
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( DataFrameLoadedEvent.class, loadedListener );
    }

    private void registerDataFrameClosedListener( ProjectRegistry projectRegistry2 ) {
        // Register to close event, so we can mark the hierarchy, that the frame is closed in the hierarchy. 
        DataFrameEventListenerAdapter closedListener = new DataFrameEventListenerAdapter() {
            @Override
            public void handleDataFrame( DataFrame dataFrame ) {
                dataframeHierarchy.removeNode( dataFrame );

                projectRegistry.getEventDispatcher().dispatchEvent( new DataFrameHierarchyUpdatedEvent( dataframeHierarchy ) );
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( DataFrameClosedEvent.class, closedListener );
    }

    /**
     * @return the dataframeHierarchy
     */
    public DataFrameHierarchy getDataframeHierarchy() {
        return dataframeHierarchy;
    }

}
