/**
 * This file is part of the Joana IFC project. It is developed at the
 * Programming Paradigms Group of the Karlsruhe Institute of Technology.
 *
 * For further details on licensing please read the information at
 * http://joana.ipd.kit.edu or contact the authors.
 */
package edu.kit.joana.deprecated.jsdg.sdg.parammodel.objgraph.params;

import java.util.Set;

import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.intset.OrdinalSet;

/**
 * Base class for formal nodes.
 *
 * @author Juergen Graf <graf@kit.edu>
 *
 */
public abstract class FormNode extends ObjGraphParameter {

	public FormNode(int id, TypeReference type, boolean isPrimitive, Set<PointerKey> pKey, OrdinalSet<InstanceKey> pts) {
		super(id, type, isPrimitive, pKey, pts);
	}

	public final boolean isActual() {
		return false;
	}

}
