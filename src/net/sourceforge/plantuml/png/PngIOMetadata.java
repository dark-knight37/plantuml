/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 4231 $
 *
 */
package net.sourceforge.plantuml.png;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

import net.sourceforge.plantuml.Log;

import com.sun.imageio.plugins.png.PNGMetadata;

public class PngIOMetadata {

	private static final String copyleft = "Generated by http://plantuml.sourceforge.net";

	public static void writeWithMetadata(RenderedImage image, OutputStream os, String metadata, int dpi) throws IOException {

		final ImageWriter imagewriter = getImageWriter();
		Log.debug("PngIOMetadata imagewriter=" + imagewriter);

		imagewriter.setOutput(ImageIO.createImageOutputStream(os));

		// Create & populate metadata
		final PNGMetadata pngMetadata = new PNGMetadata();

		if (dpi != 96) {
			pngMetadata.pHYs_present = true;
			pngMetadata.pHYs_unitSpecifier = PNGMetadata.PHYS_UNIT_METER;
			pngMetadata.pHYs_pixelsPerUnitXAxis = (int) Math.round(dpi / .0254 + 0.5);
			pngMetadata.pHYs_pixelsPerUnitYAxis = pngMetadata.pHYs_pixelsPerUnitXAxis;
		}

		if (metadata != null) {
			pngMetadata.zTXt_keyword.add("plantuml");
			pngMetadata.zTXt_compressionMethod.add(new Integer(0));
			pngMetadata.zTXt_text.add(metadata);
			// System.err.println("metadata=" + metadata);
			// if (metadata.equals("Generated by
			// http://plantuml.sourceforge.net")) {
			// throw new IllegalArgumentException();
			// }
		}

		pngMetadata.tEXt_keyword.add("copyleft");
		pngMetadata.tEXt_text.add(copyleft);

		Log.debug("PngIOMetadata pngMetadata=" + pngMetadata);

		// Render the PNG to file
		final IIOImage iioImage = new IIOImage(image, null, pngMetadata);
		Log.debug("PngIOMetadata iioImage=" + iioImage);
		// Attach the metadata
		imagewriter.write(null, iioImage, null);
		Log.debug("PngIOMetadata before flush");
		os.flush();
		Log.debug("PngIOMetadata after flush");
	}

	private static ImageWriter getImageWriter() {
		final Iterator<ImageWriter> iterator = ImageIO.getImageWritersBySuffix("png");
		for (final Iterator<ImageWriter> it = ImageIO.getImageWritersBySuffix("png"); it.hasNext();) {
			final ImageWriter imagewriter = iterator.next();
			Log.debug("PngIOMetadata countImageWriter = " + it.next());
			if (imagewriter.getClass().getName().equals("com.sun.imageio.plugins.png.PNGImageWriter")) {
				Log.debug("PngIOMetadata Found sun PNGImageWriter");
				return imagewriter;
			}

		}
		Log.debug("Using first one");
		return ImageIO.getImageWritersBySuffix("png").next();
	}

}
