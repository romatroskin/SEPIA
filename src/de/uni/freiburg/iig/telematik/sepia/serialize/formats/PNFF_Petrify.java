package de.uni.freiburg.iig.telematik.sepia.serialize.formats;

import java.nio.charset.Charset;

import de.invation.code.toval.file.FileFormat;


public class PNFF_Petrify  extends FileFormat{

	@Override
	public String getFileExtension() {
		return "pn";
	}
	
	@Override
	public String getFileFooter() {
		return ".end";
	}

	@Override
	public String getName() {
		return "Petrify";
	}

	@Override
	public boolean supportsCharset(Charset charset) {
		return true;
	}

}
