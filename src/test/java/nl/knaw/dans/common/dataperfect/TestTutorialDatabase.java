/**
 * Copyright (C) 2009 DANS (info@dans.knaw.nl)
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

import org.junit.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/*
 * Test class which globally tests all DataPerfect databases retrieved from
 * ftp://ftp.corel.com/pub/WordPerfect/old_wp_products/dataperfect/
 * plus the UD databases from
 * http://dataperfect.nl/CMS/index.php?option=com_frontpage&Itemid=1
 *
 * Moreover, the class tests the details of some specific records.
 *
 */
public class TestTutorialDatabase
{
    private Database database;

    /*
     * Test all databases globally. This test may take some time.
     */

    //@Ignore
    @Test
    public void testGlobally()
                      throws IOException, NoSuchRecordFieldException, DataPerfectLibException
    {
        File mainDirectory = new File("src/test/resources/DP26G/TUTORIAL_DATABASES");
        File[] subDirectories = mainDirectory.listFiles();

        for (File subDirectory : subDirectories)
        {
            String[] files = subDirectory.list();

            for (String file : files)
            {
                if (file.endsWith(".STR"))
                {
                    database = new Database(new File(subDirectory, file));
                    database.open();

                    List<Panel> panels = database.getPanels();

                    for (Panel panel : panels)
                    {
                        List<Field> fields = panel.getFields();

                        for (Field field : fields)
                        {
                            /*
                             * Unlike a data link field, a panel link field does not have
                             * a format type and does not contain data.
                             */
                            if (field.getType() == Type.NONE)
                            {
                                assertTrue(field.getLink().getType() == LinkType.PANEL_LINK);

                                hasNoValues(panel, field);
                            }

                            /*
                             * The field format part ::C designates a computed field.
                             *
                             * The values of a computed field are computed at runtime and
                             * not saved as data.
                             */
                            else if (field.getFormat().contains("::C"))
                            {
                                assertTrue(field.isComputedField());

                                hasNoValues(panel, field);
                            }
                            else
                            {
                                hasValues(panel, field);
                            }
                        }
                    }

                    database.close();

                    break;
                }
            }
        }
    }

