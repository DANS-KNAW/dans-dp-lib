/*
 * Copyright 2009 Data Archiving and Networked Services (DANS), Netherlands.
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

public class TestTimeFormatter
{
    private TimeFormatter formatter;

    @Test
    public void standardCases()
    {
        int secondsOfHour = 3600;
        int secondsOfMinute = 60;

        // Unspecified
        // TODO: currently hard coded in TimeFormatter, but should be read from .STR
        formatter = new TimeFormatter("T99:99:99", "HMS");
        assertEquals("01:59:59",
                     formatter.format(1 * secondsOfHour + 59 * secondsOfMinute + 59));

        // HMS
        formatter = new TimeFormatter("THMS99:99:99", "");
        assertEquals("02:00:00",
                     formatter.format(2 * secondsOfHour));

        // HM
        formatter = new TimeFormatter("THMS99:99", "");
        assertEquals("11:59",
                     formatter.format(11 * secondsOfHour + 59 * secondsOfMinute));

        // H
        formatter = new TimeFormatter("THMS99", "");
        assertEquals("11",
                     formatter.format(11 * secondsOfHour));

        // MS
        formatter = new TimeFormatter("TMSH99:99", "");
        assertEquals("59:59",
                     formatter.format(59 * secondsOfMinute + 59));

        // M
        formatter = new TimeFormatter("TMSH99", "");
        assertEquals("59",
                     formatter.format(59 * secondsOfMinute));
        // S
        formatter = new TimeFormatter("TSMH99", "");
        assertEquals("59",
                     formatter.format(59));
    }

    @Test
    public void dataLength()
    {
        formatter = new TimeFormatter("THMS99:99:99", "");
        assertEquals(8,
                     formatter.getDataLength());
        formatter = new TimeFormatter("T99:99:99", "");
        assertEquals(8,
                     formatter.getDataLength());
        formatter = new TimeFormatter("THMS", "");
        assertEquals(0,
                     formatter.getDataLength());
        formatter = new TimeFormatter("T", "");
        assertEquals(0,
                     formatter.getDataLength());
        formatter = new TimeFormatter("T99:99:99", "HMS");
        assertEquals(8,
                     formatter.getDataLength());
    }
}
