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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
import de.mindscan.brightflux.dataframes.DataFrameBuilder;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.DataFrameRowQueryCallback;
import de.mindscan.brightflux.dataframes.filterpredicate.DataFrameRowFilterPredicateFactory;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.favrecipes.FavRecipesComponent;
import de.mindscan.brightflux.plugin.highlighter.HighlighterCallbacks;
import de.mindscan.brightflux.plugin.highlighter.HighlighterComponent;
import de.mindscan.brightflux.plugin.highlighter.commands.HighlighterCommandFactory;
import de.mindscan.brightflux.system.commands.DataFrameCommandFactory;
import de.mindscan.brightflux.system.events.BFEventFactory;
import de.mindscan.brightflux.system.filedescription.FileDescriptions;
import de.mindscan.brightflux.system.services.SystemServices;
import de.mindscan.brightflux.viewer.parts.ui.BrightFluxFileDialogs;
import de.mindscan.brightflux.viewer.uicommands.UICommandFactory;
import de.mindscan.brightflux.viewer.uievents.LocatePredictedTimestampRequestedHandler;
import de.mindscan.brightflux.viewer.uievents.UIEventFactory;
import de.mindscan.brightflux.viewer.uiplugin.search.command.SearchUICommandsFactory;

/**
 * 
 */
public class DataFrameTableComposite extends Composite implements ProjectRegistryParticipant, LocatePredictedTimestampRequestedHandler {

    // this is not good enough yet, because some operations should be dependend on the columns...
    private static final String HARDCODED_H2_MSG = "h2.msg";
    private static final String HARDCODED_HXX_MSG = "hxx.msg";
    private static final String HARDCODED_H1_TS = "h1.ts";

    /**
     * 
     */
    private final class PathSelectionAdapter extends SelectionAdapter {

        private String key;

        public PathSelectionAdapter( String key ) {
            this.key = key;
        }

        public void widgetSelected( SelectionEvent e ) {
            applyFavoriteRecipe( key );
        }

    }

    private Table table;

    private ProjectRegistry projectRegistry;

    private DataFrame ingestedDF = new DataFrameBuilder( "(empty)" ).build();;

    private TableViewer tableViewer;

    private Shell parentShell;

    private HighlighterComponent highlighterComponent;

    protected DataFrameRow currentSelectedRow;

