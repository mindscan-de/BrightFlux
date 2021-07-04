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

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.ingest.IngestHeartCsv;

/**
 * 
 */
public class MainProjectComposite extends Composite {
    private Table table;

    // XXX: Awful hack right now.
    private final static Path path = Paths
                    .get( "D:\\Projects\\SinglePageApplication\\Angular\\BrightFlux\\BrightFlux-Core\\test\\de\\mindscan\\brightflux\\ingest\\heart.csv" );
    IngestHeartCsv ingest = new IngestHeartCsv();

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public MainProjectComposite( Composite parent, int style ) {
        super( parent, style );

        // TODO: * register for project configuration updates/changes, and provide a listener, so that we can add and remove the content for data frames to show in the tab folder.
        //       * we should also be able to update the project configuration by closing a dataframe and such.

        // build layout - basically the tabFolder.
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

        // XXX: This is an awful quick hack to move towards the goal to present some data, 
        //      we then will refactor to patterns if it works good enough.
        // TODO: we have to set the ContentProvider on the tableViewer - 
        tableViewer.setContentProvider( ArrayContentProvider.getInstance() );
        // TODO: The contentprovider should be initialized with a dataframe and be a DataFrameContentProvider instead of an ArrayContentProvider
        DataFrame ingestedDF = ingest.loadCsvAsDataFrameV2( path );
        tableViewer.setInput( ingestedDF.getRows().toArray() );
        // TODO: https://www.vogella.com/tutorials/EclipseJFaceTable/article.html

        // [/Desired TabItem]

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
