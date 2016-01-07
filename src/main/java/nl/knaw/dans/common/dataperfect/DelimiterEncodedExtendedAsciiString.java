/**
 * Copyright (C) 2009-2016 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
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
