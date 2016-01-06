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
public class TestNumberFormatterDataLength
{
    @Test
    public void testDataLength()
    {
        NumberFormatter nf = new NumberFormatter("G9999");
        assertEquals(4,
                     nf.getDataLength());

        nf = new NumberFormatter("G.,9999");
        assertEquals(4,
                     nf.getDataLength());

        nf = new NumberFormatter("G$9999");
        assertEquals(5,
                     nf.getDataLength());

        nf = new NumberFormatter("G+9999");
        assertEquals(5,
                     nf.getDataLength());

        nf = new NumberFormatter("G9999-");
        assertEquals(5,
                     nf.getDataLength());

        nf = new NumberFormatter("G+$9999");
        assertEquals(6,
                     nf.getDataLength());

        nf = new NumberFormatter("HABCD9999");
        assertEquals(8,
                     nf.getDataLength());

        nf = new NumberFormatter("H9999ABCD");
        assertEquals(8,
                     nf.getDataLength());

        nf = new NumberFormatter("H99,999.99");
        assertEquals(9,
                     nf.getDataLength());

        nf = new NumberFormatter("H$99,999.99+");
        assertEquals(11,
                     nf.getDataLength());
    }
}
