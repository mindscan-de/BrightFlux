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

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.dataframehierarchy.DataFrameHierarchy;
import de.mindscan.brightflux.plugin.dataframehierarchy.DataFrameHierarchyComponent;
import de.mindscan.brightflux.plugin.dataframehierarchy.DataFrameHierarchyEventListenerAdapter;
import de.mindscan.brightflux.system.dataframehierarchy.DataFrameHierarchyNode;
import de.mindscan.brightflux.system.events.DataFrameEventListenerAdapter;
import de.mindscan.brightflux.system.services.SystemServices;
import de.mindscan.brightflux.viewer.parts.SystemEvents;
import de.mindscan.brightflux.viewer.parts.UIEvents;
import de.mindscan.brightflux.viewer.uievents.UIEventFactory;

/**
 * 
 */
public class DataFrameHierarchyViewComposite extends Composite implements ProjectRegistryParticipant {

    private ProjectRegistry projectRegistry;
    private DataFrameHierarchyComponent dfHierarchyComponent;
    private TreeViewer treeViewer;
    protected UUID currentSelectedID;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public DataFrameHierarchyViewComposite( Composite parent, int style ) {
        super( parent, style );

        this.dfHierarchyComponent = SystemServices.getInstance().getService( DataFrameHierarchyComponent.class );

        buildLayout();

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;

        // register to the UIDataFrameSelectionEvent
        registerDataFrameSelectedListener( this.projectRegistry );

        // register to DataFrameHierarchy Events
        registerDataFrameHierarchyUpdatedListener( this.projectRegistry );
    }

    private void registerDataFrameHierarchyUpdatedListener( ProjectRegistry projectRegistry2 ) {
        DataFrameHierarchyEventListenerAdapter updatedListener = new DataFrameHierarchyEventListenerAdapter() {
            @Override
            public void handleDataFrameHierarchy( DataFrameHierarchy dfHierarchy ) {
                updateDataframeTree( dfHierarchy );
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.DataFrameHierarchyUpdated, updatedListener );
    }

    private void registerDataFrameSelectedListener( ProjectRegistry projectRegistry2 ) {
        DataFrameEventListenerAdapter selectedListener = new DataFrameEventListenerAdapter() {
            @Override
            public void handleDataFrame( DataFrame dataFrame ) {
                currentSelectedID = dataFrame.getUuid();
                updateDataframeTree( dfHierarchyComponent.getDataframeHierarchy() );
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
        tree.setLinesVisible( true );
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

        if (dfHierarchyComponent != null) {
            treeViewer.setInput( dfHierarchyComponent.getDataframeHierarchy() );
        }

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    private void updateDataframeTree( DataFrameHierarchy dfHierarchy ) {
        treeViewer.refresh();

        if (currentSelectedID != null) {
            DataFrameHierarchyNode selectedNode = dfHierarchy.getNodeByUUID( currentSelectedID );
            if (selectedNode != null) {
                treeViewer.expandToLevel( selectedNode, TreeViewer.ALL_LEVELS );
                treeViewer.setSelection( new StructuredSelection( selectedNode ) );
            }
        }
    }
}
