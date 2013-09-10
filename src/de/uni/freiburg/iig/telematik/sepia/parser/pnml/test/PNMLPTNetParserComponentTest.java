package de.uni.freiburg.iig.telematik.sepia.parser.pnml.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException;
import de.uni.freiburg.iig.telematik.sepia.test.TestResourceFile;

/**
 * <p>
 * Component unit tests for the P/T-net PNML parser.
 * </p>
 * <p>
 * Because of the socket connections needed to get the PNTD RelaxNG schemes from remote, these tests can take a while.
 * </p>
 * 
 * @author Adrian Lange
 */
public class PNMLPTNetParserComponentTest {

	/**
	 * Aborts tests after the specified time in milliseconds for the case of connection issues. Especially needed for the validating tests which create a socket
	 * connection to a remote server to get the needed RelaxNG schemes.
	 */
	public static final int VALIDATION_TIMEOUT = 5000;

	/** Project intern path to the test resources without leading slash */
	public static final String PTNET_TEST_RESOURCES_PATH = "test-resources/pnml/pt/";

	/** Valid P/T-net */
	@Rule
	public TestResourceFile PTnetResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet.pnml");
	/** P/T-net without type attribute */
	@Rule
	public TestResourceFile PTnetWithoutTypeResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-noTypeAttribute.pnml");
	/** P/T-net without type attribute */
	@Rule
	public TestResourceFile PTnetWithUnknownTypeResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-unknownTypeAttribute.pnml");
	/** P/T-net without page tags */
	@Rule
	public TestResourceFile PTnetWithoutPageTagsResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-noPageTags.pnml");
	/** P/T-net without page tags */
	@Rule
	public TestResourceFile PTnetWithMultiplePageTagsResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-multiplePageTags.pnml");
	/** P/T-net without a place ID */
	@Rule
	public TestResourceFile PTnetNoPlaceIDResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-noPlaceID.pnml");
	/** P/T-net missing a place name */
	@Rule
	public TestResourceFile PTnetNoPlaceNameResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-noPlaceName.pnml");
	/** P/T-net with a place with an invalid initial marking */
	@Rule
	public TestResourceFile PTnetInvalidInitialMarkingResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-invalidPlaceInitialMarking.pnml");
	/** P/T-net with a place with invalid graphical information */
	@Rule
	public TestResourceFile PTnetInvalidPlaceGraphicsResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-invalidPlaceGraphics.pnml");
	/** P/T-net with a place with invalid graphical information */
	@Rule
	public TestResourceFile PTnetInvalidPlaceCapacityResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-invalidPlaceCapacity.pnml");

	/**
	 * Test if all sample files of the P/T-net exist.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void samplePTNetFilesExist() throws Exception {
		assertTrue(PTnetResource.getFile().exists());
		assertTrue(PTnetWithoutTypeResource.getFile().exists());
		assertTrue(PTnetWithUnknownTypeResource.getFile().exists());
		assertTrue(PTnetWithoutPageTagsResource.getFile().exists());
		assertTrue(PTnetWithMultiplePageTagsResource.getFile().exists());
		assertTrue(PTnetNoPlaceIDResource.getFile().exists());
		assertTrue(PTnetNoPlaceNameResource.getFile().exists());
		assertTrue(PTnetInvalidInitialMarkingResource.getFile().exists());
		assertTrue(PTnetInvalidPlaceGraphicsResource.getFile().exists());
		assertTrue(PTnetInvalidPlaceCapacityResource.getFile().exists());
	}

	/**
	 * Valid P/T-net with validation, where no exception should be thrown. Performs also some shallow tests to check the correct amount of places, transitions,
	 * arcs and graphical information.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validPTnetWithValidation() {
		AbstractGraphicalPN<?, ?, ?, ?, ?> net = null;
		try {
			net = new PNMLParser().parse(PTnetResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
		assertTrue(net instanceof GraphicalPTNet);

		assertEquals(2, net.getPetriNet().getPlaces().size());
		assertEquals(1, net.getPetriNet().getTransitions().size());
		assertEquals(2, net.getPetriNet().getFlowRelations().size());
		assertEquals(2, net.getPetriNetGraphics().getPlaceGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTransitionGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getArcGraphics().size());
		assertEquals(2, net.getPetriNetGraphics().getArcAnnotationGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTokenGraphics().size());
	}

	/**
	 * Valid P/T-net without validation, where no exception should be thrown. Performs also some shallow tests to check the correct amount of places,
	 * transitions, arcs and graphical information.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validPTnetWithoutValidation() {
		AbstractGraphicalPN<?, ?, ?, ?, ?> net = null;
		try {
			net = new PNMLParser().parse(PTnetResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
		assertTrue(net instanceof GraphicalPTNet);

		assertEquals(2, net.getPetriNet().getPlaces().size());
		assertEquals(1, net.getPetriNet().getTransitions().size());
		assertEquals(2, net.getPetriNet().getFlowRelations().size());
		assertEquals(2, net.getPetriNetGraphics().getPlaceGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTransitionGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getArcGraphics().size());
		assertEquals(2, net.getPetriNetGraphics().getArcAnnotationGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTokenGraphics().size());
	}

	/*
	 * TYPE ATTRIBUTE TESTS
	 */

