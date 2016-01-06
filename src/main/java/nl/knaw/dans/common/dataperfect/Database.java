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
import nl.knaw.dans.common.dataperfect.BlockGroupPanelDefinition.Initialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represents a DataPerfect database. A DataPerfect database is a collection of several files. The
 * main file is the structure file, which has the extension .STR
 * <p>
 * Note that DANS DataPerfect Library does not provide writing functionality. You can only
 * <i>read</i> DataPerfect files.
 * <p>
 * <b>Note about character sets:</b> DataPerfect does not record the character encoding used to
 * encode the strings stored in the database. When reading string from the database this information
 * is of course required. The caller can either provide a charsetName if he knows what encoding is
 * used, or provide start and end delimiters for the numeric extended ASCII codes encountered. If
 * neither of these is provided, the default character encoding on the JVM running the code will be
 * used.
 *
 * @see #setExtAsciiCodeDelimiters(String, String)
 *
 * @author Jan van Mansum
 * @author Martin Braaksma
 */
public class Database
{
    private static final String DEFAULT_CHARSET_NAME = "IBM437";
    private final StructureFile structureFile;
    private final List<Panel> panelList = new ArrayList<Panel>();
    private final Map<String, Panel> namePanelMap = new HashMap<String, Panel>();
    private final Map<String, Panel> fileNamePanelMap = new HashMap<String, Panel>();
    private final Map<DefaultSettings, Short> defaultSettingsMap = new HashMap<DefaultSettings, Short>();
    private final List<Panel> openedPanels = new LinkedList<Panel>();
    final DatabaseSettings databaseSettings = new DatabaseSettingsImpl();
    private boolean autoOpenPanels = true;
    private final TextFile textFile;
    private String charsetName;
    private String startDelimiterExtAsciiCode;
    private String endDelimiterExtAsciiCode;
    private BlockGroup3 bg3;
    private boolean isOpen = false;

    private class DatabaseSettingsImpl
        implements DatabaseSettings
    {
        public String getCharsetName()
        {
            return charsetName;
        }

        public String getDefaultDateOrder()
        {
            return getDefaultDateOrderInternal();
        }

        public String getDefaultTimeOrder()
        {
            return getDefaultTimeOrderInternal();
        }

        public String getExtAsciiCodeEndDelimiter()
        {
            return endDelimiterExtAsciiCode;
        }

        public String getExtAsciiCodeStartDelimiter()
        {
            return startDelimiterExtAsciiCode;
        }
    }

    /**
     * Creates a new database object and sets a custom charset.
     *
     * @param structureFile the database structure file
     * @param charsetName the database charset
     * @throws FileNotFoundException if the file is not found
     * @throws InvalidStructureFileException if the file is not a valid structure file
     */
    public Database(final File structureFile, final String charsetName)
             throws FileNotFoundException, InvalidStructureFileException
    {
        this.structureFile = new StructureFile(structureFile, databaseSettings);

        if (charsetName == null)
        {
            if (Charset.isSupported(DEFAULT_CHARSET_NAME))
            {
                this.charsetName = DEFAULT_CHARSET_NAME;
            }
            else
            {
                this.charsetName = Charset.defaultCharset().name();
            }
        }

        if (! Charset.isSupported(charsetName))
        {
            throw new UnsupportedCharsetException("The given charset is not supported.");
        }

        this.charsetName = charsetName;

        final String structureFileName = structureFile.getName();

        if (! structureFileName.toLowerCase().endsWith(".str") || structureFileName.length() <= ".str".length())
        {
            throw new InvalidStructureFileException("Structure file name must end with .STR and have at least one more character");
        }

        final TextFileFinder textFileFinder = new TextFileFinder(structureFile, databaseSettings);
        textFile = textFileFinder.find();
    }

    /**
     * Creates a new database object and sets IBM437 (DOS-US) as the default charset.
     *
     * @param structureFile the database structure file
     * @throws FileNotFoundException if the file is not found
     * @throws InvalidStructureFileException if the file is not a valid structure file
     */
    public Database(final File structureFile)
             throws FileNotFoundException, InvalidStructureFileException
    {
        this(structureFile, DEFAULT_CHARSET_NAME);
    }

