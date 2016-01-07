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
