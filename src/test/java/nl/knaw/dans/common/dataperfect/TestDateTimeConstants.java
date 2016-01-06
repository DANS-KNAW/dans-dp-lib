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

import org.junit.After;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class TestDateTimeConstants
{
    private Database database;

    @Before
    public void setUp()
               throws Exception
    {
        database = new Database(new File("src/test/resources/DP26G/D_T/MEMBERS.STR"));
        database.open();
    }

    @After
    public void tearDown()
                  throws Exception
    {
        database.close();
    }

    @Test
    public void testConstants()
                       throws Exception
    {
        assertEquals("MDY",
                     database.getDefaultDateOrder());
        assertEquals("HMS",
                     database.getDefaultTimeOrder());
    }
}