	/**
	 * P/T-net without type attribute but requiring a valid one. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypePTnetRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithoutTypeResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net without type attribute but requiring a valid one. Retrieving the net type should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypePTnetRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithoutTypeResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net without type attribute and not requiring a valid one. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypePTnetNotRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithoutTypeResource.getFile(), false, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net without type attribute and not requiring a valid one. Retrieving the net type should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypePTnetNotRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithoutTypeResource.getFile(), false, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net with unknown type attribute but requiring a known one. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void unknownTypePTnetRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithUnknownTypeResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net with unknown type attribute but requiring a known one. Retrieving the net type should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void unknownTypePTnetRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithUnknownTypeResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net with unknown type attribute and not requiring a known one. Validating the net type should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void unknownTypePTnetNotRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithUnknownTypeResource.getFile(), false, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net with unknown type attribute and not requiring a known one. No exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void unknownTypePTnetNotRequiringTypeWithoutValidation() {
		AbstractGraphicalPN<?, ?, ?, ?, ?> net = null;
		try {
			net = new PNMLParser().parse(PTnetWithUnknownTypeResource.getFile(), false, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
		// Unknown net should be interpreted as P/T-net
		assertTrue(net instanceof GraphicalPTNet);
	}

	/*
	 * PAGE TAG TESTS
	 */

	/**
	 * P/T-net without page tags. The validation should throw an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noPageTagsPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithoutPageTagsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net without page tags and no validation. As the page tags are ignored by the parser, no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noPageTagsPTnetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithoutPageTagsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net with multiple page tags. Although this is valid, the parser can't handle it and throws an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void multiplePageTagsPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithMultiplePageTagsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net with multiple page tags without validation. The parser can't handle this and throws an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void multiplePageTagsPTnetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithMultiplePageTagsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * PLACE TESTS
	 */

	/**
	 * P/T-net with a missing place ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noPlaceIDPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetNoPlaceIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net with a missing place ID. The parser should throw an exception while reading the flow relations.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noPlaceIDPTnetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetNoPlaceIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net with a missing place name. As a place name is not necessary, no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noPlacenamePTnetWithValidation() {
		try {
			new PNMLParser().parse(PTnetNoPlaceNameResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net with a missing place name. As a place name is not necessary, no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noPlacenamePTnetWithoutValidation() {
		try {
			new PNMLParser().parse(PTnetNoPlaceNameResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net with an invalid place initial marking. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidInitialMarkingPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetInvalidInitialMarkingResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net with an invalid place initial marking. The parser should throw an exception while reading the places.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidInitialMarkingPTnetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetInvalidInitialMarkingResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net with a invalid place graphics. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidPlaceGraphicsPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetInvalidPlaceGraphicsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net with a invalid place graphics. The invalid offset tag should be ignored.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void invalidPlaceGraphicsPTnetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetInvalidPlaceGraphicsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
		// A NodeGraphics object can't contain offsets. They are just ignored.
	}

	/**
	 * P/T-net with a place capacity of zero. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidPlaceCapacityPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetInvalidPlaceCapacityResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * P/T-net with a place capacity of zero. The parser should throw an exception while reading the places.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidPlaceCapacityPTnetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetInvalidPlaceCapacityResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * TRANSITION TESTS
	 */

	// TODO no transition id
	// TODO no transition name
	// TODO no transition graphics
	// TODO invalid transition graphics

	/*
	 * ARC TESTS
	 */

	// TODO no arc id
	// TODO no source id
	// TODO no target id
	// TODO no arc inscription
	// TODO negative arc inscription
	// TODO no arc graphics
	// TODO invalid arc graphics

	/*
	 * CAPACITY TESTS
	 */

	// TODO Bounded P/T net isBounded
	// TODO Unbounded P/T net isBounded
}
