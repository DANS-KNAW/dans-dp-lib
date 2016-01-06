/*
 * Copyright 2009-2016 Data Archiving and Networked Services (DANS), Netherlands.
 *
 * This file is part of DANS DataPerfect Library.
 *
 * DANS DataPerfect Library is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * DANS DataPerfect Library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DANS DataPerfect
 * Library. If not, see <http://www.gnu.org/licenses/>.
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
