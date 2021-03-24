/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
 * Original Author:  Matthew Leather
 * 
 *
 */
package net.sourceforge.plantuml.ugraphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;

import net.sourceforge.plantuml.AnimatedGifEncoder;
import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.CMapData;
import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.CornerParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.SvgCharSizeHack;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.anim.AffineTransformation;
import net.sourceforge.plantuml.anim.Animation;
import net.sourceforge.plantuml.api.ImageDataComplex;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.braille.UGraphicBraille;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.eps.EpsStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.mjpeg.MJPEGGenerator;
import net.sourceforge.plantuml.security.ImageIO;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.svg.LengthAdjust;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorBackground;
import net.sourceforge.plantuml.ugraphic.color.HColorGradient;
import net.sourceforge.plantuml.ugraphic.color.HColorSimple;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;
import net.sourceforge.plantuml.ugraphic.debug.UGraphicDebug;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;
import net.sourceforge.plantuml.ugraphic.hand.UGraphicHandwritten;
import net.sourceforge.plantuml.ugraphic.html5.UGraphicHtml5;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;
import net.sourceforge.plantuml.ugraphic.tikz.UGraphicTikz;
import net.sourceforge.plantuml.ugraphic.txt.UGraphicTxt;
import net.sourceforge.plantuml.ugraphic.visio.UGraphicVdx;

public class ImageBuilder {

	private Animation animation;
	private boolean annotations;
	private HColor backcolor = HColorUtils.WHITE;
	private HColor borderColor;
	private double borderCorner;
	private UStroke borderStroke;
	private ColorMapper colorMapper = new ColorMapperIdentity();
	private int dpi = 96;
	private final FileFormatOption fileFormatOption;
	private boolean handwritten;
	private LengthAdjust lengthAdjust = LengthAdjust.defaultValue();
	private UDrawable udrawable;
	private ClockwiseTopRightBottomLeft margin = ClockwiseTopRightBottomLeft.none();
	private String metadata;
	private Scale scale;
	private long seed = 42;
	private int status = 0;
	private SvgCharSizeHack svgCharSizeHack = SvgCharSizeHack.NO_HACK;
	private boolean svgDimensionStyle = true;
	private TitledDiagram titledDiagram;
	private boolean randomPixel;
	private String warningOrError;

	public static ImageBuilder plainImageBuilder(UDrawable drawable, FileFormatOption fileFormatOption) {
		return new ImageBuilder(drawable, fileFormatOption);
	}

	public static ImageBuilder plainPngBuilder(UDrawable drawable) {
		return plainImageBuilder(drawable, new FileFormatOption(FileFormat.PNG));
	}

	// TODO do something with "index"
	public static ImageBuilder styledImageBuilder(TitledDiagram diagram, UDrawable drawable, int index,
												  FileFormatOption fileFormatOption) {
		return new ImageBuilder(drawable, fileFormatOption)
				.styled(diagram);
	}

	private ImageBuilder(UDrawable drawable, FileFormatOption fileFormatOption) {
		this.udrawable = drawable;
		this.fileFormatOption = fileFormatOption;

		if (drawable instanceof TextBlockBackcolored) {
			backcolor = ((TextBlockBackcolored) drawable).getBackcolor();
		}
	}

	public ImageBuilder annotations(boolean annotations) {
		this.annotations = annotations;
		return this;
	}

	public ImageBuilder backcolor(HColor backcolor) {
		this.backcolor = backcolor;
		return this;
	}

	public ImageBuilder blackBackcolor() {
		return backcolor(HColorUtils.BLACK);
	}

	public ImageBuilder margin(ClockwiseTopRightBottomLeft margin) {
		this.margin = margin;
		return this;
	}

	public ImageBuilder metadata(String metadata) {
		this.metadata = metadata;
		return this;
	}

	public ImageBuilder randomPixel() {
		this.randomPixel = true;
		return this;
	}

	public ImageBuilder seed(long seed) {
		this.seed = seed;
		return this;
	}

	public ImageBuilder status(int status) {
		this.status = status;
		return this;
	}

	public ImageBuilder warningOrError(String warningOrError) {
		this.warningOrError = warningOrError;
		return this;
	}

