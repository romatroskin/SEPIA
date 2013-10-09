package de.uni.freiburg.iig.telematik.sepia.parser.pnml;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException.ErrorCode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;

/**
 * <p>
 * Parser for PT nets. The process of parsing a PNML file is the following:
 * </p>
 * <ol>
 * <li>Check if the document is well-formed XML.</li>
 * <li>Determine net type by reading the net type URI (get type from URINettypeRefs table).</li>
 * <li>Read the net type specific net components. To avoid violating a constraint, the objects must be read in multiple iterations:
 * <ol>
 * <li>Read nodes (places and transitions) with their marking and labeling.</li>
 * <li>Read edges (arcs) with their annotations and specific starting and ending nodes.</li>
 * </ol>
 * </li>
 * </ol>
 * 
 * @author Adrian Lange
 */
public class PNMLPTNetParser extends AbstractPNMLParser<PTPlace, PTTransition, PTFlowRelation, PTMarking, Integer> {

	public GraphicalPTNet parse(Document pnmlDocument) throws ParameterException, ParserException {

		net = new PTNet();
		graphics = new PTGraphics();

		// Check if the net is defined on a single page
		NodeList pageNodes = pnmlDocument.getElementsByTagName("page");
		if (pageNodes.getLength() > 1)
			throw new PNMLParserException(ErrorCode.NOT_ON_ONE_PAGE);

		// Read places and transitions
		NodeList placeNodes = pnmlDocument.getElementsByTagName("place");
		readPlaces(placeNodes);
		NodeList transitionNodes = pnmlDocument.getElementsByTagName("transition");
		readTransitions(transitionNodes);
		// Read arcs
		NodeList arcNodes = pnmlDocument.getElementsByTagName("arc");
		readArcs(arcNodes);

		return new GraphicalPTNet(net, graphics);
	}

	/**
	 * Reads all arcs given in a list of DOM nodes and adds them to the {@link GraphicalPTNet}.
	 */
	protected void readArcs(NodeList arcNodes) throws ParameterException, ParserException {
		// read and add each arc/flow relation
		for (int a = 0; a < arcNodes.getLength(); a++) {
			if (arcNodes.item(a).getNodeType() == Node.ELEMENT_NODE) {
				Element arc = (Element) arcNodes.item(a);
				// ID must be available in a valid net
				String sourceName = arc.getAttribute("source");
				String targetName = arc.getAttribute("target");

				// get inscription
				int inscription = 1;
				NodeList arcInscriptions = arc.getElementsByTagName("inscription");
				if (arcInscriptions.getLength() == 1) {
					String inscriptionStr = readText(arcInscriptions.item(0));
					if (inscriptionStr != null)
						inscription = Integer.parseInt(inscriptionStr);
				}

				PTFlowRelation flowRelation;
				// if PT relation
				if (net.getPlace(sourceName) != null && net.getTransition(targetName) != null) {
					flowRelation = ((PTNet) net).addFlowRelationPT(sourceName, targetName, inscription);
				}
				// if TP relation
				else if (net.getPlace(targetName) != null && net.getTransition(sourceName) != null) {
					flowRelation = ((PTNet) net).addFlowRelationTP(sourceName, targetName, inscription);
				} else {
					throw new PNMLParserException(ErrorCode.INVALID_FLOW_RELATION, "Couldn't determine flow relation between \"" + sourceName + "\" and \"" + targetName + "\".");
				}

				// Check if there's a label
				String arcName = arc.getAttribute("id");
				if (arcName != null && arcName.length() > 0)
					flowRelation.setName(arcName);

				// annotation graphics
				if (arcInscriptions.getLength() == 1) {
					AnnotationGraphics arcAnnotationGraphics = readAnnotationGraphicsElement((Element) arcInscriptions.item(0));
					if (arcAnnotationGraphics != null)
						graphics.getArcAnnotationGraphics().put(flowRelation.getName(), arcAnnotationGraphics);
				}

				// get graphical information
				ArcGraphics arcGraphics = readArcGraphicsElement(arc);
				if (arcGraphics != null)
					graphics.getArcGraphics().put(flowRelation.getName(), arcGraphics);
			}
		}
	}

