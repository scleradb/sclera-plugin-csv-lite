/**
* Sclera - CSV Lite
* Copyright 2012 - 2020 Sclera, Inc.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*     http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.csv

import java.net.URL
import java.nio.charset.Charset

import org.apache.commons.csv.{CSVFormat, CSVRecord, CSVParser}

import scala.jdk.CollectionConverters._

import com.scleradb.sql.types.SqlCharVarying
import com.scleradb.sql.expr.{SortExpr, CharConst}
import com.scleradb.sql.datatypes.Column
import com.scleradb.sql.result.{TableResult, ScalTableRow}

/** Wrapper over the Apache Commons CSV parser object.
  * Generates a table containing the contents of a CSV file.
  *
  * @param url CSV URL or file name or enclosing directory
  */
class CSVResult(url: String) extends TableResult {
    /** CSV Parser */
    private val parser: CSVParser = CSVParser.parse(
        new URL(url), Charset.defaultCharset(), CSVFormat.DEFAULT.withHeader()
    )

    /** Columns of the result (virtual table)
      * obtained from the first line (header) of the CSV file.
      * Each column has type CHAR VARYING.
      */
    override val columns: List[Column] =
        parser.getHeaderNames.asScala.toList.map { name =>
            Column(name.trim.toUpperCase, SqlCharVarying(None))
        }

    /** Reads the CSV file and emits the data as an iterator on rows.
      * Each row contains the (column-name -> value) pairs.
      */
    override def rows: Iterator[ScalTableRow] =
        parser.iterator.asScala.map { record =>
            val vals: Iterator[String] = record.iterator.asScala
            val colVals: List[(String, CharConst)] = columns.zip(vals).map {
                case (col, v) => (col.name -> CharConst(v.trim))
            }

            ScalTableRow(colVals)
        }

    /** Sort order of result - not known */
    override val resultOrder: List[SortExpr] = Nil

    /** Closes the reader */
    override def close(): Unit = parser.close()
}