    /*
     * Test UD, panel 7, record 2. This panel includes computed fields and panel links.
     */
    @Test
    public void testUD()
                throws IOException, DataPerfectLibException, NoSuchRecordFieldException
    {
        database = new Database(new File("src/test/resources/DP26G/TUTORIAL_DATABASES/UD/ud.str"));
        database.open();

        List<Panel> panels = database.getPanels();
        Panel panel = panels.get(6);
        List<Field> fields = panel.getFields();
        Field field = null;

        assertEquals("UDELICF.DAT",
                     panel.getFile().getName());
        assertEquals("ELIs Aiding Parent ::C Fields                       Account Panel",
                     panel.getName());

        Iterator<Record> recordIterator = panel.recordIterator();
        Record record = recordIterator.next();
        record = recordIterator.next();

        field = fields.get(0);
        assertEquals(5,
                     field.getNumber());
        assertEquals("F5 - Acct Num in Acct Panel",
                     field.getName());
        assertEquals("G99999::I",
                     field.getFormat());
        assertEquals(null,
                     field.getInitialization());
        assertEquals(false,
                     field.isComputedField());
        assertEquals(null,
                     field.getLink());
        assertEquals("00002",
                     record.getValueAsString(field.getNumber()));
        assertEquals("00002",
                     record.getValueAsString(field.getName()));
        assertEquals(2,
                     record.getValueAsNumber(field.getNumber()));
        assertEquals(2,
                     record.getValueAsNumber(field.getName()));

        field = fields.get(1);
        assertEquals(1,
                     field.getNumber());
        assertEquals("F1 - Last Name in Account Panel",
                     field.getName());
        assertEquals("A15",
                     field.getFormat());
        assertEquals(null,
                     field.getInitialization());
        assertEquals(false,
                     field.isComputedField());
        assertEquals(null,
                     field.getLink());
        assertEquals("Cranky         ",
                     record.getValueAsString(field.getNumber()));
        assertEquals("Cranky         ",
                     record.getValueAsString(field.getName()));

        field = fields.get(2);
        assertEquals(2,
                     field.getNumber());
        assertEquals("F2 - First Name in Acct Panel",
                     field.getName());
        assertEquals("A15",
                     field.getFormat());
        assertEquals(null,
                     field.getInitialization());
        assertEquals(false,
                     field.isComputedField());
        assertEquals(null,
                     field.getLink());
        assertEquals("Joan           ",
                     record.getValueAsString(field.getNumber()));
        assertEquals("Joan           ",
                     record.getValueAsString(field.getName()));

        field = fields.get(3);
        assertEquals(8,
                     field.getNumber());
        assertEquals("F8 - Last Trx Amount for Acct",
                     field.getName());
        assertEquals("H$ZZZ9.99::C",
                     field.getFormat());
        assertEquals("P7F10P8F3",
                     field.getInitialization());
        assertEquals(true,
                     field.isComputedField());
        assertEquals(null,
                     field.getLink());
        assertEquals(null,
                     record.getValueAsString(field.getNumber()));
        assertEquals(null,
                     record.getValueAsString(field.getName()));

        field = fields.get(4);
        assertEquals(9,
                     field.getNumber());
        assertEquals("F9 - Last Trx Date for Acct",
                     field.getName());
        assertEquals("D99/99/99::C",
                     field.getFormat());
        assertEquals("P7F10P8F7",
                     field.getInitialization());
        assertEquals(true,
                     field.isComputedField());
        assertEquals(null,
                     field.getLink());
        assertEquals(null,
                     record.getValueAsString(field.getNumber()));
        assertEquals(null,
                     record.getValueAsString(field.getName()));

        field = fields.get(5);
        assertEquals(10,
                     field.getNumber());
        assertEquals("F10 - Panel link to Trx Panel last Trx for Acct",
                     field.getName());
        assertEquals(null,
                     field.getFormat());
        assertEquals(null,
                     field.getInitialization());
        assertEquals(false,
                     field.isComputedField());
        assertEquals(LinkType.PANEL_LINK,
                     field.getLink().getType());
        assertEquals(null,
                     record.getValueAsString(field.getNumber()));
        assertEquals(null,
                     record.getValueAsString(field.getName()));

        field = fields.get(6);
        assertEquals(3,
                     field.getNumber());
        assertEquals("F3 - Last Payment Amount in Acct Panel",
                     field.getName());
        assertEquals("H$ZZZ9.99::C",
                     field.getFormat());
        assertEquals("P7F7P8F3",
                     field.getInitialization());
        assertEquals(true,
                     field.isComputedField());
        assertEquals(null,
                     field.getLink());
        assertEquals(null,
                     record.getValueAsString(field.getNumber()));
        assertEquals(null,
                     record.getValueAsString(field.getName()));

        field = fields.get(7);
        assertEquals(4,
                     field.getNumber());
        assertEquals("F4 - Last Payment Date in Acct Panel",
                     field.getName());
        assertEquals("D99/99/99::C",
                     field.getFormat());
        assertEquals("P7F7P8F7",
                     field.getInitialization());
        assertEquals(true,
                     field.isComputedField());
        assertEquals(null,
                     field.getLink());
        assertEquals(null,
                     record.getValueAsString(field.getNumber()));
        assertEquals(null,
                     record.getValueAsString(field.getName()));

        field = fields.get(8);
        assertEquals(7,
                     field.getNumber());
        assertEquals("F7 - Panel link to Trx Panel based Pymnt ELI",
                     field.getName());
        assertEquals(null,
                     field.getFormat());
        assertEquals(null,
                     field.getInitialization());
        assertEquals(false,
                     field.isComputedField());
        assertEquals(LinkType.PANEL_LINK,
                     field.getLink().getType());
        assertEquals(null,
                     record.getValueAsString(field.getNumber()));
        assertEquals(null,
                     record.getValueAsString(field.getName()));

        field = fields.get(9);
        assertEquals(6,
                     field.getNumber());
        assertEquals("F6 - Panel link to Trx Panel",
                     field.getName());
        assertEquals(null,
                     field.getFormat());
        assertEquals(null,
                     field.getInitialization());
        assertEquals(false,
                     field.isComputedField());
        assertEquals(LinkType.PANEL_LINK,
                     field.getLink().getType());
        assertEquals(null,
                     record.getValueAsString(field.getNumber()));
        assertEquals(null,
                     record.getValueAsString(field.getName()));

        database.close();
    }

