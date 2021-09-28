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
package de.mindscan.brightflux.system.commands.io;

import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.events.BFEvent;

/**
 * 
 */
public class ExpandProprietaryZipStreamCommand implements BFCommand {

    private Path filePath;

    /**
     * @param filePath
     */
    public ExpandProprietaryZipStreamCommand( Path filePath ) {
        this.filePath = filePath;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void execute( Consumer<BFEvent> eventConsumer ) {
        // TODO Auto-generated method stub

        // TODO: Check if the file starts with PK - Phil Kahn (Zipfile)

        // we want to expand the path by the directory "expanded"
        // we want to create the target file position and the parent directory "expanded"...

        long processed = 0L;

        // TODO: write the output stream...

        try (ZipInputStream zip = new ZipInputStream( Files.newInputStream( filePath, StandardOpenOption.READ ) )) {

            ZipEntry firstEnry = zip.getNextEntry();
            System.out.println( "firstEntryName... is: '" + firstEnry.getName() + "'" );

            byte[] data;
            while (true) {
                data = zip.readNBytes( 8192 );
                if (data == null) {
                    // TODO: close the outputstream
                    break;
                }

                int dataLength = data.length;
                processed += dataLength;

                if (dataLength > 0) {
                    // TODO: write the data to the disk to the target file
                    // TODO: write all bytes to the outputstream
                }

                if (dataLength < 8192) {
                    // TODO: close the outputstream
                    break;
                }

                // i have some kind of corrupt/broken format... where i will always produce an EOFEception. It is intentionally encoded this way.
            }

            System.out.println( "Processed this numbe of expanded bytes: " + processed );
        }
        catch (EOFException eof) {
            // we know this happens with this file format....
            System.out.println( "We knew this would happen..." );
        }
        catch (IOException e) {
            // this is something unexpected....
            e.printStackTrace();
        }

        System.out.println( "Processed this number of expanded bytes: " + processed );
    }

}
