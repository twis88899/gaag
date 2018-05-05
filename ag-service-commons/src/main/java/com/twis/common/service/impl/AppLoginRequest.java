package com.twis.common.service.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for appLoginRequest complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="appLoginRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="browser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cleintIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="clientOS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="loginSys" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="loginType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "appLoginRequest", propOrder = { "browser", "cleintIP",
		"clientOS", "entName", "loginSys", "loginType", "userId" })
public class AppLoginRequest {

	protected String browser;
	protected String cleintIP;
	protected String clientOS;
	protected String entName;
	protected String loginSys;
	protected String loginType;
	protected String userId;

	/**
	 * Gets the value of the browser property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBrowser() {
		return browser;
	}

	/**
	 * Sets the value of the browser property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setBrowser(String value) {
		this.browser = value;
	}

	/**
	 * Gets the value of the cleintIP property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCleintIP() {
		return cleintIP;
	}

	/**
	 * Sets the value of the cleintIP property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCleintIP(String value) {
		this.cleintIP = value;
	}

	/**
	 * Gets the value of the clientOS property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getClientOS() {
		return clientOS;
	}

	/**
	 * Sets the value of the clientOS property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setClientOS(String value) {
		this.clientOS = value;
	}

	/**
	 * Gets the value of the entName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEntName() {
		return entName;
	}

	/**
	 * Sets the value of the entName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEntName(String value) {
		this.entName = value;
	}

	/**
	 * Gets the value of the loginSys property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLoginSys() {
		return loginSys;
	}

	/**
	 * Sets the value of the loginSys property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLoginSys(String value) {
		this.loginSys = value;
	}

	/**
	 * Gets the value of the loginType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLoginType() {
		return loginType;
	}

	/**
	 * Sets the value of the loginType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLoginType(String value) {
		this.loginType = value;
	}

	/**
	 * Gets the value of the userId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the value of the userId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUserId(String value) {
		this.userId = value;
	}

}
