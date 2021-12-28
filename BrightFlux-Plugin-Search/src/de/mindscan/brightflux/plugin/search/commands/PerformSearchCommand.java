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
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.plugin.search.backend.furiousiron.SearchResultModel;
import de.mindscan.brightflux.plugin.search.events.SearchResultDataframeCreatedEvent;
import de.mindscan.brightflux.plugin.search.request.RestRequestService;
import de.mindscan.brightflux.plugin.search.utils.SearchUtils;

/**
 * 
 */
public class PerformSearchCommand implements BFCommand {

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
        RestRequestService requestService = new RestRequestService();

        SearchResultModel queryResult = requestService.requestFuriousIronQueryResults( userQuery );
        DataFrame queryResultDataFrame = SearchUtils.buildResultDataFrame( queryResult );

        // then create an event, that the search results are available.
        eventConsumer.accept( new SearchResultDataframeCreatedEvent( queryResultDataFrame ) );
    }

}
