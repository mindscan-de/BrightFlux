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
package de.mindscan.brightflux.plugin.search.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.mindscan.brightflux.plugin.search.backend.furiousiron.SearchResultModel;

/**
 * This is a first start to access the furious iron project. This kind of interface should be abstracted away, 
 * so that multiple search engines can be accessed by this search plugin.
 * 
 * Currently there is no need do develop for something else (at this moment in time), but this is not
 * built in stone.
 */
public class RestRequestService {

    public final static String SERVER = "http://localhost:8000/";
    public final static String REST_SEARCH_RESULT = "SearchBackend/rest/search/result?";
    public final static String REST_RETRIEVE_CONTENT = "SearchBackend/rest/cached/content?";

    /**
     * 
     */
    public RestRequestService() {
        // intentionally left blank
    }

    public SearchResultModel requestFuriousIronQueryResults( String query ) {
        try {
            // System.out.println( "Query is = '" + query + "'" );

            Map<String, String> parameters = new LinkedHashMap<>();
            parameters.put( "q", query );

            String parameterString = buildGetParameterString( parameters );

            URL url = new URL( SERVER + REST_SEARCH_RESULT + parameterString );

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod( "GET" );

            connection.setRequestProperty( "Content-Type", "application/json" );

            StringBuffer searchResultContentJsonString = retrieveHTTPResponse( connection );

            SearchResultModel decodedModel = decodeResultModel( SearchResultModel.class, searchResultContentJsonString.toString() );

            // System.out.println( "Number of results: " + decodedModel.getNumberOfQeueryResults() );
            // System.out.println( "Results calculated in (ms): " + decodedModel.getSearchTimeInMs() );

            // decodedModel.getQueryResultItems().stream().forEach( element -> {
            //    System.out.println( element.getQueryResultSimpleFilename() );
            // } );

            return decodedModel;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String requestFuriousIronQueryContentByPath( String pathInformation ) {
        try {
            System.out.println( "Requested Path is = '" + pathInformation + "'" );

            Map<String, String> parameters = new LinkedHashMap<>();
            parameters.put( "p", pathInformation );

            String parameterString = buildGetParameterString( parameters );

            URL url = new URL( SERVER + REST_RETRIEVE_CONTENT + parameterString );

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod( "GET" );

            connection.setRequestProperty( "Content-Type", "text/plain" );

            StringBuffer searchResultContentJsonString = retrieveHTTPResponse( connection );

            return searchResultContentJsonString.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

    private String buildGetParameterString( Map<String, String> parameters ) {
        String parameterString = parameters.entrySet().stream() //
                        .map( entry -> entry.getKey() + "=" + URLEncoder.encode( entry.getValue(), StandardCharsets.UTF_8 ) ) //
                        .collect( Collectors.joining( "&" ) );
        return parameterString;
    }

    private <T> T decodeResultModel( Class<T> class1, String jsonString ) {
        Gson gson = new GsonBuilder().create();

        T fromJson = class1.cast( gson.fromJson( jsonString, class1 ) );

        return fromJson;
    }

    private StringBuffer retrieveHTTPResponse( HttpURLConnection con ) throws IOException {
        StringBuffer requestContent = new StringBuffer();

        int status = con.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            System.out.println( "Status is something other than OK : " + status );
            requestContent.append( "{}" );
            return requestContent;
        }

        try (BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream() ) );) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                requestContent.append( inputLine );
                requestContent.append( "\n" );
            }
        }

        return requestContent;
    }

}