    /*
     * Test BIB/BIBCARDS, panel 4, record 3. This panel includes data links and
     * multi-line text fields.
     */
    @Test
    public void testBibCards()
                      throws IOException, DataPerfectLibException, NoSuchRecordFieldException
    {
        database = new Database(new File("src/test/resources/DP26G/TUTORIAL_DATABASES/BIB/BIBCARDS.STR"));
        database.open();

        List<Panel> panels = database.getPanels();
        Panel panel = panels.get(3);
        List<Field> fields = panel.getFields();
        Field field = null;

        assertEquals("BIBCARDS.D04",
                     panel.getFile().getName());
        assertEquals("  Memorable Quotes File                                                 [04]",
                     panel.getName());

        Iterator<Record> recordIterator = panel.recordIterator();
        Record record = null;

        for (int i = 0; i < 3; ++i)
        {
            record = recordIterator.next();
        }

        field = fields.get(0);
        assertEquals(1,
                     field.getNumber());
        assertEquals("Title of Work",
                     field.getName());
        assertEquals("A53::M;;T",
                     field.getFormat());
        assertEquals("P4F8P1F1",
                     field.getInitialization());
        assertEquals(false,
                     field.isComputedField());
        assertEquals(LinkType.DATA_LINK,
                     field.getLink().getType());
        assertEquals("DataPerfect Gets Thumbs Up From Users                ",
                     record.getValueAsString(field.getNumber()));
        assertEquals("DataPerfect Gets Thumbs Up From Users                ",
                     record.getValueAsString(field.getName()));

        field = fields.get(1);
        assertEquals(2,
                     field.getNumber());
        assertEquals("First Name",
                     field.getName());
        assertEquals("A15;;T",
                     field.getFormat());
        assertEquals("P4F8P1F2",
                     field.getInitialization());
        assertEquals(false,
                     field.isComputedField());
        assertEquals(LinkType.DATA_LINK,
                     field.getLink().getType());
        assertEquals("David          ",
                     record.getValueAsString(field.getNumber()));
        assertEquals("David          ",
                     record.getValueAsString(field.getName()));

        field = fields.get(7);
        assertEquals(11,
                     field.getNumber());
        assertEquals("Memorable Quote",
                     field.getName());
        assertEquals("A71A5",
                     field.getFormat());
        assertEquals(null,
                     field.getInitialization());
        assertEquals(false,
                     field.isComputedField());
        assertEquals(null,
                     field.getLink());
        assertEquals("DataPerfect is very good -- up to WordPerfect standards.... It is an excellent product for beginners.",
                     record.getValueAsString(field.getNumber()));
        assertEquals("DataPerfect is very good -- up to WordPerfect standards.... It is an excellent product for beginners.",
                     record.getValueAsString(field.getName()));

        database.close();
    }

    /*
     * Data links and non-computed fields should contain values.
     */
    private void hasValues(final Panel panel, final Field field)
                    throws NoSuchRecordFieldException
    {
        for (Iterator<Record> recordIterator = panel.recordIterator(); recordIterator.hasNext();)
        {
            Record record = recordIterator.next();

            assertFalse(record.getValueAsString(field.getNumber()) == null);

            if (field.getName() != null)
            {
                assertFalse(record.getValueAsString(field.getName()) == null);
            }

            try
            {
                assertFalse(record.getValueAsNumber(field.getNumber()) == null);

                if (field.getName() != null)
                {
                    assertFalse(record.getValueAsString(field.getName()) == null);
                }
            }
            catch (ClassCastException e)
            {
            }
        }
    }

    /*
     * Panel links and computed fields should not contain values.
     */
    private void hasNoValues(final Panel panel, final Field field)
                      throws NoSuchRecordFieldException
    {
        for (Iterator<Record> recordIterator = panel.recordIterator(); recordIterator.hasNext();)
        {
            Record record = recordIterator.next();

            assertTrue(record.getValueAsNumber(field.getNumber()) == null);
            assertTrue(record.getValueAsString(field.getNumber()) == null);

            if (field.getName() != null)
            {
                assertTrue(record.getValueAsNumber(field.getName()) == null);
                assertTrue(record.getValueAsString(field.getName()) == null);
            }
        }
    }
}
