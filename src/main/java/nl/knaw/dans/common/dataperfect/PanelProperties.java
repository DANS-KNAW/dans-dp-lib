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
import java.util.ArrayList;
import java.util.List;

/**
 * Container for the properties of a panel. Used internally to pass the the panel's properties to
 * the constructor by means of a named parameter idiom.
 *
 * @author Jan van Mansum
 * @see FieldProperties
 */
class PanelProperties
{
    private File file;
    private String name;
    private int x;
    private int y;
    private List<Field> fields = new ArrayList<Field>();
    private List<Field> fieldsWithPanelLinks;
    private int recordSize;

    PanelProperties withFile(final File file)
    {
        this.file = file;

        return this;
    }

    File getFile()
    {
        return file;
    }

    PanelProperties withName(final String name)
    {
        this.name = name;

        return this;
    }

    String getName()
    {
        return name;
    }

    PanelProperties withX(final int x)
    {
        this.x = x;

        return this;
    }

    int getX()
    {
        return x;
    }

    PanelProperties withY(final int y)
    {
        this.y = y;

        return this;
    }

    int getY()
    {
        return y;
    }

    PanelProperties withFields(final List<Field> fieldsWithPanelLinks)
    {
        this.fieldsWithPanelLinks = fieldsWithPanelLinks;

        for (final Field field : fieldsWithPanelLinks)
        {
            if (field.getType() == Type.NONE)
            {
                continue;
            }

            fields.add(field);
        }

        return this;
    }

    List<Field> getFields()
    {
        return fields;
    }

    List<Field> getFields(final boolean includePanelLinks)
    {
        return includePanelLinks ? fieldsWithPanelLinks : fields;
    }

    PanelProperties withRecordSize(final int recordSize)
    {
        this.recordSize = recordSize;

        return this;
    }

    int getRecordSize()
    {
        return recordSize;
    }
}
