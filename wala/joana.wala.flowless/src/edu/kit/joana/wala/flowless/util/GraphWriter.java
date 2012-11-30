/**
 * This file is part of the Joana IFC project. It is developed at the
 * Programming Paradigms Group of the Karlsruhe Institute of Technology.
 *
 * For further details on licensing please read the information at
 * http://joana.ipd.kit.edu or contact the authors.
 */
package edu.kit.joana.wala.flowless.util;

import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.graph.Graph;
import com.ibm.wala.viz.DotUtil;
import com.ibm.wala.viz.NodeDecorator;

public abstract class GraphWriter {

	public abstract void writeGraph(Graph<?> graph, String nameSuffix);

	public static GraphWriter NO_OUTPUT = new GraphWriter() {
		@Override
		public void writeGraph(Graph<?> graph, String nameSuffix) {}
	};

	public static class DotWriter extends GraphWriter {
		private final String outDir;
		private final String prefix;

		public DotWriter(String outDir, String prefix) {
			this.outDir = outDir;
			this.prefix = prefix;
		}

		public void writeGraph(Graph<?> graph, String nameSuffix) {
			try {
				final String name = prefix + (nameSuffix == null ? "" : nameSuffix);
				DotUtil.writeDotFile(graph, NodeDecorator.DEFAULT, graph.getClass().getSimpleName() + " of " + name, outDir + name + ".dot");
			} catch (WalaException e) {
				e.printStackTrace();
			}
		}
	}

}
