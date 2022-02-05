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
package de.mindscan.brightflux.plugin.queryhistory;

import java.util.ArrayDeque;

/**
 * 
 */
public class QueryHistoryComponent {

    private ArrayDeque<String> queryHistoryStack = new ArrayDeque<>();

    /**
     * 
     */
    public QueryHistoryComponent() {
        queryHistoryStack = new ArrayDeque<>();

        initQueryHistory();
    }

    /**
     * 
     */
    private void initQueryHistory() {
        // TODO: Provide previous history through other means.
        queryHistoryStack.add( "SELECT * FROM df WHERE (df.''==  )" );
        queryHistoryStack.add( "SELECT * FROM df WHERE ((df.''==  ) && (df.''==  ))" );
        queryHistoryStack.add( "SELECT * FROM df WHERE (df.''.contains(''))" );

        queryHistoryStack.add( "SELECT 'h1.ts', 'h2.msg' FROM df" );
        queryHistoryStack.add( "SELECT 'h1.ts', 'h2.msg' FROM df WHERE (df.''==  )" );
        queryHistoryStack.add( "SELECT 'h1.ts', 'h2.msg' FROM df WHERE ((df.''==  ) && (df.''==  ))" );

        queryHistoryStack.add( "ROWCALLBACK highlight_red FROM df" );
        queryHistoryStack.add( "ROWCALLBACK highlight_green FROM df" );
        queryHistoryStack.add( "ROWCALLBACK highlight_blue FROM df" );
        queryHistoryStack.add( "ROWCALLBACK highlight_yellow FROM df" );
        queryHistoryStack.add( "ROWCALLBACK highlight_pink FROM df" );
        queryHistoryStack.add( "ROWCALLBACK highlight_  FROM df WHERE (df.''.contains(''))" );
        queryHistoryStack.add( "ROWCALLBACK highlight_  FROM df WHERE (df.''== " );
    }

    public String[] getHistory() {
        return queryHistoryStack.toArray( new String[0] );
    }

    public void addEntry( String query ) {
        queryHistoryStack.addFirst( query );
    }
}
