/*
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
 */
package org.codice.imaging.nitf.core;

/**
    Image compression format.
*/
public enum ImageCompression {

    /**
        Unknown image compression format.
        <p>
        This indicates an unknown format, and typically indicates a broken file or
        an error during parsing. This is not a valid value in a NITF image subheader.
    */
    UNKNOWN (""),
    /**
        User specified compression.
        <p>
        This is only valid for NITF 2.0 files.
    */
    USERDEFINED ("C0"),

    /**
        Bi-level.
        <p>
        This is specified in ITU-T T.4, AMD2.
    */
    BILEVEL ("C1"),

    /**
        Compressed ARIDPCM.
        <p>
        This is specified in MIL-STD-188-197A.
        <p>
        This is only valid for NITF 2.0 files.
    */
    ARIDPCM ("C2"),

    /**
        JPEG.
        <p>
        This is specified in MIL-STD-188-198A.
    */
    JPEG ("C3"),

    /**
        Vector Quantization.
        <p>
        This is specified in MIL-STD-188-199.
    */
    VECTORQUANTIZATION ("C4"),

    /**
        Lossless JPEG.
        <p>
        This is specified in NGA N0106-97.
        <p>
        This is not valid for NITF 2.0 files.
    */
    LOSSLESSJPEG ("C5"),

    /**
        Downsampled JPEG.
        <p>
        This is specified in NGA N0106-97.
        <p>
        This is not valid for NITF 2.0 files.
    */
    DOWNSAMPLEDJPEG ("I1"),

    /**
        Not compressed.
    */
    NOTCOMPRESSED ("NC"),

    /**
        Bi-level mask.
        <p>
        This is specified in ITU-T T.4, AMD2.
    */
    BILEVELMASK ("M1"),

    /**
        JPEG mask.
        <p>
        This is specified in MIL-STD-188-198A.
    */
    JPEGMASK ("M3"),

    /**
        Vector Quantization mask.
        <p>
        This is specified in MIL-STD-188-199.
    */
    VECTORQUANTIZATIONMASK ("M4"),

    /**
        Lossless JPEG mask.
        <p>
        This is specified in NGA N0106-97.
        <p>
        This is not valid for NITF 2.0 files.
    */
    LOSSLESSJPEGMASK ("M5"),

    /**
        Not compressed mask.
    */
    NOTCOMPRESSEDMASK ("NM"),

    /**
        JPEG 2000.
        <p>
        This is specified in ISO/IEC 15444-1:2000 (with amendments 1 and 2).
        <p>
        This is not valid for NITF 2.0 files.
    */
    JPEG2000 ("C8"),
    /**
        JPEG 2000 mask.
        <p>
        This is specified in ISO/IEC 15444-1:2000 (with amendments 1 and 2).
        <p>
        This is not valid for NITF 2.0 files.
    */
    JPEG2000MASK ("M8");

    private final String textEquivalent;

    /**
        Constructor.
        <p>
        This is required for enumeration initialisation.

        @param abbreviation the text abbreviation for the enumeration value.
    */
    ImageCompression(final String abbreviation) {
        textEquivalent = abbreviation;
    }

    /**
        Create image compression enumerated value from the text equivalent.
        <p>
        This is intended to support file parsing, and is not usually necessary
        for other purposes.

        @param textEquivalent the single character text equivalent for image compression.
        @return the image compression enumerated value.
    */
    public static ImageCompression getEnumValue(final String textEquivalent) {
        for (ImageCompression ic : values()) {
            if (textEquivalent.equals(ic.textEquivalent)) {
                return ic;
            }
        }
        return UNKNOWN;
    }

    /**
        Return the text equivalent for an image compression type.
        <p>
        This is intended for debug output and output writing, and is not usually
        necessary for other purposes.

        @return the text equivalent for an image compression type.
    */
    public String getTextEquivalent() {
        return textEquivalent;
    }
};

