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

import java.io.CharArrayWriter;

/**
 * Converts a byte array into a string. Interprets the bytes as unsigned bytes. Bytes 0-127 are
 * interpreted as ASCII. Bytes 128-255 are put into the string as delimited numbers.
 *
 * @author Jan van Mansum
 * @author Martin Braaksma
 * @see Database#setExtAsciiCodeDelimiters(String, String)
 */
final class DelimiterEncodedExtendedAsciiString
{
    private final String string;

    DelimiterEncodedExtendedAsciiString(final byte[] bytes, final String startDelimiter, final String endDelimiter)
    {
        final CharArrayWriter buffer = new CharArrayWriter();

        for (int i = 0; i < bytes.length; ++i)
        {
            if ((0xFF & bytes[i]) > 127)
            {
                final String codeString = startDelimiter + (0xFF & bytes[i]) + endDelimiter;
                buffer.append(codeString);
            }
            else
            {
                buffer.append((char) bytes[i]);
            }
        }

        string = buffer.toString();
    }

    @Override
    public String toString()
    {
        return string;
    }
}
