/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.acc.setup;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.Config;
import com.acc.constants.YcommercewebservicesConstants;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Data setup class used at the time of initialization and update
 */
@SystemSetup(extension = YcommercewebservicesConstants.EXTENSIONNAME)
public class BncwebservicesDataSetup
{
	private static final Logger LOG = Logger.getLogger(BncwebservicesDataSetup.class);

	private String fileEncoding = "UTF-8";
	private ImportService importService;
	private SolrIndexerService solrIndexerService;
	private SynchronizationService synchronizationService;

	@SystemSetup(process = Process.ALL, type = Type.ESSENTIAL)
	public void createEssentialData(final SystemSetupContext context)
	{
		importService.importData(new StreamBasedImpExResource(BncwebservicesDataSetup.class
				.getResourceAsStream("/import/yCommerceWebServicesEssentialData.csv"), CSVConstants.HYBRIS_ENCODING, Character
				.valueOf(';')));
	}

	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		if (Config.getBoolean(YcommercewebservicesConstants.LOAD_DATA_PROPERTY, false))
		{
			loadSampleData();
			getSynchronizationService().createProductCatalogSyncJob("wsTestProductCatalog");
			getSynchronizationService().executeCatalogSyncJob("wsTestProductCatalog");
			importImpexFile("/import/wsTestData/promotions.impex", false);//must be loaded after synchronization
			getSolrIndexerService().executeSolrIndexerCronJob("wsTestIndex");
		}
	}

	protected void loadSampleData()
	{
		importImpexFile("/import/wsTestData/essential-data.impex", false);
		importImpexFile("/import/wsTestData/countries.impex", false);
		importImpexFile("/import/wsTestData/user-groups.impex", false);
		importImpexFile("/import/wsTestData/delivery-modes.impex", false);
		importImpexFile("/import/wsTestData/catalog.impex", false);
		importImpexFile("/import/wsTestData/categories.impex", false);
		importImpexFile("/import/wsTestData/classifications-units.impex", false);
		importImpexFile("/import/wsTestData/categories-classifications.impex", false);
		importImpexFile("/import/wsTestData/suppliers.impex", false);
		importImpexFile("/import/wsTestData/products.impex", false);
		importImpexFile("/import/wsTestData/products-classifications.impex", false);
		importImpexFile("/import/wsTestData/products-prices.impex", false);
		importImpexFile("/import/wsTestData/products-media.impex", false);
		importImpexFile("/import/wsTestData/products-stocklevels.impex", false);
		importImpexFile("/import/wsTestData/products-pos-stocklevels.impex", false);
		importImpexFile("/import/wsTestData/products-relations.impex", false);
		importImpexFile("/import/wsTestData/store.impex", false);
		importImpexFile("/import/wsTestData/site.impex", false);
		importImpexFile("/import/wsTestData/points-of-service.impex", false);
		importImpexFile("/import/wsTestData/solr.impex", false);
		importImpexFile("/import/wsTestData/vouchers.impex", false);
	}

	protected void importImpexFile(final String file, final boolean legacyMode)
	{
		final String message = "Importing [" + file + "]...";
		final InputStream resourceAsStream = getClass().getResourceAsStream(file);
		if (resourceAsStream == null)
		{
			LOG.error(message + "ERROR (MISSING FILE)");
		}
		else
		{
			try
			{
				LOG.info(message);

				final ImportConfig importConfig = new ImportConfig();
				importConfig.setScript(new StreamBasedImpExResource(resourceAsStream, getFileEncoding()));
				importConfig.setLegacyMode(Boolean.valueOf(legacyMode));

				final ImportResult importResult = getImportService().importData(importConfig);
				if (importResult.isError())
				{
					LOG.error(message + " FAILED");
				}
			}
			catch (final Exception e)
			{
				LOG.error(message + " FAILED", e);
			}
		}
	}

	@Required
	public void setImportService(final ImportService importService)
	{
		this.importService = importService;
	}

	public ImportService getImportService()
	{
		return importService;
	}

	public String getFileEncoding()
	{
		return fileEncoding;
	}

	public void setFileEncoding(final String fileEncoding)
	{
		this.fileEncoding = fileEncoding;
	}

	public SolrIndexerService getSolrIndexerService()
	{
		return solrIndexerService;
	}

	@Required
	public void setSolrIndexerService(final SolrIndexerService solrIndexerService)
	{
		this.solrIndexerService = solrIndexerService;
	}

	public SynchronizationService getSynchronizationService()
	{
		return synchronizationService;
	}

	@Required
	public void setSynchronizationService(final SynchronizationService synchronizationService)
	{
		this.synchronizationService = synchronizationService;
	}
}
