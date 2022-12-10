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

import nl.knaw.dans.common.dataperfect.BlockGroupPanelDefinition.DoorDefinition;

/**
 * Represents a linked field. A link is either a data link or a panel link.
 *
 * @author Jan van Mansum
 * @author Martin Braaksma
 *
 */
public class Link
{
    private short targetPanelNumber;
    private short targetLandingField;
    private short targetIndex;
    private short flags;
    private short sourceFieldList;
    private short autoDisp;
    private LinkType linkType;

    Link(final DoorDefinition doorDefinition, LinkType linkType)
    {
        this.targetPanelNumber = doorDefinition.targetPanelNumber;
        this.targetLandingField = doorDefinition.targetLandingField;
        this.targetIndex = doorDefinition.targetIndex;
        this.flags = doorDefinition.flags;
        this.sourceFieldList = doorDefinition.sourceFieldList;
        this.autoDisp = doorDefinition.autoDisp;
        this.linkType = linkType;
    }

    /**
     * Returns the number of the panel to which the field will be linked.
     *
     * @return the target panel number
     */
    public short getTargetPanelNumber()
    {
        return targetPanelNumber;
    }

    /**
     * Returns the target field for the cursor.
     *
     * @return the target landing field
     */
    public short getTargetLandingField()
    {
        return targetLandingField;
    }

    /**
     * Returns the target index.
     *
     * @return the target index
     */
    public short getTargetIndex()
    {
        return targetIndex;
    }

    /**
     *
     * @return the flags
     */
    public short getFlags()
    {
        return flags;
    }

    /**
     * Returns the source fields. The source fields must match the target index.
     *
     * @return the source fields
     */
    public short getSourceFieldList()
    {
        return sourceFieldList;
    }

    /**
     *
     * @return autoDisp
     */
    public short getAutoDisp()
    {
        return autoDisp;
    }

    /**
     * Returns the link type.
     *
     * @return the link type
     */
    public LinkType getType()
    {
        return linkType;
    }
}
