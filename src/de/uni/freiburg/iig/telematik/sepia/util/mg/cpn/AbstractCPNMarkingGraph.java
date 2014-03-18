package de.uni.freiburg.iig.telematik.sepia.util.mg.cpn;

import java.util.Collection;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.util.mg.abstr.AbstractMarkingGraph;

public abstract class AbstractCPNMarkingGraph<M extends AbstractCPNMarking, T extends AbstractCPNMarkingGraphState<M>> extends AbstractMarkingGraph<M, Multiset<String>, T> {

	public AbstractCPNMarkingGraph() {
		super();
	}

	public AbstractCPNMarkingGraph(Collection<String> states, Collection<String> events) throws ParameterException {
		super(states, events);
	}

	public AbstractCPNMarkingGraph(Collection<String> states) throws ParameterException {
		super(states);
	}

	public AbstractCPNMarkingGraph(String name, Collection<String> states, Collection<String> events) throws ParameterException {
		super(name, states, events);
	}

	public AbstractCPNMarkingGraph(String name, Collection<String> states) throws ParameterException {
		super(name, states);
	}

	public AbstractCPNMarkingGraph(String name) throws ParameterException {
		super(name);
	}

}