    /**
     * Opens the database for reading.
     *
     * @throws IOException if the database could not be read
     * @throws DataPerfectLibException if the database contains errors
     */
    public void open()
              throws IOException, DataPerfectLibException
    {
        structureFile.open();
        textFile.open();
        readBlockGroup3();
        readConstants();
        readPanels();
        isOpen = true;
    }

    private void readBlockGroup3()
                          throws IOException
    {
        bg3 = new BlockGroup3(structureFile);
    }

    /**
     * Closes any files that the database has opened.
     *
     * @throws IOException if one of the database files could not be closed
     */
    public void close()
               throws IOException
    {
        if (isOpen)
        {
            isOpen = false;
            structureFile.close();
            textFile.close();

            /*
             * Close all the panels that were opened by this database. If the panels were already
             * closed, the call will be ignored.
             */
            for (final Panel panel : openedPanels)
            {
                panel.close();
            }
        }
    }

    private void readPanels()
                     throws IOException, InvalidFormatException
    {
        final BlockGroupPanelList bgPanelList = new BlockGroupPanelList(bg3.blkPanels, structureFile);
        final Iterator<Integer> panelIterator = bgPanelList.panelList.iterator();

        while (panelIterator.hasNext())
        {
            final BlockGroupPanelDefinition bgPanelDef =
                new BlockGroupPanelDefinition(panelIterator.next(), structureFile);

            final String panelFileName = new BlockGroupString(bgPanelDef.blkPanelFileName, structureFile).value;

            File panelFile = findPanelFile(panelFileName);

            if (panelFile == null)
            {
                throw new FileNotFoundException("Panel file not found: " + panelFileName);
            }

            final String panelName = new BlockGroupString(bgPanelDef.blkPanelName, structureFile).value;

            try
            {
                final List<Field> fields = readFields(bgPanelDef.fieldInfoList, bgPanelDef.fieldExtDataList);

                final Panel panel =
                    new Panel(this,
                              new PanelProperties() //
                    .withName(panelName) //
                    .withFile(panelFile) //
                    .withRecordSize(bgPanelDef.recordLength) //
                    .withFields(fields) //
                    .withX(bgPanelDef.x) //
                    .withY(bgPanelDef.y)); //

                panelList.add(panel);

                if (! "".equals(panelName))
                {
                    namePanelMap.put(panelName, panel);
                }

                fileNamePanelMap.put(panelFileName.toUpperCase(),
                                     panel);
            }
            catch (final InvalidFormatException invalidFormatException)
            {
                throw new InvalidFormatException(invalidFormatException.getMessage() + " on panel " + panelFileName);
            }
        }
    }

