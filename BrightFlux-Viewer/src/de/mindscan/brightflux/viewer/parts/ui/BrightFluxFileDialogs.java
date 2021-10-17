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
package de.mindscan.brightflux.viewer.parts.ui;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import de.mindscan.brightflux.system.filedescription.FileDescription;

/**
 * 
 */
public class BrightFluxFileDialogs {

    public static void openRegularFileAndConsumePath( Shell shell, String header, FileDescription description, Consumer<Path> pathConsumer ) {
        openRegularFileAndConsumePath( shell, header, description.getFileExtensions(), description.getFileDescriptions(), pathConsumer );
    }

    public static void openRegularFileAndConsumePath( Shell shell, String header, String[] filterextensions, String[] filternames,
                    Consumer<Path> pathConsumer ) {
        Path startPath = Paths.get( "." );

        String filePathToOpen = openDialog( shell, SWT.OPEN, startPath, header, filterextensions, filternames );

        if (filePathToOpen != null) {
            Path path = Paths.get( filePathToOpen );
            if (Files.isRegularFile( path, LinkOption.NOFOLLOW_LINKS )) {
                pathConsumer.accept( path );
            }
        }
    }

    public static void saveRegularFileAndConsumePath( Shell shell, String header, FileDescription description, Consumer<Path> pathConsumer ) {
        saveRegularFileAndConsumePath( shell, header, description.getFileExtensions(), description.getFileDescriptions(), pathConsumer );
    }

    public static void saveRegularFileAndConsumePath( Shell shell, String header, String[] filterextensions, String[] filternames,
                    Consumer<Path> pathConsumer ) {
        Path startPath = Paths.get( "." );
        String filePathToOpen = openDialog( shell, SWT.SAVE, startPath, header, filterextensions, filternames );

        if (filePathToOpen != null) {
            String extension = filterextensions[0].replaceAll( "*", "" );

            if (!filePathToOpen.endsWith( extension )) {
                filePathToOpen += extension;
            }

            Path path = Paths.get( filePathToOpen );
            pathConsumer.accept( path );
        }
    }

    private static String openDialog( Shell shell, int dialogType, Path startPath, String header, String[] filterextensions, String[] filternames ) {
        FileDialog fileDlg = new FileDialog( shell, dialogType );

        fileDlg.setText( header );
        fileDlg.setFilterExtensions( filterextensions );
        fileDlg.setFilterNames( filternames );
        fileDlg.setFilterPath( startPath.toString() );

        return fileDlg.open();
    }

}
