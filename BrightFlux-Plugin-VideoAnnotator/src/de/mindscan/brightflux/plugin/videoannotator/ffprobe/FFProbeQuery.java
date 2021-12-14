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
package de.mindscan.brightflux.plugin.videoannotator.ffprobe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.mindscan.brightflux.plugin.videoannotator.ffprobe.impl.FFProbeInfoValue;

/**
 * 
 */
public class FFProbeQuery {
    private final String FFPROBE_PATH;
    private FFProbeInfoValue videoQueryInformation;

    public FFProbeQuery( Path ffprobePath ) {
        FFPROBE_PATH = ffprobePath.toAbsolutePath().toString();
    }

    public FFProbeInfoValue retrieveFormatInfo( Path videoPath ) throws IOException, InterruptedException {

        List<String> command = new ArrayList<>();
        command.add( FFPROBE_PATH );
        // verbose mode show errors only 
        command.add( "-v" );
        command.add( "error" );
        // what to test

        command.add( "-select_streams" );
        command.add( "v" );

        command.add( "-show_entries" );
        command.add( "format" );

        command.add( "-show_entries" );
        command.add( "stream=r_frame_rate,avg_frame_rate,index,codec_type,codec_name,codec_long_name,nb_frames,width,height" );

        command.add( "-of" );
        command.add( "json" );
        // which file to test.
        command.add( videoPath.toAbsolutePath().toString() );

        Process process = new ProcessBuilder( command ).start();
        BufferedReader bre = new BufferedReader( new InputStreamReader( process.getErrorStream() ) );
        BufferedReader bro = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
        process.waitFor();

        int exitCode = process.exitValue();

        if (exitCode == 0) {
            String jsonString = bro.lines().collect( Collectors.joining( "\n" ) );
            System.out.println( jsonString );

            Gson gson = new GsonBuilder().create();
            videoQueryInformation = gson.fromJson( jsonString, FFProbeInfoValue.class );

            return videoQueryInformation;
        }
        else {
            throw new IllegalArgumentException( bre.lines().collect( Collectors.joining( "\n" ) ) );
        }
    }

    /**
     * @return the videoQueryInformation
     */
    public FFProbeInfoValue getVideoQueryInformation() {
        return videoQueryInformation;
    }
}
