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
 * <p>Java class for aocsOpModeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="aocsOpModeType">
 *   &lt;restriction base="{}string">
 *     &lt;enumeration value="No Mode"/>
 *     &lt;enumeration value="Normal Pointing Mode"/>
 *     &lt;enumeration value="Orbit Control Mode"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "aocsOpModeType")
@XmlEnum
public enum AocsOpModeType {

    @XmlEnumValue("No Mode")
    NO_MODE("No Mode"),
    @XmlEnumValue("Normal Pointing Mode")
    NORMAL_POINTING_MODE("Normal Pointing Mode"),
    @XmlEnumValue("Orbit Control Mode")
    ORBIT_CONTROL_MODE("Orbit Control Mode");
    private final String value;

    AocsOpModeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AocsOpModeType fromValue(String v) {
        for (AocsOpModeType c: AocsOpModeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
