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
package org.codice.imaging.nitf.core;

import java.text.ParseException;

/**
    Security metadata for a NITF file header or segment subheader.
*/
public class NitfSecurityMetadata {

    private NitfSecurityClassification nitfSecurityClassification = NitfSecurityClassification.UNKNOWN;
    private String nitfSecurityClassificationSystem = null;
    private String nitfCodewords = null;
    private String nitfControlAndHandling = null;
    private String nitfReleaseInstructions = null;
    // Could be an enumerated type
    private String nitfDeclassificationType = null;
    // String instead of Date because its frequently just an empty string
    private String nitfDeclassificationDate = null;
    private String nitfDeclassificationExemption = null;
    private String nitfDowngrade = null;
    // String instead of Date because its frequently just an empty string
    private String nitfDowngradeDate = null;
    private String nitfClassificationText = null;
    // Could be an enumerated type
    private String nitfClassificationAuthorityType = null;
    private String nitfClassificationAuthority = null;
    private String nitfClassificationReason = null;
    private String nitfSecuritySourceDate = null;
    private String nitfSecurityControlNumber = null;

    // NITF 2.0 values
    private String downgradeDateOrSpecialCase = null;
    private String downgradeEvent = null;

    /**
        Default constructor.
    */
    public NitfSecurityMetadata() {
    }

    /**
        Constructor.
        <p>
        This builds a new object using the specified NitfReader.

        @param nitfReader the reader to use, positioned at the start of the security metadata.
        @throws ParseException if any error in the metadata is detected.
    */
    public NitfSecurityMetadata(final NitfReader nitfReader) throws ParseException {
        NitfSecurityMetadataParser parser = new NitfSecurityMetadataParser();
        parser.parse(nitfReader, this);
    }

    /**
        Set the security classification.

        @param securityClassification security classification
    */
    public final void setSecurityClassification(final NitfSecurityClassification securityClassification) {
        nitfSecurityClassification = securityClassification;
    }

    /**
        Return the security classification.

        @return security classification
    */
    public final NitfSecurityClassification getSecurityClassification() {
        return nitfSecurityClassification;
    }

    /**
        Set the security classification system.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall contain valid values indicating the national or
        multinational security system used to classify the file. Country Codes per FIPS PUB 10-4 shall be used to
        indicate national security systems. The designator "XN" is for classified data generated by a component using
        NATO security system marking guidance. This code is outside the FIPS 10-4 document listing, and was
        selected to not duplicate that document's existing codes."
        <p>
        So system means "which country specified it". This field can be empty indicating no security classification
        system applied.
        <p>
        This field must be set if security-related details (e.g. codewords, control and handling instructions, release
        instructions, declassification instructions, declassification authorities, declassification dates or
        declassification exemptions) are set.

        @param securityClassificationSystem the security classification system (2 character country code)
    */
    public final void setSecurityClassificationSystem(final String securityClassificationSystem) {
        nitfSecurityClassificationSystem = securityClassificationSystem;
    }

    /**
        Return the security classification system.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall contain valid values indicating the national or
        multinational security system used to classify the file. Country Codes per FIPS PUB 10-4 shall be used to
        indicate national security systems. The designator "XN" is for classified data generated by a component using
        NATO security system marking guidance. This code is outside the FIPS 10-4 document listing, and was
        selected to not duplicate that document's existing codes."
        <p>
        So system means "which country specified it". This field can be empty indicating no security classification
        system applied.

        @return security classification system
    */
    public final String getSecurityClassificationSystem() {
        return nitfSecurityClassificationSystem;
    }

    /**
        Set the security codewords.
        <p>
        "This field shall contain a valid indicator of the security compartments associated with
        the file. Values include one or more of the digraphs found table A-4. Multiple entries shall be separated by a
        single ECS space (0x20): The selection of a relevant set of codewords is application specific."
        <p>
        Note that the list in MIL-STD-2500C table A-4 includes digraphs that are no longer used. Consult current guidance.
        <p>
        This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files. The maximum length is 11 characters for
        NITF 2.1 / NSIF 1.0; and 40 characters for NITF 2.0.

        @param codewords security codewords or an empty string if no codewords apply
    */
    public final void setCodewords(final String codewords) {
        nitfCodewords = codewords;
    }

