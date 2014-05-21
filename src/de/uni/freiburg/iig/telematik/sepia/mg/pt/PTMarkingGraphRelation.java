package de.uni.freiburg.iig.telematik.sepia.mg.pt;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;

public class PTMarkingGraphRelation extends AbstractPTMarkingGraphRelation<PTMarking, PTMarkingGraphState>{

	public PTMarkingGraphRelation(PTMarkingGraphState source, PTMarkingGraphState target, Event event) {
		super(source, target, event);
	}

	public PTMarkingGraphRelation(PTMarkingGraphState source, PTMarkingGraphState target) {
		super(source, target);
	}
	
}
