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
package de.mindscan.brightflux.plugin.search.commands;

import java.util.function.Consumer;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.framework.command.BFBackgroundCommand;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.plugin.search.SearchComponent;
import de.mindscan.brightflux.plugin.search.backend.furiousiron.SearchResultModel;
import de.mindscan.brightflux.plugin.search.events.SearchEventFactory;
import de.mindscan.brightflux.plugin.search.request.RestRequestService;
import de.mindscan.brightflux.plugin.search.utils.SearchUtils;
import de.mindscan.brightflux.system.services.SystemServices;

/**
 * 
 */
public class PerformSearchCommand implements BFCommand, BFBackgroundCommand {

    private String userQuery;
    @SuppressWarnings( "unused" )
    private String profileQuery;

    /**
     * @param userQuery
     * @param profileQuery
     */
    public PerformSearchCommand( String userQuery, String profileQuery ) {
        this.userQuery = userQuery;
        this.profileQuery = profileQuery;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void execute( Consumer<BFEvent> eventConsumer ) {
        SearchComponent service = SystemServices.getInstance().getService( SearchComponent.class );
        RestRequestService requestService = service.getRestRequestService();

        SearchResultModel queryResult = requestService.requestFuriousIronQueryResults( userQuery );
        DataFrame queryResultDataFrame = SearchUtils.buildResultDataFrame( queryResult );

        eventConsumer.accept( SearchEventFactory.searchResultDataframeCreated( queryResultDataFrame ) );
    }

}
