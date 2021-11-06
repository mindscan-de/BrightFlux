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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;

import swing2swt.layout.BorderLayout;

/**
 * 
 */
public class BFVideoAnnotationSingleVideoViewComposite extends Composite {
    private Text currentVideoDuration;
    private Text currentVideoPosition;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public BFVideoAnnotationSingleVideoViewComposite( Composite parent, int style ) {
        super( parent, style );
        setLayout( new BorderLayout( 0, 0 ) );

        Composite leftComposite = new Composite( this, SWT.NONE );
        leftComposite.setLayoutData( BorderLayout.WEST );
        leftComposite.setLayout( new GridLayout( 1, false ) );

        Label lblVideoDuration = new Label( leftComposite, SWT.NONE );
        lblVideoDuration.setText( "Video Duration" );

        currentVideoDuration = new Text( leftComposite, SWT.BORDER | SWT.READ_ONLY );
        currentVideoDuration.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );

        Label lblCurrentPosition = new Label( leftComposite, SWT.NONE );
        lblCurrentPosition.setText( "Current Position" );

        currentVideoPosition = new Text( leftComposite, SWT.BORDER | SWT.READ_ONLY );
        currentVideoPosition.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );

        Composite bottomComposite = new Composite( this, SWT.NONE );
        bottomComposite.setLayoutData( BorderLayout.SOUTH );
        bottomComposite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Slider slider = new Slider( bottomComposite, SWT.NONE );

        Composite centerComposite = new Composite( this, SWT.NONE );
        centerComposite.setLayoutData( BorderLayout.CENTER );
        centerComposite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        SashForm sashForm = new SashForm( centerComposite, SWT.VERTICAL );

        Composite composite = new Composite( sashForm, SWT.NONE );
        composite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        StyledText currentVideoTimestampStyledText = new StyledText( composite, SWT.BORDER );

        SashForm sashForm_1 = new SashForm( sashForm, SWT.NONE );

        Composite composite_1 = new Composite( sashForm_1, SWT.NONE );

        Composite composite_2 = new Composite( sashForm_1, SWT.NONE );
        sashForm_1.setWeights( new int[] { 1, 1 } );
        sashForm.setWeights( new int[] { 1, 1 } );

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
