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

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.KeyValueWidgetVisualizer;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.KeyValueWidgetVisualizerProvider;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.TimestampWidgetVisualizer;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.TimestampWidgetVisualizerProvider;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.widgets.providers.KeyValueColumnLabelProvider;

/**
 * Just start with something that works (specifically) and then abstract from here more generally
 */
public class RamUsageDashboardWidget extends Composite implements TimestampWidgetVisualizerProvider, KeyValueWidgetVisualizerProvider {
    private Table table;

    private final Map<String, String> map = new LinkedHashMap<>();

    private TableViewer tableViewer;
    private Text textTimestamp;

    private Group grpSomeheading;

    private TimestampWidgetVisualizer timestampVisualizer = new TimestampWidgetVisualizer() {

        @Override
        public void setTimestamp( String timestamp ) {
            textTimestamp.setText( timestamp );
        }

        @Override
        public void setTimestampNA() {
            textTimestamp.setText( "" );
        }
    };

    private KeyValueWidgetVisualizer keyValueVisualizer = new KeyValueWidgetVisualizer() {

        @Override
        public void setPairNA( String key ) {
            map.put( key, "N/A" );
        }

        @Override
        public void setPair( String key, String value ) {
            map.put( key, value );
            tableViewer.setInput( map.keySet().toArray() );
        }
    };

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public RamUsageDashboardWidget( Composite parent, int style ) {
        super( parent, style );
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        grpSomeheading = new Group( this, SWT.NONE );
        grpSomeheading.setText( "Some Heading" );
        grpSomeheading.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Composite composite = new Composite( grpSomeheading, SWT.NONE );
        composite.setLayout( new GridLayout( 1, false ) );

        textTimestamp = new Text( composite, SWT.BORDER );
        textTimestamp.setFont( SWTResourceManager.getFont( "Courier New", 12, SWT.NORMAL ) );
        textTimestamp.setBackground( SWTResourceManager.getColor( SWT.COLOR_WHITE ) );
        textTimestamp.setEditable( false );
        textTimestamp.setToolTipText( "timestamp" );
        GridData gd_text = new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 );
        gd_text.widthHint = 412;
        textTimestamp.setLayoutData( gd_text );

        Composite composite_1 = new Composite( composite, SWT.NONE );
        GridData gd_composite_1 = new GridData( SWT.FILL, SWT.FILL, false, false, 1, 1 );
        gd_composite_1.widthHint = 392;
        gd_composite_1.heightHint = 190;
        composite_1.setLayoutData( gd_composite_1 );
        composite_1.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        tableViewer = new TableViewer( composite_1, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL );
        tableViewer.setUseHashlookup( true );
        table = tableViewer.getTable();
        table.setHeaderVisible( true );
        table.setLinesVisible( true );

        TableViewerColumn tableViewerColumn = new TableViewerColumn( tableViewer, SWT.NONE );
        TableColumn tblclmnKeyColumn = tableViewerColumn.getColumn();
        tblclmnKeyColumn.setWidth( 170 );
        tblclmnKeyColumn.setText( "Key" );
        tableViewerColumn.setLabelProvider( new KeyValueColumnLabelProvider( "Key", map ) );

        TableViewerColumn tableViewerColumn_1 = new TableViewerColumn( tableViewer, SWT.NONE );
        TableColumn tblclmnValueColumn = tableViewerColumn_1.getColumn();
        tblclmnValueColumn.setWidth( 180 );
        tblclmnValueColumn.setText( "Value" );
        tableViewerColumn_1.setLabelProvider( new KeyValueColumnLabelProvider( "Value", map ) );

        Menu menu = new Menu( table );
        table.setMenu( menu );

        MenuItem mntmCopyRow = new MenuItem( menu, SWT.NONE );
        mntmCopyRow.setText( "Copy this Row" );

        MenuItem mntmCopyAllRows = new MenuItem( menu, SWT.NONE );
        mntmCopyAllRows.setText( "Copy all Rows" );

        tableViewer.setContentProvider( new ArrayContentProvider() );

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public TimestampWidgetVisualizer getTimestampVisualizer() {
        return timestampVisualizer;
    }

    @Override
    public KeyValueWidgetVisualizer getKeyValueVisualizer() {
        return keyValueVisualizer;
    }

    public void setHeading( String heading ) {
        grpSomeheading.setText( heading );
    }

    public void setNA() {
        timestampVisualizer.setTimestampNA();
        for (String key : map.keySet()) {
            keyValueVisualizer.setPairNA( key );
        }
        tableViewer.setInput( map.keySet().toArray() );
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
