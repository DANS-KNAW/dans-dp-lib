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

public class TestStringFormatter
{
    @Test
    public void dataLength()
    {
        StringFormatter sf = new StringFormatter("A25");
        assertEquals(25,
                     sf.getDataLength());

        sf = new StringFormatter("U5");
        assertEquals(5,
                     sf.getDataLength());

        sf = new StringFormatter("A25A3");
        assertEquals(32000,
                     sf.getDataLength());
    }
}