    /**
        Return the security codewords.
        <p>
        "This field shall contain a valid indicator of the security compartments associated with
        the file. Values include one or more of the digraphs found table A-4. Multiple entries shall be separated by a
        single ECS space (0x20): The selection of a relevant set of codewords is application specific."
        <p>
        Note that the list in MIL-STD-2500C table A-4 includes digraphs that are no longer used. Consult current guidance.
        <p>
        This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files.

        @return security codewords or an empty string if no codewords apply.
    */
    public final String getCodewords() {
        return nitfCodewords;
    }

    /**
        Set the security control and handling code instructions.
        <p>
        "This field shall contain valid additional security control and/or handling instructions
        (caveats) associated with the file. Values include digraphs found in table A-4. The digraph may indicate
        single or multiple caveats. The selection of a relevant caveat(s) is application specific."
        <p>
        Note that the list in MIL-STD-2500C table A-4 includes digraphs that are no longer used. Consult current guidance.
        <p>
        This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files. The maximum length is 2 characters for
        NITF 2.1 / NSIF 1.0; and 40 characters for NITF 2.0.

        @param instructions security control and handling codes, or an empty string if no codes apply.
    */
    public final void setControlAndHandling(final String instructions) {
        nitfControlAndHandling = instructions;
    }

    /**
        Return the security control and handling code instructions.
        <p>
        "This field shall contain valid additional security control and/or handling instructions
        (caveats) associated with the file. Values include digraphs found in table A-4. The digraph may indicate
        single or multiple caveats. The selection of a relevant caveat(s) is application specific."
        <p>
        Note that the list in MIL-STD-2500C table A-4 includes digraphs that are no longer used. Consult current guidance.
        <p>
        This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files.

        @return security control and handling codes, or an empty string if no codes apply.
    */
    public final String getControlAndHandling() {
        return nitfControlAndHandling;
    }

    /**
        Set the release instructions.
        <p>
        "This field shall contain a valid list of country and/or multilateral entity codes to
        which countries and/or multilateral entities the file is authorized for release. Valid items in
        the list are one or more country codes as found in FIPS PUB 10-4 separated by a single ECS space (0x20)."
        <p>
        So the release instructions are the countries that this is "REL TO".
        <p>
        This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files. The maximum length is 20 characters for
        NITF 2.1 / NSIF 1.0; and 40 characters for NITF 2.0.

        @param releaseInstructions release instructions, or an empty string if no release instructions apply.
    */
    public final void setReleaseInstructions(final String releaseInstructions) {
        nitfReleaseInstructions = releaseInstructions;
    }

    /**
        Return the release instructions.
        <p>
        "This field shall contain a valid list of country and/or multilateral entity codes to
        which countries and/or multilateral entities the file is authorized for release. Valid items in
        the list are one or more country codes as found in FIPS PUB 10-4 separated by a single ECS space (0x20)."
        <p>
        So the release instructions are the countries that this is "REL TO".
        <p>
        This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files.

        @return release instructions, or an empty string if no release instructions apply.
    */
    public final String getReleaseInstructions() {
        return nitfReleaseInstructions;
    }

    /**
        Set the security declassification type.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall contain a valid indicator of the type of security declassification or
        downgrading instructions which apply to the file. Valid values are DD (=declassify on a specific date), DE
        (=declassify upon occurrence of an event), GD (=downgrade to a specified level on a specific date), GE
        (=downgrade to a specified level upon occurrence of an event), O (=OADR), and X (= exempt from automatic
        declassification)."
        <p>
        An empty string indicates that no declassification type applies.

        @param declassificationType the declassification type (2 characters maximum), or an empty string.
    */
    public final void setDeclassificationType(final String declassificationType) {
        nitfDeclassificationType = declassificationType;
    }

    /**
        Return the security declassification type.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall contain a valid indicator of the type of security declassification or
        downgrading instructions which apply to the file. Valid values are DD (=declassify on a specific date), DE
        (=declassify upon occurrence of an event), GD (=downgrade to a specified level on a specific date), GE
        (=downgrade to a specified level upon occurrence of an event), O (=OADR), and X (= exempt from automatic
        declassification)."

        @return the declassification type, or an empty string if no declassification instructions apply
    */
    public final String getDeclassificationType() {
        return nitfDeclassificationType;
    }

