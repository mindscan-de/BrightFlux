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
package de.mindscan.brightflux.viewer.uiplugin.search.ui;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.dataframes.columntypes.ColumnValueTypes;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.search.commands.SearchCommandFactory;
import de.mindscan.brightflux.system.services.SystemServices;
import de.mindscan.brightflux.viewer.uiplugin.search.SearchUIProxyComponent;
import de.mindscan.brightflux.viewer.uiplugin.search.SearchWindow;
import de.mindscan.brightflux.viewer.uiplugin.search.ui.df.SearchResultDataFrameTableComposite;
import swing2swt.layout.BorderLayout;

/**
 * 
 */
public class SearchWindowDialog extends Dialog implements SearchWindow, ProjectRegistryParticipant {

    protected Object result;
    protected Shell shlSearchWindow;
    private Text text;
    private ProjectRegistry projectRegistry;
    private CTabFolder searchResultTabFolder;

    /**
     * Create the dialog.
     * @param parent
     * @param style
     */
    public SearchWindowDialog( Shell parent, int style ) {
        super( parent, SWT.DIALOG_TRIM | SWT.MAX | SWT.RESIZE );
        setText( "SWT Dialog" );
    }

    /**
     * Open the dialog.
     * @return the result
     */
    public Object open() {
        createContents();
        shlSearchWindow.open();
        shlSearchWindow.layout();
        Display display = getParent().getDisplay();

        // TODO: either the command becomes a worker thread, or we have to ban it into a thread, which can be aborted.
        // TODO: maybe this should be the reason to assign the execution of commands to their special worker/threads.
        // or we clarify this at a command, and implement a command which is starting a background task, so this can 
        // be run safely outside?
        // or is there any other way to do it correctly?
        while (!shlSearchWindow.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        registerAtSearchUIProxyComponent();

        shlSearchWindow = new Shell( getParent(), SWT.DIALOG_TRIM | SWT.MAX | SWT.RESIZE );
        shlSearchWindow.setSize( 450, 300 );
        shlSearchWindow.setText( "Search Tools" );
        shlSearchWindow.setLayout( new BorderLayout( 0, 0 ) );

        shlSearchWindow.addListener( SWT.Traverse, new Listener() {
            /** 
             * {@inheritDoc}
             */
            @Override
            public void handleEvent( Event event ) {
                // don't close on escape key.
                if (event.character == SWT.ESC) {
                    event.doit = false;
                }
            }
        } );

        shlSearchWindow.addListener( SWT.Close, new Listener() {
            /** 
             * {@inheritDoc}
             */
            @Override
            public void handleEvent( Event event ) {
                unregisterAtSearchUIProxyComponent();
            }
        } );

        Composite upperComposite = new Composite( shlSearchWindow, SWT.NONE );
        upperComposite.setLayoutData( BorderLayout.NORTH );
        upperComposite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Composite composite = new Composite( upperComposite, SWT.NONE );
        composite.setLayout( new BorderLayout( 0, 0 ) );

        Button btnSearch = new Button( composite, SWT.NONE );
        btnSearch.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {

                String query = text.getText().trim();
                //"+SharedClusterSnapshotRestore";

                // TODO this may provide context e.g. filetype, language, and additionals meta labels and will be completed by the actual userquery 
                String profileQuerySuffix = "";

                // we might have to compose a query like +(userquery) +(profileQuerySuffix)
                String fullQuery = query + " " + profileQuerySuffix;

                // we use the plugin search command, which will then do the heavy lifting.
                dispatchCommand( SearchCommandFactory.performSearch( query, profileQuerySuffix ) );
            }
        } );
        btnSearch.setText( "Search" );

        Composite composite_1 = new Composite( composite, SWT.NONE );
        composite_1.setLayoutData( BorderLayout.NORTH );
        composite_1.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        text = new Text( composite_1, SWT.BORDER );
        text.setFont( SWTResourceManager.getFont( "Courier New", 12, SWT.NORMAL ) );

        Composite composite_2 = new Composite( composite, SWT.NONE );
        composite_2.setLayoutData( BorderLayout.SOUTH );

