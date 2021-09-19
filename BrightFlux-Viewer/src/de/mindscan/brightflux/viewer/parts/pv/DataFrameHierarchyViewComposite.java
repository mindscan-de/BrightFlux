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

import org.eclipse.jface.viewers.ColumnLabelProvider;
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
import de.mindscan.brightflux.system.events.BFEventListenerAdapter;
import de.mindscan.brightflux.viewer.parts.SystemEvents;

/**
 * 
 */
public class DataFrameHierarchyViewComposite extends Composite implements ProjectRegistryParticipant {

    private ProjectRegistry projectRegistry;

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

        // TODO: Register to dataframe close events, so we can mark the hierarchy, that the frame is away from the hierarchy. 
        // (disable intermediate dataframes, remove leaf dataframes)
    }

    private void registerDataFrameCreatedListener( ProjectRegistry projectRegistry ) {
        // register to (create event / created from a parent frame)
        // these contain a hierarchy
        BFEventListener createdListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {

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

            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.DataFrameLoaded, loadedListener );
    }

    private void buildLayout() {
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        TreeViewer treeViewer = new TreeViewer( this, SWT.BORDER );
        treeViewer.setContentProvider( new DataFrameHierarchyTreeContentProvider() );

        Tree tree = treeViewer.getTree();
        tree.setLinesVisible( true );
        tree.setHeaderVisible( true );

        TreeViewerColumn treeViewerColumn = new TreeViewerColumn( treeViewer, SWT.NONE );
        TreeColumn trclmnDataframe = treeViewerColumn.getColumn();
        trclmnDataframe.setWidth( 100 );
        trclmnDataframe.setText( "DataFrame" );
        treeViewerColumn.setLabelProvider( new ColumnLabelProvider() );

        treeViewer.setInput( new String[] { "MyFirstDataFrame", "MySecondDataFrame" } );
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}
