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

    PanelProperties withFields(final List<Field> fields)
    {
        for (final Field field : fields)
        {
            this.fields.add(field);
        }

        return this;
    }

    List<Field> getFields()
    {
        return fields;
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
