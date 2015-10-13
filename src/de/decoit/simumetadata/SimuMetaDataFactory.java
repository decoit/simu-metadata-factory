/* 
 * Copyright (C) 2015 DECOIT GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.decoit.simumetadata;

import org.w3c.dom.Document;

import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.identifier.Identifier;

/**
 * MetaData Factory. Creating SIMU specific Metadaten
 * 
 * @author Leonid Schwenke
 * 
 */
public interface SimuMetaDataFactory {

	public final String SIMU_METADATA_PREFIX = "simu";
	public final String SIMU_METADATA_URI = "http://simu-project.de/XMLSchema/1";
	public final String SIMU_IDENTIFIER_URI = "http://simu-project.de/XMLSchema/1";

	/**
	 * Creating a identifies-as link. Assigns a identity to a access-request.
	 * 
	 * @return Simu:identifies-as Metadatum document
	 */
	public Document createIdentifiesAs();

	/**
	 * Creating a login-failure link Describes a failed login onto an service.
	 * 
	 * @param type
	 *            credential type (If you need to use credential type other, use
	 *            the other signature!)
	 * @param reason
	 *            reason why the login failed (If you need to use reason type
	 *            other, use the other signature!)
	 * @return Simu:LoginFailure Metadata document
	 */
	public Document createLoginFailure(CredentialType type,
			LoginFailureReason reason);

	/**
	 * Creating a login-failure link. Describes a failed login onto an service.
	 * 
	 * @param type
	 *            used credential type for login
	 * @param reason
	 *            reason why the login failed
	 * @param typeDef
	 *            Other-credential-type-definition: More description if
	 *            credential type is other (Optional, if type is not other)
	 * @param reasonDef
	 *            Other-reason-type-definition: More description if reason type
	 *            is other (Optional, if type not other)
	 * @return Simu:LoginFailure Metadata document
	 */
	public Document createLoginFailure(CredentialType type,
			LoginFailureReason reason, String typeDef, String reasonDef);

	/**
	 * Creating a login-success link. Describes a success login onto an service.
	 * 
	 * @param type
	 *            used credential type for login (If you want to use credential
	 *            type other, use the other signature!)
	 * @return Simu:login-success metadata document
	 */
	public Document createLoginSuccess(CredentialType type);

	/**
	 * Creating a login-success link. Describes a success login onto an service.
	 * 
	 * @param type
	 *            used credential type for login
	 * @param typeDef
	 *            Other-credential-type-definition: More description if
	 *            credential type is other (Optional, if not type is not other)
	 * @return Simu:login-success metadata document
	 */
	public Document createLoginSuccess(CredentialType type, String typeDef);

	/**
	 * Creating a Service-IP link. Describes on what ip the service is running
	 * 
	 * @return Simu:Service-IP Metadata document
	 */
	public Document createServiceIP();

	/**
	 * Creating a Service-Discovered-By link. Describes that a service is
	 * discovered by a device
	 * 
	 * @return simu:Service-Discovered-By metadata document
	 */
	public Document createServiceDiscoveredBy();

	/**
	 * Creating a Device-Discovered-By link. Describes that a device is
	 * discovered by another device
	 * 
	 * @return simu:Device-Discovered-By metadata document
	 */
	public Document createDeviceDiscoveredBy();

	/**
	 * Creating a attack-detected link. Describes a attack on a service.
	 * 
	 * @param type
	 *            Type of Vulnerability (For example CVE)
	 * @param id
	 *            ID of Vulnerability (For example a CVE-ID)
	 * @param severity
	 *            severity of the attack(Optional)
	 * @return simu:attack-detected metadatum document
	 */
	public Document createAttackDetected(String type, String id, float severity);

	/**
	 * Creating a implementation-vulnerability link. Assings a vulnerability to
	 * a service.
	 * 
	 * @return Simu:implementation-vulnerability metadata document
	 */
	public Document createImplementationVulnerability();

	/**
	 * Creating a Service-Implementation link. Assigns a service a
	 * implementation (software version)
	 * 
	 * @return Simu:Service-Implementation metadata document
	 */
	public Document createServiceImplementation();

	/**
	 * Creating a fileMonitored link between an file identifier and an device
	 * identifier
	 * 
	 * @return Simu:fileMonitored metadata document
	 */
	public Document createFileMonitored();

	/**
	 * Creating a fileStatus metadata on a file identifier
	 * 
	 * @param kind
	 *            File status
	 * @param time
	 *            discovering time
	 * @param importance
	 *            severity of status change
	 * @return Simu:file-status metadata document
	 */
	public Document createFileChanged(String kind, String time,
			String importance);

	/**
	 * Creating a Vulnerability identity. Describes a specific Vulnerability
	 * 
	 * @param type
	 *            Type of Vulnerability (For example CVE)
	 * @param id
	 *            ID of Vulnerability (For example a CVE-ID)
	 * @param severity
	 *            severity of the Vulnerability(Optional)
	 * @return Vulnerability identifier
	 * @throws MarshalException
	 */
	public Identifier createVulnerability(String type, String id, float severity)
			throws MarshalException;

	/**
	 * Creating a Vulnerability identity. Describes a specific Vulnerability
	 * 
	 * @param type
	 *            Type of Vulnerability (For example CVE)
	 * @param id
	 *            ID of Vulnerability (For example a CVE-ID)
	 * @return Vulnerability identifier
	 * @throws MarshalException
	 */
	public Identifier createVulnerability(String type, String id)
			throws MarshalException;

	/**
	 * 
	 * Creating a Implementation identifier. Specifying a software in an exact
	 * version
	 * 
	 * @param name
	 *            Name of the Software
	 * @param version
	 *            Softwareversion, defined by the developer
	 * @param localVersion
	 *            Name of the Software (Optional)
	 * @param platform
	 *            Targetplatform (Optional)
	 * @return Implementation identifier
	 * @throws MarshalException
	 */
	public Identifier createImplementation(String name, String version,
			String localVersion, String platform) throws MarshalException;

	/**
	 * Creating a Implementation Identifier. Specifying a software in an exact
	 * version
	 * 
	 * @param name
	 *            Name of the Software
	 * @param version
	 *            Softwareversion, defined by the developer
	 * @return Implementation identifier
	 * @throws MarshalException
	 */
	public Identifier createImplementation(String name, String version)
			throws MarshalException;

	/**
	 * 
	 * Creating a Service Identifier. Specifying a service with an exact
	 * version.
	 * 
	 * @param type
	 *            Type of the service
	 * @param name
	 *            Name of the service(For Example DNS Name)
	 * @param port
	 *            Access port of the Service
	 * @param ad
	 *            administrative domain, see IfMapJ Documentation
	 * @return Service identifier
	 * @throws MarshalException
	 */
	public Identifier createService(String type, String name, int port,
			String ad) throws MarshalException;

	/**
	 * Creating a File Identifier. Specifying a file or directory
	 * 
	 * @param path
	 *            Path to the file/dir
	 * @param ad
	 *            administrative domain
	 * @return File identifier
	 * @throws MarshalException
	 */
	public Identifier createFileIdentifier(String path, String ad)
			throws MarshalException;

}
