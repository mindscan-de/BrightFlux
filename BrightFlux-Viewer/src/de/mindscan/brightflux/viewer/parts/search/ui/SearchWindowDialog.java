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
package de.mindscan.brightflux.viewer.parts.search.ui;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.dataframes.columntypes.ColumnValueTypes;
import de.mindscan.brightflux.system.services.SystemServices;
import de.mindscan.brightflux.viewer.parts.search.SearchUIProxyComponent;
import de.mindscan.brightflux.viewer.parts.search.SearchWindow;

/**
 * 
 */
public class SearchWindowDialog extends Dialog implements SearchWindow {

    protected Object result;
    protected Shell shlSearchWindow;
    private Text text;

    /**
     * Create the dialog.
     * @param parent
     * @param style
     */
    public SearchWindowDialog( Shell parent, int style ) {
        super( parent, style );
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
        while (!shlSearchWindow.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
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
     * Create contents of the dialog.
     */
    private void createContents() {
        registerAtSearchUIProxyComponent();

        shlSearchWindow = new Shell( getParent(), SWT.DIALOG_TRIM | SWT.RESIZE );
        shlSearchWindow.setSize( 450, 300 );
        shlSearchWindow.setText( "Search Tools" );
        shlSearchWindow.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        SashForm sashForm = new SashForm( shlSearchWindow, SWT.VERTICAL );

        Composite upperComposite = new Composite( sashForm, SWT.NONE );

        text = new Text( upperComposite, SWT.BORDER );
        text.setBounds( 10, 10, 363, 19 );

        Composite lowerComposite = new Composite( sashForm, SWT.NONE );
        lowerComposite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        CTabFolder searchResultsTabFolder = new CTabFolder( lowerComposite, SWT.BORDER );
        searchResultsTabFolder.setTabPosition( SWT.BOTTOM );
        searchResultsTabFolder.setSelectionBackground( Display.getCurrent().getSystemColor( SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT ) );
        sashForm.setWeights( new int[] { 82, 188 } );

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
}