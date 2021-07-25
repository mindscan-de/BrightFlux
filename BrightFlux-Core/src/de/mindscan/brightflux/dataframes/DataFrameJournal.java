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
package de.mindscan.brightflux.dataframes;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntry;

/**
 * A DataframeJournal keeps a log on what Data-Operations are executed on a particular DataFrame 
 * or a particular DataFrameView. The Idea is to have the raw data available and to replay every
 * data operation and then reach the same state, which we want to view or to present or to export
 * to the customer.
 * 
 * The Idea is to preserve the original data and just have a view on this data. It is like having
 * a raw image file and only to store the transformations to recreate the working state, as long
 * as the image is not finally rendered and saved to the disk.
 * 
 * The same applies for Log-Data, we don't have the intent to modify it, we rather want to re-apply
 * the data transformations.
 * 
 * This concept will also enable one of the future key features, to save receipts for data 
 * preparation / cleanup, which can be used for first analysis.
 * 
 * A Dataframe view can apply a dataframe journal. We also can have redo and undo operations on 
 * the DataFrame, because we can calculate the previous state, by applying every operation before 
 * the change.
 *  
 */
public class DataFrameJournal {

    private List<DataFrameJournalEntry> journal = new LinkedList<>();

    public DataFrameJournal() {
        this.journal = new LinkedList<>();
    }

    public List<DataFrameJournalEntry> getJournalEntries() {
        return journal;
    }

    public void addJournalEntry( DataFrameJournalEntry entry ) {
        journal.add( entry );
    }

    public void addAllJournalEntries( Collection<DataFrameJournalEntry> entries ) {
        journal.addAll( entries );
    }

}
