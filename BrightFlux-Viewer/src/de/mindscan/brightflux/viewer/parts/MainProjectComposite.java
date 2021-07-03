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
package de.mindscan.brightflux.viewer.parts;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;

/**
 * 
 */
public class MainProjectComposite extends Composite {
    private Table table;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public MainProjectComposite( Composite parent, int style ) {
        super( parent, style );
        buildLayout();
    }

    private void buildLayout() {
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        TabFolder tabFolder = new TabFolder( this, SWT.NONE );

        // [Desired TabItem] - but leave it like this until we added some other interesting stuff to it. - e.g. a contentprovider.... 
        TabItem tbtmNewItem = new TabItem( tabFolder, SWT.NONE );
        tbtmNewItem.setText( "NameOfDataFrame" );

        Composite composite = new Composite( tabFolder, SWT.NONE );
        tbtmNewItem.setControl( composite );
        composite.setLayout( new TableColumnLayout() );

        TableViewer tableViewer = new TableViewer( composite, SWT.BORDER | SWT.FULL_SELECTION );
        table = tableViewer.getTable();
        table.setHeaderVisible( true );
        table.setLinesVisible( true );
        // [/Desired TabItem]

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
