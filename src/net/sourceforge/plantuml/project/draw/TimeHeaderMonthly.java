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
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.project.draw;

import java.util.Map;

import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.project.LoadPlanable;
import net.sourceforge.plantuml.project.core.PrintScale;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;
import net.sourceforge.plantuml.project.time.MonthYear;
import net.sourceforge.plantuml.project.timescale.TimeScaleCompressed;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class TimeHeaderMonthly extends TimeHeader {
	
	private final LoadPlanable defaultPlan;
	private final Map<Day, HColor> colorDays;
	private final Map<DayOfWeek, HColor> colorDaysOfWeek;


	protected double getTimeHeaderHeight() {
		return 16 + 13;
	}

	public double getTimeFooterHeight() {
		return 16 + 13 - 1;
	}

	public TimeHeaderMonthly(Day calendar, Day min, Day max, LoadPlanable defaultPlan, Map<Day, HColor> colorDays,
			Map<DayOfWeek, HColor> colorDaysOfWeek, Map<Day, String> nameDays) {
		super(min, max, new TimeScaleCompressed(calendar, PrintScale.MONTHLY.getCompress()));
		this.defaultPlan = defaultPlan;
		this.colorDays = colorDays;
		this.colorDaysOfWeek = colorDaysOfWeek;
	}

	class Pending {
		final double x1;
		double x2;
		final HColor color;

		Pending(HColor color, double x1, double x2) {
			this.x1 = x1;
			this.x2 = x2;
			this.color = color;
		}

		public void draw(UGraphic ug, double height) {
			drawRectangle(ug.apply(color.bg()), height, x1, x2);
		}
	}

	private void drawTextsBackground(UGraphic ug, double totalHeightWithoutFooter) {

		final double height = totalHeightWithoutFooter - getFullHeaderHeight();
		Pending pending = null;

		for (Day wink = min; wink.compareTo(max) <= 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			final double x2 = getTimeScale().getEndingPosition(wink);
			HColor back = colorDays.get(wink);
			// Day of week should be stronger than period of time (back color).
			final HColor backDoW = colorDaysOfWeek.get(wink.getDayOfWeek());
			if (backDoW != null) {
				back = backDoW;
			}
			if (back == null && defaultPlan.getLoadAt(wink) == 0) {
				back = veryLightGray;
			}
			if (back == null) {
				if (pending != null)
					pending.draw(ug, height);
				pending = null;
			} else {
				if (pending != null && pending.color.equals(back) == false) {
					pending.draw(ug, height);
					pending = null;
				}
				if (pending == null) {
					pending = new Pending(back, x1, x2);
				} else {
					pending.x2 = x2;
				}
			}
		}
	}

	@Override
	public void drawTimeHeader(final UGraphic ug, double totalHeightWithoutFooter) {
		drawTextsBackground(ug, totalHeightWithoutFooter);
		drawYears(ug);
		drawMonths(ug.apply(UTranslate.dy(16)));
		drawHline(ug, 0);
		drawHline(ug, 16);
		drawHline(ug, getFullHeaderHeight());
	}

	@Override
	public void drawTimeFooter(UGraphic ug) {
		ug = ug.apply(UTranslate.dy(3));
		drawMonths(ug);
		drawYears(ug.apply(UTranslate.dy(13)));
		drawHline(ug, 0);
		drawHline(ug, 13);
		drawHline(ug, getTimeFooterHeight());
	}

	private void drawYears(final UGraphic ug) {
		MonthYear last = null;
		double lastChange = -1;
		for (Day wink = min; wink.compareTo(max) < 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			if (last == null || wink.monthYear().year() != last.year()) {
				drawVbar(ug, x1, 0, 15);
				if (last != null) {
					printYear(ug, last, lastChange, x1);
				}
				lastChange = x1;
				last = wink.monthYear();
			}
		}
		final double x1 = getTimeScale().getStartingPosition(max.increment());
		if (x1 > lastChange) {
			printYear(ug, last, lastChange, x1);
		}
		drawVbar(ug, getTimeScale().getEndingPosition(max), 0, 15);
	}

	private void drawMonths(UGraphic ug) {
		MonthYear last = null;
		double lastChange = -1;
		for (Day wink = min; wink.compareTo(max) < 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			if (wink.monthYear().equals(last) == false) {
				drawVbar(ug, x1, 0, 12);
				if (last != null) {
					printMonth(ug, last, lastChange, x1);
				}
				lastChange = x1;
				last = wink.monthYear();
			}
		}
		final double x1 = getTimeScale().getStartingPosition(max.increment());
		if (x1 > lastChange) {
			printMonth(ug, last, lastChange, x1);
		}
		drawVbar(ug, getTimeScale().getEndingPosition(max), 0, 12);
	}

	private void printYear(UGraphic ug, MonthYear monthYear, double start, double end) {
		final TextBlock small = getTextBlock("" + monthYear.year(), 12, true, HColorUtils.BLACK);
		printCentered(ug, start, end, small);
	}

	private void printMonth(UGraphic ug, MonthYear monthYear, double start, double end) {
		final TextBlock small = getTextBlock(monthYear.shortName(), 10, false, HColorUtils.BLACK);
		final TextBlock big = getTextBlock(monthYear.longName(), 10, false, HColorUtils.BLACK);
		printCentered(ug, start, end, small, big);
	}

	private void drawVbar(UGraphic ug, double x, double y1, double y2) {
		final ULine vbar = ULine.vline(y2 - y1);
		ug.apply(HColorUtils.LIGHT_GRAY).apply(new UTranslate(x, y1)).draw(vbar);
	}

	private void printLeft(UGraphic ug, TextBlock text, double start) {
		text.drawU(ug.apply(UTranslate.dx(start)));
	}

	@Override
	public double getFullHeaderHeight() {
		return getTimeHeaderHeight();
	}

}
