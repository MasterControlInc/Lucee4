/**
 *
 * Copyright (c) 2014, the Railo Company Ltd. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 **/
package lucee.transformer.bytecode.op;

import lucee.runtime.exp.TemplateException;
import lucee.transformer.bytecode.BytecodeContext;
import lucee.transformer.bytecode.BytecodeException;
import lucee.transformer.bytecode.Literal;
import lucee.transformer.bytecode.Position;
import lucee.transformer.bytecode.cast.CastDouble;
import lucee.transformer.bytecode.expression.ExprDouble;
import lucee.transformer.bytecode.expression.Expression;
import lucee.transformer.bytecode.expression.ExpressionBase;
import lucee.transformer.bytecode.literal.LitDouble;
import lucee.transformer.bytecode.util.Methods;
import lucee.transformer.bytecode.util.Types;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

public final class OpNegateNumber extends ExpressionBase implements ExprDouble {

	private ExprDouble expr;
	

	public static final int PLUS = 0;
	public static final int MINUS = 1;

	private OpNegateNumber(Expression expr, Position start, Position end) {
        super(start,end);
        this.expr=CastDouble.toExprDouble(expr);
    }
    
    /**
     * Create a String expression from a Expression
     * @param left 
     * @param right 
     * 
     * @return String expression
     * @throws TemplateException 
     */
    public static ExprDouble toExprDouble(Expression expr, Position start, Position end) {
        if(expr instanceof Literal) {
        	Double d=((Literal) expr).getDouble(null);
        	if(d!=null) {
        		return LitDouble.toExprDouble(-d.doubleValue(),start,end);
        	}
        }
        return new OpNegateNumber(expr,start,end);
    }
    
    public static ExprDouble toExprDouble(Expression expr, int operation, Position start, Position end) {
    	if(operation==MINUS) return toExprDouble(expr, start,end);
    	return CastDouble.toExprDouble(expr);
    }
	
	
	/**
	 *
	 * @see lucee.transformer.bytecode.expression.ExpressionBase#_writeOut(org.objectweb.asm.commons.GeneratorAdapter, int)
	 */
	public Type _writeOut(BytecodeContext bc, int mode) throws BytecodeException {
		GeneratorAdapter adapter = bc.getAdapter();
    	if(mode==MODE_REF) {
            _writeOut(bc,MODE_VALUE);
            adapter.invokeStatic(Types.CASTER,Methods.METHOD_TO_DOUBLE_FROM_DOUBLE);
            return Types.DOUBLE;
        }
    	
    	expr.writeOut(bc, MODE_VALUE);
    	adapter.visitInsn(Opcodes.DNEG);
    	

        return Types.DOUBLE_VALUE;
	}
}
