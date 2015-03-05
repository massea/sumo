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
 * <p>Java class for orbitAttitudeSourceType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="orbitAttitudeSourceType">
 *   &lt;restriction base="{}string">
 *     &lt;enumeration value="Downlink"/>
 *     &lt;enumeration value="Auxiliary"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "orbitAttitudeSourceType")
@XmlEnum
public enum OrbitAttitudeSourceType {

    @XmlEnumValue("Downlink")
    DOWNLINK("Downlink"),
    @XmlEnumValue("Auxiliary")
    AUXILIARY("Auxiliary");
    private final String value;

    OrbitAttitudeSourceType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OrbitAttitudeSourceType fromValue(String v) {
        for (OrbitAttitudeSourceType c: OrbitAttitudeSourceType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
