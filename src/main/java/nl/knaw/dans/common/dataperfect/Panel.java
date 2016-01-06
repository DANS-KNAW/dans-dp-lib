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
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Represents a panel in a database. A panel is a "table" plus user interface. The concept of data
 * in an abstract table and the way it is represented on screen are combined the in DataPerfect
 * panel concept. That is why a panel contains both data and representational information. Examples
 * of data are the fields (columns) and records, examples of representational information are the x-
 * and y-coordinates of the panel on the screen.
 *
 * @author Jan van Mansum
 */
public class Panel
{
    private final PanelProperties properties;
    private final Database database;
    private DataFile dataFile;
    private int recordCount;

    Panel(final Database database, final PanelProperties panelProperties)
    {
        this.database = database;
        this.properties = panelProperties;
    }

    /**
     * Opens the panel for reading. If the the <tt>autoOpenPanels</tt> property of {@link Database}
     * is set to <tt>true</tt> the panel is opened automatically.
     *
     * @throws IOException if the database cannot be opened
     */
    public void open()
              throws IOException
    {
        if (dataFile == null)
        {
            dataFile =
                new DataFile(properties.getFile(),
                             properties.getRecordSize(),
                             database.databaseSettings);

            dataFile.open();
            recordCount = dataFile.readNumberOfRecords();
        }
    }

    /**
     * Closes this panel for reading. {@link Database#close() } calls this method on all panels. If
     * <tt>Database.autoOpenPanels</tt> is <tt>true</tt> it is still allowed to call this method to
     * close the panel explicitly. However, reading records from the <tt>Panel</tt> requires that
     * the it is open.
     *
     * @throws IOException if the data file cannot be closed
     */
    public void close()
               throws IOException
    {
        if (dataFile != null)
        {
            dataFile.close();
            dataFile = null;
        }
    }

    /**
     * Returns the file that stores the records of this panel. Large size fields are not stored in
     * this file, but in a shared file. However, the panel file is important because its name is
     * used as the table name in the case that the panel has no explicit name.
     *
     * @see #getName()
     *
     * @return the panel file
     */
    public File getFile()
    {
        return properties.getFile();
    }

    /**
     * The name of this panel as it was explicitly set by the database creator or <code>null</code>
     * if none was specified.
     *
     * @see #getFile()
     *
     * @return the name of the panel or <tt>null</tt>
     */
    public String getName()
    {
        return properties.getName();
    }

    /**
     * Returns the zero-based x-coordinate (from top-left) in character position of the panel.
     *
     * @return the x-coordinate
     */
    public int getX()
    {
        return properties.getX();
    }

    /**
     * Returns the zero-based y-coordinate (from top-left) in character position of the panel.
     *
     * @return the y-coordinate
     */
    public int getY()
    {
        return properties.getY();
    }

    /**
     * Returns the list of fields.
     *
     * @return the list of fields
     */
    public List<Field> getFields()
    {
        return properties.getFields();
    }

    /**
     * Returns a record iterator.
     *
     * @return a record iterator
     */
    public Iterator<Record> recordIterator()
    {
        return new Iterator<Record>()
            {
                private int recordNumber = 0;
                private Record lastReadRecord = null;

                public boolean hasNext()
                {
                    if (lastReadRecord == null && recordNumber < recordCount)
                    {
                        try
                        {
                            lastReadRecord = readRecord(recordNumber);
                            ++recordNumber;
                        }
                        catch (final IOException ioe)
                        {
                            throw new RecordReadException("Error reading record number " + recordNumber, ioe);
                        }
                    }

                    return lastReadRecord != null;
                }

                public Record next()
                {
                    if (hasNext())
                    {
                        final Record nextRecord = lastReadRecord;
                        lastReadRecord = null;

                        return nextRecord;
                    }

                    throw new NoSuchElementException();
                }

                public void remove()
                {
                    throw new UnsupportedOperationException("Modification of the database not supported");
                }
            };
    }

    /*
     * Default access for unit tests.
     */
    Record readRecord(final int recordIndex)
               throws IOException
    {
        final int MAX_LENGTH_INTEGER_REPRESENTATION = 9;
        final Map<Field, Object> fieldToValueMap = new HashMap<Field, Object>();

        for (final Field field : properties.getFields())
        {
            Object value = null;

            if (isPanelLink(field) || field.isComputedField())
            {
                fieldToValueMap.put(field, null);

                continue;
            }

            dataFile.jumpToRecordField(recordIndex,
                                       field.getOffsetInRecord());

            switch (field.getType())
            {
                case A:
                case U:
                    value = dataFile.readString(field.getTypeDependentLengthInfo());

                    break;

                case AA:
                case AU:
                case UU:
                case UA:
                    value = database.readFromTextFile(dataFile.readBlockNumber());

                    break;

                case G:
                case H:
                case N:

                    if (field.getNumberOfDigitsInFormat() <= MAX_LENGTH_INTEGER_REPRESENTATION)
                    {
                        value = dataFile.readInteger();
                    }
                    else
                    {
                        value = dataFile.readPackedNumber();
                    }

                    break;

                case D:
                    value = dataFile.readUnsignedShort();

                    break;

                case T:
                    value = dataFile.readInteger();

                    break;

                case NONE:
                    assert false : "NONE type fields should have been filtered out";

                    continue;

                default:
                    assert false : "Not all types handled in switch";
            }

            fieldToValueMap.put(field, value);
        }

        return new Record(fieldToValueMap);
    }

    private boolean isPanelLink(final Field field)
    {
        if (field.getLink() != null && field.getLink().getType() == LinkType.PANEL_LINK)
        {
            return true;
        }

        return false;
    }

    // TODO: implement other properties
}
