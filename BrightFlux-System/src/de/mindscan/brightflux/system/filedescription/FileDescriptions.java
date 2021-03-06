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
package de.mindscan.brightflux.system.filedescription;

/**
 * 
 */
public class FileDescriptions {

    public final static FileDescription ANY = new FileDescription( //
                    new String[] { "*.*" }, //
                    new String[] { "All files" } );

    public final static FileDescription CSV = new FileDescription( //
                    new String[] { "*.csv" }, //
                    new String[] { "Comma Separated files (*.csv)" } );

    public final static FileDescription RAW_ANY = new FileDescription( //
                    new String[] { "*.raw", "*.*" }, //
                    new String[] { "Raw log files (*.raw)", "All files (*.*)" } );

    public final static FileDescription BFRECIPE = new FileDescription( //
                    new String[] { "*.bfrecipe" }, //
                    new String[] { "Brightflux recipe files (*.bfrecipe)" } );

    public final static FileDescription BF_ANNOTATION = new FileDescription( //
                    new String[] { "*.annotations.bf_jsonl" }, //
                    new String[] { "Brightflux anotation file (*.annotations.bf_jsonl)" } );

    public final static FileDescription BF_HIGHLIGHT = new FileDescription( //
                    new String[] { "*.highlight.bf_jsonl" }, //
                    new String[] { "Brightflux anotation file (*.highlight.bf_jsonl)" } );

    public final static FileDescription BF_VIDEO_ANNOTATION = new FileDescription( //
                    new String[] { "*.video.bf_jsonl" }, //
                    new String[] { "Brightflux video anotation file (*.video.bf_jsonl)" } );
}
