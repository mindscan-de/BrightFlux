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
package de.mindscan.brightflux.viewer.parts.ov;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntry;

/**
 * 
 */
public class DataFrameJournalEntriesColumnLabelProvider extends ColumnLabelProvider {

    private final String columnName;

    public final static String TIMESTAMP_HEADER = "Timestamp";
    public final static String TYPE_HEADER = "Type";
    public final static String MESSAGE_HEADER = "Message";

    /**
     * 
     */
    public DataFrameJournalEntriesColumnLabelProvider( String columnName ) {
        this.columnName = columnName;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getText( Object element ) {
        if (element == null) {
            return "";
        }

        DataFrameJournalEntry entry = (DataFrameJournalEntry) element;

        switch (columnName) {
            case MESSAGE_HEADER:
                return entry.getLogMessage();
            case TYPE_HEADER:
                return entry.getEntryType().name();
            case TIMESTAMP_HEADER:
                return Long.toString( entry.getTimestamp() );
            default:
                return super.getText( element );
        }
    }
}
