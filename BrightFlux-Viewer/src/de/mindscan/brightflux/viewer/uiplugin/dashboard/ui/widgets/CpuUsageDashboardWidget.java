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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.ClearContentWidgetVisualizer;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.StringWidgetVisualizer;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.StringWidgetVisualizerProvider;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.TimestampWidgetVisualizer;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.TimestampWidgetVisualizerProvider;
import swing2swt.layout.FlowLayout;

/**
 * Just start with something that works (specifically) and then abstract from here more generally
 */
public class CpuUsageDashboardWidget extends Composite
                implements TimestampWidgetVisualizerProvider, StringWidgetVisualizerProvider, ClearContentWidgetVisualizer {
    private Label textTimestamp;
    private Label textUsage;
    private Label lblCpuNumber;

    private TimestampWidgetVisualizer timestampVisualizer = new TimestampWidgetVisualizer() {
        @Override
        public void setTimestampNA() {
            textTimestamp.setText( "" );
        }

        @Override
        public void setTimestamp( String timestamp ) {
            textTimestamp.setText( timestamp );
        }
    };

    private StringWidgetVisualizer stringVisualizer = new StringWidgetVisualizer() {

        @Override
        public void setScalarNA() {
            textUsage.setText( "N/A" );
        }

        @Override
        public void setScalar( String scalarValue ) {
            textUsage.setText( scalarValue + "%" );
        }
    };

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public CpuUsageDashboardWidget( Composite parent, int style ) {
        super( parent, style );
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Group grpTabname = new Group( this, SWT.NONE );
        grpTabname.setText( "Latest CPU Info" );
        grpTabname.setLayout( new FlowLayout( FlowLayout.CENTER, 5, 5 ) );

        lblCpuNumber = new Label( grpTabname, SWT.NONE );
        lblCpuNumber.setText( "CPU 1" );

        textTimestamp = new Label( grpTabname, SWT.BORDER );
        textTimestamp.setAlignment( SWT.RIGHT );
        textTimestamp.setText( "000000000.000000000" );
        textTimestamp.setFont( SWTResourceManager.getFont( "Courier New", 11, SWT.NORMAL ) );
        textTimestamp.setBackground( SWTResourceManager.getColor( SWT.COLOR_WHITE ) );

        textUsage = new Label( grpTabname, SWT.BORDER );
        textUsage.setAlignment( SWT.RIGHT );
        textUsage.setText( "000.00%" );
        textUsage.setFont( SWTResourceManager.getFont( "Courier New", 14, SWT.NORMAL ) );
        textUsage.setBackground( SWTResourceManager.getColor( SWT.COLOR_WHITE ) );
    }

    @Override
    public TimestampWidgetVisualizer getTimestampVisualizer() {
        return timestampVisualizer;
    }

    @Override
    public StringWidgetVisualizer getStringVisualizer() {
        return stringVisualizer;
    }

    @Override
    public void setNA() {
        timestampVisualizer.setTimestampNA();
        stringVisualizer.setScalarNA();
    }

    public void setCpuName( String cpuName ) {
        lblCpuNumber.setText( cpuName );
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}
