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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Reads the block group containing a panel definition.
 *
 * @author Jan van Mansum
 */
final class BlockGroupPanelDefinition
    extends AbstractBlockGroup
{
    /*
     * Offsets with the panel definition block group are given from the third byte of the block
     * group, not from the start.
     */
    private static final int OFFSET_CORRECTION = 2;
    private static final int DOOR_DEFINITION_BYTES_READ = 3;
    final short panelNumber;
    final int recordLength;
    final int blkPanelFileName;
    final int colorCode;
    final int x;
    final int y;
    final int width;
    final int height;
    final int offsetFieldList;
    final int offsetFieldExtensionData;
    final int blkPanelName;

    // TODO: Read the rest of the panel definition group fields
    final List<SubblockFieldInfo> fieldInfoList = new LinkedList<SubblockFieldInfo>();
    final List<SubblockFieldExtensionData> fieldExtDataList = new LinkedList<SubblockFieldExtensionData>();

    /**
     * Reads and stores a sub-block with a field info structure. Assumes the structure file to be at
     * the correct position.
     */
    static final class SubblockFieldInfo
    {
        final int fieldNumber;
        final int typeDependentLengthInfo;
        final int offsetInRecord;
        final int offsetInFieldExtensionData;

        SubblockFieldInfo(final StructureFile structureFile)
                   throws IOException
        {
            fieldNumber = structureFile.readByte();

            if (fieldNumber == 0)
            {
                typeDependentLengthInfo = 0;
                offsetInRecord = 0;
                offsetInFieldExtensionData = 0;

                return;
            }

            structureFile.skipBytes(2);
            typeDependentLengthInfo = structureFile.readByte();
            offsetInRecord = structureFile.readUnsignedShort();
            offsetInFieldExtensionData = structureFile.readUnsignedShort();
        }
    }

    /**
     * Reads and stores a sub-block with a field extension data structure. Assumes the structure
     * file to be at the correct position.
     */
    static final class SubblockFieldExtensionData
    {
        final int length;
        final int x;
        final int y;
        final int help;

        // TODO: Read fixed size values
        final Map<Integer, TypedValue> valueMap = new HashMap<Integer, TypedValue>();

        SubblockFieldExtensionData(final StructureFile structureFile)
                            throws IOException
        {
            length = structureFile.readByte();
            x = structureFile.readByte();
            y = structureFile.readByte();
            help = structureFile.readBlockNumber();

            // TODO: Read fixed size data also
            structureFile.skipBytes(5);

            TypedValue value = new TypedValue(structureFile);

            while (value.type != 0)
            {
                valueMap.put(value.type, value);
                value = new TypedValue(structureFile);
            }
        }
    }

    /**
     * Reads and stores a typed value from the field extension data. It is not declared as a nested
     * type within SubblockFieldExtensionData only to keep the level of nesting under control. The
     * actual type of the value field depends on the type of value represented.
     */
    static final class TypedValue
    {
        static final int TARGET_GROUPS = 1;
        static final int FIELD_VALUE_LIST = 2;
        static final int INITIALIZATION = 3;
        static final int EDIT_CHECK = 4;
        static final int PICTURE = 5;
        static final int SEARCH_LIST = 6;
        static final int DOOR_DEFINITION = 7;
        static final int AUTO_INCREMENT = 8;
        static final int LOOKUP_LIST = 9;
        static final int DISPLAY = 10;
        static final int AUTO_LOOKUP_LIST = 11;
        static final int FIELD_NAME = 12;
        final int type;
        final int length;
        Object value;

        TypedValue(final StructureFile structureFile)
            throws IOException
        {
            type = structureFile.readByte();

            if (type == 0)
            {
                length = 0;

                return;
            }

            length = structureFile.readByte();

            switch (type)
            {
                case TARGET_GROUPS:
                case FIELD_VALUE_LIST:
                case EDIT_CHECK:
                case AUTO_INCREMENT:
                case LOOKUP_LIST:
                case DISPLAY:
                case AUTO_LOOKUP_LIST:
                case SEARCH_LIST:
                    // Ignore for now, just skip to next
                    structureFile.skipBytes(length);

                    break;

                case INITIALIZATION:
                    value = new Initialization(structureFile);

                    break;

                case PICTURE:
                    value = structureFile.readString(length);

                    break;

                case DOOR_DEFINITION:
                    value = new DoorDefinition(structureFile);

                    /*
                     * Skip any bytes beyond 3.
                     *
                     * As only the first 3 bytes are read,
                     * any remaining bytes will be skipped.
                     */
                    if (length > DOOR_DEFINITION_BYTES_READ)
                    {
                        structureFile.skipBytes(length - DOOR_DEFINITION_BYTES_READ);
                    }

                    break;

                case FIELD_NAME:
                    value = structureFile.readString(length);

                    break;

                default:
                    assert false : "Unkown type for field extension typed value: " + type;
            }
        }
    }

    /**
     * Defines a Data Link or Panel Link.
     *
     */
    static final class DoorDefinition
    {
        final short targetPanelNumber;
        final short targetFieldNumber;
        final short targetIndex;

        DoorDefinition(final StructureFile structureFile)
                throws IOException
        {
            targetPanelNumber = structureFile.readByte();
            targetFieldNumber = structureFile.readByte();
            targetIndex = structureFile.readByte();
        }
    }

    /**
     * Defines a field's initial value, range or formula.
     *
     */
    static final class Initialization
    {
        final short kind;
        final int blkPointer1;
        final int blkPointer2;

        Initialization(final StructureFile structureFile)
                throws IOException
        {
            kind = structureFile.readByte();
            blkPointer1 = structureFile.readBlockNumber();
            blkPointer2 = structureFile.readBlockNumber();
        }
    }

    /**
     * Initializes a {@link BlockGroupPanelDefinition}.
     *
     * @param blockNumber the block number of the block group's first block
     * @param structureFile the structure file from which to read
     * @throws IOException if structure file cannot be read
     */
    BlockGroupPanelDefinition(final int blockNumber, final StructureFile structureFile)
                       throws IOException
    {
        /*
         * Read the block group header.
         */
        super(blockNumber, structureFile);

        /*
         * Read the fixed size data.
         */
        structureFile.skipBytes(1);
        panelNumber = structureFile.readByte();
        recordLength = structureFile.readUnsignedShort();
        blkPanelFileName = structureFile.readBlockNumber();
        colorCode = structureFile.readByte();
        x = structureFile.readByte();
        y = structureFile.readByte();
        width = structureFile.readByte();
        height = structureFile.readByte();
        offsetFieldList = structureFile.readUnsignedShort();
        offsetFieldExtensionData = structureFile.readUnsignedShort();
        // TODO: Read other fields from block group panel definition
        structureFile.skipBytes(11);
        blkPanelName = structureFile.readBlockNumber();

        /*
         * Read the field list
         */
        structureFile.jumpToOffset(blockNumber, offsetFieldList + OFFSET_CORRECTION);

        SubblockFieldInfo fieldBlock = new SubblockFieldInfo(structureFile);

        while (fieldBlock.fieldNumber != 0)
        {
            fieldInfoList.add(fieldBlock);
            fieldBlock = new SubblockFieldInfo(structureFile);
        }

        final Iterator<SubblockFieldInfo> fieldInfoIterator = fieldInfoList.iterator();

        while (fieldInfoIterator.hasNext())
        {
            final SubblockFieldInfo fieldInfo = fieldInfoIterator.next();

            structureFile.jumpToOffset(blockNumber,
                                       offsetFieldExtensionData + OFFSET_CORRECTION
                                       + fieldInfo.offsetInFieldExtensionData);
            fieldExtDataList.add(new SubblockFieldExtensionData(structureFile));
        }
    }
}
