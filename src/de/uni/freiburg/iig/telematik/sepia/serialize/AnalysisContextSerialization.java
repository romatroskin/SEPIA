package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;

public class AnalysisContextSerialization {

	public static String serialize(AnalysisContext analysisContext) throws SerializationException {
		Validate.notNull(analysisContext);
		return new AnalysisContextSerializer(analysisContext).serialize();
	}

	public static void serialize(AnalysisContext analysisContext, String fileName) throws SerializationException, IOException {
		Validate.noDirectory(fileName);

		File file = new File(fileName);
		serialize(analysisContext, FileUtils.getPath(file), FileUtils.separateFileNameFromEnding(file));
	}

	public static void serialize(AnalysisContext analysisContext, String path, String fileName) throws SerializationException, IOException {
		Validate.notNull(analysisContext);
		new AnalysisContextSerializer(analysisContext).serialize(path, fileName);
	}

}
