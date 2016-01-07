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