	private ImageBuilder styled(TitledDiagram diagram) {
		final ISkinParam skinParam = diagram.getSkinParam();
		animation = diagram.getAnimation();
		annotations = true;
		backcolor = calculateBackColor(diagram);
		borderColor = new Rose().getHtmlColor(skinParam, ColorParam.diagramBorder);
		borderCorner = skinParam.getRoundCorner(CornerParam.diagramBorder, null);
		borderStroke = calculateBorderStroke(borderColor, skinParam);
		colorMapper = skinParam.getColorMapper();
		dpi = skinParam.getDpi();
		handwritten = skinParam.handwritten();
		lengthAdjust = skinParam.getlengthAdjust();
		margin = calculateMargin(diagram);
		metadata = fileFormatOption.isWithMetadata() ? diagram.getMetadata() : null;
		scale = diagram.getScale();
		seed = diagram.seed();
		svgCharSizeHack = skinParam;
		svgDimensionStyle = skinParam.svgDimensionStyle();
		titledDiagram = diagram;
		warningOrError = diagram.getWarningOrError();
		return this;
	}

	public ImageData write(OutputStream os) throws IOException {
		if (annotations && titledDiagram != null) {
			if (!(udrawable instanceof TextBlock)) throw new IllegalStateException("udrawable is not a TextBlock");
			final ISkinParam skinParam = titledDiagram.getSkinParam();
			final StringBounder stringBounder = fileFormatOption.getDefaultStringBounder(skinParam);
			final AnnotatedWorker annotatedWorker = new AnnotatedWorker(titledDiagram, skinParam, stringBounder);
			udrawable = annotatedWorker.addAdd((TextBlock) udrawable);
		}

		switch (fileFormatOption.getFileFormat()) {
			case MJPEG:
				return writeImageMjpeg(os);
			case ANIMATED_GIF:
				return writeImageAnimatedGif(os);
			default:
				return writeImageInternal(fileFormatOption, os, animation);
		}
	}

