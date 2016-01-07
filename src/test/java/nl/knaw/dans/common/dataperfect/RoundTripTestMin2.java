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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Global check of the main parts of the library's functionality. Uses the MIN2 data set.
 *
 * @author Jan van Mansum
 */
public class RoundTripTestMin2
{
    private Database db;

    /**
     * @throws IOException if the database could not be read
     * @throws DataPerfectLibException if the database contains errors
     */
    @Before
    public void setUp()
               throws IOException, DataPerfectLibException
    {
        db = new Database(new File("src/test/resources/DP26G/MIN2/MIN2.STR"));
        db.open();
    }

    /**
     * @throws IOException if one of the database files could not be closed
     */
    @After
    public void tearDown()
                  throws IOException
    {
        db.close();
    }

    @Test
    public void checkNumberOfPanels()
                             throws IOException, FileNotFoundException, DataPerfectLibException
    {
        final List<Panel> panelList = db.getPanels();

        assertEquals(2,
                     panelList.size());
    }

    @Test
    public void checkPropertiesPanel01()
                                throws IOException
    {
        final Panel panel01 = db.getPanelByFileName("PANEL01.FIL");

        assertEquals(1,
                     panel01.getX());
        assertEquals(9,
                     panel01.getY());

        assertEquals(new File("src/test/resources/DP26G/MIN2/PANEL01.FIL"),
                     panel01.getFile());
    }

    @Test
    public void checkPropertiesPanel02()
                                throws IOException
    {
        final Panel panel02 = db.getPanelByFileName("PANEL02.FIL");

        assertEquals(1,
                     panel02.getX());
        assertEquals(9,
                     panel02.getY());
        assertEquals(new File("src/test/resources/DP26G/MIN2/PANEL02.FIL"),
                     panel02.getFile());
    }

    @Test
    public void checkFieldsPanel01()
                            throws IOException
    {
        final Panel panel01 = db.getPanelByFileName("PANEL01.FIL");
        final List<Field> fieldList = panel01.getFields();

        assertEquals(1,
                     fieldList.size());

        final Field fld01 = fieldList.get(0);

        assertEquals(1,
                     fld01.getNumber());
        assertEquals("FLD01",
                     fld01.getName());
        assertEquals("A10",
                     fld01.getFormat());
    }

    @Test
    public void checkFieldsPanel02()
                            throws IOException
    {
        final Panel panel02 = db.getPanelByFileName("PANEL02.FIL");
        final List<Field> fieldList = panel02.getFields();

        assertEquals(2,
                     fieldList.size());

        final Field fld01 = fieldList.get(0);

        assertEquals(1,
                     fld01.getNumber());
        assertEquals("Number",
                     fld01.getName());
        assertEquals("N999",
                     fld01.getFormat());

        final Field fld02 = fieldList.get(1);

        assertEquals(2,
                     fld02.getNumber());
        assertEquals("Long text",
                     fld02.getName());
        assertEquals("A50",
                     fld02.getFormat());
    }

    @Test
    public void checkRowsPanel01()
                          throws IOException, NoSuchRecordFieldException
    {
        final Panel panel01 = db.getPanelByFileName("PANEL01.FIL");
        final Iterator<Record> recordIterator = panel01.recordIterator();

        assertTrue(recordIterator.hasNext());

        final Record r1 = recordIterator.next();

        assertEquals("ROW 1     ",
                     r1.getValueAsString(1));

        assertTrue(recordIterator.hasNext());

        final Record r2 = recordIterator.next();

        assertEquals("ROW 2     ",
                     r2.getValueAsString(1));

        assertFalse(recordIterator.hasNext());
    }

    @Test
    public void checkRowsPanel02()
                          throws IOException, NoSuchRecordFieldException
    {
        final Panel panel02 = db.getPanelByFileName("PANEL02.FIL");
        final Iterator<Record> recordIterator = panel02.recordIterator();

        assertTrue(recordIterator.hasNext());

        final Record r1 = recordIterator.next();

        assertEquals(1,
                     r1.getValueAsNumber(1));
        assertEquals("Example 01                                        ",
                     r1.getValueAsString(2));

        assertTrue(recordIterator.hasNext());

        final Record r2 = recordIterator.next();

        assertEquals(2,
                     r2.getValueAsNumber(1));
        assertEquals("Example 02                                        ",
                     r2.getValueAsString(2));

        assertFalse(recordIterator.hasNext());
    }
}