    /**
        Set the declassification date.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall indicate the date on which a file is to be declassified if the value in
        Declassification Type is DD."
        <p>
        An empty string means that no declassification date applies.

        @param declassificationDate the declassification date (format CCYYMMDD), or an empty string.
    */
    public final void setDeclassificationDate(final String declassificationDate) {
        nitfDeclassificationDate = declassificationDate;
    }

    /**
        Set the declassification date.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall indicate the date on which a file is to be declassified if the value in
        File Declassification Type is DD."
        <p>
        An empty string means that no declassification date applies.

        @return the declassification date (format CCYYMMDD), or an empty string.
    */
    public final String getDeclassificationDate() {
        return nitfDeclassificationDate;
    }

    /**
        Set the declassification exemption.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall indicate the reason the file is exempt from automatic
        declassification if the value in Declassification Type is X. Valid values are X1 to X8 and X251 to
        X259. X1 to X8 correspond to the declassification exemptions found in DOD 5200.1-R, paragraphs 4-202b(1) to (8)
        for material exempt from the 10-year rule. X251 to X259 correspond to the declassification exemptions found
        in DOD 5200.1-R, paragraphs 4-301a(1) to (9) for permanently valuable material exempt from the 25-year
        declassification system."
        <p>
        An empty string means that no declassification exemption applies.

        @param declassificationExemption the declassification exemption (four characters maximum), or an empty string.
    */
    public final void setDeclassificationExemption(final String declassificationExemption) {
        nitfDeclassificationExemption = declassificationExemption;
    }

    /**
        Return the declassification exemption.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall indicate the reason the file is exempt from automatic
        declassification if the value in Declassification Type is X. Valid values are X1 to X8 and X251 to
        X259. X1 to X8 correspond to the declassification exemptions found in DOD 5200.1-R, paragraphs 4-202b(1) to (8)
        for material exempt from the 10-year rule. X251 to X259 correspond to the declassification exemptions found
        in DOD 5200.1-R, paragraphs 4-301a(1) to (9) for permanently valuable material exempt from the 25-year
        declassification system."
        <p>
        An empty string means that no declassification exemption applies.

        @return the declassification exemption, or an empty string to indicate no declassification exemption applies.
    */
    public final String getDeclassificationExemption() {
        return nitfDeclassificationExemption;
    }

    /**
        Set the security downgrade.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall indicate the classification level to which a file is to be downgraded if
        the values in Declassification Type are GD or GE. Valid values are S (=Secret), C (=Confidential), R (=
        Restricted)."
        <p>
        An empty string indicates that security downgrading does not apply.

        @param downgrade the downgrade classification level (1 character), or an empty string.
    */
    public final void setDowngrade(final String downgrade) {
        nitfDowngrade = downgrade;
    }

    /**
        Return the security downgrade.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall indicate the classification level to which a file is to be downgraded if
        the values in Declassification Type are GD or GE. Valid values are S (=Secret), C (=Confidential), R (=
        Restricted)."
        <p>
        An empty string indicates that security downgrading does not apply.

        @return the downgrade classification level, or an empty string.
    */
    public final String getDowngrade() {
        return nitfDowngrade;
    }

    /**
        Set the downgrade date.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall indicate the date on which a file is to be downgraded if the value in
        Declassification Type is GD."
        <p>
        An empty string indicates that a security downgrading date does not apply.

        @param downgradeDate the downgrade date (format CCYYMMDD), or an empty string if downgrading does not apply.
    */
    public final void setDowngradeDate(final String downgradeDate) {
        nitfDowngradeDate = downgradeDate;
    }

    /**
        Return the downgrade date.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall indicate the date on which a file is to be downgraded if the value in
        Declassification Type is GD."
        <p>
        An empty string indicates that a security downgrading date does not apply.

        @return the downgrade date (format CCYYMMDD), or an empty string if downgrading does not apply.
    */
    public final String getDowngradeDate() {
        return nitfDowngradeDate;
    }

