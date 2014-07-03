/**
 * Copyright (c) Codice Foundation
 * 
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 * 
 **/
package org.codice.nitf.filereader;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NitfHeaderReader
{
    private FileType fileType = FileType.UNKNOWN;
    private int nitfComplexityLevel = 0;
    private String nitfStandardType = null;
    private String nitfOriginatingStationId = null;
    private Date nitfFileDateTime = null;
    private String nitfFileTitle = null;
    private NitfFileSecurityMetadata fileSecurityMetadata = null;
    private byte nitfFileBackgroundColourRed = 0;
    private byte nitfFileBackgroundColourGreen = 0;
    private byte nitfFileBackgroundColourBlue = 0;
    private String nitfOriginatorsName = null;
    private String nitfOriginatorsPhoneNumber = null;
    private long nitfFileLength = -1;
    private int nitfHeaderLength = -1;
    private int numberImageSegments = 0;
    private ArrayList<Integer> lish = new ArrayList<Integer>();
    private ArrayList<Long> li = new ArrayList<Long>();
    private ArrayList<Integer> lssh = new ArrayList<Integer>();
    private ArrayList<Integer> ls = new ArrayList<Integer>();
    private ArrayList<Integer> ltsh = new ArrayList<Integer>();
    private ArrayList<Integer> lt = new ArrayList<Integer>();
    private ArrayList<Integer> ldsh = new ArrayList<Integer>();
    private ArrayList<Long> ld = new ArrayList<Long>();
    private int numberGraphicSegments = 0;
    private int numberTextSegments = 0;
    private int numberDataExtensionSegments = 0;
    private int numberReservedExtensionSegments = 0;
    private int userDefinedHeaderDataLength = 0;
    private int extendedHeaderDataLength = 0;
    private int extendedHeaderOverflow = 0;
    private String extendedHeaderData = null;

    private ArrayList<NitfImageSegment> imageSegments = new ArrayList<NitfImageSegment>();
    private ArrayList<NitfGraphicSegment> graphicSegments = new ArrayList<NitfGraphicSegment>();
    private ArrayList<NitfTextSegment> textSegments = new ArrayList<NitfTextSegment>();
    private ArrayList<NitfDataExtensionSegment> dataExtensionSegments = new ArrayList<NitfDataExtensionSegment>();

    private NitfReader reader = null;

    private static final int FHDR_LENGTH = 4;
    private static final String NITF_FHDR = "NITF";
    private static final int FVER_LENGTH = 5;
    private static final int CLEVEL_LENGTH = 2;
    private static final int STYPE_LENGTH = 4;
    private static final int OSTAID_LENGTH = 10;
    private static final int FTITLE_LENGTH = 80;
    private static final int FBKGC_LENGTH = 3;
    private static final int ONAME_LENGTH = 24;
    private static final int OPHONE_LENGTH = 18;
    private static final int FL_LENGTH = 12;
    private static final int HL_LENGTH = 6;
    private static final int NUMI_LENGTH = 3;
    private static final int LISH_LENGTH = 6;
    private static final int LI_LENGTH = 10;
    private static final int NUMS_LENGTH = 3;
    private static final int LSSH_LENGTH = 4;
    private static final int LS_LENGTH = 6;
    private static final int NUMX_LENGTH = 3;
    private static final int NUMT_LENGTH = 3;
    private static final int LTSH_LENGTH = 4;
    private static final int LT_LENGTH = 5;
    private static final int NUMDES_LENGTH = 3;
    private static final int LDSH_LENGTH = 4;
    private static final int LD_LENGTH = 9;
    private static final int NUMRES_LENGTH = 3;
    private static final int UDHDL_LENGTH = 5;
    private static final int XHDL_LENGTH = 5;
    private static final int XHDLOFL_LENGTH = 3;

    private static final long STREAMING_FILE_MODE = 999999999999L;

    public NitfHeaderReader(InputStream nitfInputStream) throws ParseException, UnsupportedOperationException {
        reader = new NitfReader(new BufferedInputStream((nitfInputStream)), 0);
        readFHDRFVER();
        readCLEVEL();
        readSTYPE();
        readOSTAID();
        readFDT();
        readFTITLE();
        fileSecurityMetadata = new NitfFileSecurityMetadata(reader);
        reader.readENCRYP();
        readFBKGC();
        readONAME();
        readOPHONE();
        readFL();
        if (nitfFileLength == STREAMING_FILE_MODE) {
            if (!reader.canSeek()) {
                throw new UnsupportedOperationException("No support for streaming mode unless input is seekable");
            }
            // TODO If we can ever seek, we need to read the streaming mode DES and update properties here.
        }
        readHL();
        readNUMI();
        for (int i = 0; i < numberImageSegments; ++i) {
            readLISH();
            readLI();
        }
        readNUMS();
        for (int i = 0; i < numberGraphicSegments; ++i) {
            readLSSH();
            readLS();
        }
        readNUMX();
        readNUMT();
        for (int i = 0; i < numberTextSegments; ++i) {
            readLTSH();
            readLT();
        }
        readNUMDES();
        for (int i = 0; i < numberDataExtensionSegments; ++i) {
            readLDSH();
            readLD();
        }
        readNUMRES();
        for (int i = 0; i < numberReservedExtensionSegments; ++i) {
            // TODO: find a case that exercises this and implement it
            throw new UnsupportedOperationException("IMPLEMENT RES PARSING");
        }
        readUDHDL();
        if (userDefinedHeaderDataLength > 0) {
            // TODO: find a case that exercises this and implement it
            throw new UnsupportedOperationException("IMPLEMENT USER DEFINED SEGMENT PARSING");
        }
        readXHDL();
        if (extendedHeaderDataLength > 0) {
            readXHDLOFL();
            readXHD();
        }
        readImageSegments();
        readGraphicSegments();
        readTextSegments();
        readDataExtensionSegments();
    }

    public FileType getFileType() {
        return fileType;
    }

    public int getComplexityLevel() {
        return nitfComplexityLevel;
    }

    public String getStandardType() {
        return nitfStandardType;
    }

    public String getOriginatingStationId() {
        return nitfOriginatingStationId;
    }

    public Date getFileDateTime() {
        return nitfFileDateTime;
    }

    public String getFileTitle() {
        return nitfFileTitle;
    }

    public NitfFileSecurityMetadata getFileSecurityMetadata() {
        return fileSecurityMetadata;
    }

    public byte getFileBackgroundColourRed() {
        return nitfFileBackgroundColourRed;
    }

    public byte getFileBackgroundColourGreen() {
        return nitfFileBackgroundColourGreen;
    }

    public byte getFileBackgroundColourBlue() {
        return nitfFileBackgroundColourBlue;
    }

    public String getOriginatorsName() {
        return nitfOriginatorsName;
    }

    public String getOriginatorsPhoneNumber() {
        return nitfOriginatorsPhoneNumber;
    }

    public long getFileLength() {
        return nitfFileLength;
    }

    public int getHeaderLength() {
        return nitfHeaderLength;
    }

    public int getNumberOfImageSegments() {
        // TODO: this should be based on the number we actually found, not what the header claimed
        return numberImageSegments;
    }

    public int getLengthOfImageSubheader(int i) {
        return lish.get(i);
    }

    public long getLengthOfImage(int i) {
        return li.get(i);
    }

    public int getNumberOfGraphicSegments() {
        return numberGraphicSegments;
    }

    public int getLengthOfGraphicSubheader(int i) {
        return lssh.get(i);
    }

    public int getLengthOfGraphic(int i) {
        return ls.get(i);
    }

    public int getNumberOfTextSegments() {
        return numberTextSegments;
    }

    public int getLengthOfTextSubheader(int i) {
        return ltsh.get(i);
    }

    public int getLengthOfText(int i) {
        return lt.get(i);
    }

    public int getNumberOfDataExtensionSegments() {
        return numberDataExtensionSegments;
    }

    public int getNumberOfReservedExtensionSegments() {
        return numberReservedExtensionSegments;
    }

    public int getUserDefinedHeaderDataLength() {
        return userDefinedHeaderDataLength;
    }

    public int getExtendedHeaderDataLength() {
        return extendedHeaderDataLength;
    }

    public String getRawExtendedHeaderData() {
        return extendedHeaderData;
    }

    public NitfImageSegment getImageSegment(int segmentNumber) {
        return getImageSegmentZeroBase(segmentNumber - 1);
    }

    public NitfImageSegment getImageSegmentZeroBase(int segmentNumberZeroBase) {
        return imageSegments.get(segmentNumberZeroBase);
    }

    public NitfGraphicSegment getGraphicSegment(int segmentNumber) {
        return getGraphicSegmentZeroBase(segmentNumber - 1);
    }

    public NitfGraphicSegment getGraphicSegmentZeroBase(int segmentNumberZeroBase) {
        return graphicSegments.get(segmentNumberZeroBase);
    }

    public NitfTextSegment getTextSegment(int segmentNumber) {
        return getTextSegmentZeroBase(segmentNumber - 1);
    }

    public NitfTextSegment getTextSegmentZeroBase(int segmentNumberZeroBase) {
        return textSegments.get(segmentNumberZeroBase);
    }

    public NitfDataExtensionSegment getDataExtensionSegment(int segmentNumber) {
        return getDataExtensionSegmentZeroBase(segmentNumber - 1);
    }

    public NitfDataExtensionSegment getDataExtensionSegmentZeroBase(int segmentNumberZeroBase) {
        return dataExtensionSegments.get(segmentNumberZeroBase);
    }

    private void readFHDRFVER() throws ParseException {
        String fhdrfver = reader.readBytes(FHDR_LENGTH + FVER_LENGTH);
        fileType = FileType.getEnumValue(fhdrfver);
    }

    private void readCLEVEL() throws ParseException {
        nitfComplexityLevel = reader.readBytesAsInteger(CLEVEL_LENGTH);
        if ((nitfComplexityLevel < 0) || (nitfComplexityLevel > 99)) {
            throw new ParseException(String.format("CLEVEL out of range: %i", nitfComplexityLevel), reader.numBytesRead);
        }
    }

    private void readSTYPE() throws ParseException {
        nitfStandardType = reader.readBytes(STYPE_LENGTH);
    }

    private void readOSTAID() throws ParseException {
        nitfOriginatingStationId = reader.readTrimmedBytes(OSTAID_LENGTH);
    }

    private void readFDT() throws ParseException {
        nitfFileDateTime = reader.readNitfDateTime();
    }

    private void readFTITLE() throws ParseException {
        nitfFileTitle = reader.readTrimmedBytes(FTITLE_LENGTH);
    }

    private void readFBKGC() throws ParseException {
        byte[] fbkgc = reader.readBytesRaw(FBKGC_LENGTH);
        nitfFileBackgroundColourRed = fbkgc[0];
        nitfFileBackgroundColourGreen = fbkgc[1];
        nitfFileBackgroundColourBlue = fbkgc[2];
    }

    private void readONAME() throws ParseException {
        nitfOriginatorsName = reader.readTrimmedBytes(ONAME_LENGTH);
    }

    private void readOPHONE() throws ParseException {
        nitfOriginatorsPhoneNumber = reader.readTrimmedBytes(OPHONE_LENGTH);
    }

    private void readFL() throws ParseException {
        nitfFileLength = reader.readBytesAsLong(FL_LENGTH);
    }

    private void readHL() throws ParseException {
        nitfHeaderLength = reader.readBytesAsInteger(HL_LENGTH);
    }

    private void readNUMI() throws ParseException {
        numberImageSegments = reader.readBytesAsInteger(NUMI_LENGTH);
    }

    private void readLISH() throws ParseException {
        lish.add(reader.readBytesAsInteger(LISH_LENGTH));
    }

    private void readLI() throws ParseException {
        li.add(reader.readBytesAsLong(LI_LENGTH));
    }

    private void readNUMS() throws ParseException {
        numberGraphicSegments = reader.readBytesAsInteger(NUMS_LENGTH);
    }

    private void readLSSH() throws ParseException {
        lssh.add(reader.readBytesAsInteger(LSSH_LENGTH));
    }

    private void readLS() throws ParseException {
        ls.add(reader.readBytesAsInteger(LS_LENGTH));
    }

    private void readNUMX() throws ParseException {
        reader.skip(NUMX_LENGTH);
    }

    private void readNUMT() throws ParseException {
        numberTextSegments = reader.readBytesAsInteger(NUMT_LENGTH);
    }

    private void readLTSH() throws ParseException {
        ltsh.add(reader.readBytesAsInteger(LTSH_LENGTH));
    }

    private void readLT() throws ParseException {
        lt.add(reader.readBytesAsInteger(LT_LENGTH));
    }

    private void readNUMDES() throws ParseException {
        numberDataExtensionSegments = reader.readBytesAsInteger(NUMDES_LENGTH);
    }

    private void readLDSH() throws ParseException {
        ldsh.add(reader.readBytesAsInteger(LDSH_LENGTH));
    }

    private void readLD() throws ParseException {
        ld.add(reader.readBytesAsLong(LD_LENGTH));
    }

    private void readNUMRES() throws ParseException {
        numberReservedExtensionSegments = reader.readBytesAsInteger(NUMRES_LENGTH);
    }

    private void readUDHDL() throws ParseException {
        userDefinedHeaderDataLength = reader.readBytesAsInteger(UDHDL_LENGTH);
    }

    private void readXHDL() throws ParseException {
        extendedHeaderDataLength = reader.readBytesAsInteger(XHDL_LENGTH);
    }

    private void readXHDLOFL() throws ParseException {
        extendedHeaderOverflow = reader.readBytesAsInteger(XHDLOFL_LENGTH);
    }

    private void readXHD() throws ParseException {
        extendedHeaderData = reader.readTrimmedBytes(extendedHeaderDataLength - XHDLOFL_LENGTH);
    }

    private void readImageSegments() throws ParseException {
        for (int i = 0; i < numberImageSegments; ++i) {
            imageSegments.add(new NitfImageSegment(reader, li.get(i)));
        }
    }

    private void readGraphicSegments() throws ParseException {
        for (int i = 0; i < numberGraphicSegments; ++i) {
            graphicSegments.add(new NitfGraphicSegment(reader, ls.get(i)));
        }
    }

    private void readTextSegments() throws ParseException {
        for (int i = 0; i < numberTextSegments; ++i) {
            textSegments.add(new NitfTextSegment(reader, lt.get(i)));
        }
    }

    private void readDataExtensionSegments() throws ParseException {
        for (int i = 0; i < numberDataExtensionSegments; ++i) {
            throw new UnsupportedOperationException("Implement DES parsing");
            // dataExtensionSegments.add(new NitfDataExtensionSegment(reader, ld.get(i)));
        }
    }
}
