package nl.knaw.dans.common.dataperfect;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author martinbraaksma
 *
 */
public class TestComputedField
{
    Database database;

    /**
     * @throws IOException
     * @throws DataPerfectLibException
     */
    @Before
    public void setUp()
               throws IOException, DataPerfectLibException
    {
        database = new Database(new File("src/test/resources/DP26G/TUTORIAL_DATABASES/UD/ud.str"));
        database.open();
    }

    /**
     * @throws IOException
     */
    @After
    public void tearDown()
                  throws IOException
    {
        database.close();
    }

    /**
     * @throws NoSuchRecordFieldException
     * @throws IOException
     */
    @Test
    public void testComputedField()
                           throws NoSuchRecordFieldException, IOException
    {
        List<Panel> panelList = database.getPanels();

        for (Panel panel : panelList)
        {
            List<Field> fieldList = panel.getFields();

            for (Iterator<Record> recordIterator = panel.recordIterator(); recordIterator.hasNext();)
            {
                Record record = recordIterator.next();
                Iterator<Field> fieldIterator = fieldList.iterator();

                while (fieldIterator.hasNext())
                {
                    Field field = fieldIterator.next();

                    if (field.getInitialization() != null)
                    {
                        assertTrue(field.getInitialization() != null);

                        if (field.isComputedField())
                        {
                            assertTrue(field.isComputedField());
                            assertTrue(record.getValueAsString(field.getNumber()) == null);
                        }
                        else
                        {
                            assertFalse(field.isComputedField());
                            assertTrue(record.getValueAsString(field.getNumber()) != null);
                        }
                    }
                    else
                    {
                        if (field.getLink() == null || field.getLink().getType() == LinkType.DATA_LINK)
                        {
                            assertTrue(record.getValueAsString(field.getNumber()) != null);
                        }
                    }
                }
            }
        }
    }
}
