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
    final static class SubblockFieldInfo
    {
        final int fieldNumber;
        final int typeDependentLengthInfo;
        final int offsetInRecord;
        final int offsetInFieldExtensionData;

        public SubblockFieldInfo(final StructureFile structureFile)
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

        // TODO: Read fixed size values
        final Map<Integer, TypedValue> valueMap = new HashMap<Integer, TypedValue>();

        SubblockFieldExtensionData(final StructureFile structureFile)
                            throws IOException
        {
            length = structureFile.readByte();
            x = structureFile.readByte();
            y = structureFile.readByte();

            // TODO: Read fixed size data also
            structureFile.skipBytes(8);

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
                case INITIALIZATION:
                case EDIT_CHECK:
                    // Ignore for now, just skip to next
                    structureFile.skipBytes(length);

                    break;

                case PICTURE:
                    value = structureFile.readString(length);

                    break;

                case SEARCH_LIST:
                    break;

                case DOOR_DEFINITION:
                    value = new DoorDefinition(structureFile);

                    break;

                case AUTO_INCREMENT:
                case LOOKUP_LIST:
                case DISPLAY:
                case AUTO_LOOKUP_LIST:
                    // Ignore for now, just skip to next
                    structureFile.skipBytes(length);

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
     * Specifies a Data Link or Panel Link.
     *
     */
    static final class DoorDefinition
    {
        final short targetPanelNumber;
        final short targetLandingField;
        final short targetIndex;
        final short flags;
        final short sourceFieldList;
        final short autoDisp;

        DoorDefinition(final StructureFile structureFile)
                throws IOException
        {
            targetPanelNumber = structureFile.readByte();
            targetLandingField = structureFile.readByte();
            targetIndex = structureFile.readByte();
            flags = structureFile.readByte();
            sourceFieldList = structureFile.readByte();
            autoDisp = structureFile.readByte();
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
