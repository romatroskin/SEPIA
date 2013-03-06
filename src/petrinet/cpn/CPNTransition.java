package petrinet.cpn;

import petrinet.cpn.abstr.AbstractCPNTransition;
import validate.ParameterException;

public class CPNTransition extends AbstractCPNTransition<CPNFlowRelation> {

	public CPNTransition(String name, boolean isEmpty) throws ParameterException {
		super(name, isEmpty);
	}

	public CPNTransition(String name, String label, boolean isEmpty) throws ParameterException {
		super(name, label, isEmpty);
	}

	public CPNTransition(String name, String label) throws ParameterException {
		super(name, label);
	}

	public CPNTransition(String name) throws ParameterException {
		super(name);
	}

	@Override
	public String toPNML() {
		// TODO Auto-generated method stub
		return null;
	}
	
}