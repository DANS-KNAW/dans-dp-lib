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

import java.io.UnsupportedEncodingException;

public class TestReadString
{
    @Test
    public void testReadStringWithDelimiters()
                                      throws UnsupportedEncodingException
    {
        byte[] input = "Caf\u00E9".getBytes("ISO-8859-1");

        assertEquals("Caf<233/>",
                     new DelimiterEncodedExtendedAsciiString(input, "<", "/>").toString());
    }
}
