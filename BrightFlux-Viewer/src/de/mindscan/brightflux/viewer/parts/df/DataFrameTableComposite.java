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
import de.mindscan.brightflux.system.commands.DataFrameCommandFactory;
import de.mindscan.brightflux.system.events.BFEventFactory;
import de.mindscan.brightflux.system.favrecipes.FavRecipesComponent;
import de.mindscan.brightflux.system.favrecipes.FavRecipesKeyUtils;
import de.mindscan.brightflux.system.filedescription.FileDescriptions;
import de.mindscan.brightflux.system.highlighter.HighlighterCallbacks;
import de.mindscan.brightflux.system.highlighter.HighlighterComponent;
import de.mindscan.brightflux.system.services.SystemServices;
import de.mindscan.brightflux.viewer.parts.ui.BrightFluxFileDialogs;
import de.mindscan.brightflux.viewer.uicommands.UICommandFactory;
import de.mindscan.brightflux.viewer.uievents.UIEventFactory;

/**
 * TODO: provide a catalog of recipes and build a filter/recipe menu from it
 */
public class DataFrameTableComposite extends Composite implements ProjectRegistryParticipant {

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
        tableViewer = new TableViewer( this, SWT.BORDER | SWT.FULL_SELECTION );
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

        MenuItem mntmHighlighter = new MenuItem( menu, SWT.CASCADE );
        mntmHighlighter.setText( "Highlighter" );

        Menu menu_1 = new Menu( mntmHighlighter );
        mntmHighlighter.setMenu( menu_1 );

        MenuItem mntmEnableFeature = new MenuItem( menu_1, SWT.NONE );
        mntmEnableFeature.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                if (projectRegistry != null) {
                    BFCommand command = DataFrameCommandFactory.createHighlightDataFrame();
                    dispatchCommand( command );
                }
            }
        } );
        mntmEnableFeature.setText( "Enable Feature" );

        new MenuItem( menu_1, SWT.SEPARATOR );

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

                    BFCommand command = DataFrameCommandFactory.clearHighlightRow( ingestedDF, rowIndex );
                    dispatchCommand( command );
                }
            }
        } );
        mntmHighlightNone.setText( "Highlight Clear" );

        new MenuItem( menu_1, SWT.SEPARATOR );

        MenuItem mntmSaveHighlights = new MenuItem( menu_1, SWT.NONE );
        mntmSaveHighlights.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                String header = "Save Highlight Information";
                BrightFluxFileDialogs.saveRegularFileAndConsumePath( parentShell, header, FileDescriptions.BF_HIGHLIGHT, p -> {
                    if (highlighterComponent != null) {
                        if (highlighterComponent.getLogHighlightFrame() != null) {
                            BFCommand command = DataFrameCommandFactory.saveHighlightDataFrame( highlighterComponent.getLogHighlightFrame(), p );
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
                    BFCommand command = DataFrameCommandFactory.loadHighlightDataFrame( p );
                    dispatchCommand( command );
                } );

            }
        } );
        mntmLoadHighlights.setText( "Load Highlights ..." );

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
            tableViewerColumn.setLabelProvider( labelProvider );
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

    protected void applyValueFilter( DataFrame dataFrame, String value ) {
        DataFrameRowFilterPredicate predicate = DataFrameRowFilterPredicateFactory.containsStr( "h2.msg", value );
        dispatchCommand( DataFrameCommandFactory.filterDataFrame( dataFrame, predicate ) );

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
            BFCommand command = DataFrameCommandFactory.highlightRow( ingestedDF, currentSelectedRow.getOriginalRowIndex(), color );
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
        favoriteMenus.put( "", menu );

        FavRecipesComponent favRecipeService = systemServices.getFavRecipesServices();

        if (favRecipeService == null) {
            return;
        }

        for (String favoriteRecipeKey : favRecipeService.getAllIntermediateNodes()) {
            // select the menu because of the parent key:
            String parentKey = FavRecipesKeyUtils.calculateParent( favoriteRecipeKey );
            if (parentKey == null) {
                parentKey = "";
            }
            Menu parentMenu = favoriteMenus.get( parentKey );

            // Build the menu for the correct parentMenu
            MenuItem menuItem = new MenuItem( parentMenu, SWT.CASCADE );
            menuItem.setText( FavRecipesKeyUtils.calculateName( favoriteRecipeKey ) );

            Menu subMenu = new Menu( menuItem );
            menuItem.setMenu( subMenu );

            // register created the new menu for the current favoriteRecipeKey
            favoriteMenus.put( favoriteRecipeKey, subMenu );
        }
    }

    private void appendFavoriteRecipeShortcutItems( SystemServices systemServices ) {

        FavRecipesComponent favRecipeService = systemServices.getFavRecipesServices();

        if (favRecipeService == null) {
            return;
        }

        for (String favoriteRecipeKey : favRecipeService.getAllLeafNodes()) {
            String parentKey = FavRecipesKeyUtils.calculateParent( favoriteRecipeKey );
            if (parentKey == null) {
                parentKey = "";
            }
            Menu parentMenu = favoriteMenus.get( parentKey );

            // Build the menu for the correct parentMenu
            MenuItem menuItem = new MenuItem( parentMenu, SWT.NONE );

            SelectionAdapter selectionAdapter = new PathSelectionAdapter( favoriteRecipeKey );
            menuItem.addSelectionListener( selectionAdapter );

            menuItem.setText( FavRecipesKeyUtils.calculateName( favoriteRecipeKey ) );

        }

    }

    public void applyFavoriteRecipe( String key ) {
        SystemServices systemServices = SystemServices.getInstance();
        FavRecipesComponent favRecipeService = systemServices.getFavRecipesServices();

        Path favoriteRecipePath = favRecipeService.getFavorite( key );

        applyRecipe( ingestedDF, favoriteRecipePath );
    }
}
