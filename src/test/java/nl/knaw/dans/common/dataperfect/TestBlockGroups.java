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

import org.junit.After;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Low-level test that checks whether individual block-groups are extracted correctly from manually
 * checked structure file.
 *
 * @author Jan van Mansum
 */
public class TestBlockGroups
{
    private StructureFile structureFile =
        new StructureFile(new File("src/test/resources/DP26G/MIN2/MIN2.STR"), DefaultTestDatabaseSettings.INSTANCE);

    @Before
    public void setUp()
               throws FileNotFoundException
    {
        structureFile.open();
    }

    @After
    public void tearDown()
                  throws IOException
    {
        structureFile.close();
    }

    @Test
    public void shouldReadMin2StrBlock3()
                                 throws IOException
    {
        final BlockGroup3 bg3 = new BlockGroup3(structureFile);

        // Header
        assertEquals(0x00, bg3.nrBlockPreviousGroup);
        assertEquals(0x39, bg3.nrBytesAfterHeader);

        // Body
        assertEquals(0x27, bg3.blkPrinterMaps);
        assertEquals(0x0F, bg3.blkConstants);
        assertEquals(0x05, bg3.blkPanels);
    }

    @Test
    public void shouldReadMin2StrBlockGroupPanelList()
                                              throws IOException
    {
        final BlockGroupPanelList bgPanelList = new BlockGroupPanelList(0x05, structureFile);

        // Header
        assertEquals(0x02, bgPanelList.nrBlockPreviousGroup);
        assertEquals(0x012c, bgPanelList.nrBytesAfterHeader);

        assertEquals(2,
                     bgPanelList.panelList.size());

        // Body
        assertEquals(0x3b,
                     bgPanelList.panelList.get(0).intValue());
        assertEquals(0x41,
                     bgPanelList.panelList.get(1).intValue());
    }

    @Test
    public void shouldReadMin2StrBlockGroupPanelDefinition1()
                                                     throws IOException
    {
        final BlockGroupPanelDefinition bgPanelDefinition = new BlockGroupPanelDefinition(0x3b, structureFile);

        // Header
        assertEquals(bgPanelDefinition.nrBlockPreviousGroup, 0x01);
        assertEquals(bgPanelDefinition.nrBytesAfterHeader, 0x5d);

        // Body
        assertEquals(0x01, bgPanelDefinition.panelNumber);
        assertEquals(10, bgPanelDefinition.recordLength);
        assertEquals(0x39, bgPanelDefinition.blkPanelFileName);
        assertEquals(7, bgPanelDefinition.colorCode);
        assertEquals(1, bgPanelDefinition.x);
        assertEquals(9, bgPanelDefinition.y);
        assertEquals(77, bgPanelDefinition.width);
        assertEquals(14, bgPanelDefinition.height);

        /*
         * Check the field data list.
         */
        assertEquals(1,
                     bgPanelDefinition.fieldInfoList.size());

        final BlockGroupPanelDefinition.SubblockFieldInfo subblockFieldInfo = bgPanelDefinition.fieldInfoList.get(0);

        assertEquals(1, subblockFieldInfo.fieldNumber);
        assertEquals(0x0a, subblockFieldInfo.typeDependentLengthInfo);
        assertEquals(0, subblockFieldInfo.offsetInRecord);
        assertEquals(0, subblockFieldInfo.offsetInFieldExtensionData);

        /*
         * Check the field extension data list.
         */
        assertEquals(1,
                     bgPanelDefinition.fieldExtDataList.size());

        final BlockGroupPanelDefinition.SubblockFieldExtensionData fieldExtData =
            bgPanelDefinition.fieldExtDataList.get(0);

        assertEquals(0x1b, fieldExtData.length);

        assertEquals(3,
                     fieldExtData.valueMap.size());

        final BlockGroupPanelDefinition.TypedValue pictureValue =
            fieldExtData.valueMap.get(BlockGroupPanelDefinition.TypedValue.PICTURE);
        assertEquals(BlockGroupPanelDefinition.TypedValue.PICTURE, pictureValue.type);
        assertEquals("A10", pictureValue.value);

        final BlockGroupPanelDefinition.TypedValue displayValue =
            fieldExtData.valueMap.get(BlockGroupPanelDefinition.TypedValue.DISPLAY);
        assertEquals(BlockGroupPanelDefinition.TypedValue.DISPLAY, displayValue.type);

        final BlockGroupPanelDefinition.TypedValue nameValue =
            fieldExtData.valueMap.get(BlockGroupPanelDefinition.TypedValue.FIELD_NAME);
        assertEquals(BlockGroupPanelDefinition.TypedValue.FIELD_NAME, nameValue.type);
        assertEquals("FLD01", nameValue.value);
    }

