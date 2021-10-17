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
package de.mindscan.brightflux.dataframes.writer.bfdfjson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameColumn;

/**
 * 
 */
public class JsonLinesDataFrameInfo {

    private String name = "";
    private String title = "";
    private int gen = 0;
    private UUID uuid = null;
    private ArrayList<BFDFColumnInfo> columns = new ArrayList<>();

    /**
     * 
     */
    public JsonLinesDataFrameInfo() {
    }

    public void initWithDataFrame( DataFrame df ) {
        this.name = df.getName();
        this.title = df.getTitle();
        this.gen = df.getDataFrameGeneration();
        this.uuid = df.getUuid();
        this.columns = buildColumnInfoList( df );
    }

    private ArrayList<BFDFColumnInfo> buildColumnInfoList( DataFrame df ) {
        ArrayList<BFDFColumnInfo> columnInfoList = new ArrayList<>();

        for (DataFrameColumn<?> dataFrameColumn : df.getColumns()) {
            columnInfoList.add( new BFDFColumnInfo( dataFrameColumn.getColumnName(), dataFrameColumn.getColumnType(), dataFrameColumn.getColumnValueType() ) );
        }

        return columnInfoList;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public int getGen() {
        return gen;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<BFDFColumnInfo> getColumns() {
        return columns;
    }

}