    /**
        Set the downgrade date or special case for this file.
        <p>
        This field is only valid for NITF 2.0 files.
        <p>
        The valid values are:
        (1) the calendar date in the format YYMMDD
        (2) the code "999999" when the originating agency's determination is required (OADR)
        (3) the code "999998" when a specific event determines at what point declassification or downgrading is to take place.
        <p>
        If the third case (999998) is set, use setDowngradeEvent() to specify the downgrade event.

        @param dateOrSpecialCase the date or special case
    */
    public final void setDowngradeDateOrSpecialCase(final String dateOrSpecialCase) {
        downgradeDateOrSpecialCase = dateOrSpecialCase;
    }

    /**
        Return the downgrade date or special case for this file.
        <p>
        This field is only valid for NITF 2.0 files.
        <p>
        The valid values are:
        (1) the calendar date in the format YYMMDD
        (2) the code "999999" when the originating agency's determination is required (OADR)
        (3) the code "999998" when a specific event determines at what point declassification or downgrading is to take place.
        <p>
        If the third case (999998) occurs, use getDowngradeEvent() to determine the downgrade event.

        @return the downgrade date or special case flag value
    */
    public final String getDowngradeDateOrSpecialCase() {
        return downgradeDateOrSpecialCase;
    }

    /**
        Set the specific downgrade event for this file.
        <p>
        This field is only valid for NITF 2.0 files.
        <p>
        This is only valid if getDowngradeDateOrSpecialCase() is equal to 999998.

        @param event the downgrade event, or an empty string if no downgrade event applies
    */
    public final void setDowngradeEvent(final String event) {
        downgradeEvent = event;
    }

    /**
      Get the specific downgrade event for this file.
      <p>
      This field is only valid for NITF 2.0 files.
      <p>
      This is only valid if getDowngradeDateOrSpecialCase() is equal to 999998.

      @return the downgrade event
    */
    public final String getDowngradeEvent() {
        return downgradeEvent;
    }

    /**
        Set the classification text.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall be used to provide additional information about file classification
        to include identification of a declassification or downgrading event if the values in Declassification
        Type are DE or GE. It may also be used to identify multiple classification sources and/or any other special
        handling rules. Values are user defined free text."
        <p>
        An empty string indicates that  additional information about file classification does not apply.

        @param classificationText the classification text (43 characters maximum), or an empty string.
    */
    public final void setClassificationText(final String classificationText) {
        nitfClassificationText = classificationText;
    }

    /**
        Return the classification text.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall be used to provide additional information about file classification
        to include identification of a declassification or downgrading event if the values in Declassification
        Type are DE or GE. It may also be used to identify multiple classification sources and/or any other special
        handling rules. Values are user defined free text."
        <p>
        An empty string indicates that  additional information about file classification does not apply.

        @return the classification text or an empty string.
    */
    public final String getClassificationText() {
        return nitfClassificationText;
    }

    /**
        Set the classification authority type.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall indicate the type of authority used to classify the file. Valid values are
        O (= original classification authority),
        D (= derivative from a single source), and
        M (= derivative from multiple sources)."
        <p>
        An empty string indicates that classification authority type does not apply.

        @param classificationAuthorityType classification authority type (1 character), or an empty string.
    */
    public final void setClassificationAuthorityType(final String classificationAuthorityType) {
        nitfClassificationAuthorityType = classificationAuthorityType;
    }

    /**
        Return the classification authority type.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.
        <p>
        "This field shall indicate the type of authority used to classify the file. Valid values are
        O (= original classification authority),
        D (= derivative from a single source), and
        M (= derivative from multiple sources)."
        <p>
        An empty string indicates that classification authority type does not apply.

        @return classification authority type, or an empty string.
    */
    public final String getClassificationAuthorityType() {
        return nitfClassificationAuthorityType;
    }

    /**
        Set the classification security authority.
        <p>
        "This field shall identify the classification authority for the file dependent upon
        the value in Classification Authority Type. Values are user defined free text which should contain the
        following information: original classification authority name and position or personal identifier if the value in
        Classification Authority Type is O; title of the document or security classification guide used to classify
        the file if the value in Classification Authority Type is D; and Derive-Multiple if the file classification was
        derived from multiple sources and the value of the Classification Authority Type field is M.
        In the latter case, the file originator will maintain a record of the sources used in accordance
        with existing security directives. One of the multiple sources may also be identified in Classification Text
        if desired."
        <p>
        An empty string indicates that no file classification authority applies.
        <p>
        This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files. The maximum length is 40 characters for
        NITF 2.1 / NSIF 1.0; and 20 characters for NITF 2.0.

        @param classificationAuthority classification authority or an empty string.
    */
    public final void setClassificationAuthority(final String classificationAuthority) {
        nitfClassificationAuthority = classificationAuthority;
    }

