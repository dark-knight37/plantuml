/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.png;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.quantization.Quantizer;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.utils.Log;

public class PngIO {

	// ::comment when WASM
	private static final String copyleft = "Generated by http://plantuml.com";
	public static boolean USE_QUANTIZATION = false;
	// ::done

	public static void write(RenderedImage image, ColorMapper mapper, SFile file, String metadata, int dpi)
			throws IOException {
		try (OutputStream os = file.createBufferedOutputStream()) {
			write(image, mapper, os, metadata, dpi);
		}
		Log.debug("File is " + file);
		Log.debug("File size " + file.length());
		if (file.length() == 0) {
			Log.error("File size is zero: " + file);
			SImageIO.write(image, "png", file);
		}
	}

	public static void write(RenderedImage image, ColorMapper mapper, OutputStream os, String metadata, int dpi)
			throws IOException {
		write(image, mapper, os, metadata, dpi, null);
	}

	private static void write(RenderedImage image, ColorMapper mapper, OutputStream os, String metadata, int dpi,
			String debugData) throws IOException {

		// ::comment when WASM
		if (USE_QUANTIZATION)
			image = Quantizer.quantizeNow(mapper, (BufferedImage) image);

		if (metadata == null)
			// ::done
			SImageIO.write(image, "png", os);
		// ::comment when WASM
		else
			PngIOMetadata.writeWithMetadata(image, os, metadata, dpi, debugData);
		// ::done

	}

//	/** writes a BufferedImage of type TYPE_INT_ARGB to PNG using PNGJ */
//	public static void writeARGB(BufferedImage bi, OutputStream os, String metadata) {
//		// if (bi.getType() != BufferedImage.TYPE_INT_ARGB)
//		// throw new PngjException("This method expects  BufferedImage.TYPE_INT_ARGB");
//		ImageInfo imi = new ImageInfo(bi.getWidth(), bi.getHeight(), 8, false);
//		PngChunkTEXT chunkText = new PngChunkTEXT(imi, "copyleft", copyleft);
//		// PngChunkTEXT chunkTextDebug = new PngChunkTEXT(imi, "debug", "debugData");
//		PngChunkITXT meta = new PngChunkITXT(imi);
//		meta.setKeyVal("plantuml", metadata);
//		meta.setCompressed(true);
//
//		PngWriter pngw = new PngWriter(os, imi);
//		pngw.setCompLevel(9);// maximum compression, not critical usually
//		// pngw.setFilterType(FilterType.FILTER_ADAPTIVE_FAST); // see what you prefer here
//		// pngw.setFilterType(FilterType.FILTER_ADAPTIVE_MEDIUM); // see what you prefer here
//		pngw.setFilterType(FilterType.FILTER_ADAPTIVE_FULL); // see what you prefer here
//		pngw.queueChunk(chunkText);
//		// // pngw.queueChunk(chunkTextDebug);
//		pngw.queueChunk(meta);
//		DataBufferInt db = ((DataBufferInt) bi.getRaster().getDataBuffer());
//		SinglePixelPackedSampleModel samplemodel = (SinglePixelPackedSampleModel) bi.getSampleModel();
//		if (db.getNumBanks() != 1)
//			throw new PngjException("This method expects one bank");
//		ImageLineInt line = new ImageLineInt(imi);
//		for (int row = 0; row < imi.rows; row++) {
//			int elem = samplemodel.getOffset(0, row);
//			for (int col = 0, j = 0; col < imi.cols; col++) {
//				int sample = db.getElem(elem++);
//				line.scanline[j++] = (sample & 0xFF0000) >> 16; // R
//				line.scanline[j++] = (sample & 0xFF00) >> 8; // G
//				line.scanline[j++] = (sample & 0xFF); // B
//				// line.scanline[j++] = (((sample & 0xFF000000) >> 24) & 0xFF); // A
//			}
//			pngw.writeRow(line, row);
//		}
//		pngw.end();
//	}

}
