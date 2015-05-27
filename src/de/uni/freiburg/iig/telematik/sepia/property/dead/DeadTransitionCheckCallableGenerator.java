package de.uni.freiburg.iig.telematik.sepia.property.dead;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.property.AbstractCallableGenerator;

public class DeadTransitionCheckCallableGenerator<	P extends AbstractPlace<F,S>, 
													T extends AbstractTransition<F,S>, 
													F extends AbstractFlowRelation<P,T,S>, 
													M extends AbstractMarking<S>, 
													S extends Object,
													X extends AbstractMarkingGraphState<M,S>,
													Y extends AbstractMarkingGraphRelation<M,X,S>> extends AbstractCallableGenerator<P,T,F,M,S>{

	private AbstractMarkingGraph<M,S,X,Y> markingGraph = null;
	
	public DeadTransitionCheckCallableGenerator(AbstractPetriNet<P,T,F,M,S> petriNet) {
		super(petriNet);
	}
	
	public AbstractMarkingGraph<M,S,X,Y> getMarkingGraph() {
		return markingGraph;
	}
	
	public void setMarkingGraph(AbstractMarkingGraph<M, S, X, Y> markingGraph) {
		this.markingGraph = markingGraph;
	}

}