    /**
        Return the classification security authority.
        <p>
        "This field shall identify the classification authority for the file dependent upon
        the value in Classification Authority Type. Values are user defined free text which should contain the
        following information: original classification authority name and position or personal identifier if the value in
        Classification Authority Type is O; title of the document or security classification guide used to classify
        the file if the value in Classification Authority Type is D; and Derive-Multiple if the file classification was
        derived from multiple sources and the value of the Classification Authority Type field is M.
        In the latter case, the file originator will maintain a record of the sources used in accordance
        with existing security directives. One of the multiple sources may also be identified in Classification Text
        if desired."
        <p>
        An empty string indicates that no file classification authority applies.
        <p>
        This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files.

        @return classification authority or an empty string.
    */
    public final String getClassificationAuthority() {
        return nitfClassificationAuthority;
    }

    /**
        Set the classification reason.
        <p>
        "This field shall contain values indicating the reason for classifying the file.
        Valid values are A to G. These correspond to the reasons for original classification per E.O. 12958,
        Section 1.5.(a) to (g)."
        <p>
        An empty string indicates that no file classification reason applies.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.

        @param classificationReason the classification reason (1 character), or an empty string.
    */
    public final void setClassificationReason(final String classificationReason) {
        nitfClassificationReason = classificationReason;
    }

    /**
        Return the classification reason.
        <p>
        "This field shall contain values indicating the reason for classifying the file.
        Valid values are A to G. These correspond to the reasons for original classification per E.O. 12958,
        Section 1.5.(a) to (g)."
        <p>
        An empty string indicates that no file classification reason applies.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.

        @return the classification reason (1 character), or an empty string.
    */
    public final String getClassificationReason() {
        return nitfClassificationReason;
    }

    /**
        Set the security source date.
        <p>
        "This field shall indicate the date of the source used to derive the classification of the
        file. In the case of multiple sources, the date of the most recent source shall be used."
        <p>
        An empty string indicates that a security source date does not apply.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.

        @param securitySourceDate the security source date (format CCYYMMDD), or an empty string.
    */
    public final void setSecuritySourceDate(final String securitySourceDate) {
        nitfSecuritySourceDate = securitySourceDate;
    }

    /**
        Return the security source date.
        <p>
        "This field shall indicate the date of the source used to derive the classification of the
        file. In the case of multiple sources, the date of the most recent source shall be used."
        <p>
        An empty string indicates that a security source date does not apply.
        <p>
        This field is only valid for NITF 2.1 / NSIF 1.0 files.

        @return the security source date (format CCYYMMDD), or an empty string.
    */
    public final String getSecuritySourceDate() {
        return nitfSecuritySourceDate;
    }

    /**
        Set the security control number.
        <p>
        "This field shall contain a valid security control number associated with the file.
        The format of the security control number shall be in accordance with the regulations governing the
        appropriate security channel(s)."
        <p>
        An empty string indicates that no file security control number applies.
        <p>
        This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files. The maximum length is 15 characters for
        NITF 2.1 / NSIF 1.0; and 20 characters for NITF 2.0.

        @param securityControlNumber the security control number, or an empty string.
    */
    public final void setSecurityControlNumber(final String securityControlNumber) {
        nitfSecurityControlNumber = securityControlNumber;
    }

    /**
        Return the security control number.
        <p>
        "This field shall contain a valid security control number associated with the file.
        The format of the security control number shall be in accordance with the regulations governing the
        appropriate security channel(s)."
        <p>
        An empty string indicates that no file security control number applies.
        <p>
        This field is valid for NITF 2.0 and NITF 2.1 / NSIF 1.0 files.

        @return the security control number, or an empty string.
    */
    public final String getSecurityControlNumber() {
        return nitfSecurityControlNumber;
    }
};
