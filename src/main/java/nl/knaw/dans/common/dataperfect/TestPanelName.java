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

import java.io.File;
import java.io.IOException;

public class TestPanelName
{
    @Test
    public void testPanelNamesDatabase1()
                                 throws IOException, DataPerfectLibException
    {
        final Database database1 = new Database(new File("src/test/resources/DP26G/D_T/MEMBERS.STR"));

        try
        {
            database1.open();

            final Panel panel1 = database1.getPanelByName("MEMBERSPANEL1");
            final Panel panel2 = database1.getPanelByName("MEMBERSPANEL2");

            assertEquals("MEMBERSPANEL1",
                         panel1.getName());
            assertEquals("MEMBERSPANEL2",
                         panel2.getName());
        }
        finally
        {
            database1.close();
        }
    }

    @Test
    public void testPanelNamesDatabase2()
                                 throws IOException, DataPerfectLibException
    {
        final Database database2 = new Database(new File("src/test/resources/DP26G/TRAVELS/TRAVELS.STR"));

        try
        {
            database2.open();

            final Panel panel1 =
                database2.getPanelByName("Bibliografische gegevens van REISBESCHRIJVINGEN over het Midden-Oosten");
            assertEquals("Bibliografische gegevens van REISBESCHRIJVINGEN over het Midden-Oosten",
                         panel1.getName());
        }
        finally
        {
            database2.close();
        }
    }
}