    private List<Field> readFields(final List<BlockGroupPanelDefinition.SubblockFieldInfo> fieldInfoList,
                                   final List<BlockGroupPanelDefinition.SubblockFieldExtensionData> fieldExtDataList)
                            throws InvalidFormatException, IOException
    {
        final Iterator<BlockGroupPanelDefinition.SubblockFieldInfo> fieldInfoIterator = fieldInfoList.iterator();
        final Iterator<BlockGroupPanelDefinition.SubblockFieldExtensionData> fieldExtDataIterator =
            fieldExtDataList.iterator();
        final List<Field> fieldList = new ArrayList<Field>();

        while (fieldInfoIterator.hasNext())
        {
            final BlockGroupPanelDefinition.SubblockFieldInfo fieldInfo = fieldInfoIterator.next();

            /*
             * Assumption: all fields have field extension data.
             */
            final BlockGroupPanelDefinition.SubblockFieldExtensionData fieldExtData = fieldExtDataIterator.next();

            final int blkHelp = fieldExtData.help;
            final String help = blkHelp == 0 ? null : new BlockGroupHelp(blkHelp, structureFile).toString();

            final BlockGroupPanelDefinition.TypedValue nameValue =
                fieldExtData.valueMap.get(BlockGroupPanelDefinition.TypedValue.FIELD_NAME);
            final String name = nameValue == null ? null : (String) nameValue.value;

            final BlockGroupPanelDefinition.TypedValue pictureValue =
                fieldExtData.valueMap.get(BlockGroupPanelDefinition.TypedValue.PICTURE);
            final String picture = pictureValue == null ? null : (String) pictureValue.value;

            final BlockGroupPanelDefinition.TypedValue doorDefinitionValue =
                fieldExtData.valueMap.get(BlockGroupPanelDefinition.TypedValue.DOOR_DEFINITION);
            final DoorDefinition link = doorDefinitionValue == null ? null : (DoorDefinition) doorDefinitionValue.value;

            final BlockGroupPanelDefinition.TypedValue initializationValue =
                fieldExtData.valueMap.get(BlockGroupPanelDefinition.TypedValue.INITIALIZATION);
            final String initialization =
                initializationValue == null ? null
                                            : readInitialization((Initialization) initializationValue.value, picture);

            final Field field =
                new Field(new FieldProperties().withNumber(fieldInfo.fieldNumber) //
                .withName(name) //
                .withFormat(picture) //
                .withHelp(help) //
                .withX(fieldExtData.x) //
                .withY(fieldExtData.y) //
                .withTypeDependentLengthInfo(fieldInfo.typeDependentLengthInfo) //
                .withOffsetInRecord(fieldInfo.offsetInRecord) //
                .withDatabaseSettings(databaseSettings) //
                .withLink(link) //
                .withInitialization(initialization)); //

            fieldList.add(field);
        }

        return fieldList;
    }

    private String readInitialization(final Initialization initialization, final String picture)
                               throws IOException
    {
        final short value = 1;
        final short range = 2;
        final short formula = 3;

        switch (initialization.kind)
        {
            case value:
                return new BlockGroupInitialValue(initialization.blkPointer1, structureFile, picture).toString();

            case range:

                // TODO
                break;

            case formula:
                return new BlockGroupFormula(initialization.blkPointer2, structureFile).toString();
        }

        return null;
    }

    /**
     * Reads a string from the "text file." This is the file that contains variable length string
     * data.
     *
     * @param blockNumber the block number in the text file
     * @return the string read
     * @throws IOException if the text file could not be read
     */
    String readFromTextFile(final int blockNumber)
                     throws IOException
    {
        return textFile.readTextAt(blockNumber);
    }

    /**
     * Returns the list of panels. If <code>Database.autoOpenPanels</code> is <code>true</code> the
     * panels will be opened for reading automatically. Otherwise {@link Panel#open()} needs to be
     * called explicitly before attempting to read the panel's data.
     *
     * @return the list of panels
     * @throws IOException if <code>Database.autoOpenPanels</code> is <code>true</code> but a panel
     *             could not be read
     * @throws IllegalStateException if the database was not open
     */
    public List<Panel> getPanels()
                          throws IOException
    {
        checkIsOpen();

        if (autoOpenPanels)
        {
            for (final Panel panel : panelList)
            {
                openIfRequired(panel);
            }
        }

        return panelList;
    }

    /**
     * Returns the {@link Panel} with the specified name or <code>null</code> if no such panel
     * exists. If <code>Database.autoOpenPanels</code> is <code>true</code> the panel is opened
     * automatically.
     *
     * @param name the name of the panel
     * @return a panel or <code>null</code>
     * @throws IOException if <code>Database.autoOpenPanels</code> is <code>true</code> but the
     *             panel could not be read
     * @throws IllegalStateException if the database was not open
     */
    public Panel getPanelByName(final String name)
                         throws IOException
    {
        checkIsOpen();

        final Panel panel = namePanelMap.get(name);
        openIfRequired(panel);

        return panel;
    }

