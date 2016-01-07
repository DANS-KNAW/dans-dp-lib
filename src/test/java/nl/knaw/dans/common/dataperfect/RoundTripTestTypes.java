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

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Global check of the main parts of the library's functionality. Uses the TYPES data set.
 *
 * @author Jan van Mansum
 */
public class RoundTripTestTypes
{
    private Database db;

    @Before
    public void setUp()
               throws IOException, DataPerfectLibException
    {
        db = new Database(new File("src/test/resources/DP26G/TYPES/TYPES.STR"));
        db.open();
    }

    @After
    public void tearDown()
                  throws IOException
    {
        db.close();
    }

    @Test
    public void checkFields()
                     throws IOException
    {
        final List<Panel> panelList = db.getPanels();

        assertEquals(1,
                     panelList.size());

        final Panel panel = panelList.get(0);
        final List<Field> fieldList = panel.getFields();

        assertEquals(8,
                     fieldList.size());

        final Iterator<Field> fieldIterator = fieldList.iterator();

        final Field fld01 = fieldIterator.next();
        assertEquals(1,
                     fld01.getNumber());
        assertEquals("A25",
                     fld01.getFormat());
        assertEquals(Type.A,
                     fld01.getType());

        final Field fld02 = fieldIterator.next();
        assertEquals(2,
                     fld02.getNumber());
        assertEquals(Type.AA,
                     fld02.getType());

        final Field fld03 = fieldIterator.next();
        assertEquals(3,
                     fld03.getNumber());
        assertEquals("N9999",
                     fld03.getFormat());
        assertEquals(Type.N,
                     fld03.getType());

        final Field fld04 = fieldIterator.next();
        assertEquals(4,
                     fld04.getNumber());
        assertEquals("N999999999999",
                     fld04.getFormat());
        assertEquals(Type.N,
                     fld04.getType());

        final Field fld05 = fieldIterator.next();
        assertEquals(5,
                     fld05.getNumber());
        assertEquals("G-ZZZ9",
                     fld05.getFormat());
        assertEquals(Type.G,
                     fld05.getType());

        final Field fld06 = fieldIterator.next();
        assertEquals(6,
                     fld06.getNumber());
        assertEquals("G-ZZZZZZZZZZZ9",
                     fld06.getFormat());
        assertEquals(Type.G,
                     fld06.getType());

        final Field fld07 = fieldIterator.next();
        assertEquals(7,
                     fld07.getNumber());
        assertEquals("D99/99/99",
                     fld07.getFormat());
        assertEquals(Type.D,
                     fld07.getType());

        final Field fld08 = fieldIterator.next();
        assertEquals(8,
                     fld08.getNumber());
        assertEquals("TZ9:99",
                     fld08.getFormat());
        assertEquals(Type.T,
                     fld08.getType());
    }

    @Test
    public void checkRecords()
                      throws IOException, NoSuchRecordFieldException
    {
        final Panel panel = db.getPanels().get(0);

        try
        {
            panel.open();

            final Iterator<Record> recordIterator = panel.recordIterator();

            assertTrue(recordIterator.hasNext());

            final Record r1 = recordIterator.next();

            assertEquals("String value             ",
                         r1.getValueAsString(1));
            assertEquals("Long string value 12345 67890 12345 67890 12345 67890 12345 67890 12345 67890 12345 67890\n",
                         r1.getValueAsString(2));
            assertEquals(1234,
                         r1.getValueAsNumber(3));
            assertEquals(123456789012L,
                         r1.getValueAsNumber(4));
            assertEquals(-1234,
                         r1.getValueAsNumber(5));
            assertEquals(-123456789012L,
                         r1.getValueAsNumber(6));
            assertEquals(25949,
                         r1.getValueAsNumber(7)); // Number of days after
                                                  // 1 March 1900

            assertEquals(29700,
                         r1.getValueAsNumber(8)); // Number of seconds
                                                  // since midnight
        }
        finally
        {
            panel.close();
        }
    }
}
