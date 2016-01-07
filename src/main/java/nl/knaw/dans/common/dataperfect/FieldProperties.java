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

import nl.knaw.dans.common.dataperfect.BlockGroupPanelDefinition.DoorDefinition;

/**
 * Internal class that stores the properties of a field and supports the named parameter idiom.
 *
 * @author Jan van Mansum
 * @see PanelProperties
 */
final class FieldProperties
{
    private int number;
    private String name;
    private int x;
    private int y;
    private String format;
    private String help;
    private int offsetInRecord;
    private int typeDependentLengthInfo;
    private DatabaseSettings databaseSettings;
    private Link link;
    private String initialization;

    FieldProperties withNumber(final int number)
    {
        this.number = number;

        return this;
    }

    int getNumber()
    {
        return number;
    }

    FieldProperties withName(final String name)
    {
        this.name = name;

        return this;
    }

    String getName()
    {
        return name;
    }

    FieldProperties withX(final int x)
    {
        this.x = x;

        return this;
    }

    int getX()
    {
        return x;
    }

    FieldProperties withY(final int y)
    {
        this.y = y;

        return this;
    }

    int getY()
    {
        return y;
    }

    FieldProperties withFormat(final String format)
    {
        this.format = format;

        return this;
    }

    String getFormat()
    {
        return format;
    }

    FieldProperties withHelp(final String help)
    {
        this.help = help;

        return this;
    }

    String getHelp()
    {
        return help;
    }

    FieldProperties withOffsetInRecord(final int offsetInRecord)
    {
        this.offsetInRecord = offsetInRecord;

        return this;
    }

    int getOffsetInRecord()
    {
        return offsetInRecord;
    }

    FieldProperties withTypeDependentLengthInfo(final int typeDependentLengthInfo)
    {
        this.typeDependentLengthInfo = typeDependentLengthInfo;

        return this;
    }

    int getTypeDependentLengthInfo()
    {
        return typeDependentLengthInfo;
    }

    FieldProperties withDatabaseSettings(final DatabaseSettings databaseSettings)
    {
        this.databaseSettings = databaseSettings;

        return this;
    }

    DatabaseSettings getDatabaseSettings()
    {
        return databaseSettings;
    }

    FieldProperties withLink(final DoorDefinition doorDefinition)
    {
        if (doorDefinition != null)
        {
            link = (format == null) ? new Link(doorDefinition, LinkType.PANEL_LINK)
                                    : new Link(doorDefinition, LinkType.DATA_LINK);
        }

        return this;
    }

    Link getLink()
    {
        return link;
    }

    FieldProperties withInitialization(final String initialization)
    {
        this.initialization = initialization;

        return this;
    }

    String getInitialization()
    {
        return initialization;
    }
}