    /**
     * Returns the {@link Panel} with the specified file name or <code>null</code> if no such panel
     * exists.
     *
     * @param fileName the file name of the panel
     * @return a panel or <code>null</code>
     * @throws IOException if <code>Database.autoOpenPanels</code> is <code>true</code> but the
     *             panel could not be read
     * @throws IllegalStateException if the database was not open
     */
    public Panel getPanelByFileName(final String fileName)
                             throws IOException
    {
        checkIsOpen();

        final Panel panel = fileNamePanelMap.get(fileName.toUpperCase());
        openIfRequired(panel);

        return panel;
    }

    private void openIfRequired(final Panel panel)
                         throws IOException
    {
        if (autoOpenPanels)
        {
            panel.open();
            openedPanels.add(panel);
        }
    }

    /**
     * Returns the list of reports.
     *
     * @return the list of report.
     * @throws UnsupportedOperationException always (not yet implemented)
     */
    public List<Report> getReports()
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Sets the <code>Database.autoOpenPanels</code> property. If <code>true</code> panels retrieved
     * through this <code>Database</code>'s methods are opened for reading automatically. This
     * property is <code>true</code> by default.
     *
     * @param autoOpenPanels the new value of autoOpenPanels
     */
    public void setAutoOpenPanels(final boolean autoOpenPanels)
    {
        this.autoOpenPanels = autoOpenPanels;
    }

    /**
     * Returns the <code>Database.autoOpenPanels</code> property.
     *
     * @return the value of the <code>Database.autoOpenPanels</code> property
     *
     * @see #setAutoOpenPanels(boolean)
     */
    public boolean isAutoOpenPanels()
    {
        return autoOpenPanels;
    }

    /**
     * Returns the character decoder that will be used to decode strings read from the database. If
     * <code>null</code> either the <code>Database.extAsciiCodeDelimiters</code> or the default
     * charset will be used.
     *
     * @return the character decoder
     * @see #setExtAsciiCodeDelimiters(String, String)
     */
    public String getCharsetName()
    {
        return charsetName;
    }

    /**
     * Sets the strings with which to delimit extended ASCII characters. For instance, if and
     * extended ASCII character with numeric value 130 was encountered in the string "café",
     * and the start delimiter is "&lt;char code=" and the end delimiter is "/&gt;", the resulting
     * string will be
     *
     * <pre>
     *  caf&lt;char code="130"/&gt;
     * </pre>
     *
     * The charsetName property must be <code>null</code> for the library to use these delimiters.
     *
     * @param startDelimiterExtAsciiCode the start delimiter to use
     * @param endDelimiterExtAsciiCode the end delimiter to use
     */
    public void setExtAsciiCodeDelimiters(final String startDelimiterExtAsciiCode, final String endDelimiterExtAsciiCode)
    {
        this.startDelimiterExtAsciiCode = startDelimiterExtAsciiCode;
        this.endDelimiterExtAsciiCode = endDelimiterExtAsciiCode;
    }

    /**
     * Returns the start delimiter for extended ASCII character codes.
     *
     * @return the start delimiter for extended ASCII character codes.
     */
    public String getExtAsciiCodeStartDelimiter()
    {
        return startDelimiterExtAsciiCode;
    }

    /**
     * Returns the end delimiter for extended ASCII character codes.
     *
     * @return the end delimiter for extended ASCII character codes.
     */
    public String getExtAsciiCodeEndDelimiter()
    {
        return endDelimiterExtAsciiCode;
    }

    /**
     * Returns the default date order for this database. That is the date order used in date fields
     * that do not specify a custom date order.
     *
     * @return the default date order
     * @throws IllegalStateException if the database was not open
     */
    public String getDefaultDateOrder()
    {
        checkIsOpen();

        return getDefaultDateOrderInternal();
    }

    private String getDefaultDateOrderInternal()
    {
        final short order = defaultSettingsMap.get(DefaultSettings.YMD);

        /*
         * The byte returned by DP is within the range 1-3 or 5-7.
         */
        switch (order)
        {
            case 1:
                return "DMY";

            case 2:
                return "MYD";

            case 3:
                return "YDM";

            case 5:
                return "DYM";

            case 6:
                return "YMD";

            case 7:
                return "MDY";

            case 4:default:
                assert false : "Illegal date order byte should have been detected in readConstants";

                return null;
        }
    }

