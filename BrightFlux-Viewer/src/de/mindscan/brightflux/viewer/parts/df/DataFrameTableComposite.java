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
package de.mindscan.brightflux.viewer.parts.df;

import java.nio.file.Path;
import java.util.Collection;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameImpl;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.filterpredicate.DataFrameRowFilterPredicateFactory;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.system.commands.DataFrameCommandFactory;
import de.mindscan.brightflux.system.events.BFEventFactory;
import de.mindscan.brightflux.system.filedescription.FileDescriptions;
import de.mindscan.brightflux.viewer.parts.ui.BrightFluxFileDialogs;
import de.mindscan.brightflux.viewer.uievents.DataFrameRowSelectedEvent;

/**
 * 
 */
public class DataFrameTableComposite extends Composite implements ProjectRegistryParticipant {

    private Table table;

    private ProjectRegistry projectRegistry;

    private DataFrame ingestedDF = new DataFrameImpl( "(empty)" );

    private TableViewer tableViewer;

    private Shell parentShell;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public DataFrameTableComposite( Composite parent, int style ) {
        super( parent, style );

        parentShell = parent.getShell();

        buildLayout();
    }

    private void buildLayout() {
        tableViewer = new TableViewer( this, SWT.BORDER | SWT.FULL_SELECTION );
        table = tableViewer.getTable();
        table.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                if (e.item != null && e.item.getData() != null) {
                    DataFrameRow rowData = (DataFrameRow) e.item.getData();
                    selectDataFrameRow( rowData.getRowIndex(), rowData );
                }
            }
        } );

        table.addMenuDetectListener( new MenuDetectListener() {
            public void menuDetected( MenuDetectEvent e ) {
                table.getMenu().setVisible( true );
            }
        } );

        table.setLinesVisible( true );
        table.setHeaderVisible( true );
        table.setFont( SWTResourceManager.getFont( "Tahoma", 10, SWT.NORMAL ) );
        table.setBounds( 0, 0, 446, 278 );

        Menu menu = new Menu( table );
        table.setMenu( menu );

        MenuItem mntmDataframe = new MenuItem( menu, SWT.CASCADE );
        mntmDataframe.setText( "DataFrame" );

        Menu menu_DataFrame = new Menu( mntmDataframe );
        mntmDataframe.setMenu( menu_DataFrame );

        MenuItem mntmJournal = new MenuItem( menu_DataFrame, SWT.CASCADE );
        mntmJournal.setText( "Journal" );

        Menu menu_4 = new Menu( mntmJournal );
        mntmJournal.setMenu( menu_4 );

        MenuItem mntmSaveAsRecipe = new MenuItem( menu_4, SWT.NONE );
        mntmSaveAsRecipe.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                BrightFluxFileDialogs.saveRegularFileAndConsumePath( parentShell, "Save Recipe", //
                                FileDescriptions.BFRECIPE, //
                                path -> {
                                    saveRecipe( ingestedDF, path );
                                } );
            }
        } );
        mntmSaveAsRecipe.setText( "Save As Recipe ..." );

        MenuItem mntmSaveToFile = new MenuItem( menu_DataFrame, SWT.NONE );
        mntmSaveToFile.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                BrightFluxFileDialogs.saveRegularFileAndConsumePath( parentShell, "Save Data Frame", //
                                FileDescriptions.CSV, //
                                path -> {
                                    saveAsCSV( ingestedDF, path );
                                } );
            }
        } );
        mntmSaveToFile.setText( "Save As CSV ..." );

        MenuItem mntmFilters = new MenuItem( menu, SWT.CASCADE );
        mntmFilters.setText( "Filters" );

        Menu menu_2 = new Menu( mntmFilters );
        mntmFilters.setMenu( menu_2 );

        MenuItem mntmFilterText = new MenuItem( menu_2, SWT.NONE );
        mntmFilterText.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                IInputValidator validator = new IInputValidator() {
                    @Override
                    public String isValid( String newText ) {
                        return null;
                    }
                };
                InputDialog inputDialog = new InputDialog( parentShell, "Text", "Text used for filter.", "", validator );
                int inputresult = inputDialog.open();

                if (inputresult == InputDialog.OK) {
                    String value = inputDialog.getValue();
                    if (value != null && !value.isBlank()) {
                        applyValueFilter( ingestedDF, value );
                    }
                }
            }
        } );
        mntmFilterText.setText( "Filter Text ..." );

