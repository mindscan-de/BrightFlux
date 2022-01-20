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
package de.mindscan.brightflux.plugin.reports.impl;

import de.mindscan.brightflux.plugin.reports.ReportGenerator;

/**
 * 
 */
public class ReportGeneratorImpl implements ReportGenerator {

    private static final String TEMPLATE = "%s\n\n{code}\n%s\n{code}\n\n";
    private String lastMessage = "";
    private String lastRows = "";

    private StringBuilder sb = new StringBuilder();

    @Override
    public void startReport() {
        lastMessage = "";
        lastRows = "";
        sb = new StringBuilder();
    }

    private void addAnnotationMessage( String newMessage ) {
        buildPendingLines();
        lastMessage = newMessage;
        lastRows = "";
    }

    private void addDataRow( String renderedDataRow ) {
        if (lastRows.isEmpty()) {
            lastRows = renderedDataRow;
        }
        else {
            lastRows += "\n" + renderedDataRow;
        }
    }

    @Override
    public String build() {
        buildPendingLines();
        String report = sb.toString();

        // cleanup previous state and start a new report.
        startReport();

        return report;
    }

    private void buildPendingLines() {
        if (lastMessage.isEmpty()) {
            return;
        }
        sb.append( String.format( TEMPLATE, lastMessage, lastRows ) );
    }

    private boolean isOmit( String trimmed ) {
        return "..".equals( trimmed );
    }

    private boolean isConnect( String trimmed ) {
        return ".".equals( trimmed );
    }

    @Override
    public void appendMessageAndRow( String message, String renderedDataRowContent ) {
        String trimmed = message.trim();
        // check it is not any kind of connector or omit'tor
        if (!isConnect( trimmed ) && !isOmit( trimmed )) {
            addAnnotationMessage( message );
        }

        if (isOmit( trimmed )) {
            addDataRow( "[..]" );
        }

        addDataRow( renderedDataRowContent );
    }

}
