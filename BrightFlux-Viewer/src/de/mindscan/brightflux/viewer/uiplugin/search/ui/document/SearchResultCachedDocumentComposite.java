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
package de.mindscan.brightflux.viewer.uiplugin.search.ui.document;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import swing2swt.layout.BorderLayout;

/**
 * 
 */
public class SearchResultCachedDocumentComposite extends Composite implements ProjectRegistryParticipant {

    private StyledText documentContentStyledText;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public SearchResultCachedDocumentComposite( Composite parent, int style ) {
        super( parent, style );
        setLayout( new BorderLayout( 0, 0 ) );

        Composite composite_ruler_left = new Composite( this, SWT.NONE );
        composite_ruler_left.setLayoutData( BorderLayout.WEST );
        composite_ruler_left.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Button btnLines = new Button( composite_ruler_left, SWT.CHECK );

        Composite composite_ruler_right = new Composite( this, SWT.NONE );
        composite_ruler_right.setLayoutData( BorderLayout.EAST );
        composite_ruler_right.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Button btnCheckButton = new Button( composite_ruler_right, SWT.CHECK );

        Composite composite = new Composite( this, SWT.NONE );
        composite.setLayoutData( BorderLayout.CENTER );
        composite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        documentContentStyledText = new StyledText( composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
        documentContentStyledText.setEditable( false );
        documentContentStyledText.setFont( SWTResourceManager.getFont( "Courier New", 11, SWT.NORMAL ) );

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * @param projectRegistry
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        // intentionally left blank
    }

    /**
     * @param cachedDocumentContent
     */
    public void setDocumentContent( String cachedDocumentContent ) {
        documentContentStyledText.setText( cachedDocumentContent );
    }
}
