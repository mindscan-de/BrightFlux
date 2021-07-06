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
package de.mindscan.brightflux.viewer.commands;

import java.nio.file.Path;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.ingest.IngestHeartCsv;
import de.mindscan.brightflux.viewer.dispatcher.EventDispatcher;
import de.mindscan.brightflux.viewer.events.DataFrameLoadedEvent;

/**
 * 
 */
public class IngestCommand implements BFCommand {

    private Path fileToIngest;

    public IngestCommand( Path fileToIngest ) {
        this.fileToIngest = fileToIngest;
    }

    // This might be executed in some worker thread, or in the same thread as everything else.
    // This depends on the implemenation of the CommandDispatcher.
    /**
     * {@inheritDoc}
     */
    public void execute( EventDispatcher eventDispatcher ) {
        // decide because of file which ingestion to perform, but we decide for a CSV right now.
        // But this might be totally complex... But this encapsulation here should to the trick.

        IngestHeartCsv ingest = new IngestHeartCsv();
        DataFrame newDataFrame1 = ingest.loadCsvAsDataFrameV2( this.fileToIngest );

        // Now tell the Eventdisplatcher, that we have a new DataFrame as a result of our work, issue a 
        // DataframeLoaded Event, where components

        // can subscribe to... e.g.
        eventDispatcher.dispatchEvent( new DataFrameLoadedEvent( newDataFrame1 ) );
    }

}
