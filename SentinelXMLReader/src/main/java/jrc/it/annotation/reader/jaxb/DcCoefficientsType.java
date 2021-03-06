//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.05 at 04:36:39 PM CET 
//


package jrc.it.annotation.reader.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Annotation record for Doppler centroid coefficients.
 * 
 * <p>Java class for dcCoefficientsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dcCoefficientsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="d0" type="{}double"/>
 *         &lt;element name="d1" type="{}double"/>
 *         &lt;element name="d2" type="{}double"/>
 *         &lt;element name="d3" type="{}double"/>
 *         &lt;element name="d4" type="{}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dcCoefficientsType", propOrder = {
    "d0",
    "d1",
    "d2",
    "d3",
    "d4"
})
public class DcCoefficientsType {

    @XmlElement(required = true)
    protected Double d0;
    @XmlElement(required = true)
    protected Double d1;
    @XmlElement(required = true)
    protected Double d2;
    @XmlElement(required = true)
    protected Double d3;
    @XmlElement(required = true)
    protected Double d4;

    /**
     * Gets the value of the d0 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getD0() {
        return d0;
    }

    /**
     * Sets the value of the d0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setD0(Double value) {
        this.d0 = value;
    }

    /**
     * Gets the value of the d1 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getD1() {
        return d1;
    }

    /**
     * Sets the value of the d1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setD1(Double value) {
        this.d1 = value;
    }

    /**
     * Gets the value of the d2 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getD2() {
        return d2;
    }

    /**
     * Sets the value of the d2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setD2(Double value) {
        this.d2 = value;
    }

    /**
     * Gets the value of the d3 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getD3() {
        return d3;
    }

    /**
     * Sets the value of the d3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setD3(Double value) {
        this.d3 = value;
    }

    /**
     * Gets the value of the d4 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getD4() {
        return d4;
    }

    /**
     * Sets the value of the d4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setD4(Double value) {
        this.d4 = value;
    }

}
