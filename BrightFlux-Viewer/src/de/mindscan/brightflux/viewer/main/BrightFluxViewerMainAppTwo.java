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
package de.mindscan.brightflux.viewer.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import de.mindscan.brightflux.viewer.parts.MainProjectComposite;

/**
 * 
 */
public class BrightFluxViewerMainAppTwo {

    protected Shell shellBFViewerMainApp;

    /**
     * Launch the application.
     * @param args
     */
    public static void main( String[] args ) {
        try {
            BrightFluxViewerMainAppTwo window = new BrightFluxViewerMainAppTwo();
            window.open();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        shellBFViewerMainApp.open();
        shellBFViewerMainApp.layout();
        while (!shellBFViewerMainApp.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shellBFViewerMainApp = new Shell();
        shellBFViewerMainApp.setSize( 853, 642 );
        shellBFViewerMainApp.setText( "SWT Application" );
        shellBFViewerMainApp.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Menu menu = new Menu( shellBFViewerMainApp, SWT.BAR );
        shellBFViewerMainApp.setMenuBar( menu );

        MenuItem mntmFile = new MenuItem( menu, SWT.CASCADE );
        mntmFile.setText( "File" );

        Menu menu_1 = new Menu( mntmFile );
        mntmFile.setMenu( menu_1 );

        Composite composite = new Composite( shellBFViewerMainApp, SWT.NONE );
        composite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        SashForm sashForm_1 = new SashForm( composite, SWT.NONE );

        SashForm sashForm_2 = new SashForm( sashForm_1, SWT.VERTICAL );

        Composite topLeftProjectComposite = new Composite( sashForm_2, SWT.NONE );
        topLeftProjectComposite.setBackground( SWTResourceManager.getColor( SWT.COLOR_RED ) );

        Composite bottomLeftDataOutlineComposite = new Composite( sashForm_2, SWT.NONE );
        bottomLeftDataOutlineComposite.setBackground( SWTResourceManager.getColor( SWT.COLOR_BLUE ) );
        sashForm_2.setWeights( new int[] { 316, 277 } );

        SashForm sashForm = new SashForm( sashForm_1, SWT.VERTICAL );

        Composite mainProjectComposite = new MainProjectComposite( sashForm, SWT.NONE );

        Composite bottomRightCommonViewAreaComposite = new Composite( sashForm, SWT.NONE );
        bottomRightCommonViewAreaComposite.setBackground( SWTResourceManager.getColor( SWT.COLOR_MAGENTA ) );
        sashForm.setWeights( new int[] { 379, 214 } );
        sashForm_1.setWeights( new int[] { 190, 652 } );

    }
}
