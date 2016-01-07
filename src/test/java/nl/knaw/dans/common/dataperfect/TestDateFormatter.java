/**
 * Copyright (C) 2009-2016 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.common.dataperfect;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestDateFormatter
{
    private DateFormatter formatter;

    /*
     * Tests standard cases with a 99/99/99 or 99/99/9999 date format.
     *
     * The default field order is MDY but a DataPerfect user can change the default using either
     * order applied in the test.
     */
    @Test
    public void standardCases()
    {
        // 0. Unspecified
        // TODO: field order default MDY currently hard coded in DateFormatter,
        // but should be read from .STR
        formatter = new DateFormatter("D99-99-99", "MDY");
        assertEquals("03-02-00",
                     formatter.format(1));

        // 1. DMY
        formatter = new DateFormatter("DDMY99-99-9999", "");
        assertEquals("01-03-1901",
                     formatter.format(365));

        // 2. MYD
        formatter = new DateFormatter("DMYD99-9999-99", "");
        assertEquals("06-1900-09",
                     formatter.format(100));

        // 3. YDM
        formatter = new DateFormatter("DYDM9999-99-99", "");
        assertEquals("1900-19-02",
                     formatter.format(-10));

        // 4. DYM
        formatter = new DateFormatter("DDYM99-9999-99", "");
        assertEquals("31-1899-12",
                     formatter.format(-60));

        // 5. YMD
        formatter = new DateFormatter("DYMD9999-99-99", "");
        assertEquals("1900-03-02",
                     formatter.format(1));

        // 6. MDY Default if the default has not been changed.
        formatter = new DateFormatter("DMDY99/99/9999", "");
        assertEquals("03/03/1900",
                     formatter.format(2));
    }

    /*
     * Tests incomplete cases, i.e. formats which specify an incomplete date. Note that the field
     * order must always be complete.
     */
    @Test
    public void incompleteCases()
    {
        // 1. DMY
        formatter = new DateFormatter("DDMY99-99", "");
        assertEquals("01-03",
                     formatter.format(365));

        // 2. MYD
        formatter = new DateFormatter("DMYD99-99", "");
        assertEquals("06-00",
                     formatter.format(100));

        // 3. YDM
        formatter = new DateFormatter("DYDM9999-99", "");
        assertEquals("1900-19",
                     formatter.format(-10));

        // 4. DYM
        formatter = new DateFormatter("DDYM99-9999", "");
        assertEquals("31-1899",
                     formatter.format(-60));

        // 5. YMD
        formatter = new DateFormatter("DYMD9999/99", "");
        assertEquals("1900/03",
                     formatter.format(1));

        // 6. MDY
        formatter = new DateFormatter("DMDY99/99", "");
        assertEquals("03/03",
                     formatter.format(2));
    }

    /*
     * Tests 1. left-padding the value with zeros when the format exceeds the value. 2.
     * left-truncating the value when the value exceeds the format.
     */
    @Test
    public void leadingZeroCases()
    {
        // 1. DMY
        formatter = new DateFormatter("DDMY99999-9999-9999", "");
        assertEquals("00001-0003-1901",
                     formatter.format(365));

        // 2. MDY
        formatter = new DateFormatter("DMDY9-9-99", "");
        // "02-28-01"
        assertEquals("2-8-01",
                     formatter.format(364));
    }

    @Test
    public void dataLength()
    {
        formatter = new DateFormatter("DDMY99-99-9999", "");
        assertEquals(10,
                     formatter.getDataLength());
        formatter = new DateFormatter("D99-99-9999", "");
        assertEquals(10,
                     formatter.getDataLength());
        formatter = new DateFormatter("DDMY", "");
        assertEquals(0,
                     formatter.getDataLength());
        formatter = new DateFormatter("D", "");
        assertEquals(0,
                     formatter.getDataLength());
        formatter = new DateFormatter("D99-99-99", "MDY");
        assertEquals(8,
                     formatter.getDataLength());
    }
}
