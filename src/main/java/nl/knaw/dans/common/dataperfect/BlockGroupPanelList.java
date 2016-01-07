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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the block group that contains the list of block references to panel definitions.
 *
 * @author Jan van Mansum
 */
final class BlockGroupPanelList
    extends AbstractBlockGroup
{
    private static final int NO_PANEL_DEFINITION = 65280;
    final List<Integer> panelList = new LinkedList<Integer>();

    /**
     * @see AbstractBlockGroup
     * @param blockNumber the block number
     * @param structureFile the structure file
     * @throws IOException if the structure file cannot be read
     */
    BlockGroupPanelList(final int blockNumber, final StructureFile structureFile)
                 throws IOException
    {
        super(blockNumber, structureFile);

        int panelBlkNr = structureFile.readBlockNumber();

        while (panelBlkNr != 0)
        {
            /*
             * Do not add the panel if it does not
             * contain a definition, i.e.
             * panelBlkNr == 65280 (FF00).
             */
            if (panelBlkNr != NO_PANEL_DEFINITION)
            {
                panelList.add(panelBlkNr);
            }

            panelBlkNr = structureFile.readBlockNumber();
        }
    }
}
