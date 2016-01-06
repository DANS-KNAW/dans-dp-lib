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

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class TestDateAndTime
{
    private Database database;

    @Before
    public void openDatabase()
                      throws Exception
    {
        database = new Database(new File("src/test/resources/DP26G/D_T/MEMBERS.STR"));
        database.open();
    }

    @After
    public void closeDatabase()
                       throws Exception
    {
        database.close();
    }

    @Test
    public void testDefaultDate()
    {
        assertEquals("MDY",
                     database.getDefaultDateOrder());
    }

    @Test
    public void testPanelWithDates()
                            throws IOException, NoSuchRecordFieldException
    {
        final Panel panel01 = database.getPanelByFileName("MEMBPN1");

        final Iterator<Record> recordIterator = panel01.recordIterator();

        assertTrue(recordIterator.hasNext());

        final Record r1 = recordIterator.next();

        assertEquals("Martin                   ",
                     r1.getValueAsString(1));
        assertEquals("01/10/1957",
                     r1.getValueAsString(2));
        assertEquals(21033,
                     r1.getValueAsNumber(2));
        assertEquals("03/2008",
                     r1.getValueAsString(3));
        assertEquals(39447,
                     r1.getValueAsNumber(3));

        assertTrue(recordIterator.hasNext());

        final Record r2 = recordIterator.next();

        assertEquals("Joke                     ",
                     r2.getValueAsString(1));
        assertEquals("10/10/1965",
                     r2.getValueAsString(2));
        assertEquals(23964,
                     r2.getValueAsNumber(2));
        assertEquals("10/2003",
                     r2.getValueAsString(3));
        assertEquals(37834,
                     r2.getValueAsNumber(3));
    }

    @Test
    public void testPanelWithTimes()
                            throws IOException, NoSuchRecordFieldException
    {
        final Panel panel02 = database.getPanelByFileName("MEMBPN2");
        final Iterator<Record> recordIterator = panel02.recordIterator();
        final Record r1 = recordIterator.next();

        /*
         * By default DataPerfect returns a 24-hour time format as a 12-hour format, e.g. 14:15 is
         * returned as [0]2:15.
         */
        assertEquals("10:15",
                     r1.getValueAsString(1));
        assertEquals(36900,
                     r1.getValueAsNumber(1));

        assertEquals("02:15",
                     r1.getValueAsString(2));
        assertEquals(51300,
                     r1.getValueAsNumber(2));
    }
}
