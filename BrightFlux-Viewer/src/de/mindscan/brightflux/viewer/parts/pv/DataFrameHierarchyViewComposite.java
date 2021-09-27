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
package de.mindscan.brightflux.viewer.parts.pv;

import java.util.UUID;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.events.BFEventListener;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.system.dataframehierarchy.DataFrameHierarchy;
import de.mindscan.brightflux.system.dataframehierarchy.DataFrameHierarchyNode;
import de.mindscan.brightflux.system.dataframehierarchy.impl.DataFrameHierarchyImpl;
import de.mindscan.brightflux.system.events.BFDataFrameEvent;
import de.mindscan.brightflux.system.events.BFEventListenerAdapter;
import de.mindscan.brightflux.system.events.dataframe.DataFrameCreatedEvent;
import de.mindscan.brightflux.viewer.parts.SystemEvents;
import de.mindscan.brightflux.viewer.parts.UIEvents;
import de.mindscan.brightflux.viewer.uievents.UIEventFactory;

/**
 * 
 */
public class DataFrameHierarchyViewComposite extends Composite implements ProjectRegistryParticipant {

    private ProjectRegistry projectRegistry;
    private DataFrameHierarchy dfHierarchy = new DataFrameHierarchyImpl();
    private TreeViewer treeViewer;
    protected UUID currentSelectedID;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public DataFrameHierarchyViewComposite( Composite parent, int style ) {
        super( parent, style );

        buildLayout();

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

        // register to the UIDataFrameSelectionEvent
        registerDataFrameSelectedListener( this.projectRegistry );
    }

    private void registerDataFrameClosedListener( ProjectRegistry projectRegistry2 ) {
        // Register to close event, so we can mark the hierarchy, that the frame is closed in the hierarchy. 
        BFEventListener closedListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof BFDataFrameEvent) {
                    BFDataFrameEvent dfEvent = (BFDataFrameEvent) event;

                    dfHierarchy.removeNode( dfEvent.getDataFrame() );
                    updateDataframeTree( dfHierarchy );
                }
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.DataFrameClosed, closedListener );
    }

    private void registerDataFrameCreatedListener( ProjectRegistry projectRegistry ) {
        // register to (create event / created from a parent frame)
        // these contain a hierarchy
        BFEventListener createdListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof BFDataFrameEvent) {
                    DataFrameCreatedEvent dfEvent = (DataFrameCreatedEvent) event;

                    dfHierarchy.addLeafNode( dfEvent.getDataFrame(), UUID.fromString( dfEvent.getParentDataFrameUUID() ) );
                    updateDataframeTree( dfHierarchy );
                }
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.DataFrameCreated, createdListener );
    }

    private void registerDataFrameLoadedListener( ProjectRegistry projectRegistry ) {
        // register to (load events)
        // these do not contain a hierarchy
        BFEventListener loadedListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof BFDataFrameEvent) {
                    BFDataFrameEvent dfEvent = (BFDataFrameEvent) event;

                    dfHierarchy.addRootNode( dfEvent.getDataFrame() );
                    updateDataframeTree( dfHierarchy );
                }
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.DataFrameLoaded, loadedListener );
    }

    private void registerDataFrameSelectedListener( ProjectRegistry projectRegistry2 ) {
        BFEventListener selectedListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof BFDataFrameEvent) {
                    BFDataFrameEvent dfEvent = (BFDataFrameEvent) event;

                    currentSelectedID = dfEvent.getDataFrame().getUuid();
                    updateDataframeTree( dfHierarchy );
                }
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( UIEvents.DataFrameSelectedEvent, selectedListener );
    }

    private void buildLayout() {
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        treeViewer = new TreeViewer( this, SWT.BORDER | SWT.FULL_SELECTION );
        treeViewer.addDoubleClickListener( new IDoubleClickListener() {
            public void doubleClick( DoubleClickEvent event ) {
                TreeSelection selection = (TreeSelection) event.getSelection();
                if (selection.isEmpty()) {
                    return;
                }

                Object firstElement = selection.getFirstElement();
                if (firstElement instanceof DataFrameHierarchyNode) {
                    UUID uuidToSelect = ((DataFrameHierarchyNode) firstElement).getDataFrameUUID();
                    BFEvent bfevent = UIEventFactory.dataFrameSelectionRequest( uuidToSelect );
                    projectRegistry.getEventDispatcher().dispatchEvent( bfevent );
                }

            }
        } );
        treeViewer.setContentProvider( new DataFrameHierarchyTreeContentProvider() );

        Tree tree = treeViewer.getTree();
        // tree.setLinesVisible( true );
        tree.setHeaderVisible( true );

        TreeViewerColumn treeViewerNameColumn = new TreeViewerColumn( treeViewer, SWT.NONE );
        TreeColumn trclmnDataframeName = treeViewerNameColumn.getColumn();
        trclmnDataframeName.setWidth( 150 );
        trclmnDataframeName.setText( "Name" );
        treeViewerNameColumn.setLabelProvider( new DataFrameHierarchyTreeColumnLabelProvider( DataFrameHierarchyTreeColumnLabelProvider.DF_NAME_HEADER ) );

        TreeViewerColumn treeViewerUUIDColumn = new TreeViewerColumn( treeViewer, SWT.NONE );
        TreeColumn trclmnUuid = treeViewerUUIDColumn.getColumn();
        trclmnUuid.setWidth( 150 );
        trclmnUuid.setText( "uuid" );
        treeViewerUUIDColumn.setLabelProvider( new DataFrameHierarchyTreeColumnLabelProvider( DataFrameHierarchyTreeColumnLabelProvider.DF_UUID ) );

        treeViewer.setInput( dfHierarchy );

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    private void updateDataframeTree( DataFrameHierarchy dfHierarchy2 ) {
        treeViewer.refresh();

        if (currentSelectedID != null) {
            DataFrameHierarchyNode selectedNode = dfHierarchy2.getNodeByUUID( currentSelectedID );
            if (selectedNode != null) {
                treeViewer.expandToLevel( selectedNode, TreeViewer.ALL_LEVELS );
                treeViewer.setSelection( new StructuredSelection( selectedNode ) );
            }
        }
    }

}
