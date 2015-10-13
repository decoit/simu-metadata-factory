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

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.metadata.Cardinality;

/**
 * Implemented Simu MetaData Factory. Creating SIMU specific Metadaten
 * 
 * @author Leonid Schwenke
 * 
 */
public class SimuMetadataFactoryImpl implements SimuMetaDataFactory {

	private DocumentBuilder mDocumentBuilder;

	/**
	 * Constructor
	 * 
	 * @throws ParserConfigurationException
	 */
	public SimuMetadataFactoryImpl() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		mDocumentBuilder = dbf.newDocumentBuilder();
	}

	private Document createSimuSingleElementDocument(String name,
			Cardinality card) {
		return createSingleElementDocument(SIMU_METADATA_PREFIX + ":" + name,
				SIMU_METADATA_URI, card);
	}

	/**
	 * Creating a Single Elemented Document
	 * 
	 * @param qualifiedName
	 *            Qualified Name
	 * @param uri
	 *            URI
	 * @param cardinality
	 *            Singevalue or Multivalue
	 * @return Created Document
	 */
	private Document createSingleElementDocument(String qualifiedName,
			String uri, Cardinality cardinality) {
		Document doc = mDocumentBuilder.newDocument();
		Element e = doc.createElementNS(uri, qualifiedName);
		e.setAttributeNS(null, "ifmap-cardinality", cardinality.toString());
		doc.appendChild(e);
		return doc;
	}

	/**
	 * Helper to create a new element with name elName and append it to the
	 * {@link Element} given by parent if the given value is non-null. The new
	 * {@link Element} will have {@link Text} node containing value.
	 * 
	 * @param doc
	 *            {@link Document} where parent is located in
	 * @param parent
	 *            where to append the new element
	 * @param elName
	 *            the name of the new element.
	 * @param value
	 *            the value of the {@link Text} node appended to the new
	 *            element, using toString() on the object.
	 */
	private void appendTextElementIfNotNull(Document doc, Element parent,
			String elName, Object value) {

		if (value == null) {
			return;
		}

		createAndAppendTextElementCheckNull(doc, parent, elName, value);
	}

	/**
	 * Helper to create a new element with name elName and append it to the
	 * {@link Element} given by parent. The new {@link Element} will have
	 * {@link Text} node containing value.
	 * 
	 * @param doc
	 *            {@link Document} where parent is located in
	 * @param parent
	 *            where to append the new element
	 * @param elName
	 *            the name of the new element.
	 * @param value
	 *            the value of the {@link Text} node appended to the new
	 *            element, using toString() on the object. is null
	 * @return the new {@link Element}
	 */
	private Element createAndAppendTextElementCheckNull(Document doc,
			Element parent, String elName, Object value) {

		if (doc == null || parent == null || elName == null) {
			throw new NullPointerException("bad parameters given");
		}

		if (value == null) {
			throw new NullPointerException("null is not allowed for " + elName
					+ " in " + doc.getFirstChild().getLocalName());
		}

		String valueStr = value.toString();
		if (valueStr == null) {
			throw new NullPointerException("null-string not allowed for "
					+ elName + " in " + doc.getFirstChild().getLocalName());
		}

		Element child = createAndAppendElement(doc, parent, elName);
		Text txtcElement = doc.createTextNode(valueStr);
		child.appendChild(txtcElement);
		return child;
	}

	/**
	 * Helper to create an {@link Element} without a namespace in
	 * {@link Document} doc and append it to the {@link Element} given by
	 * parent.
	 * 
	 * @param doc
	 *            the target {@link Document}
	 * @param parent
	 *            the parent {@link Element}
	 * @param elName
	 *            the name of the new {@link Element}
	 * @return the new {@link Element}
	 */
	private Element createAndAppendElement(Document doc, Element parent,
			String elName) {
		Element el = doc.createElementNS(null, elName);
		parent.appendChild(el);
		return el;
	}

	@Override
	public Document createAttackDetected(String type, String id, float severity) {
		Document doc = createSimuSingleElementDocument("attack-detected",
				Cardinality.multiValue);
		Element root = (Element) doc.getFirstChild();
		createAndAppendTextElementCheckNull(doc, root, "type", type);
		createAndAppendTextElementCheckNull(doc, root, "id", id);
		appendTextElementIfNotNull(doc, root, "severity", severity);
		return doc;
	}

	@Override
	public Document createIdentifiesAs() {
		return createSimuSingleElementDocument("identifies-as",
				Cardinality.singleValue);
	}

	@Override
	public Document createLoginFailure(CredentialType type,
			LoginFailureReason reason) {
		return createLoginFailure(type, reason, null, null);
	}

	@Override
	public Document createLoginFailure(CredentialType type,
			LoginFailureReason reason, String typeDef, String reasonDef) {
		Document doc = createSimuSingleElementDocument("login-failure",
				Cardinality.multiValue);
		Element root = (Element) doc.getFirstChild();

		createAndAppendTextElementCheckNull(doc, root, "credential-type",
				type.toString());
		createAndAppendTextElementCheckNull(doc, root, "reason",
				reason.toString());

		if (type.equals(CredentialType.OTHER)) {
			createAndAppendTextElementCheckNull(doc, root,
					"other-credential-type-definition", typeDef);
		}
		if (reason.equals(LoginFailureReason.OTHER)) {
			createAndAppendTextElementCheckNull(doc, root,
					"other-reason-type-definition", reasonDef);
		}
		return doc;
	}

	@Override
	public Document createLoginSuccess(CredentialType type) {
		return createLoginSuccess(type, null);
	}

	@Override
	public Document createLoginSuccess(CredentialType type, String typeDef) {
		Document doc = createSimuSingleElementDocument("login-success",
				Cardinality.multiValue);
		Element root = (Element) doc.getFirstChild();
		createAndAppendTextElementCheckNull(doc, root, "credential-type",
				type.toString());
		if (type.equals(CredentialType.OTHER)) {
			createAndAppendTextElementCheckNull(doc, root,
					"other-credential-type-definition", typeDef);
		}
		return doc;
	}

	@Override
	public Document createServiceIP() {
		return createSimuSingleElementDocument("service-ip",
				Cardinality.singleValue);
	}

	@Override
	public Document createServiceDiscoveredBy() {
		return createSimuSingleElementDocument("service-discovered-by",
				Cardinality.singleValue);
	}

	@Override
	public Document createDeviceDiscoveredBy() {
		return createSimuSingleElementDocument("device-discovered-by",
				Cardinality.singleValue);
	}

	// @Override
	// public Document createLatency(double value) {
	// // Text txtcElement = doc.createTextNode(valueStr);
	// // child.appendChild(txtcElement);
	// Document root = createSimuSingleElementDocument("latency",
	// Cardinality.singleValue);
	// root.setNodeValue(value+"");
	// return root;
	// }
	//
	// @Override
	// public Document createHopCount(int count) {
	// Document root = createSimuSingleElementDocument("hop-count",
	// Cardinality.singleValue);
	//
	// root.getFirstChild().setNodeValue(count + "");
	// root.appendChild(root);
	// // root.appendChild(txtcElement);
	// return root;
	// }

	@Override
	public Document createImplementationVulnerability() {
		return createSimuSingleElementDocument("implementation-vulnerability",
				Cardinality.singleValue);
	}

	@Override
	public Document createServiceImplementation() {
		return createSimuSingleElementDocument("service-implementation",
				Cardinality.singleValue);
	}

	@Override
	public Document createFileMonitored() {
		Document doc = createSimuSingleElementDocument("file-monitored",
				Cardinality.singleValue);
		return doc;
	}

	@Override
	public Document createFileChanged(String kind, String time,
			String importance) {
		Document doc = createSimuSingleElementDocument("file-status",
				Cardinality.multiValue);
		Element root = (Element) doc.getFirstChild();
		createAndAppendTextElementCheckNull(doc, root, "status", kind);
		createAndAppendTextElementCheckNull(doc, root, "discovered-time", time);
		createAndAppendTextElementCheckNull(doc, root, "importance", importance);
		return doc;
	}

	@Override
	public Identifier createVulnerability(String type, String id, float severity)
			throws MarshalException {
		Identifier vulnerability = null;

		Document doc = mDocumentBuilder.newDocument();
		Element e = doc
				.createElementNS(SIMU_IDENTIFIER_URI, SIMU_METADATA_PREFIX+":vulnerability");
		e.setAttribute("type", type);
		e.setAttribute("id", id);
		e.setAttribute("severity-score", severity + "");
		doc.appendChild(e);

		vulnerability = Identifiers.createExtendedIdentity(doc);

		return vulnerability;
	}

	@Override
	public Identifier createVulnerability(String type, String id)
			throws MarshalException {
		Identifier vulnerability = null;

		Document doc = mDocumentBuilder.newDocument();
		Element e = doc
				.createElementNS(SIMU_IDENTIFIER_URI, SIMU_METADATA_PREFIX+":vulnerability");
		e.setAttribute("type", type);
		e.setAttribute("id", id);
		doc.appendChild(e);

		vulnerability = Identifiers.createExtendedIdentity(doc);

		return vulnerability;
	}

	@Override
	public Identifier createImplementation(String name, String version,
			String localVersion, String platform) throws MarshalException {
		Identifier implementation = null;
		Document doc = mDocumentBuilder.newDocument();
		Element e = doc
				.createElementNS(SIMU_IDENTIFIER_URI, SIMU_METADATA_PREFIX+":vulnerability");
		e.setAttribute("name", name);
		e.setAttribute("version", version);
		if (localVersion != null) {
			e.setAttribute("local-version", localVersion);
		}
		if (platform != null) {
			e.setAttribute("platform", platform);
		}
		doc.appendChild(e);

		implementation = Identifiers.createExtendedIdentity(doc);

		return implementation;
	}

	@Override
	public Identifier createImplementation(String name, String version)
			throws MarshalException {
		return createImplementation(name, version, null, null);
	}

	@Override
	public Identifier createService(String type, String name, int port,
			String ad) throws MarshalException {

		Identifier service = null;
		Document doc = mDocumentBuilder.newDocument();
		Element e = doc.createElementNS(SIMU_IDENTIFIER_URI, SIMU_METADATA_PREFIX+":service");
		e.setAttribute("type", type);
		e.setAttribute("name", name);
		e.setAttribute("port", port + "");
		e.setAttribute("administrative-domain", ad);
		doc.appendChild(e);

		service = Identifiers.createExtendedIdentity(doc);

		return service;
	}

	public Identifier createFileIdentifier(String path, String ad)
			throws MarshalException {

		Identifier service = null;
		Document doc = mDocumentBuilder.newDocument();
		Element e = doc.createElementNS(SIMU_IDENTIFIER_URI, SIMU_METADATA_PREFIX+":file");
		e.setAttribute("path", path);
		e.setAttribute("administrative-domain", ad);
		doc.appendChild(e);

		service = Identifiers.createExtendedIdentity(doc);
		return service;
	}
	
	public String getXMLString(Document doc){
		String output = null;
		try {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();

			transformer.transform(new DOMSource(doc), new StreamResult(writer));

		output = writer.getBuffer().toString().replaceAll("\n|\r", "");
		} catch (TransformerException e1) {
			e1.printStackTrace();
		}
		return output;
	}
}
