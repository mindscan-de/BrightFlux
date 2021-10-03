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
import java.io.OutputStream;
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
        // TODO: Check if the file starts with PK - Phil Kahn (Zipfile)

        // we want to expand the path by the directory "expanded"
        // we want to create the target file position and the parent directory "expanded"...

        long processedSize = 0L;
        int startChunkSize = 1536;

        long safeSize = calculateSafeSize( startChunkSize, filePath );

        Path extractFile = filePath.getParent().resolve( "extracted" ).resolve( filePath.getFileName() + ".bin" );
        try {
            Files.createDirectories( extractFile.getParent() );
            Files.createFile( extractFile );
        }
        catch (Exception e) {
            return;
        }

        System.out.println( " We should exract to this file path.. " + extractFile.toAbsolutePath().toString() );

        try (OutputStream outputStream = Files.newOutputStream( extractFile, StandardOpenOption.WRITE );
                        ZipInputStream zip = new ZipInputStream( Files.newInputStream( filePath, StandardOpenOption.READ ) )) {

            ZipEntry firstEntry = zip.getNextEntry();
            long extractedSize = firstEntry.getSize();

            if (extractedSize != -1) {
                safeSize = extractedSize;
            }

            // in this proprietary format it has a size of -1 (probably threated as unknown)
            // actually we then need to read this file once, until we find the cutoff, and from there we read byte for byte....
            // we note the cutoff and after we reached the cutoff we switch tactics and read byte by byte...

            System.out.println( "firstEntryName... is: '" + firstEntry.getName() + "' and is " + firstEntry.getSize() + " bytes long... safe size is: "
                            + safeSize );

            int chunksize = startChunkSize;

            byte[] data;
            while (true) {
                // once the safesize is reached, we can't read safesize bytes any more in full, but from here we go byte by byte.
                if (processedSize >= safeSize) {
                    chunksize = 1;
                }

                data = zip.readNBytes( chunksize );
                if (data == null) {
                    outputStream.close();
                    break;
                }

                int dataLength = data.length;
                processedSize += dataLength;

                if (dataLength > 0) {
                    outputStream.write( data );
                }

                if (dataLength < chunksize) {
                    outputStream.close();
                    break;
                }
            }

            System.out.println( "Processed this numbe of expanded bytes: " + processedSize );
        }
        catch (EOFException eof) {
            // we know this happens with this special file format.... / but here we can close the output file... in case we didn't before.
            System.out.println( "EOF found." );
        }
        catch (IOException e) {
            // this is something unexpected....
            e.printStackTrace();
        }

        System.out.println( "Processed this number of expanded bytes: " + processedSize );
    }

    /**
     * Calculate how much bytes can be safely read using the given chunk size, since we don't know how many bytes 
     * are there, we have the following method: read as many bytes using the given chunksize until we get an 
     * EOF exception.
     *   
     * @param chunkSize
     * @param filePath
     * @return
     */
    private long calculateSafeSize( int chunkSize, Path filePath ) {
        // we must calculate a safe size, by trying to read it once without saving the file...
        // another way would be to detect that the zipinputstream is near the end of the input file...
        // the chunksize would be quarter of the remaining bytes? 

        long processedSize = 0L;

        try (ZipInputStream zip = new ZipInputStream( Files.newInputStream( filePath, StandardOpenOption.READ ) )) {
            // go to first zip entry
            zip.getNextEntry();

            byte[] data;
            while (true) {
                data = zip.readNBytes( chunkSize );
                if (data == null) {
                    break;
                }
                if (data.length == 0) {
                    break;
                }

                processedSize += data.length;
            }
        }
        catch (Exception e) {
            // intentionally left blank
        }

        return processedSize;
    }

}
