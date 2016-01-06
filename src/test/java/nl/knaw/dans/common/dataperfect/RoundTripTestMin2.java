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
