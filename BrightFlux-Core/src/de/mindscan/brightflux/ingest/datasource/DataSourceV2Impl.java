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
package de.mindscan.brightflux.ingest.datasource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.ingest.datasource.impl.StringBackedDataSourceLexer;

/**
 * This is currently a collection of random datasource providing mechanisms. I want to collect them in a proper place 
 * which can then later be refactored to a proper software architecture. 
 */
public class DataSourceV2Impl implements DataSourceV2 {

    private static final String METHOD_INPUT_STRING = "inputString";
    private static final String METHOD_FILE_PATH = "filePath";

    private Path ingestInputPath;
    private String inputString;
    private String method;

    @Override
    public DataSourceLexer getAsStringBackedDataSourceLexer() {
        switch (method) {
            case METHOD_FILE_PATH: {
                DataSourceLexer lexer = new StringBackedDataSourceLexer();
                lexer.resetTokenPositions();
                lexer.setInputString( readAllLinesFromFile( ingestInputPath ) );
                return lexer;
            }
            case METHOD_INPUT_STRING: {
                DataSourceLexer lexer = new StringBackedDataSourceLexer();
                lexer.resetTokenPositions();
                lexer.setInputString( inputString );
                return lexer;
            }
            default:
                throw new NotYetImplemetedException();
        }
    }

    /**
     * @param ingestInputPath
     */
    @Override
    public void setInput( Path ingestInputPath ) {
        this.ingestInputPath = ingestInputPath;
        this.method = METHOD_FILE_PATH;
    }

    @Override
    public void setInput( String inputString ) {
        this.inputString = inputString;
        this.method = METHOD_INPUT_STRING;
    }

    /**
     * @return the ingestInputPath
     */
    @Override
    public Path getIngestInputPath() {
        return ingestInputPath;
    }

    private static String readAllLinesFromFile( Path path ) {
        try {
            List<String> allLines = Files.readAllLines( path );
            return String.join( "\n", allLines );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