    private Map<String, Menu> favoriteMenus = new HashMap<>();

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
        tableViewer = new TableViewer( this, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL );
        tableViewer.setUseHashlookup( true );
        table = tableViewer.getTable();
        table.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                if (e.item != null && e.item.getData() != null) {
                    DataFrameRow rowData = (DataFrameRow) e.item.getData();
                    currentSelectedRow = rowData;
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

        new MenuItem( menu_DataFrame, SWT.SEPARATOR );

        MenuItem mntmTokenizeAsHxx = new MenuItem( menu_DataFrame, SWT.NONE );
        mntmTokenizeAsHxx.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                tokenizeAsHXX( ingestedDF, HARDCODED_H2_MSG );
            }
        } );
        mntmTokenizeAsHxx.setText( "Tokenize as HXX" );

        new MenuItem( menu_DataFrame, SWT.SEPARATOR );

        MenuItem mntmLocateTimestampToAll = new MenuItem( menu_DataFrame, SWT.NONE );
        mntmLocateTimestampToAll.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                // TODO: shortcut here.  hardcoded values...
                // Actually we should locate a column containing time stamps
                // then ask the user which to take if many, or just register them all...
                long timestamp = ((Long) currentSelectedRow.get( HARDCODED_H1_TS )).longValue();
                dispatchLocateAll( HARDCODED_H1_TS, timestamp );
            }
        } );
        mntmLocateTimestampToAll.setText( "Locate Timestamp to All" );

        MenuItem mntmFilters = new MenuItem( menu, SWT.CASCADE );
        mntmFilters.setText( "Filters" );

        Menu menu_2 = new Menu( mntmFilters );
        mntmFilters.setMenu( menu_2 );

        MenuItem mntmFilterContainsText = new MenuItem( menu_2, SWT.NONE );
        mntmFilterContainsText.addSelectionListener( new SelectionAdapter() {
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
                        applyContainsValueFilter( ingestedDF, value, HARDCODED_H2_MSG );
                    }
                }
            }
        } );
        mntmFilterContainsText.setText( "h2.msg contains Text ..." );

        MenuItem mntmFilterNotContainsText = new MenuItem( menu_2, SWT.NONE );
        mntmFilterNotContainsText.addSelectionListener( new SelectionAdapter() {
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
                        applyNotContainsValueFilter( ingestedDF, value, HARDCODED_H2_MSG );
                    }
                }
            }
        } );
        mntmFilterNotContainsText.setText( "h2.msg not contains Text ..." );

        new MenuItem( menu_2, SWT.SEPARATOR );

        MenuItem mntmHxxmsgContainsText = new MenuItem( menu_2, SWT.NONE );
        mntmHxxmsgContainsText.addSelectionListener( new SelectionAdapter() {
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
                        applyContainsValueFilter( ingestedDF, value, HARDCODED_HXX_MSG );
                    }
                }
            }
        } );
        mntmHxxmsgContainsText.setText( "hxx.msg contains Text ..." );

        MenuItem mntmHxxmsgNotContains = new MenuItem( menu_2, SWT.NONE );
        mntmHxxmsgNotContains.addSelectionListener( new SelectionAdapter() {
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
                        applyNotContainsValueFilter( ingestedDF, value, HARDCODED_HXX_MSG );
                    }
                }
            }
        } );
        mntmHxxmsgNotContains.setText( "hxx.msg not contains Text ..." );

        MenuItem mntmHighlighter = new MenuItem( menu, SWT.CASCADE );
        mntmHighlighter.setText( "Highlighter" );

        Menu menu_1 = new Menu( mntmHighlighter );
        mntmHighlighter.setMenu( menu_1 );

        MenuItem mntmHighlightYellow = new MenuItem( menu_1, SWT.NONE );
        mntmHighlightYellow.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                if (projectRegistry != null) {
                    highlightRow( currentSelectedRow, "yellow" );
                }
            }
        } );
        mntmHighlightYellow.setText( "Highlight Yellow" );

        MenuItem mntmHighlightPink = new MenuItem( menu_1, SWT.NONE );
        mntmHighlightPink.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                if (projectRegistry != null) {
                    highlightRow( currentSelectedRow, "pink" );
                }
            }
        } );
        mntmHighlightPink.setText( "Highlight Pink" );

        MenuItem mntmHighlightRed = new MenuItem( menu_1, SWT.NONE );
        mntmHighlightRed.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                if (projectRegistry != null) {
                    highlightRow( currentSelectedRow, "red" );
                }
            }
        } );
        mntmHighlightRed.setText( "Highlight Red" );

        MenuItem mntmHighlightGreen = new MenuItem( menu_1, SWT.NONE );
        mntmHighlightGreen.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                if (projectRegistry != null) {
                    highlightRow( currentSelectedRow, "green" );
                }
            }
        } );
        mntmHighlightGreen.setText( "Highlight Green" );

        MenuItem mntmHighlightBlue = new MenuItem( menu_1, SWT.NONE );
        mntmHighlightBlue.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                if (projectRegistry != null) {
                    highlightRow( currentSelectedRow, "blue" );
                }
            }
        } );
        mntmHighlightBlue.setText( "Highlight Blue" );

        MenuItem mntmHighlightNone = new MenuItem( menu_1, SWT.NONE );
        mntmHighlightNone.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                if (projectRegistry != null) {
                    DataFrameRow rowData = (DataFrameRow) e.item.getData();
                    int rowIndex = rowData.getOriginalRowIndex();

                    BFCommand command = HighlighterCommandFactory.clearHighlightRow( ingestedDF, rowIndex );
                    dispatchCommand( command );
                }
            }
        } );
        mntmHighlightNone.setText( "Highlight Clear" );

        new MenuItem( menu_1, SWT.SEPARATOR );

        MenuItem mntmClearAllHighlights = new MenuItem( menu_1, SWT.NONE );
        mntmClearAllHighlights.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                if (projectRegistry != null) {
                    BFCommand command = HighlighterCommandFactory.createHighlightDataFrame();
                    dispatchCommand( command );
                }
            }
        } );
        mntmClearAllHighlights.setText( "Clear All Highlights" );

        new MenuItem( menu_1, SWT.SEPARATOR );

        MenuItem mntmSaveHighlights = new MenuItem( menu_1, SWT.NONE );
        mntmSaveHighlights.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                String header = "Save Highlight Information";
                BrightFluxFileDialogs.saveRegularFileAndConsumePath( parentShell, header, FileDescriptions.BF_HIGHLIGHT, p -> {
                    if (highlighterComponent != null) {
                        if (highlighterComponent.getLogHighlightFrame() != null) {
                            BFCommand command = HighlighterCommandFactory.saveHighlightDataFrame( highlighterComponent.getLogHighlightFrame(), p );
                            dispatchCommand( command );
                        }
                    }
                } );
            }
        } );
        mntmSaveHighlights.setText( "Save Highlights ..." );

        MenuItem mntmLoadHighlights = new MenuItem( menu_1, SWT.NONE );
        mntmLoadHighlights.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                String header = "Load Highlights File.";
                BrightFluxFileDialogs.openRegularFileAndConsumePath( parentShell, header, FileDescriptions.BF_HIGHLIGHT, p -> {
                    BFCommand command = HighlighterCommandFactory.loadHighlightDataFrame( p );
                    dispatchCommand( command );
                } );

            }
        } );
        mntmLoadHighlights.setText( "Load Highlights ..." );

        MenuItem mntmSearch = new MenuItem( menu, SWT.CASCADE );
        mntmSearch.setText( "Search" );

        Menu menu_4 = new Menu( mntmSearch );
        mntmSearch.setMenu( menu_4 );

        MenuItem mntmFuriousironCodeSearch = new MenuItem( menu_4, SWT.NONE );
        mntmFuriousironCodeSearch.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                // current selection
                // create uicommand for search
                // dispatch the ui command for search
                if (currentSelectedRow != null) {
                    dispatchCommand( SearchUICommandsFactory.searchDataFrameRow( currentSelectedRow, "default" ) );
                }

                // That will hopefully locate the search window
                // the search window is already subscribed to the kind of events...
                // and if not open the search window
                // and then create the event to request the search window to prepare the search based on the current selection

            }
        } );
        mntmFuriousironCodeSearch.setText( "FuriousIron Search" );

        new MenuItem( menu_4, SWT.SEPARATOR );

        MenuItem mntmSearchProfiles = new MenuItem( menu_4, SWT.CASCADE );
        mntmSearchProfiles.setText( "Search Profiles" );

        Menu menu_5 = new Menu( mntmSearchProfiles );
        mntmSearchProfiles.setMenu( menu_5 );

        new MenuItem( menu, SWT.SEPARATOR );

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

        MenuItem mntmSaveAsRecipe = new MenuItem( menu_3, SWT.NONE );
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

        new MenuItem( menu_3, SWT.SEPARATOR );

        // start Favorites Menu ... Append to the Recipe Menu...
        appendFavoriteRecipeShortcutMenus( SystemServices.getInstance(), menu_3 );
        appendFavoriteRecipeShortcutItems( SystemServices.getInstance() );

        new MenuItem( menu, SWT.SEPARATOR );

        MenuItem mntmRefreshTableContent = new MenuItem( menu, SWT.NONE );
        mntmRefreshTableContent.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                refresh();
            }
        } );
        mntmRefreshTableContent.setText( "Refresh table content" );

        MenuItem mntmCopytoclipboard = new MenuItem( menu, SWT.NONE );
        mntmCopytoclipboard.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                String rowValue = Arrays.stream( currentSelectedRow.getAll() ).map( v -> String.valueOf( v ) ).collect( Collectors.joining( "\t" ) );
                BFCommand command = UICommandFactory.copyToClipboard( parentShell, rowValue );
                dispatchCommand( command );
            }
        } );
        mntmCopytoclipboard.setText( "Copy Row to Clipboard" );
    }

    protected void dispatchLocateAll( String columnName, long timestamp ) {
        dispatchCommand( UICommandFactory.locatePredictedTimestampForColumnAllDataframes( columnName, timestamp ) );
    }

    public void refresh() {
        setVisible( false );
        tableViewer.refresh();
        setVisible( true );
    }

    /**
     * @param ingestedDF the ingestedDF to set
     */
    public void setDataFrame( DataFrame dataFrame ) {
        this.ingestedDF = dataFrame;

        if (this.ingestedDF != null) {
            appendDataFrameColumns( ingestedDF, tableViewer, this );

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

    private void appendDataFrameColumns( DataFrame ingestedDF, TableViewer tableViewer, Composite composite ) {
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

            DataFrameColumnLabelProvider labelProvider = new DataFrameColumnLabelProvider( columname );
            labelProvider.setHighLightherComponent( highlighterComponent );
            // TODO: implement setting the ui highlighter component
            // labelProvider.setHighlighterUIComponent( highlighterUIComponent );
            tableViewerColumn.setLabelProvider( labelProvider );
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void handleLocatePredictedTimestamp( String columnName, long timestamp ) {
        // TODO: Implement the locate index feature in UI
        // TODO: use currentDataFrame, to locate the "insert" position in the given ColumnName
        // TODO: now somehow scroll such that index position is in the visible range.
        int predictedRow = this.ingestedDF.predictRowIndex( columnName, Long.valueOf( timestamp ) );
        System.out.println( "predicted Row = " + predictedRow );

        tableViewer.getTable().setSelection( predictedRow );
        tableViewer.getTable().showSelection();
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

    protected void applyContainsValueFilter( DataFrame dataFrame, String value, String columnName ) {
        DataFrameRowFilterPredicate predicate = DataFrameRowFilterPredicateFactory.containsStr( columnName, value );
        dispatchCommand( DataFrameCommandFactory.filterDataFrame( dataFrame, predicate ) );
    }

    protected void applyNotContainsValueFilter( DataFrame dataFrame, String value, String columnName ) {
        DataFrameRowFilterPredicate predicate = DataFrameRowFilterPredicateFactory.not( DataFrameRowFilterPredicateFactory.containsStr( columnName, value ) );
        dispatchCommand( DataFrameCommandFactory.filterDataFrame( dataFrame, predicate ) );
    }

    protected void tokenizeAsHXX( DataFrame ingestedDF2, String inputColumn ) {
        dispatchCommand( DataFrameCommandFactory.ingestSpecialHXX( ingestedDF2, inputColumn ) );
    }

    private void selectDataFrameRow( int rowIndex, Object rowData ) {
        dispatchEvent( UIEventFactory.dataFrameRowSelected( ingestedDF, rowIndex, rowData ) );
    }

    private void applyRecipe( DataFrame dataFrame, Path recipePath ) {
        Map<String, DataFrameRowQueryCallback> callbacks = HighlighterCallbacks.getInstance().getCallbacks();
        dispatchCommand( DataFrameCommandFactory.applyRecipe( dataFrame, recipePath, callbacks ) );
    }

    private void saveRecipe( DataFrame dataFrame, Path recipePath ) {
        dispatchCommand( DataFrameCommandFactory.saveRecipe( dataFrame, recipePath ) );
    }

    private void saveAsCSV( DataFrame dataFrame, Path targetPath ) {
        dispatchCommand( DataFrameCommandFactory.saveCSV( dataFrame, targetPath ) );
    }

    private void highlightRow( DataFrameRow currentSelectedRow, String color ) {
        if (currentSelectedRow != null) {
            BFCommand command = HighlighterCommandFactory.highlightRow( ingestedDF, currentSelectedRow.getOriginalRowIndex(), color );
            dispatchCommand( command );
        }
    }

    private void dispatchCommand( BFCommand command ) {
        if (projectRegistry != null && command != null) {
            projectRegistry.getCommandDispatcher().dispatchCommand( command );
        }
    }

    private void dispatchEvent( BFEvent event ) {
        if (projectRegistry != null && event != null) {
            projectRegistry.getEventDispatcher().dispatchEvent( event );
        }
    }

    public void setHighlighterComponent( HighlighterComponent highlighterComponent ) {
        this.highlighterComponent = highlighterComponent;
    }

    private void appendFavoriteRecipeShortcutMenus( SystemServices systemServices, Menu menu ) {
        favoriteMenus.put( FavRecipesComponent.ROOT, menu );

        FavRecipesComponent favRecipeService = systemServices.getService( FavRecipesComponent.class );

        if (favRecipeService != null) {
            favRecipeService.forAllIntermediateNodes( this::buildCascadingMenu );
        }
    }

    private void buildCascadingMenu( String favoriteRecipeKey, String menuName, String parentKey ) {
        MenuItem menuItem = new MenuItem( favoriteMenus.get( parentKey ), SWT.CASCADE );
        menuItem.setText( menuName );

        Menu subMenu = new Menu( menuItem );
        menuItem.setMenu( subMenu );

        // register created the new menu for the current favoriteRecipeKey
        favoriteMenus.put( favoriteRecipeKey, subMenu );
    }

    private void appendFavoriteRecipeShortcutItems( SystemServices systemServices ) {
        FavRecipesComponent favRecipeService = systemServices.getService( FavRecipesComponent.class );

        if (favRecipeService != null) {
            favRecipeService.forAllLeafNodes( this::buildFinalMenuItem );
        }
    }

    private void buildFinalMenuItem( String favoriteRecipeKey, String recipeName, String parentKey ) {
        MenuItem menuItem = new MenuItem( favoriteMenus.get( parentKey ), SWT.NONE );

        SelectionAdapter selectionAdapter = new PathSelectionAdapter( favoriteRecipeKey );
        menuItem.addSelectionListener( selectionAdapter );
        menuItem.setText( recipeName );
    }

    private void applyFavoriteRecipe( String key ) {
        SystemServices systemServices = SystemServices.getInstance();
        FavRecipesComponent favRecipeService = systemServices.getService( FavRecipesComponent.class );

        if (favRecipeService != null) {
            Path favoriteRecipePath = favRecipeService.getFavorite( key );
            applyRecipe( ingestedDF, favoriteRecipePath );
        }
    }
}