    @Test
    public void shouldReadMin2StrBlockGroupPanelDefinition2()
                                                     throws IOException
    {
        final BlockGroupPanelDefinition bgPanelDefinition = new BlockGroupPanelDefinition(0x41, structureFile);

        // Header
        assertEquals(bgPanelDefinition.nrBlockPreviousGroup, 0x01);
        assertEquals(bgPanelDefinition.nrBytesAfterHeader, 0x7e);

        // Body
        assertEquals(0x02, bgPanelDefinition.panelNumber);
        assertEquals(54, bgPanelDefinition.recordLength);
        assertEquals(0x3f, bgPanelDefinition.blkPanelFileName);
        assertEquals(7, bgPanelDefinition.colorCode);
        assertEquals(1, bgPanelDefinition.x);
        assertEquals(9, bgPanelDefinition.y);
        assertEquals(77, bgPanelDefinition.width);
        assertEquals(14, bgPanelDefinition.height);

        /*
         * Check the field data list.
         */
        assertEquals(2,
                     bgPanelDefinition.fieldInfoList.size());

        final BlockGroupPanelDefinition.SubblockFieldInfo subblockFieldInfo1 = bgPanelDefinition.fieldInfoList.get(0);

        assertEquals(1, subblockFieldInfo1.fieldNumber);
        assertEquals(0x03, subblockFieldInfo1.typeDependentLengthInfo);
        assertEquals(0, subblockFieldInfo1.offsetInRecord);
        assertEquals(0, subblockFieldInfo1.offsetInFieldExtensionData);

        final BlockGroupPanelDefinition.SubblockFieldInfo subblockFieldInfo2 = bgPanelDefinition.fieldInfoList.get(1);

        assertEquals(2, subblockFieldInfo2.fieldNumber);
        assertEquals(0x32, subblockFieldInfo2.typeDependentLengthInfo);
        assertEquals(0x04, subblockFieldInfo2.offsetInRecord);
        assertEquals(0x1e, subblockFieldInfo2.offsetInFieldExtensionData);

        /*
         * Check the field extension data list.
         */
        assertEquals(2,
                     bgPanelDefinition.fieldExtDataList.size());

        final BlockGroupPanelDefinition.SubblockFieldExtensionData fieldExtData0 =
            bgPanelDefinition.fieldExtDataList.get(0);

        assertEquals(0x1d, fieldExtData0.length);

        assertEquals(3,
                     fieldExtData0.valueMap.size());

        final BlockGroupPanelDefinition.TypedValue pictureValue0 =
            fieldExtData0.valueMap.get(BlockGroupPanelDefinition.TypedValue.PICTURE);
        assertEquals(BlockGroupPanelDefinition.TypedValue.PICTURE, pictureValue0.type);
        assertEquals("N999", pictureValue0.value);

        final BlockGroupPanelDefinition.TypedValue displayValue0 =
            fieldExtData0.valueMap.get(BlockGroupPanelDefinition.TypedValue.DISPLAY);
        assertEquals(BlockGroupPanelDefinition.TypedValue.DISPLAY, displayValue0.type);

        final BlockGroupPanelDefinition.TypedValue nameValue0 =
            fieldExtData0.valueMap.get(BlockGroupPanelDefinition.TypedValue.FIELD_NAME);
        assertEquals(BlockGroupPanelDefinition.TypedValue.FIELD_NAME, nameValue0.type);
        assertEquals("Number", nameValue0.value);

        final BlockGroupPanelDefinition.SubblockFieldExtensionData fieldExtData1 =
            bgPanelDefinition.fieldExtDataList.get(1);

        assertEquals(0x1f, fieldExtData1.length);

        assertEquals(3,
                     fieldExtData1.valueMap.size());

        final BlockGroupPanelDefinition.TypedValue pictureValue1 =
            fieldExtData1.valueMap.get(BlockGroupPanelDefinition.TypedValue.PICTURE);
        assertEquals(BlockGroupPanelDefinition.TypedValue.PICTURE, pictureValue1.type);
        assertEquals("A50", pictureValue1.value);

        final BlockGroupPanelDefinition.TypedValue displayValue1 =
            fieldExtData1.valueMap.get(BlockGroupPanelDefinition.TypedValue.DISPLAY);
        assertEquals(BlockGroupPanelDefinition.TypedValue.DISPLAY, displayValue1.type);

        final BlockGroupPanelDefinition.TypedValue nameValue1 =
            fieldExtData1.valueMap.get(BlockGroupPanelDefinition.TypedValue.FIELD_NAME);
        assertEquals(BlockGroupPanelDefinition.TypedValue.FIELD_NAME, nameValue1.type);
        assertEquals("Long text", nameValue1.value);
    }
}
