//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.27 at 05:01:05 PM CEST 
//


package jrc.it.safe.reader.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://www.esa.int/safe/sentinel-1.0/sentinel-1}orbitProperties"/>
 *         &lt;element ref="{http://www.esa.int/safe/sentinel-1.0/sentinel-1}timeANX"/>
 *         &lt;element ref="{http://www.esa.int/safe/sentinel-1.0/sentinel-1/sar/level-1}instrumentMode"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "orbitProperties",
    "timeANX",
    "instrumentMode"
})
@XmlRootElement(name = "extension")
public class Extension {

    @XmlElement(namespace = "http://www.esa.int/safe/sentinel-1.0/sentinel-1")
    protected OrbitProperties orbitProperties;
    @XmlElement(namespace = "http://www.esa.int/safe/sentinel-1.0/sentinel-1")
    protected TimeANX timeANX;
    @XmlElement(namespace = "http://www.esa.int/safe/sentinel-1.0/sentinel-1/sar/level-1")
    protected InstrumentMode instrumentMode;

    /**
     * Gets the value of the orbitProperties property.
     * 
     * @return
     *     possible object is
     *     {@link OrbitProperties }
     *     
     */
    public OrbitProperties getOrbitProperties() {
        return orbitProperties;
    }

    /**
     * Sets the value of the orbitProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrbitProperties }
     *     
     */
    public void setOrbitProperties(OrbitProperties value) {
        this.orbitProperties = value;
    }

    /**
     * Gets the value of the timeANX property.
     * 
     * @return
     *     possible object is
     *     {@link TimeANX }
     *     
     */
    public TimeANX getTimeANX() {
        return timeANX;
    }

    /**
     * Sets the value of the timeANX property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeANX }
     *     
     */
    public void setTimeANX(TimeANX value) {
        this.timeANX = value;
    }

    /**
     * Gets the value of the instrumentMode property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentMode }
     *     
     */
    public InstrumentMode getInstrumentMode() {
        return instrumentMode;
    }

    /**
     * Sets the value of the instrumentMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentMode }
     *     
     */
    public void setInstrumentMode(InstrumentMode value) {
        this.instrumentMode = value;
    }

}
