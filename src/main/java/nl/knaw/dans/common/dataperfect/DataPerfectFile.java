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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

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
    private final DatabaseSettings databaseSettings;
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

    String readString()
               throws IOException
    {
        final int length = raFile.readByte();

        return readString(length);
    }

    String readString(final int aLength)
               throws IOException
    {
        final byte[] valueIso = new byte[aLength];
        raFile.readFully(valueIso);
        //byte[] valueUtf8 = new String(valueIso, "ISO-8859-1").getBytes("UTF-8");
        byte[] valueUtf8 = new String(valueIso, "Windows-1252").getBytes("UTF-8");
        //String strValueUtf8 = new String (valueIso.getBytes(Charset.forName("UTF-8")), Charset.forName("Windows-1252"));

        String s = valueUtf8.toString();
        if (databaseSettings.getCharsetName() != null)
        {
            return new String(valueUtf8,
                              databaseSettings.getCharsetName());
        }
        else if (databaseSettings.getExtAsciiCodeStartDelimiter() != null)
        {
            final DelimiterEncodedExtendedAsciiString encodedExtAsciiString =
                new DelimiterEncodedExtendedAsciiString(valueIso,
                                                        databaseSettings.getExtAsciiCodeStartDelimiter(),
                                                        databaseSettings.getExtAsciiCodeEndDelimiter());

            return encodedExtAsciiString.toString(); // und hier TODO
        }
        else
        {
            return new String(valueUtf8);
        }
    }

    int readUnsignedShort()
                   throws IOException
    {
        final byte b1 = raFile.readByte();
        final byte b2 = raFile.readByte();

        return (0xFF & b2) * 0x0100 + (0xFF & b1);
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
