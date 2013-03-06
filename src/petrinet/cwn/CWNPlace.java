package petrinet.cwn;

import petrinet.cwn.abstr.AbstractCWNPlace;
import validate.ParameterException;

public class CWNPlace extends AbstractCWNPlace<CWNFlowRelation> {

	public CWNPlace(String name, String label) throws ParameterException {
		super(name, label);
	}

	public CWNPlace(String name) throws ParameterException {
		super(name);
	}

	@Override
	public String toPNML() {
		// TODO Auto-generated method stub
		return null;
	}

}