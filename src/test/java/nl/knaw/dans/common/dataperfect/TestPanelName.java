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
