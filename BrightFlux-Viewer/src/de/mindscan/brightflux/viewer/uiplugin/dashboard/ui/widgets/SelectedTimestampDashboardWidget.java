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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.transform.TimeConversions;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.SimpleContentWidgetVisualizer;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.TimestampWidgetVisualizer;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.TimestampWidgetVisualizerProvider;

/**
 * 
 */
public class SelectedTimestampDashboardWidget extends Composite implements TimestampWidgetVisualizerProvider, SimpleContentWidgetVisualizer {

    private Text textAsIsTimestamp;
    private Text textNano2nanoDayTimestamp;
    private Text text_2;
    private Group grpSomeTitle;

    private TimestampWidgetVisualizer timestampVisualizer = new TimestampWidgetVisualizer() {
        @Override
        public void setTimestampNA() {
            textAsIsTimestamp.setText( "N/A" );
            textNano2nanoDayTimestamp.setText( "N/A" );
        }

        @Override
        public void setTimestamp( String timestamp ) {
            textAsIsTimestamp.setText( timestamp );
            textNano2nanoDayTimestamp.setText( TimeConversions.convertNanoToNanoDate( timestamp ) );
        }
    };

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public SelectedTimestampDashboardWidget( Composite parent, int style ) {
        super( parent, style );
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        grpSomeTitle = new Group( this, SWT.NONE );
        grpSomeTitle.setText( "Title" );
        grpSomeTitle.setLayout( new GridLayout( 2, false ) );

        Label lblNewLabel = new Label( grpSomeTitle, SWT.NONE );
        lblNewLabel.setLayoutData( new GridData( SWT.RIGHT, SWT.CENTER, false, false, 1, 1 ) );
        lblNewLabel.setBounds( 0, 0, 49, 13 );
        lblNewLabel.setText( "As Is" );

        textAsIsTimestamp = new Text( grpSomeTitle, SWT.BORDER );
        textAsIsTimestamp.setFont( SWTResourceManager.getFont( "Courier New", 12, SWT.NORMAL ) );
        textAsIsTimestamp.setBackground( SWTResourceManager.getColor( SWT.COLOR_WHITE ) );
        textAsIsTimestamp.setEditable( false );
        textAsIsTimestamp.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );

        Label lblNewLabel_1 = new Label( grpSomeTitle, SWT.NONE );
        lblNewLabel_1.setLayoutData( new GridData( SWT.RIGHT, SWT.CENTER, false, false, 1, 1 ) );
        lblNewLabel_1.setBounds( 0, 0, 49, 13 );
        lblNewLabel_1.setText( "Time with Nanos" );

        textNano2nanoDayTimestamp = new Text( grpSomeTitle, SWT.BORDER );
        textNano2nanoDayTimestamp.setFont( SWTResourceManager.getFont( "Courier New", 12, SWT.NORMAL ) );
        textNano2nanoDayTimestamp.setBackground( SWTResourceManager.getColor( SWT.COLOR_WHITE ) );
        textNano2nanoDayTimestamp.setEditable( false );
        textNano2nanoDayTimestamp.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );

        Label lblNewLabel_2 = new Label( grpSomeTitle, SWT.NONE );
        lblNewLabel_2.setLayoutData( new GridData( SWT.RIGHT, SWT.CENTER, false, false, 1, 1 ) );
        lblNewLabel_2.setBounds( 0, 0, 49, 13 );
        lblNewLabel_2.setText( "New Label" );

        text_2 = new Text( grpSomeTitle, SWT.BORDER );
        text_2.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setNA() {
        timestampVisualizer.setTimestampNA();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setName( String name ) {
        grpSomeTitle.setText( name );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public TimestampWidgetVisualizer getTimestampVisualizer() {
        return timestampVisualizer;
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
