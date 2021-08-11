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
package de.mindscan.brightflux.system.commands;

import java.nio.file.Path;
import java.util.function.Consumer;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.ingest.IngestCsv;
import de.mindscan.brightflux.system.events.DataFrameLoadedEvent;

/**
 * The IngestCommand implements the load and convert operation for a given file path to a 
 * DataFrame.
 * 
 * @resultevent {@link DataFrameLoadedEvent}
 */
public class IngestCommand implements BFCommand {

    private Path fileToIngest;

    public IngestCommand( Path fileToIngest ) {
        this.fileToIngest = fileToIngest;
    }

    /**
     * {@inheritDoc}
     */
    public void execute( Consumer<BFEvent> eventConsumer ) {
        // TODO: auto-detect the type of file ?
        // decide because of file which ingest-operation to perform, but we hard decide for a CSV right now.
        // But this might be totally complex... But this encapsulation here should to the trick.

        IngestCsv ingest = new IngestCsv();
        DataFrame resultDataFrame = ingest.loadAsDataFrame( this.fileToIngest );

        eventConsumer.accept( new DataFrameLoadedEvent( resultDataFrame ) );
    }

}