	/**
	 * Reads all places given in a list of DOM nodes and adds them to the {@link GraphicalPTNet}.
	 */
	protected void readPlaces(NodeList placeNodes) throws ParameterException, ParserException {
		// add each place
		PTMarking marking = new PTMarking();
		for (int p = 0; p < placeNodes.getLength(); p++) {
			Node placeNode = placeNodes.item(p);
			if (placeNode.getNodeType() == Node.ELEMENT_NODE) {
				Element place = (Element) placeNode;
				// ID must be available in a valid net
				String placeName = place.getAttribute("id");
				String placeLabel = null;
				// Check if there's a label
				NodeList placeLabels = place.getElementsByTagName("name");
				if (placeLabels.getLength() == 1) {
					if (readText(placeLabels.item(0)) != null) {
						placeLabel = readText(placeLabels.item(0));
					}
					// annotation graphics
					AnnotationGraphics placeAnnotationGraphics = readAnnotationGraphicsElement((Element) placeLabels.item(0));
					if (placeAnnotationGraphics != null)
						graphics.getPlaceLabelAnnotationGraphics().put(placeName, placeAnnotationGraphics);
				} else {
					placeLabel = placeName;
				}
				net.addPlace(placeName, placeLabel);

				// Read marking with graphical information
				NodeList placeInitialMarkings = place.getElementsByTagName("initialMarking");
				if (placeInitialMarkings.getLength() == 1) {
					int initialMarking = readInitialMarking(placeInitialMarkings.item(0));
					if (initialMarking < 0) {
						throw new PNMLParserException(ErrorCode.VALIDATION_FAILED, "Place initial markings must not be a negative number.");
					} else if (initialMarking > 0) {
						marking.set(placeName, initialMarking);

						// graphics
						NodeList graphicsList = ((Element) placeInitialMarkings.item(0)).getElementsByTagName("tokenposition");
						if (graphicsList.getLength() > 0) {
							Set<TokenGraphics> tokenGraphics = new HashSet<TokenGraphics>();
							for (int tp = 0; tp < graphicsList.getLength(); tp++) {
								Element tokenPos = (Element) graphicsList.item(tp);
								TokenGraphics tokenGraphic = new TokenGraphics();
								tokenGraphic.setTokenposition(readTokenPosition(tokenPos));
								tokenGraphics.add(tokenGraphic);
							}
							graphics.getTokenGraphics().put(placeName, tokenGraphics);
						}
					}
				}

				// Read and add place capacities
				NodeList placeCapacitiesList = place.getElementsByTagName("capacity");
				for (int i = 0; i < placeCapacitiesList.getLength(); i++) {
					// If node is element node and is direct child of the place node
					if (placeCapacitiesList.item(i).getNodeType() == Node.ELEMENT_NODE && placeCapacitiesList.item(i).getParentNode().equals(place)) {
						Element placeCapacitiesElement = (Element) placeCapacitiesList.item(i);
						Integer placeCapacity = readPlaceCapacity(placeCapacitiesElement);
						// add place capacity
						if (placeCapacity != null) {
							PTPlace currentPlace = net.getPlace(placeName);
							currentPlace.setCapacity(placeCapacity);
						}
					}
				}

				// Read graphical information
				NodeGraphics placeGraphics = readNodeGraphicsElement(place);
				if (placeGraphics != null)
					graphics.getPlaceGraphics().put(placeName, placeGraphics);
			}
		}
		net.setInitialMarking(marking);
	}

	/**
	 * Reads all transitions given in a list of DOM nodes and adds them to the {@link GraphicalPTNet}.
	 */
	protected void readTransitions(NodeList transitionNodes) throws ParameterException, ParserException {
		// read and add each transition
		for (int t = 0; t < transitionNodes.getLength(); t++) {
			if (transitionNodes.item(t).getNodeType() == Node.ELEMENT_NODE) {
				Element transition = (Element) transitionNodes.item(t);
				// ID must be available in a valid net
				String transitionName = transition.getAttribute("id");
				String transitionLabel = null;
				// Check if there's a label
				NodeList transitionLabels = transition.getElementsByTagName("name");
				if (transitionLabels.getLength() == 1) {
					transitionLabel = readText(transitionLabels.item(0));
					if (transitionLabel != null && transitionLabel.length() == 0)
						transitionLabel = null;
					// annotation graphics
					AnnotationGraphics transitionLabelAnnotationGraphics = readAnnotationGraphicsElement((Element) transitionLabels.item(0));
					if (transitionLabelAnnotationGraphics != null)
						graphics.getTransitionLabelAnnotationGraphics().put(transitionName, transitionLabelAnnotationGraphics);
				}
				if (transitionLabel != null)
					net.addTransition(transitionName, transitionLabel);
				else
					net.addTransition(transitionName);

				// read graphical information
				NodeGraphics transitionGraphics = readNodeGraphicsElement(transition);
				if (transitionGraphics != null)
					graphics.getTransitionGraphics().put(transitionName, transitionGraphics);

				// transitions have no inscription/marking
			}
		}
	}
}
