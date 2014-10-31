package com.acc.test.groovy.webservicetests.docu;

/**
 * Save all responses from test to one file in directory: {@link SaveWSOutputStrategy#WS_OUTPUT_DIR}
 */
public class DefaultSaveWSOuptutStrategy implements SaveWSOutputStrategy {

	private File dir = null
	private String  ln = null

	public DefaultSaveWSOuptutStrategy() {
		ln = System.getProperty('line.separator');
		dir = new File(WS_OUTPUT_DIR)
		if(!dir.exists()) {
			dir.mkdirs();
		}
	}

	@Override
	public void saveFailedTest(final SummaryOfRunningTest summary, Throwable t) {

		File f= new File(dir,generateOutputFileName(summary, dir))
		f.newWriter() // TODO
		def ln = System.getProperty('line.separator')

		summary.with {
			f << "TEST NAME: " << testName << ln;
			f << "DATE: " << new Date() << ln;
			f << "TEST FAILED: " << true << ln;
			f << "TEST FAILURE REASON: " << ln << t.message << ln;
		}

	}

	@Override
	public void saveSucceededTest(final SummaryOfRunningTest summary)
	{
		File f= new File(dir,generateOutputFileName(summary, dir))
		f.newWriter() // TODO
		def ln = System.getProperty('line.separator')

		summary.with {
			f << "TEST NAME: " << testName << ln;
			f << "DATE: " << new Date() << ln << ln;

			for(WSRequestSummary req:requests) {
				f << "RESOURCE: " << req.resource << ln;
				f << "HTTP METHOD: " << req.httpMethod << ln;
				f << "ACCEPT: " << req.accept << ln;
				f << "RESPONSE: " << ln << req.response << ln;
				f << "*".multiply(80) << ln;
			}

		}
	}

	/**
	 * generate file name according to pattern: TESTNAME_(RESOURCES)*.txt
	 */
	private String generateOutputFileName(final SummaryOfRunningTest sum, final File dir)
	{
		final int maxFileNameLength = 255;
		String reqs = "";
		for (final WSRequestSummary reqSummary : sum.requests)
		{
			reqs += "_"	+ reqSummary.resource.replace('\\', '#').replace('/', '#').replace(':', '#').replace('?', '#').replace('"', '#').replace('<', '#').replace('>', '#').replace('|', '#')
			reqs +=  "_" + reqSummary.accept;
		}

		final int maxReqLen = maxFileNameLength - dir.absolutePath.length() - sum.testName.length() - ".txt".length();
		reqs = reqs.substring(0, maxReqLen < reqs.length() ? maxReqLen : reqs.length());

		return sum.testName + reqs + ".txt";
	}

}
