package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.soundness;

import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;

public class ThreadedCWNSoundnessChecker<P extends AbstractCPNPlace<F>,
										 T extends AbstractCPNTransition<F>, 
										 F extends AbstractCPNFlowRelation<P,T>, 
										 M extends AbstractCPNMarking> 

										extends AbstractThreadedPNPropertyChecker<P,T,F,M,Multiset<String>,
																		  		  CWNProperties,
																		  		  CWNProperties,
																		  		  CWNException>{
	
	public ThreadedCWNSoundnessChecker(CWNSoundnessCheckingCallableGenerator<P,T,F,M> generator){
		super(generator);
	}
	
	@Override
	protected CWNSoundnessCheckingCallableGenerator<P,T,F,M> getGenerator() {
		return (CWNSoundnessCheckingCallableGenerator<P,T,F,M>) super.getGenerator();
	}
	
	@Override
	public AbstractCallable<CWNProperties> createCallable() {
		return new CWNSoundnessCheckingCallable<P,T,F,M>(getGenerator());
	}

	@Override
	protected CWNException createException(String message, Throwable cause) {
		return new CWNException(message, cause);
	}

	@Override
	protected CWNException executionException(ExecutionException e) {
		if(e.getCause() instanceof CWNException)
			return (CWNException) e.getCause();
		return new CWNException("Exception during CWN soundness check", e);
	}

	@Override
	protected CWNProperties getResultFromCallableResult(CWNProperties callableResult) throws Exception {
		return callableResult;
	}
	
	

}
