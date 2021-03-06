//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.05 at 04:36:39 PM CET 
//


package jrc.it.annotation.reader.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for baqCodeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="baqCodeType">
 *   &lt;restriction base="{}string">
 *     &lt;enumeration value="BAQ 3-Bit"/>
 *     &lt;enumeration value="BAQ 4-Bit"/>
 *     &lt;enumeration value="BAQ 5-Bit"/>
 *     &lt;enumeration value="BRC 0"/>
 *     &lt;enumeration value="BRC 1"/>
 *     &lt;enumeration value="BRC 2"/>
 *     &lt;enumeration value="BRC 3"/>
 *     &lt;enumeration value="BRC 4"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "baqCodeType")
@XmlEnum
public enum BaqCodeType {

    @XmlEnumValue("BAQ 3-Bit")
    BAQ_3_BIT("BAQ 3-Bit"),
    @XmlEnumValue("BAQ 4-Bit")
    BAQ_4_BIT("BAQ 4-Bit"),
    @XmlEnumValue("BAQ 5-Bit")
    BAQ_5_BIT("BAQ 5-Bit"),
    @XmlEnumValue("BRC 0")
    BRC_0("BRC 0"),
    @XmlEnumValue("BRC 1")
    BRC_1("BRC 1"),
    @XmlEnumValue("BRC 2")
    BRC_2("BRC 2"),
    @XmlEnumValue("BRC 3")
    BRC_3("BRC 3"),
    @XmlEnumValue("BRC 4")
    BRC_4("BRC 4");
    private final String value;

    BaqCodeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BaqCodeType fromValue(String v) {
        for (BaqCodeType c: BaqCodeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
