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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;

import de.mindscan.brightflux.system.videoannotator.VideoAnnotatorVideoObject;
import swing2swt.layout.BorderLayout;

/**
 * 
 */
public class BFVideoAnnotationSingleVideoViewComposite extends Composite {
    private Text currentVideoDuration;
    private Text currentVideoPosition;
    private VideoAnnotatorVideoObject videoObject;
    private Slider videoPositionSlider;
    private StyledText currentVideoTimestampAnnotation;

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

        currentVideoDuration = new Text( leftComposite, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER );
        currentVideoDuration.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );

        Label lblCurrentPosition = new Label( leftComposite, SWT.NONE );
        lblCurrentPosition.setText( "Current Position" );

        currentVideoPosition = new Text( leftComposite, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER );
        currentVideoPosition.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );
        currentVideoPosition.setText( "00:00" );

        Composite bottomComposite = new Composite( this, SWT.NONE );
        bottomComposite.setLayoutData( BorderLayout.SOUTH );
        bottomComposite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        videoPositionSlider = new Slider( bottomComposite, SWT.NONE );
        videoPositionSlider.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                setVideoPosition( videoPositionSlider.getSelection() );
            }
        } );
        videoPositionSlider.setMinimum( 0 );
        videoPositionSlider.setMaximum( 60 );

        Composite centerComposite = new Composite( this, SWT.NONE );
        centerComposite.setLayoutData( BorderLayout.CENTER );
        centerComposite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        SashForm sashForm = new SashForm( centerComposite, SWT.VERTICAL );

        Composite composite = new Composite( sashForm, SWT.NONE );
        composite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        currentVideoTimestampAnnotation = new StyledText( composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
        currentVideoTimestampAnnotation.addModifyListener( new ModifyListener() {
            public void modifyText( ModifyEvent e ) {
                updateAnnotationInVideoObject();
            }
        } );
        currentVideoTimestampAnnotation.setTopMargin( 3 );
        currentVideoTimestampAnnotation.setRightMargin( 3 );
        currentVideoTimestampAnnotation.setLeftMargin( 3 );
        currentVideoTimestampAnnotation.setBottomMargin( 3 );

        SashForm sashForm_1 = new SashForm( sashForm, SWT.NONE );

        // TODO: here we will have a dataframe containing the timestamps and texts...
        Composite composite_1 = new Composite( sashForm_1, SWT.NONE );

        // TODO: here we will have a list of phrases useful for the analysis process.
        Composite composite_2 = new Composite( sashForm_1, SWT.NONE );
        sashForm_1.setWeights( new int[] { 1, 1 } );
        sashForm.setWeights( new int[] { 105, 176 } );

    }

    /**
     * @param videoObject
     */
    public void setVideoObject( VideoAnnotatorVideoObject videoObject ) {
        this.videoObject = videoObject;

        setVideoDuration( this.videoObject.getVideoDurationInSeconds() );
        setVideoPosition( 0 );
    }

    private void setVideoDuration( int videoDurationInSeconds ) {
        currentVideoDuration.setText( convertSecondsToTimeString( videoDurationInSeconds ) );

        videoPositionSlider.setIncrement( 1 );
        videoPositionSlider.setMinimum( 0 );
        videoPositionSlider.setThumb( 10 );
        videoPositionSlider.setMaximum( videoDurationInSeconds + videoPositionSlider.getThumb() );
    }

    private void setVideoPosition( int videoPositionInSeconds ) {
        currentVideoPosition.setText( convertSecondsToTimeString( videoPositionInSeconds ) );

        if (videoObject != null) {
            currentVideoTimestampAnnotation.setText( videoObject.getAnnotationForTimestamp( videoPositionInSeconds ) );
        }
        else {
            currentVideoTimestampAnnotation.setText( "" );
        }
    }

    private void updateAnnotationInVideoObject() {
        int currentTimestamp = videoPositionSlider.getSelection();
        if (this.videoObject != null) {
            this.videoObject.setAnnotationForTimestamp( currentTimestamp, currentVideoTimestampAnnotation.getText() );
        }
    }

    private String convertSecondsToTimeString( int videoDurationInSeconds ) {
        int seconds = videoDurationInSeconds % 60;
        int minutes = (videoDurationInSeconds / 60) % 60;

        return "%02d:%02d".formatted( minutes, seconds );
    }

    public void closeVideoObject() {
        this.setVisible( false );

        if (videoObject != null) {
            // TODO: remove the videoObject from the VideoAnnotationComponent
            // TODO: DispatchCommand? DispatchEvent VideoObjectClosedEvent

            // kill reference to VideoAnnotation here.
            videoObject = null;
        }

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}
