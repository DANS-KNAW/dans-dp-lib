/**
 * Copyright (C) 2009 DANS (info@dans.knaw.nl)
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
 * Represents a linked field. A link is either a data link or a panel link.
 *
 * @author Jan van Mansum
 * @author Martin Braaksma
 *
 */
public class Link
{
    private short targetPanelNumber;
    private short targetFieldNumber;
    private short targetIndex;
    private LinkType linkType;

    Link(final DoorDefinition doorDefinition, LinkType linkType)
    {
        this.targetPanelNumber = doorDefinition.targetPanelNumber;
        this.targetFieldNumber = doorDefinition.targetFieldNumber;
        this.targetIndex = doorDefinition.targetIndex;
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
    public short getTargetFieldNumber()
    {
        return targetFieldNumber;
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
     * Returns the link type.
     *
     * @return the link type
     */
    public LinkType getType()
    {
        return linkType;
    }
}
