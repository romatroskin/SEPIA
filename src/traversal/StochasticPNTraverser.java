package traversal;

import java.util.HashMap;
import java.util.List;

import misc.valuegeneration.StochasticValueGenerator;
import misc.valuegeneration.ValueGenerationException;
import petrinet.AbstractPetriNet;
import petrinet.AbstractTransition;
import validate.InconsistencyException;
import validate.ParameterException;
import validate.Validate;

/**
 * This flow control chooses the next transition to fire 
 * on the basis of predefined probabilities of occurrences of subsequent transition pairs.
 * 
 * @author Thomas Stocker
 *
 */
public class StochasticPNTraverser extends RandomPNTraverser {
	
	public static final int DEFAULT_TOLERANCE_DENOMINATOR = 1000;
	private HashMap<AbstractTransition<?,?>, StochasticValueGenerator<AbstractTransition<?,?>>> flowProbabilities = new HashMap<AbstractTransition<?,?>, StochasticValueGenerator<AbstractTransition<?,?>>>();
	private int toleranceDenominator;
	
	public StochasticPNTraverser(AbstractPetriNet<?,?,?,?,?> net) throws ParameterException {
		this(net, DEFAULT_TOLERANCE_DENOMINATOR);
	}

	public StochasticPNTraverser(AbstractPetriNet<?,?,?,?,?> net, int toleranceDenominator) throws ParameterException {
		super(net);
		Validate.biggerEqual(toleranceDenominator, 1, "Denominator must be >=1.");
		this.toleranceDenominator = toleranceDenominator;
	}
	
	public void addFlowProbability(String fromTransitionID, String toTransitionID, double probability) throws ParameterException{
		addFlowProbability(net.getTransition(fromTransitionID), net.getTransition(toTransitionID), probability);
	}
	
	public void addFlowProbability(AbstractTransition<?,?> fromTransition, AbstractTransition<?,?> toTransition, double probability) throws ParameterException{
		Validate.notNull(fromTransition);
		Validate.notNull(toTransition);
		Validate.inclusiveBetween(0.0, 1.0, probability);
		StochasticValueGenerator<AbstractTransition<?,?>> chooser = flowProbabilities.get(fromTransition);
		if(chooser == null){
			chooser = new StochasticValueGenerator<AbstractTransition<?,?>>(toleranceDenominator);
			flowProbabilities.put(fromTransition, chooser);
		}
		chooser.addProbability(toTransition, probability);
	}

	@Override
	public AbstractTransition<?,?> chooseNextTransition(List<AbstractTransition<?,?>> enabledTransitions) throws InconsistencyException, ParameterException{
		if(!flowProbabilities.containsKey(net.getLastFiredTransition()))
			return super.chooseNextTransition(enabledTransitions);
		if(!isValid())
			throw new InconsistencyException("At least one StochasticChooser is not valid.");
		Validate.notNull(enabledTransitions);
		Validate.noNullElements(enabledTransitions);
		
		if(enabledTransitions.isEmpty())
			return null;
		
		AbstractTransition<?,?> nextTransition = null;
		try {
			nextTransition = flowProbabilities.get(net.getLastFiredTransition()).getNextValue();
		} catch (ValueGenerationException e) {
			// Cannot happen, since all choosers are valid.
			e.printStackTrace();
		}
		if(!net.getEnabledTransitions().contains(nextTransition))
			throw new InconsistencyException("Cannot fire transition \""+nextTransition+"\" since it is not enabled.");
		return nextTransition;
	}
	
	/**
	 * Checks, if all maintained stochastic choosers are valid.
	 * @return <code>true</code> if all choosers are valid,<br>
	 * <code>false</code> otherwise.
	 * @see StochasticValueGenerator#isValid()
	 */
	public boolean isValid(){
		for(StochasticValueGenerator<AbstractTransition<?,?>> chooser: flowProbabilities.values())
			if(!chooser.isValid())
				return false;
		return true;
	}

}