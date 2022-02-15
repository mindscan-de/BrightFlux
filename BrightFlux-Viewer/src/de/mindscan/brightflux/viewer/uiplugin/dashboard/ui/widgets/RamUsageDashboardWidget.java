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
package de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.widgets;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Just start with something that works (specifically) and then abstract from here more generally
 */
public class RamUsageDashboardWidget extends Composite {
    private Table table;

    private final Map<String, String> map = new LinkedHashMap<>();

    private TableViewer tableViewer;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public RamUsageDashboardWidget( Composite parent, int style ) {
        super( parent, style );
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Group grpSomeheading = new Group( this, SWT.NONE );
        grpSomeheading.setText( "Some Heading" );
        grpSomeheading.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Composite composite = new Composite( grpSomeheading, SWT.NONE );
        TableColumnLayout tcl_composite = new TableColumnLayout();
        composite.setLayout( tcl_composite );

        tableViewer = new TableViewer( composite, SWT.BORDER | SWT.FULL_SELECTION );
        table = tableViewer.getTable();
        table.setHeaderVisible( true );
        table.setLinesVisible( true );

        TableViewerColumn tableViewerColumn = new TableViewerColumn( tableViewer, SWT.NONE );
        TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
        tcl_composite.setColumnData( tblclmnNewColumn, new ColumnPixelData( 150, true, true ) );
        tblclmnNewColumn.setText( "Key" );
        tableViewerColumn.setLabelProvider( new KeyValueColumnLabelProvider( "Key", map ) );

        TableViewerColumn tableViewerColumn_1 = new TableViewerColumn( tableViewer, SWT.NONE );
        TableColumn tblclmnNewColumn_1 = tableViewerColumn_1.getColumn();
        tblclmnNewColumn_1.setAlignment( SWT.RIGHT );
        tcl_composite.setColumnData( tblclmnNewColumn_1, new ColumnPixelData( 150, true, true ) );
        tblclmnNewColumn_1.setText( "Value" );
        tableViewerColumn_1.setLabelProvider( new KeyValueColumnLabelProvider( "Value", map ) );

        tableViewer.setContentProvider( new ArrayContentProvider() );

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    public void setRamUsage( String key, String value ) {
        map.put( key, value );
        tableViewer.setInput( map.keySet().toArray() );
    }
}