        Composite composite_3 = new Composite( composite, SWT.NONE );
        composite_3.setLayoutData( BorderLayout.EAST );

        Composite composite_4 = new Composite( composite, SWT.NONE );
        composite_4.setLayoutData( BorderLayout.WEST );

        Composite lowerComposite = new Composite( shlSearchWindow, SWT.NONE );
        lowerComposite.setLayoutData( BorderLayout.CENTER );
        lowerComposite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        SashForm sashForm = new SashForm( lowerComposite, SWT.VERTICAL );

        searchResultTabFolder = new CTabFolder( sashForm, SWT.BORDER | SWT.BOTTOM );
        searchResultTabFolder.addCTabFolder2Listener( new CTabFolder2Adapter() {
            @Override
            public void close( CTabFolderEvent event ) {
                if (event.item instanceof CTabItem) {
                    Control control = ((CTabItem) event.item).getControl();
                    if (control instanceof SearchResultDataFrameTableComposite) {
                        // ignore the visual update before closing, because it takes some time sometimes
                        // then we want to void the referenced data
                        ((SearchResultDataFrameTableComposite) control).closeSearchDataFrame();
                    }
                }
            }
        } );
        searchResultTabFolder.setSelectionBackground( Display.getCurrent().getSystemColor( SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT ) );

        Composite searchResultDetailsComposite = new Composite( sashForm, SWT.NONE );
        sashForm.setWeights( new int[] { 70, 30 } );

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void searchRequestedRow( DataFrameRow requestedRow ) {
        DataFrame df = requestedRow.getDataFrameInternal();

        // Locate the first Row containing a string and then set the text of the text field 
        Collection<String> columnNames = df.getColumnNames();
        for (String columnName : columnNames) {
            String columnType = df.getColumn( columnName ).getColumnValueType();
            if (ColumnValueTypes.COLUMN_TYPE_STRING.equals( columnType )) {
                text.setText( (String) requestedRow.get( columnName ) );
            }
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void addSearchResultDataFrame( DataFrame newSearchResultDataFrame ) {

        CTabItem item = addTabItem( searchResultTabFolder, newSearchResultDataFrame );
        searchResultTabFolder.setSelection( item );

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;
    }

    /**
     * @param command
     */
    private void dispatchCommand( BFCommand command ) {
        if (this.projectRegistry != null) {
            projectRegistry.getCommandDispatcher().dispatchCommand( command );
        }
    }

    /**
     * 
     */
    private void registerAtSearchUIProxyComponent() {
        // TODO registration at SearchUIProxyComponent as the currentActive Search window
        SystemServices systemServices = SystemServices.getInstance();
        if (systemServices == null) {
            return;
        }

        if (systemServices.isServiceAvailable( SearchUIProxyComponent.class )) {
            SearchUIProxyComponent service = systemServices.getService( SearchUIProxyComponent.class );
            service.registerCurrentActiveSearchWindow( this );
        }
        else {

        }
    }

    private void unregisterAtSearchUIProxyComponent() {
        // TODO registration at SearchUIProxyComponent as the currentActive Search window
        SystemServices systemServices = SystemServices.getInstance();
        if (systemServices == null) {
            return;
        }

        if (systemServices.isServiceAvailable( SearchUIProxyComponent.class )) {
            SearchUIProxyComponent service = systemServices.getService( SearchUIProxyComponent.class );
            service.unregisterCurrentActiveSearchWindow();
        }
        else {

        }
    }

    @Override
    public void bringToTop() {
        shlSearchWindow.setFocus();
    }

    private CTabItem addTabItem( CTabFolder tabFolder, final DataFrame searchResultDF ) {
        String ingestedDFName = searchResultDF.getTitle();

        CTabItem tbtmNewItem = new CTabItem( tabFolder, SWT.NONE );
        tbtmNewItem.setShowClose( true );
        tbtmNewItem.setText( ingestedDFName );

        SearchResultDataFrameTableComposite composite = new SearchResultDataFrameTableComposite( tabFolder, SWT.NONE );

        composite.setProjectRegistry( projectRegistry );
        composite.setDataFrame( searchResultDF );
        tbtmNewItem.setControl( composite );

        return tbtmNewItem;

    }
}
