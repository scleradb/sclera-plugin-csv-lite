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

import com.scleradb.sql.expr.{ScalValueBase, CharConst}
import com.scleradb.external.service.ExternalSourceService

/** CSV data source service */
class CSVSourceService extends ExternalSourceService {
    /** Identifier for the service */
    override val id: String = "CSVLITE"

    /** Creates a CSVSource object given the generic parameters
      * @param params Generic parameters
      */
    override def createSource(params: List[ScalValueBase]): CSVSource = {
        val url: URL = params.lift(0) match {
            case Some(CharConst(urlStr)) if( urlStr != "" ) => new URL(urlStr)
            case Some(v) =>
                throw new IllegalArgumentException(
                    "Illegal file name/URL specified for \"" + id +
                    "\": " + v.repr
                )
            case None =>
                throw new IllegalArgumentException(
                    "File name/URL not specified for \"" + id + "\""
                )
        }

        new CSVSource(id, url)
    }
}
