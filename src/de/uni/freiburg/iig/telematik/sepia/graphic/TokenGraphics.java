package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Position;

/**
 * <p>
 * Token graphics attribute class containing the attribute token position.
 * </p>
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class TokenGraphics extends AbstractObjectGraphics {

	/** Default position */
	public static final Position DEFAULT_TOKENPOSITION = new Position();

	private Position tokenposition;

	public TokenGraphics() {
		tokenposition = DEFAULT_TOKENPOSITION;
	}

	public TokenGraphics(Position tokenposition) throws ParameterException {
		setTokenposition(tokenposition);
	}

	public Position getTokenposition() {
		return tokenposition;
	}

	public void setTokenposition(Position tokenposition) throws ParameterException {
		Validate.notNull(tokenposition);
		this.tokenposition = tokenposition;
	}

	@Override
	public boolean hasContent() {
		return tokenposition.hasContent();
	}

	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append("[");

		if (tokenposition != DEFAULT_TOKENPOSITION) {
			str.append(tokenposition);
		}

		str.append("]");

		return str.toString();
	}
}
