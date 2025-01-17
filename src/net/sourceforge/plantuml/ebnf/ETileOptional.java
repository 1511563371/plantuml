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
package net.sourceforge.plantuml.ebnf;

import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColors;

public class ETileOptional extends ETile {

	private final double deltax;
	private final double deltay = 16;
	private final ETile orig;

	private final boolean specialForAlternate;

	public ETileOptional(ETile orig) {
		this.orig = orig;
		this.specialForAlternate = orig instanceof ETileAlternation;
		this.deltax = this.specialForAlternate ? 0 : 15;
	}

	@Override
	public double getH1(StringBounder stringBounder) {
		return deltay + orig.getH1(stringBounder);
	}

	@Override
	public double getH2(StringBounder stringBounder) {
		return orig.getH2(stringBounder);
	}

	@Override
	public double getWidth(StringBounder stringBounder) {
		return orig.getWidth(stringBounder) + 2 * deltax;
	}

	@Override
	public void drawU(UGraphic ug) {
		final XDimension2D fullDim = calculateDimension(ug.getStringBounder());
		if (TRACE)
			ug.apply(HColors.BLUE).draw(new URectangle(fullDim));

		final double linePos = getH1(ug.getStringBounder());

		final double posA = specialForAlternate ? 12 : 8;
		final double corner = specialForAlternate ? 12 : 8;
		final double posB = fullDim.getWidth() - posA;

		CornerCurved.createSE(corner).drawU(ug.apply(new UTranslate(posA, linePos)));
		drawVline(ug, posA, corner + 5, linePos - corner);
		CornerCurved.createNW(corner).drawU(ug.apply(new UTranslate(posA, 5)));

		drawHlineDirected(ug, 5, posA + corner, posB - corner, 0.4);

		CornerCurved.createSW(corner).drawU(ug.apply(new UTranslate(posB, linePos)));
		drawVline(ug, posB, corner + 5, linePos - corner);
		CornerCurved.createNE(corner).drawU(ug.apply(new UTranslate(posB, 5)));

		drawHline(ug, linePos, 0, deltax);
		drawHline(ug, linePos, fullDim.getWidth() - deltax, fullDim.getWidth());

		orig.drawU(ug.apply(new UTranslate(deltax, deltay)));
	}

	@Override
	public void push(ETile tile) {
		throw new UnsupportedOperationException();
	}

}
