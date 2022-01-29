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
package de.mindscan.brightflux.plugin.annotator.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.plugin.annotator.AnnotatorComponent;
import de.mindscan.brightflux.plugin.reports.ReportBuilder;

/**
 * 
 */
public class AnnotatorReportBuilder {

    // Block names for the Annotator Template
    public static final String BLOCKNAME_ANNOTATION_DETAILS = "X";
    public static final String BLOCKNAME_ANNOTATION_DETAILS_LOG = "Y";

    // Special report markers, to change mode
    public static final String EXTEND_LOG_DATA_LINE = ".";
    public static final String EXTEND_LOG_DATA_LINES_WITH_SKIP = "..";

    public static String buildReport( DataFrame forThisDataFrame, DataFrame logAnalysisFrame, ReportBuilder reportBuilder ) {
        Iterator<DataFrameRow> currentDFRowsIterator2 = forThisDataFrame.rowIterator();
        while (currentDFRowsIterator2.hasNext()) {
            DataFrameRow dataFrameRow = (DataFrameRow) currentDFRowsIterator2.next();
            int originalRowIndex = dataFrameRow.getOriginalRowIndex();
            if (logAnalysisFrame.isPresent( AnnotatorComponent.ANNOTATION_COLUMN_NAME, originalRowIndex )) {
                // we found annotation details for a line in the dataframe we create the report for

                String evidence_description = ((String) logAnalysisFrame.getAt( AnnotatorComponent.ANNOTATION_COLUMN_NAME, originalRowIndex )).trim();

                Map<String, String> templateData = dataFrameRow.getAsMap( "row." );

                if (evidence_description.isBlank()) {
                    // intentionally left blank.
                }
                else if (EXTEND_LOG_DATA_LINE.equals( evidence_description )) {
                    templateData.put( "extracontent", "" );
                    reportBuilder.block( BLOCKNAME_ANNOTATION_DETAILS_LOG, templateData );
                }
                else if (EXTEND_LOG_DATA_LINES_WITH_SKIP.equals( evidence_description )) {
                    templateData.put( "extracontent", "[..]\r\n" );
                    reportBuilder.block( BLOCKNAME_ANNOTATION_DETAILS_LOG, templateData );
                }
                else {
                    templateData.put( "extracontent", "" );
                    templateData.put( "evidence_description", evidence_description );
                    reportBuilder.block( BLOCKNAME_ANNOTATION_DETAILS, templateData );
                    reportBuilder.block( BLOCKNAME_ANNOTATION_DETAILS_LOG, templateData );
                }
            }
        }
        return reportBuilder.render( new HashMap<>() );
    }

}