//       SelectionEvent e
//      Point absolute = new Point( e.x, e.y );
//      Point pt = table.toControl( absolute );
//      int columnIndex = columnIndexAtposition( table, pt );
//      if (columnIndex >= 0) {
//          if (pt.y < table.getHeaderHeight()) {
//              // header of columnindex (right) clicked....
//          }
//          else {
//              // content of columnindex (right) clicked.
//              // TODO open a menu here...
//          }
//      }

        MenuItem mntmFilterx = new MenuItem( menu_2, SWT.NONE );
        mntmFilterx.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                apply666filter( ingestedDF );
            }

        } );
        mntmFilterx.setText( "filter 666" );

        MenuItem mntmRecipe = new MenuItem( menu, SWT.CASCADE );
        mntmRecipe.setText( "Recipe" );

        Menu menu_3 = new Menu( mntmRecipe );
        mntmRecipe.setMenu( menu_3 );

        MenuItem mntmApplyRecipe = new MenuItem( menu_3, SWT.NONE );
        mntmApplyRecipe.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                BrightFluxFileDialogs.openRegularFileAndConsumePath( parentShell, "Select Recipe to Execute", //
                                FileDescriptions.BFRECIPE, //
                                path -> {
                                    applyRecipe( ingestedDF, path );
                                } );
            }
        } );
        mntmApplyRecipe.setText( "Apply Recipe ..." );

        MenuItem mntmLoganalysisframe = new MenuItem( menu, SWT.NONE );
        mntmLoganalysisframe.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                createSparseDataframe();
            }
        } );
        mntmLoganalysisframe.setText( "Enable Annotations" );
    }

    /**
     * @param ingestedDF the ingestedDF to set
     */
    public void setDataFrame( DataFrame dataFrame ) {
        this.ingestedDF = dataFrame;

        if (this.ingestedDF != null) {
            appenddDataFrameColumns( ingestedDF, tableViewer, this );

            tableViewer.setContentProvider( new DataFrameContentProvider() );
            tableViewer.setInput( ingestedDF );
        }
        else {
            tableViewer.setContentProvider( new DataFrameContentProvider() );
            tableViewer.setInput( null );
        }
    }

    public DataFrame getDataFrame() {
        return ingestedDF;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {

        this.projectRegistry = projectRegistry;
    }

//    private static int columnIndexAtposition( Table table, Point pt ) {
//        int colIndex = -1;
//        TableItem testRow = new TableItem( table, 0 );
//        for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
//            Rectangle bounds = testRow.getBounds( columnIndex );
//            if ((pt.x >= bounds.x) && (pt.x < (bounds.x + bounds.width))) {
//                colIndex = columnIndex;
//            }
//        }
//        testRow.dispose();
//        return colIndex;
//    }

    private void appenddDataFrameColumns( DataFrame ingestedDF, TableViewer tableViewer, Composite composite ) {
        Collection<String> columnNames = ingestedDF.getColumnNames();

        TableColumnLayout tcl_composite = new TableColumnLayout();
        composite.setLayout( tcl_composite );

        for (final String columname : columnNames) {
            TableViewerColumn tableViewerColumn = new TableViewerColumn( tableViewer, SWT.NONE );
            TableColumn tableColumn = tableViewerColumn.getColumn();

            // TODO: take the width from the configuration? 
            // Settings or width of presentation?
            tcl_composite.setColumnData( tableColumn, new ColumnPixelData( 45, true, true ) );
            tableColumn.setText( columname );

            // TODO: the labelprovider should depend on the columnype
            // TODO: the labelprovider should depend also on a configuration for presentation,
            //       e.g. present long-value as timstamp.
            tableViewerColumn.setLabelProvider( new DataFrameColumnLabelProvider( columname ) );
        }
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    public void closeDataframe() {
        this.setVisible( false );

        if (ingestedDF != null) {
            dispatchEvent( BFEventFactory.dataframeClosed( ingestedDF ) );
            setDataFrame( null );
        }
    }

    private void apply666filter( DataFrame dataFrame ) {
        DataFrameRowFilterPredicate predicate = DataFrameRowFilterPredicateFactory.containsStr( "h2.msg", "0x666" );
        dispatchCommand( DataFrameCommandFactory.filterDataFrame( dataFrame, predicate ) );
    }

    protected void applyValueFilter( DataFrame dataFrame, String value ) {
        DataFrameRowFilterPredicate predicate = DataFrameRowFilterPredicateFactory.containsStr( "h2.msg", value );
        dispatchCommand( DataFrameCommandFactory.filterDataFrame( dataFrame, predicate ) );

    }

    private void selectDataFrameRow( int rowIndex, Object rowData ) {
        dispatchEvent( new DataFrameRowSelectedEvent( ingestedDF, rowIndex, rowData ) );
    }

    private void applyRecipe( DataFrame dataFrame, Path recipePath ) {
        dispatchCommand( DataFrameCommandFactory.applyRecipe( dataFrame, recipePath ) );
    }

    private void saveRecipe( DataFrame dataFrame, Path recipePath ) {
        dispatchCommand( DataFrameCommandFactory.saveRecipe( dataFrame, recipePath ) );
    }

    private void saveAsCSV( DataFrame dataFrame, Path targetPath ) {
        dispatchCommand( DataFrameCommandFactory.saveCSV( dataFrame, targetPath ) );
    }

    private void createSparseDataframe() {
        dispatchCommand( DataFrameCommandFactory.createSparseDataFrame() );
    }

//    private void addAnnotation() {
//        if (table.getSelectionCount() > 0) {
//            int selectionIndex = table.getSelectionIndex();
//
//            dispatchCommand( DataFrameCommandFactory.annotateRow( ingestedDF, ingestedDF.getOriginalRowIndex( selectionIndex ),
//                            "This is my new annotation for this row...." ) );
//        }
//    }

    private void dispatchCommand( BFCommand command ) {
        if (projectRegistry != null) {
            projectRegistry.getCommandDispatcher().dispatchCommand( command );
        }
    }

    private void dispatchEvent( BFEvent event ) {
        if (projectRegistry != null) {
            projectRegistry.getEventDispatcher().dispatchEvent( event );
        }
    }
}
