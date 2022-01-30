/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
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
package de.mindscan.brightflux.plugin.reports.transformations;

/**
 * 
 */
public class TimeConversions {

    public static String convertSecondsToTime( String valueInSeconds ) {
        int videoDurationInSeconds = Integer.valueOf( valueInSeconds ).intValue();

        int seconds = videoDurationInSeconds % 60;
        int minutes = (videoDurationInSeconds / 60) % 60;

        if (videoDurationInSeconds < 3600) {
            return String.format( "%02d:%02d", minutes, seconds );
        }
        else {
            int hours = (videoDurationInSeconds / 3600);
            return String.format( "%02d:%02d:%02d", hours, minutes, seconds );
        }
    }

    public static String convertNanoToPreciseDate( String valueInNano ) {
        long timestampInNano = Long.valueOf( valueInNano ).longValue();

        long fraction = timestampInNano % 1000000000L;
        long fractionMS = (fraction / 1000000L) % 1000L;
        long fractionMY = (fraction / 1000L) % 1000L;
        long fractionNS = (fraction) % 1000L;

        long totalSeconds = (timestampInNano / 1000000000L);

        return String.format( "%d.%03d.%03d.%03d", totalSeconds, fractionMS, fractionMY, fractionNS );
    }

}
