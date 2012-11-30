package edu.kit.joana.api.test;
/**
 * This file is part of the Joana IFC project. It is developed at the
 * Programming Paradigms Group of the Karlsruhe Institute of Technology.
 *
 * For further details on licensing please read the information at
 * http://joana.ipd.kit.edu or contact the authors.
 */

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.joana.ifc.sdg.graph.SDG;
import edu.kit.joana.ifc.sdg.graph.slicer.graph.threads.ThreadsInformation;
import edu.kit.joana.ifc.sdg.graph.slicer.graph.threads.ThreadsInformation.ThreadInstance;

/**
 * @author Martin Mohr
 */
public class ConcurrentTests {

	private static final boolean FORCE_REBUILD = false;
	
	public static final String CLASSPATH = "../joana.api.testdata/bin";
	public static final String STUBS = "../../contrib/lib/stubs/jSDG-stubs-jre1.4.jar";

	private static final Map<String, TestData> testData = new HashMap<String, TestData>();

	static {
		testData.put("mantel",
			new TestData("joana.api.testdata.conc.Mantel00Page10", "mantel.pdg", 5, 0));
		testData.put("simple",
			new TestData("joana.api.testdata.conc.SimpleThreadSpawning", "simple.pdg", 2, 0));
		testData.put("spawn_after_loop",
			new TestData("joana.api.testdata.conc.SpawnAfterLoop", "spawn_after_loop.pdg", 2, 0));
		testData.put("spawn_within_loop",
			new TestData("joana.api.testdata.conc.SpawnWithinLoop", "spawn_within_loop.pdg", 2, 1));
		testData.put("thread_hierarchy",
			new TestData("joana.api.testdata.conc.ThreadHierarchy", "thread_hierarchy.pdg", 15, 0));
		testData.put("two_threads_sequential",
			new TestData("joana.api.testdata.conc.TwoThreadsSequential", "two_threads_sequential.pdg", 2, 0));
		testData.put("simple_recursive_spawning", new TestData("joana.api.testdata.conc.SimpleRecursiveSpawning", "simple_recursive_spawning.pdg", 4, 1));
	}

	@BeforeClass
	public static void setUp() {
		for (String testKey : testData.keySet()) {
			final TestData td = testData.get(testKey);
			if (FORCE_REBUILD) {
				final BuildSDG b = BuildSDG.standardConcSetup(td.mainClass, td.sdgFile);
				b.run();
			} else {
				final File f = new File(td.sdgFile);
				if (!f.exists() || !f.canRead()) {
					final BuildSDG b = BuildSDG.standardConcSetup(td.mainClass, td.sdgFile);
					b.run();
				}
			}
		}
	}

	@Test
	public void mantelTest() throws IOException {
		doTest("mantel");
	}

	@Test
	public void simpleTest() throws IOException {
		doTest("simple");
	}
	
	@Test
	public void spawnAfterLoopTest() throws IOException {
		doTest("spawn_after_loop");
	}
	
	@Test
	public void spawnWithinLoopTest() throws IOException {
		doTest("spawn_within_loop");
	}
	
	@Test
	public void threadHierarchyTest() throws IOException {
		doTest("thread_hierarchy");
	}
	
	@Test
	public void simpleRecursiveSpawningTest() throws IOException {
		doTest("simple_recursive_spawning");
	}
	
	public void doTest(String key) throws IOException {
		TestData t = testData.get(key);
		if (t == null) {
			Assert.fail("wrong test key: " + key);
		}
		SDG sdg = SDG.readFrom(t.sdgFile);
		ThreadsInformation tinfo = sdg.getThreadsInfo();
		int numberOfThreads = 0;
		int numberOfDynamicThreads = 0;
		for (ThreadInstance tinstance : tinfo) {
			numberOfThreads++;
			if (tinstance.dynamic) {
				numberOfDynamicThreads++;
			}
		}
		
		assertEquals(String.format("Wrong result in test %s! Number of threads is not as expected!", key), t.expectedNumberOfThreads, numberOfThreads);
		assertEquals(String.format("Wrong result in test %s! Number of dynamic threads is not as expected!", key), t.expectedNumberOfDynamicThreads, numberOfDynamicThreads);
	}

	private static class TestData {
		private final String mainClass;
		private final String sdgFile;
		private final int expectedNumberOfThreads;
		private final int expectedNumberOfDynamicThreads;

		TestData(String mainClass, String sdgFile, int expectedNumberOfDynamicThreads, int expectedNumberOfNonDynamicThreads) {
			this.expectedNumberOfThreads = expectedNumberOfDynamicThreads;
			this.expectedNumberOfDynamicThreads = expectedNumberOfNonDynamicThreads;
			this.mainClass = mainClass;
			this.sdgFile = sdgFile;
		}
	}

}
