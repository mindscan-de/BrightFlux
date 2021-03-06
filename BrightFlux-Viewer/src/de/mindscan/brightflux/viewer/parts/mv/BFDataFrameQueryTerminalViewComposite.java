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
package de.mindscan.brightflux.viewer.parts.mv;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.wb.swt.SWTResourceManager;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.dfquery.DataFrameQueryClassifier;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.highlighter.HighlighterCallbacks;
import de.mindscan.brightflux.plugin.queryhistory.QueryHistoryComponent;
import de.mindscan.brightflux.system.commands.DataFrameCommandFactory;
import de.mindscan.brightflux.system.events.DataFrameEventListenerAdapter;
import de.mindscan.brightflux.system.services.SystemServices;
import de.mindscan.brightflux.viewer.parts.UIEvents;

/**
 * 
 */
public class BFDataFrameQueryTerminalViewComposite extends Composite implements ProjectRegistryParticipant {

    private DataFrame currentSelectedDataFrame = null;
    private ProjectRegistry projectRegistry;
    private ListViewer listViewer;

    private CCombo combo;
    private QueryHistoryComponent queryHistoryComponent;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public BFDataFrameQueryTerminalViewComposite( Composite parent, int style ) {
        super( parent, style );
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        buildLayout();

        queryHistoryComponent = SystemServices.getInstance().getService( QueryHistoryComponent.class );

        if (queryHistoryComponent != null) {
            updateListViewer( queryHistoryComponent.getHistory() );
        }
    }

    private void buildLayout() {
        SashForm sashForm = new SashForm( this, SWT.VERTICAL );

        Composite upperComposite = new Composite( sashForm, SWT.NONE );
        upperComposite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        combo = new CCombo( upperComposite, SWT.BORDER );
        combo.addKeyListener( new KeyAdapter() {
            @Override
            public void keyReleased( KeyEvent e ) {
                if (e.character == SWT.CR) {
                    String theQuery = combo.getText();

                    addToHistory( theQuery );
                    updateComboText( "" );

                    executeQuery( theQuery );
                }
            }
        } );
        combo.setFont( SWTResourceManager.getFont( "Courier New", 11, SWT.NORMAL ) );

        Composite lowerComposite = new Composite( sashForm, SWT.NONE );
        lowerComposite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        SashForm sashForm_1 = new SashForm( lowerComposite, SWT.VERTICAL );

        listViewer = new ListViewer( sashForm_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL );
        listViewer.setContentProvider( new ArrayContentProvider() );
        List list = listViewer.getList();
        list.setFont( SWTResourceManager.getFont( "Courier New", 10, SWT.NORMAL ) );
        list.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseDoubleClick( MouseEvent e ) {
                String[] selection = ((List) e.getSource()).getSelection();
                if (selection != null && selection.length > 0) {
                    executeQuery( selection[0] );
                }
            }
        } );

        buildQueryListContextMenu( list );

        sashForm_1.setWeights( new int[] { 1 } );
        sashForm.setWeights( new int[] { 60, 237 } );
    }

    private void buildQueryListContextMenu( List list ) {
        Menu menu = new Menu( list );
        list.setMenu( menu );

        MenuItem mntmAsInput = new MenuItem( menu, SWT.NONE );
        mntmAsInput.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                List list2 = listViewer.getList();
                String[] selection = list2.getSelection();
                if (selection != null && selection.length > 0) {
                    updateComboText( selection[0] + combo.getText() );
                }
            }
        } );
        mntmAsInput.setText( "As Input" );

        MenuItem mntmExecute = new MenuItem( menu, SWT.NONE );
        mntmExecute.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                List list2 = listViewer.getList();
                String[] selection = list2.getSelection();
                if (selection != null && selection.length > 0) {
                    executeQuery( selection[0] );
                }
            }
        } );
        mntmExecute.setText( "Execute" );

        list.addMenuDetectListener( new MenuDetectListener() {
            public void menuDetected( MenuDetectEvent e ) {
                List list2 = listViewer.getList();
                e.doit = list2.getSelectionCount() > 0;
            }
        } );

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;

        DataFrameEventListenerAdapter listener = new DataFrameEventListenerAdapter() {
            @Override
            public void handleDataFrame( DataFrame dataFrame ) {
                currentSelectedDataFrame = dataFrame;

                // Update View? Show name of selected DataFrame, 
                // because the Terminal will be sensitive to the selected frame in the MainProjectComposite
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( UIEvents.DataFrameSelectedEvent, listener );

    }

    public void executeQuery( String theQuery ) {
        if (currentSelectedDataFrame != null) {

            switch (DataFrameQueryClassifier.classifyDFQLQuery( theQuery )) {
                case DMS_TOKENIZE: {
                    BFCommand command = DataFrameCommandFactory.queryTokenize( currentSelectedDataFrame, theQuery );
                    this.projectRegistry.getCommandDispatcher().dispatchCommand( command );
                    break;
                }
                case DMS_SELECT: {
                    BFCommand command = DataFrameCommandFactory.queryDataFrame( currentSelectedDataFrame, theQuery );
                    this.projectRegistry.getCommandDispatcher().dispatchCommand( command );
                    break;
                }
                case PS_CALLBACK: {
                    BFCommand command = DataFrameCommandFactory.queryCBDataFrame( currentSelectedDataFrame, theQuery,
                                    HighlighterCallbacks.getInstance().getCallbacks() );
                    this.projectRegistry.getCommandDispatcher().dispatchCommand( command );
                    break;
                }
                default: {
                    // actually this is an unknown query type
                    Exception ex = new NotYetImplemetedException( "Unkown query type" );
                    ex.printStackTrace();
                    break;
                }
            }
        }
    }

    public void addToHistory( String theQuery ) {
        int previousSelectionIndex = listViewer.getList().getSelectionIndex();

        queryHistoryComponent.addEntry( theQuery );

        updateListViewer( queryHistoryComponent.getHistory() );

        if (previousSelectionIndex >= 0) {
            listViewer.getList().setSelection( previousSelectionIndex + 1 );
        }
    }

    private void updateListViewer( Object[] queryHistory ) {
        listViewer.setInput( queryHistory );
    }

    private void updateComboText( String newComboText ) {
        combo.setText( newComboText );
    }
}
