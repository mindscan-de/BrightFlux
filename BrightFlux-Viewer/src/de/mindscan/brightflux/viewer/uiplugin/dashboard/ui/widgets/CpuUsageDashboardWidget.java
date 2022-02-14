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
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.FlowLayout;

/**
 * Just start with something that works (specifically) and then abstract from here more generally
 */
public class CpuUsageDashboardWidget extends Composite {
    private Text textTimestamp;
    private Text textUsage;
    private Label lblCpuNumber;

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

        textTimestamp = new Text( grpTabname, SWT.BORDER );
        textTimestamp.setSize( 150, 25 );
        textTimestamp.setFont( SWTResourceManager.getFont( "Courier New", 11, SWT.NORMAL ) );

        textTimestamp.setBackground( SWTResourceManager.getColor( SWT.COLOR_WHITE ) );
        textTimestamp.setEditable( false );

        textUsage = new Text( grpTabname, SWT.BORDER );
        textUsage.setBackground( SWTResourceManager.getColor( SWT.COLOR_WHITE ) );
        textUsage.setEditable( false );
    }

    public void setLatestCpuTimestamp( String timestamp ) {
        textTimestamp.setText( timestamp );
    }

    public void setLatestCpuUsageValue( String usageValue ) {
        textUsage.setText( usageValue );
    }

    public void setCpuName( String cpuName ) {
        lblCpuNumber.setText( cpuName );
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
