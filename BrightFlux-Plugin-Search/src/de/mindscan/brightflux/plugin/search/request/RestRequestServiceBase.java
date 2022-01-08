/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 */
public class RestRequestServiceBase {

    /**
     * 
     */
    public RestRequestServiceBase() {
        super();
    }

    protected HttpURLConnection openConnection( String queryURL, Map<String, String> parameters, String requestMethod, String contentType )
                    throws MalformedURLException, IOException {
        String parameterString = buildGetParameterString( parameters );
        URL url = new URL( queryURL + parameterString );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod( requestMethod );
        connection.setRequestProperty( "Content-Type", contentType );

        return connection;
    }

    private String buildGetParameterString( Map<String, String> parameters ) {
        String parameterString = parameters.entrySet().stream() //
                        .map( entry -> entry.getKey() + "=" + URLEncoder.encode( entry.getValue(), StandardCharsets.UTF_8 ) ) //
                        .collect( Collectors.joining( "&" ) );
        return parameterString;
    }

    protected StringBuffer retrieveHTTPResponse( HttpURLConnection con ) throws IOException {
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