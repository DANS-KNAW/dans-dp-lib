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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Base class for the classes that represent DataPerfect implementation files. Defines generic
 * methods, to open, close the file read values and move around.
 *
 * @author Jan van Mansum
 */
abstract class DataPerfectFile
{
    protected static final int BLOCK_SIZE = 32;
    protected final File file;
    protected final DatabaseSettings databaseSettings;
    protected RandomAccessFile raFile;

    DataPerfectFile(final File file, final DatabaseSettings databaseSettings)
    {
        this.file = file;
        this.databaseSettings = databaseSettings;
    }

    File getDirectory()
    {
        return new File(file.getParent());
    }

    void close()
        throws IOException
    {
        raFile.close();
        raFile = null;
    }

    void open()
       throws FileNotFoundException
    {
        if (raFile == null)
        {
            raFile = new RandomAccessFile(file, "r");
        }
    }

    short readByte()
            throws IOException
    {
        return (short) (0xFF & raFile.readByte());
    }

    int readUnsignedShort()
                   throws IOException
    {
        final byte b1 = raFile.readByte();
        final byte b2 = raFile.readByte();

        return (0xFF & b2) * 0x0100 + (0xFF & b1);
    }

    int readInteger()
             throws IOException
    {
        final byte b1 = raFile.readByte();
        final byte b2 = raFile.readByte();
        final byte b3 = raFile.readByte();
        final byte b4 = raFile.readByte();

        return (0xFF & b4) * 0x01000000 + (0xFF & b3) * 0x00010000 + (0xFF & b2) * 0x00000100 + (0xFF & b1);
    }

    long readPackedNumber()
                   throws IOException
    {
        /*
         * Packed number fields are of fixed length.
         */
        final int LENGTH_PACKED_NUMBER_FIELD = 7;

        /*
         * The first byte (not counted) above, contains the sign and the number of digits stored.
         */
        final byte firstByte = raFile.readByte();
        final int length = firstByte & 0x0F;
        final boolean isPositive = (firstByte & 0xF0) == 0x40;

        /*
         * Read the whole packed field (including the zero padding at the end, so that the file
         * pointer is moved to the next field).
         */
        final byte[] bytes = new byte[LENGTH_PACKED_NUMBER_FIELD];
        raFile.readFully(bytes);

        long result = 0;

        /*
         * Loop over the digits from the most significant to the least significant. Each nibble
         * (half byte) stores a decimal digit.
         */
        for (int i = 0; i < length; ++i)
        {
            result *= 10;
            result += (0xF0 & bytes[i / 2]) >> 4;
            ++i;

            /*
             * If we are NOT in the last byte, OR (we are in the last byte but) the number's length
             * in characters is even, THEN also process the second nibble. Otherwise the second
             * nibble is zero and not part of the actual number encoded.
             */
            if (i < length)
            {
                result *= 10;
                result += (0x0F & bytes[i / 2]);
            }
        }

        return result * (isPositive ? 1 : -1);
    }

    String readString()
               throws IOException
    {
        final int length = raFile.readByte();

        return readString(length);
    }

    String readString(final int aLength)
               throws IOException
    {
        final byte[] value = new byte[aLength];
        raFile.readFully(value);

        String result = null;

        if (databaseSettings.getCharsetName() != null)
        {
            result =
                new String(value,
                           databaseSettings.getCharsetName());
        }
        else if (databaseSettings.getExtAsciiCodeStartDelimiter() != null)
        {
            final DelimiterEncodedExtendedAsciiString encodedExtAsciiString =
                new DelimiterEncodedExtendedAsciiString(value,
                                                        databaseSettings.getExtAsciiCodeStartDelimiter(),
                                                        databaseSettings.getExtAsciiCodeEndDelimiter());

            result = encodedExtAsciiString.toString();
        }
        else
        {
            result = new String(value);
        }

        return replaceNonPrintableCharacters(result);
    }

    protected String replaceNonPrintableCharacters(String result)
    {
        char[] characters = result.toCharArray();

        for (int i = 0; i < characters.length; ++i)
        {
            final Character mappedChar = NonPrintableCharacterMap.get(characters[i]);

            if (mappedChar != null)
            {
                characters[i] = mappedChar;
            }
        }

        return new String(characters);
    }

    void skipBytes(final int n)
            throws IOException
    {
        raFile.skipBytes(n);
    }

    int readBlockNumber()
                 throws IOException
    {
        final byte b1 = raFile.readByte();
        final byte b2 = raFile.readByte();
        final byte b3 = raFile.readByte();

        return (0xFF & b3) * 0x000100 + (0xFF & b2) * 0x0100 + (0xFF & b1);
    }

    void jumpToBlockAt(final int blockNumber)
                throws IOException
    {
        raFile.seek(StructureFile.BLOCK_SIZE * blockNumber);
    }
}
