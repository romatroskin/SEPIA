package petrinet.pt.abstr;

import petrinet.AbstractMarking;
import validate.ParameterException;
import validate.Validate;

public abstract class AbstractPTMarking extends AbstractMarking<Integer> {
	
	public AbstractPTMarking() {
		super();
	}
	
	@Override
	protected void validateState(Integer state) throws ParameterException {
		super.validateState(state);
		Validate.notNegative(state);
	}

	@Override
	public void set(String place, Integer state) throws ParameterException {
		Validate.notNull(place);
		Validate.notNull(state);
		if(state <= 0){
			placeStates.remove(place);
		} else {
			super.set(place, state);
		}
	}

	@Override
	public String toString(){
		String placeFormat = "%s[%s] ";
		StringBuilder builder = new StringBuilder();
		for(String place: placeStates.keySet()){
			builder.append(String.format(placeFormat, place, placeStates.get(place)));
		}
		return builder.toString();
	}
	
}