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
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Annotation record for attitude quaternion.
 * 
 * <p>Java class for attitudeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="attitudeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="time" type="{}timeType"/>
 *         &lt;element name="frame" type="{}referenceFrameType"/>
 *         &lt;element name="q0" type="{}float"/>
 *         &lt;element name="q1" type="{}float"/>
 *         &lt;element name="q2" type="{}float"/>
 *         &lt;element name="q3" type="{}float"/>
 *         &lt;element name="wx" type="{}double"/>
 *         &lt;element name="wy" type="{}double"/>
 *         &lt;element name="wz" type="{}double"/>
 *         &lt;element name="roll" type="{}double"/>
 *         &lt;element name="pitch" type="{}double"/>
 *         &lt;element name="yaw" type="{}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attitudeType", propOrder = {
    "time",
    "frame",
    "q0",
    "q1",
    "q2",
    "q3",
    "wx",
    "wy",
    "wz",
    "roll",
    "pitch",
    "yaw"
})
public class AttitudeType {

    @XmlElement(required = true)
    protected XMLGregorianCalendar time;
    @XmlElement(required = true)
    protected ReferenceFrameType frame;
    @XmlElement(required = true)
    protected Float q0;
    @XmlElement(required = true)
    protected Float q1;
    @XmlElement(required = true)
    protected Float q2;
    @XmlElement(required = true)
    protected Float q3;
    @XmlElement(required = true)
    protected Double wx;
    @XmlElement(required = true)
    protected Double wy;
    @XmlElement(required = true)
    protected Double wz;
    @XmlElement(required = true)
    protected Double roll;
    @XmlElement(required = true)
    protected Double pitch;
    @XmlElement(required = true)
    protected Double yaw;

    /**
     * Gets the value of the time property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTime(XMLGregorianCalendar value) {
        this.time = value;
    }

    /**
     * Gets the value of the frame property.
     * 
     * @return
     *     possible object is
     *     {@link ReferenceFrameType }
     *     
     */
    public ReferenceFrameType getFrame() {
        return frame;
    }

    /**
     * Sets the value of the frame property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferenceFrameType }
     *     
     */
    public void setFrame(ReferenceFrameType value) {
        this.frame = value;
    }

    /**
     * Gets the value of the q0 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getQ0() {
        return q0;
    }

    /**
     * Sets the value of the q0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setQ0(Float value) {
        this.q0 = value;
    }

    /**
     * Gets the value of the q1 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getQ1() {
        return q1;
    }

    /**
     * Sets the value of the q1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setQ1(Float value) {
        this.q1 = value;
    }

    /**
     * Gets the value of the q2 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getQ2() {
        return q2;
    }

    /**
     * Sets the value of the q2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setQ2(Float value) {
        this.q2 = value;
    }

    /**
     * Gets the value of the q3 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getQ3() {
        return q3;
    }

    /**
     * Sets the value of the q3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setQ3(Float value) {
        this.q3 = value;
    }

    /**
     * Gets the value of the wx property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getWx() {
        return wx;
    }

    /**
     * Sets the value of the wx property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setWx(Double value) {
        this.wx = value;
    }

    /**
     * Gets the value of the wy property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getWy() {
        return wy;
    }

    /**
     * Sets the value of the wy property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setWy(Double value) {
        this.wy = value;
    }

    /**
     * Gets the value of the wz property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getWz() {
        return wz;
    }

    /**
     * Sets the value of the wz property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setWz(Double value) {
        this.wz = value;
    }

    /**
     * Gets the value of the roll property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRoll() {
        return roll;
    }

    /**
     * Sets the value of the roll property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRoll(Double value) {
        this.roll = value;
    }

    /**
     * Gets the value of the pitch property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getPitch() {
        return pitch;
    }

    /**
     * Sets the value of the pitch property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setPitch(Double value) {
        this.pitch = value;
    }

    /**
     * Gets the value of the yaw property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getYaw() {
        return yaw;
    }

    /**
     * Sets the value of the yaw property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setYaw(Double value) {
        this.yaw = value;
    }

}