	public byte[] writeByteArray() throws IOException {
		try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			write(baos);
			return baos.toByteArray();
		}
	}

	private ImageData writeImageInternal(FileFormatOption fileFormatOption, OutputStream os,
			Animation animationArg) throws IOException {
		Dimension2D dim = getFinalDimension(fileFormatOption.getDefaultStringBounder(svgCharSizeHack));
		double dx = 0;
		double dy = 0;
		if (animationArg != null) {
			final MinMax minmax = animationArg.getMinMax(dim);
			animationArg.setDimension(dim);
			dim = minmax.getDimension();
			dx = -minmax.getMinX();
			dy = -minmax.getMinY();
		}

		final UGraphic2 ug = createUGraphic(fileFormatOption, dim, animationArg, dx, dy);
		UGraphic ug2 = ug;

		if (borderStroke != null) {
			final HColor color = borderColor == null ? HColorUtils.BLACK : borderColor;
			final double width = dim.getWidth() - borderStroke.getThickness();
			final double height = dim.getHeight() - borderStroke.getThickness();
			final URectangle shape = new URectangle(width, height).rounded(borderCorner);
			ug2.apply(color).apply(borderStroke).draw(shape);
		}
		if (randomPixel) {
			drawRandomPoint(ug2);
		}
		ug2 = handwritten(ug2.apply(new UTranslate(margin.getLeft(), margin.getTop())));
		udrawable.drawU(ug2);
		ug2.flushUg();
		ug.writeImageTOBEMOVED(os, metadata, 96);
		os.flush();

		if (ug instanceof UGraphicG2d) {
			final Set<Url> urls = ((UGraphicG2d) ug).getAllUrlsEncountered();
			if (urls.size() > 0) {
				final CMapData cmap = CMapData.cmapString(urls, dpi);
				return new ImageDataComplex(dim, cmap, warningOrError, status);
			}
		}
		return createImageData(dim);
	}

	private void drawRandomPoint(UGraphic ug2) {
		final Random rnd = new Random();
		final int red = rnd.nextInt(40);
		final int green = rnd.nextInt(40);
		final int blue = rnd.nextInt(40);
		final Color c = new Color(red, green, blue);
		final HColor color = new HColorSimple(c, false);
		ug2.apply(color).apply(color.bg()).draw(new URectangle(1, 1));
	}

	private Dimension2D getFinalDimension(StringBounder stringBounder) {
		final LimitFinder limitFinder = new LimitFinder(stringBounder, true);
		udrawable.drawU(limitFinder);
		return new Dimension2DDouble(limitFinder.getMaxX() + 1 + margin.getLeft() + margin.getRight(),
				limitFinder.getMaxY() + 1 + margin.getTop() + margin.getBottom());
	}

	private Dimension2D getFinalDimension() {
		return getFinalDimension(fileFormatOption.getDefaultStringBounder(svgCharSizeHack));
	}

	private UGraphic handwritten(UGraphic ug) {
		if (handwritten) {
			return new UGraphicHandwritten(ug);
		}
//		if (OptionFlags.OMEGA_CROSSING) {
//			return new UGraphicCrossing(ug);
//		} else {
		return ug;
//		}
	}

	private ImageData writeImageMjpeg(OutputStream os) throws IOException {

		final Dimension2D dim = getFinalDimension();

		final SFile f = new SFile("c:/tmp.avi");

		final int nbframe = 100;

		final MJPEGGenerator m = new MJPEGGenerator(f, getAviImage(null).getWidth(null),
				getAviImage(null).getHeight(null), 12.0, nbframe);
		for (int i = 0; i < nbframe; i++) {
			// AffineTransform at = AffineTransform.getRotateInstance(1.0);
			AffineTransform at = AffineTransform.getTranslateInstance(dim.getWidth() / 2, dim.getHeight() / 2);
			at.rotate(90.0 * Math.PI / 180.0 * i / 100);
			at.translate(-dim.getWidth() / 2, -dim.getHeight() / 2);
			// final AffineTransform at = AffineTransform.getTranslateInstance(i, 0);
			// final ImageIcon ii = new ImageIcon(getAviImage(at));
			// m.addImage(ii.getImage());
			throw new UnsupportedOperationException();
		}
		m.finishAVI();

		FileUtils.copyToStream(f, os);

		return createImageData(dim);
	}

	private ImageData writeImageAnimatedGif(OutputStream os) throws IOException {

		final Dimension2D dim = getFinalDimension();

		final MinMax minmax = animation.getMinMax(dim);

		final AnimatedGifEncoder e = new AnimatedGifEncoder();
		// e.setQuality(1);
		e.setRepeat(0);
		e.start(os);
		// e.setDelay(1000); // 1 frame per sec
		// e.setDelay(100); // 10 frame per sec
		e.setDelay(60); // 16 frame per sec
		// e.setDelay(50); // 20 frame per sec

		for (AffineTransformation at : animation.getAll()) {
			final ImageIcon ii = new ImageIcon(getAviImage(at));
			e.addFrame((BufferedImage) ii.getImage());
		}
		e.finish();
		return createImageData(dim);
	}

	private Image getAviImage(AffineTransformation affineTransform) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		writeImageInternal(new FileFormatOption(FileFormat.PNG), baos, Animation.singleton(affineTransform));
		baos.close();

		final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		final Image im = ImageIO.read(bais);
		bais.close();
		return im;
	}

	private UGraphic2 createUGraphic(FileFormatOption option, final Dimension2D dim, Animation animationArg,
			double dx, double dy) {
		final double scaleFactor = (scale == null ? 1 : scale.getScale(dim.getWidth(), dim.getHeight())) * dpi / 96.0;
		switch (option.getFileFormat()) {
		case PNG:
			return createUGraphicPNG(colorMapper, scaleFactor, dim, backcolor, animationArg, dx, dy,
					option.getWatermark());
		case SVG:
			return createUGraphicSVG(colorMapper, scaleFactor, dim, backcolor, option.getSvgLinkTarget(),
					option.getHoverColor(), option.getPreserveAspectRatio(), lengthAdjust);
		case EPS:
			return new UGraphicEps(colorMapper, EpsStrategy.getDefault2());
		case EPS_TEXT:
			return new UGraphicEps(colorMapper, EpsStrategy.WITH_MACRO_AND_TEXT);
		case HTML5:
			return new UGraphicHtml5(colorMapper);
		case VDX:
			return new UGraphicVdx(colorMapper);
		case LATEX:
			return new UGraphicTikz(colorMapper, scaleFactor, true, option.getTikzFontDistortion());
		case LATEX_NO_PREAMBLE:
			return new UGraphicTikz(colorMapper, scaleFactor, false, option.getTikzFontDistortion());
		case BRAILLE_PNG:
			return new UGraphicBraille(colorMapper);
		case UTXT:
		case ATXT:
			return new UGraphicTxt();
		case DEBUG:
			return new UGraphicDebug();
		default:
			throw new UnsupportedOperationException(option.getFileFormat().toString());
		}
	}

	private UGraphic2 createUGraphicSVG(ColorMapper colorMapper, double scaleFactor, Dimension2D dim,
			final HColor suggested, String svgLinkTarget, String hover, String preserveAspectRatio,
			LengthAdjust lengthAdjust) {
		HColor backColor = HColorUtils.WHITE;
		if (suggested instanceof HColorSimple) {
			backColor = suggested;
		}
		final UGraphicSvg ug;
		if (suggested instanceof HColorGradient) {
			ug = new UGraphicSvg(svgDimensionStyle, dim, colorMapper, (HColorGradient) suggested, false, scaleFactor,
					svgLinkTarget, hover, seed, preserveAspectRatio, svgCharSizeHack, lengthAdjust);
		} else if (backColor == null || colorMapper.toColor(backColor).equals(Color.WHITE)) {
			ug = new UGraphicSvg(svgDimensionStyle, dim, colorMapper, false, scaleFactor, svgLinkTarget, hover, seed,
					preserveAspectRatio, svgCharSizeHack, lengthAdjust);
		} else {
			final String tmp = colorMapper.toSvg(backColor);
			ug = new UGraphicSvg(svgDimensionStyle, dim, colorMapper, tmp, false, scaleFactor, svgLinkTarget, hover, seed,
					preserveAspectRatio, svgCharSizeHack, lengthAdjust);
		}
		return ug;

	}

	private UGraphic2 createUGraphicPNG(ColorMapper colorMapper, double scaleFactor, final Dimension2D dim,
			HColor mybackcolor, Animation affineTransforms, double dx, double dy, String watermark) {
		Color backColor = Color.WHITE;
		if (mybackcolor instanceof HColorSimple) {
			backColor = colorMapper.toColor(mybackcolor);
		} else if (mybackcolor instanceof HColorBackground) {
			backColor = null;
		}

		final EmptyImageBuilder builder = new EmptyImageBuilder(watermark, (int) (dim.getWidth() * scaleFactor),
				(int) (dim.getHeight() * scaleFactor), backColor);
		final Graphics2D graphics2D = builder.getGraphics2D();

		final UGraphicG2d ug = new UGraphicG2d(colorMapper, graphics2D, scaleFactor,
				affineTransforms == null ? null : affineTransforms.getFirst(), dx, dy);
		ug.setBufferedImage(builder.getBufferedImage());
		final BufferedImage im = ((UGraphicG2d) ug).getBufferedImage();
		if (mybackcolor instanceof HColorGradient) {
			ug.apply(mybackcolor.bg()).draw(new URectangle(im.getWidth() / scaleFactor, im.getHeight() / scaleFactor));
		}

		return ug;
	}

	private static HColor calculateBackColor(TitledDiagram diagram) {
		if (UseStyle.useBetaStyle()) {
			final Style style = StyleSignature
					.of(SName.root, SName.document, diagram.getUmlDiagramType().getStyleName())
					.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());

			HColor backgroundColor = style.value(PName.BackGroundColor)
					.asColor(diagram.getSkinParam().getIHtmlColorSet());
			if (backgroundColor == null) {
				backgroundColor = HColorUtils.transparent();
			}
			return backgroundColor;

		}
		return diagram.getSkinParam().getBackgroundColor(false);
	}

	private static UStroke calculateBorderStroke(HColor borderColor, ISkinParam skinParam) {
		final UStroke thickness = skinParam.getThickness(LineParam.diagramBorder, null);
		return (thickness == null && borderColor != null) ? new UStroke() : thickness;
	}

	private static ClockwiseTopRightBottomLeft calculateMargin(TitledDiagram diagram) {
		if (UseStyle.useBetaStyle()) {
			final Style style = StyleSignature.of(SName.root, SName.document)
					.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());
			if (style.hasValue(PName.Margin)) {
				return style.getMargin();
			}
		}
		return diagram.getDefaultMargins();
	}

	private ImageDataSimple createImageData(Dimension2D dim) {
		return new ImageDataSimple(dim, status);
	}

}
