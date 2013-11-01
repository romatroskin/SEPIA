package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.sepia.serialize.serializer.PNMLCPNSerializer;
import de.uni.freiburg.iig.telematik.sepia.serialize.serializer.PNMLIFNetSerializer;
import de.uni.freiburg.iig.telematik.sepia.serialize.serializer.PNMLPTNetSerializer;
import de.uni.freiburg.iig.telematik.sepia.serialize.serializer.PetrifyPTNetSerializer;

public class PNSerialization {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <P extends AbstractPlace<F,S>, 
	   			   T extends AbstractTransition<F,S>, 
	   			   F extends AbstractFlowRelation<P,T,S>, 
	   			   M extends AbstractMarking<S>, 
	   			   S extends Object> 

	PNSerializer<P,T,F,M,S> 

	getSerializer(AbstractGraphicalPN<P, T, F, M, S> net, PNSerializationFormat format) throws ParameterException, SerializationException {

		// ugly unbounded wildcards as work-around for bug JDK-6932571
		Object serializer = null;
		Object petriNet = net.getPetriNet();

		switch (format) {
		case PNML:
			if (petriNet instanceof IFNet) {
				serializer = new PNMLIFNetSerializer(net);
			}
			if (petriNet instanceof AbstractCPN) {
				// CWNs fall into this category.
				serializer = new PNMLCPNSerializer(net);
			}
			if (petriNet instanceof AbstractPTNet) {
				serializer = new PNMLPTNetSerializer(net);
			}
			break;
		case PETRIFY:
			if (petriNet instanceof AbstractPTNet)
				serializer = new PetrifyPTNetSerializer(net);
			break;
		default:
			throw new SerializationException(de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException.ErrorCode.UNSUPPORTED_FORMAT, format);
		}

		if (serializer != null)
			return (PNSerializer<P, T, F, M, S>) serializer;
		else
			throw new SerializationException(de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException.ErrorCode.UNSUPPORTED_NET_TYPE, net.getClass());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <P extends AbstractPlace<F,S>, 
	   			   T extends AbstractTransition<F,S>, 
	   			   F extends AbstractFlowRelation<P,T,S>, 
	   			   M extends AbstractMarking<S>, 
	   			   S extends Object> 

	PNSerializer<P,T,F,M,S> 

	getSerializer(AbstractPetriNet<P,T,F,M,S> net, PNSerializationFormat format) throws ParameterException, SerializationException{

		// ugly unbounded wildcard as work-around for bug JDK-6932571
		Object netObject = net;

		switch(format){
		case PNML:
			if(netObject instanceof IFNet){
				return (PNSerializer<P, T, F, M, S>) new PNMLIFNetSerializer(net);
			}
			if(netObject instanceof AbstractCPN){
				// CWNs fall into this category.
				return new PNMLCPNSerializer(net);
			}
			if(netObject instanceof AbstractPTNet){
				return new PNMLPTNetSerializer(net);
				
			}
			throw new SerializationException(de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException.ErrorCode.UNSUPPORTED_NET_TYPE, net.getClass());
		case PETRIFY:
			if(net instanceof AbstractPTNet)
				return new PetrifyPTNetSerializer(net);
			throw new SerializationException(de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException.ErrorCode.UNSUPPORTED_NET_TYPE, net.getClass());
		default:
			throw new SerializationException(de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException.ErrorCode.UNSUPPORTED_FORMAT, format);
			
		}
	}
	
//	public static <P extends AbstractPlace<F,S>, 
//	   			   T extends AbstractTransition<F,S>, 
//	   			   F extends AbstractFlowRelation<P,T,S>, 
//	   			   M extends AbstractMarking<S>, 
//	   			   S extends Object> 
//	
//		   PNSerializer<P,T,F,M,S> 
//	
//		   getSerializer(NetType netType, SerializationFormat format) throws ParameterException{
//		
//		switch(netType){
//		case PTNet: 
//			switch(format){
//			case PNML: 
//				return new PTSerializer_PNML();
//			case SOLE_CARMONA:
//				return null;
//			default:
//				return null;
//			}
//		case CPN:
//			switch(format){
//			case PNML: 
//				return new PTSerializer_PNML_Old();
//			case SOLE_CARMONA:
//				throw new ParameterException(ErrorCode.INCOMPATIBILITY, String.format(incompatibilityFormat, SerializationFormat.SOLE_CARMONA, NetType.CPN));
//			default:
//				return null;
//			}
//		case CWN:
//			break;
//		case IFNet:
//			break;
//		default:
//			return null;
//		}
//		return null;
//	}
	
	public static <P extends AbstractPlace<F,S>, 
	   T extends AbstractTransition<F,S>, 
	   F extends AbstractFlowRelation<P,T,S>, 
	   M extends AbstractMarking<S>, 
	   S extends Object> 

	String 

	serialize(AbstractGraphicalPN<P,T,F,M,S> net, PNSerializationFormat format) 
			throws SerializationException, ParameterException{

		Validate.notNull(net);
		Validate.notNull(format);
		
		StringBuilder builder = new StringBuilder();
		builder.append(format.getFileFormat().getFileHeader());
		builder.append(getSerializer(net, format).serialize());
		builder.append(format.getFileFormat().getFileFooter());

		return builder.toString();
	}
	
	public static <P extends AbstractPlace<F,S>, 
	   T extends AbstractTransition<F,S>, 
	   F extends AbstractFlowRelation<P,T,S>, 
	   M extends AbstractMarking<S>, 
	   S extends Object> 

	void 

	serialize(AbstractGraphicalPN<P,T,F,M,S> net, PNSerializationFormat format, String path, String fileName) 
			throws SerializationException, ParameterException, IOException{

		Validate.notNull(net);
		Validate.notNull(format);
		
		PNSerializer<P, T, F, M, S> serializer = getSerializer(net, format);
		serializer.serialize(path, fileName);
	}
	
	public static <P extends AbstractPlace<F,S>, 
				   T extends AbstractTransition<F,S>, 
				   F extends AbstractFlowRelation<P,T,S>, 
				   M extends AbstractMarking<S>, 
				   S extends Object> 

	void 

	serialize(AbstractGraphicalPN<P,T,F,M,S> net, PNSerializationFormat format, String fileName) 
			throws SerializationException, ParameterException, IOException{

		Validate.noDirectory(fileName);
		
		File file = new File(fileName);
		serialize(net, format, FileUtils.getPath(file), FileUtils.getName(file));
	}
	
	public static <P extends AbstractPlace<F,S>, 
	   T extends AbstractTransition<F,S>, 
	   F extends AbstractFlowRelation<P,T,S>, 
	   M extends AbstractMarking<S>, 
	   S extends Object> 

	String 

	serialize(AbstractPetriNet<P,T,F,M,S> net, PNSerializationFormat format) 
			throws SerializationException, ParameterException{

		Validate.notNull(net);
		Validate.notNull(format);
		
		StringBuilder builder = new StringBuilder();
		builder.append(format.getFileFormat().getFileHeader());
		builder.append(getSerializer(net, format).serialize());
		builder.append(format.getFileFormat().getFileFooter());

		return builder.toString();
	}
	
	public static <P extends AbstractPlace<F,S>, 
	   			   T extends AbstractTransition<F,S>, 
	   			   F extends AbstractFlowRelation<P,T,S>, 
	   			   M extends AbstractMarking<S>, 
	   			   S extends Object> 
	
	void 
	
	serialize(AbstractPetriNet<P,T,F,M,S> net, PNSerializationFormat format, String path, String fileName)
			throws SerializationException, ParameterException, IOException{
		
		Validate.notNull(net);
		Validate.notNull(format);
		Validate.directory(path);
		Validate.fileName(fileName);
		
		PNSerializer<P, T, F, M, S> serializer = getSerializer(net, format);

		serializer.serialize(path, fileName);
	}
	
	public static <P extends AbstractPlace<F, S>, 
				   T extends AbstractTransition<F, S>, 
				   F extends AbstractFlowRelation<P, T, S>, 
				   M extends AbstractMarking<S>, 
				   S extends Object>

	void

	serialize(AbstractPetriNet<P, T, F, M, S> net, PNSerializationFormat format, String fileName) 
			throws SerializationException, ParameterException, IOException {

		Validate.noDirectory(fileName);
		
		File file = new File(fileName);
		serialize(net, format, FileUtils.getPath(file), FileUtils.getName(file));
	}
	


}
