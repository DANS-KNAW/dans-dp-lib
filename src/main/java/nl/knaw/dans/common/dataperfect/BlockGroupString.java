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

/**
 * A block group that contains a single string, encoded as the length followed by the string's
 * characters.
 *
 * @author Jan van Mansum
 */
final class BlockGroupString
    extends AbstractBlockGroup
{
    final String value;

    /**
     * @see AbstractBlockGroup
     * @param blockNumber the block number
     * @param structureFile the structure file
     * @throws IOException if the structure file cannot be read
     */
    public BlockGroupString(final int blockNumber, final StructureFile structureFile)
                     throws IOException
    {
        super(blockNumber, structureFile);
        value = structureFile.readString();
    }
}
