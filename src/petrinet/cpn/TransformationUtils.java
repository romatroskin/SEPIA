package petrinet.cpn;

import petrinet.pt.abstr.AbstractPTFlowRelation;
import petrinet.pt.abstr.AbstractPTMarking;
import petrinet.pt.abstr.AbstractPTNet;
import petrinet.pt.abstr.AbstractPTPlace;
import petrinet.pt.abstr.AbstractPTTransition;
import types.Multiset;
import validate.ParameterException;
import validate.Validate;

public class TransformationUtils {
	
	public static <P extends AbstractPTPlace<F>, T extends AbstractPTTransition<F>, F extends AbstractPTFlowRelation<P,T>, M extends AbstractPTMarking> CPN transform(AbstractPTNet<P,T,F,M> ptNet) throws ParameterException{
		Validate.notNull(ptNet);
		
		CPN cpn = new CPN();
		cpn.setName(ptNet.getName());
		// Add places of P/T-Net to CPN
		for(P place: ptNet.getPlaces()){
			cpn.addPlace(place.getName(), place.getLabel());
		}
		// Add transitions of P/T-Net to CPN
		for(T transition: ptNet.getTransitions()){
			cpn.addTransition(transition.getName(), transition.getLabel());
		}
		// Add relations of P/T-Net to CPN
		for(F relation: ptNet.getFlowRelations()){
			CPNFlowRelation cpnRelation = null;
			if(relation.getDirectionPT())
				cpnRelation = cpn.addFlowRelationPT(relation.getPlace().getName(), relation.getTransition().getName());
			else cpnRelation = cpn.addFlowRelationTP(relation.getTransition().getName(), relation.getPlace().getName());
			cpnRelation.addConstraint("black", relation.getWeight());
		}
		// Set the initial marking of the CPN
		CPNMarking cpnInitialMarking = new CPNMarking();
		AbstractPTMarking initialMarking = ptNet.getInitialMarking();
		for(String place: initialMarking.places()){
			Multiset<String> colorMap = new Multiset<String>();
			colorMap.setMultiplicity("black", initialMarking.get(place));
			cpnInitialMarking.set(place, colorMap);
		}
		cpn.setInitialMarking(cpnInitialMarking);
		
		return cpn;
	}

}