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
package de.mindscan.brightflux.ingest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 */
public class IngestUtils {

    public static String calculateColumnSeparator( String[] headLines ) {
        // How to identfy the column-separator
        // read some lines of the dataset

        // convert each line into a hashset of characters
        List<Set<Character>> allLinesAsSet = new ArrayList<>();
        for (String line : headLines) {
            if (!line.isBlank()) {
                allLinesAsSet.add( line.chars().mapToObj( e -> (char) e ).collect( Collectors.toSet() ) );
            }
        }

        if (allLinesAsSet.size() == 0) {
            return null;
        }

        // calculate the union of the characters
        Set<Character> union = allLinesAsSet.get( 0 );
        for (Set<Character> set : allLinesAsSet) {
            union.retainAll( set );
        }

        System.out.println( Arrays.deepToString( union.toArray() ) );
        // calculate the histogram of each line

        // build new histogram from min of the line histogram
        // then find the character with the maximum to the min-histogram.
        // that is the column separator for csv, hopefully either ',' or ';', or "\t"

        return union.toArray()[0].toString();
    }

    public static int calculateNumberOfColumns( String[] headLines, char columnSeparator ) {

        List<Integer> allLinesColumnCount = new ArrayList<>();
        for (String line : headLines) {
            if (!line.isBlank()) {
                allLinesColumnCount.add( (int) line.chars().filter( ch -> ch == columnSeparator ).count() );
            }
        }

        int columns = 1 + allLinesColumnCount.stream().min( Integer::compare ).get();

        System.out.println( columns );

        return columns;
    }
}