    /**
     * Returns the default time order for this database. That is the time order used in time fields
     * that do not specify a custom time order.
     *
     * @return the default time order
     * @throws IllegalStateException if the database was not open
     */
    public String getDefaultTimeOrder()
    {
        checkIsOpen();

        return getDefaultTimeOrderInternal();
    }

    private String getDefaultTimeOrderInternal()
    {
        final short order = defaultSettingsMap.get(DefaultSettings.HMS);

        /*
         * The byte returned by DP is within the range 1-3 or 5-7.
         */
        switch (order)
        {
            case 1:
                return "SMH";

            case 2:
                return "MHS";

            case 3:
                return "HSM";

            case 5:
                return "SHM";

            case 6:
                return "HMS";

            case 7:
                return "MSH";

            case 4:default:
                assert false : "Illegal time order byte should have been detected in readConstants";

                return null;
        }
    }

    private void readConstants()
                        throws IOException, InvalidStructureFileException
    {
        final BlockGroupConstants bgConstants = new BlockGroupConstants(bg3.blkConstants, structureFile);
        final BlockGroupDefaultSettings bgDefaultSettings =
            new BlockGroupDefaultSettings(bgConstants.blkDefaultSettings, structureFile);

        if (bgDefaultSettings.ymd == 4 || bgDefaultSettings.ymd == 0)
        {
            throw new InvalidStructureFileException("Illegal default date order byte: " + bgDefaultSettings.ymd);
        }

        if (bgDefaultSettings.hms == 4 || bgDefaultSettings.hms == 0)
        {
            throw new InvalidStructureFileException("Illegal default time order byte: " + bgDefaultSettings.hms);
        }

        defaultSettingsMap.put(DefaultSettings.COLOR_1, bgDefaultSettings.color1);
        defaultSettingsMap.put(DefaultSettings.COLOR_2, bgDefaultSettings.color2);
        defaultSettingsMap.put(DefaultSettings.COLOR_3, bgDefaultSettings.color3);
        defaultSettingsMap.put(DefaultSettings.COLOR_4, bgDefaultSettings.color4);
        defaultSettingsMap.put(DefaultSettings.MENU_EDIT_COLOR, bgDefaultSettings.menuEditColor);
        defaultSettingsMap.put(DefaultSettings.PERIOD, bgDefaultSettings.period);
        defaultSettingsMap.put(DefaultSettings.COMMA, bgDefaultSettings.comma);
        defaultSettingsMap.put(DefaultSettings.YMD, bgDefaultSettings.ymd);
        defaultSettingsMap.put(DefaultSettings.HMS, bgDefaultSettings.hms);
        defaultSettingsMap.put(DefaultSettings.LINE_0, bgDefaultSettings.line0);
        defaultSettingsMap.put(DefaultSettings.DATE_0_TYPE, bgDefaultSettings.date0Type);
        defaultSettingsMap.put(DefaultSettings.AUTO_HELP, bgDefaultSettings.autoHelp);
        defaultSettingsMap.put(DefaultSettings.REPORT_DISPLAY, bgDefaultSettings.reportDisplayOnOff);
    }

    /**
     * Methods that return data read from the database require that the database be opened before
     * they are called. They should call this function to perform the check.
     */
    private void checkIsOpen()
    {
        if (! isOpen)
        {
            throw new IllegalStateException("Database must be open.");
        }
    }

    /**
     * Finds matching file case insensitively, or null if no match is found.
     * If there is more than one match, it is not defined which file will
     *  be returned.
     *
     * @param panelFileName the panel file name
     * @return the panel file
     */
    private File findPanelFile(final String panelFileName)
    {
        File panelFile = new File(structureFile.getDirectory(),
                                  panelFileName);

        if (panelFile.exists())
        {
            return panelFile;
        }

        for (String fileName : structureFile.getDirectory().list())
        {
            if (fileName.equalsIgnoreCase(panelFileName))
            {
                return new File(structureFile.getDirectory(),
                                fileName);
            }
        }

        return null;
    }
}
