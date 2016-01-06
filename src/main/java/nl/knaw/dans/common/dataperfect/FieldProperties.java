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
