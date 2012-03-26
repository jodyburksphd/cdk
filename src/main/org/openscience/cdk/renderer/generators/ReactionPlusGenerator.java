/* Copyright (C) 2009  Stefan Kuhn <shk3@users.sf.net>
 *               2009  Gilleain Torrance <gilleain@users.sf.net>
 *
 *  Contact: cdk-devel@list.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openscience.cdk.renderer.generators;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;

import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.renderer.BoundsCalculator;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.elements.ElementGroup;
import org.openscience.cdk.renderer.elements.IRenderingElement;
import org.openscience.cdk.renderer.elements.TextElement;

/**
 * Generate the arrow for a reaction.
 * 
 * @author maclean
 * @cdk.module renderextra
 * @cdk.githash
 */
@TestClass("org.openscience.cdk.renderer.generators.ReactionPlusGeneratorTest")
public class ReactionPlusGenerator implements IGenerator<IReaction> {

	/** {@inheritDoc}} */
	@Override
	@TestMethod("testEmptyReaction")
	public IRenderingElement generate(IReaction reaction, RendererModel model) {
		ElementGroup diagram = new ElementGroup();
		
		IMoleculeSet reactants = reaction.getReactants();
        Rectangle2D totalBoundsReactants = BoundsCalculator.calculateBounds(reactants);
        Rectangle2D bounds1 = 
        	BoundsCalculator.calculateBounds(reactants.getAtomContainer(0));
        double axis = totalBoundsReactants.getCenterY();
        Color color = model.getParameter(
            BasicSceneGenerator.ForegroundColor.class).getValue();
        for (int i = 1; i < reaction.getReactantCount(); i++) {
        	Rectangle2D bounds2 = 
        		BoundsCalculator.calculateBounds(reactants.getAtomContainer(i));
        	diagram.add(makePlus(bounds1, bounds2, axis, color));
        	bounds1 = bounds2;
        }
        
        IMoleculeSet products = reaction.getProducts();
        Rectangle2D totalBoundsProducts = BoundsCalculator.calculateBounds(products);
        axis = totalBoundsProducts.getCenterY();
        bounds1 = BoundsCalculator.calculateBounds(reactants.getAtomContainer(0));
        for (int i = 1; i < reaction.getProductCount(); i++) {
        	Rectangle2D bounds2 = 
        		BoundsCalculator.calculateBounds(products.getAtomContainer(i));
        	
        	diagram.add(makePlus(bounds1, bounds2, axis, color));
        	bounds1 = bounds2;
        }
        return diagram;
	}
	
	/** Place a '+' sign between two molecules. */
	private TextElement makePlus(
	        Rectangle2D moleculeBox1, Rectangle2D moleculeBox2, double axis, Color color) {
	    double arrowCenter = (moleculeBox1.getCenterX() + moleculeBox2.getCenterX()) / 2;
	    return new TextElement(arrowCenter, axis, "+", color);
	}

	/** {@inheritDoc}} */
	@Override
	@TestMethod("testGetParameters")
	public List<IGeneratorParameter<?>> getParameters() {
        return Arrays.asList(
            new IGeneratorParameter<?>[] {
            }
        );
    }
}
