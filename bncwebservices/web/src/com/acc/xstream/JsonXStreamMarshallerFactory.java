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
package com.acc.xstream;

import java.io.Writer;

import org.springframework.oxm.xstream.XStreamMarshaller;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonWriter;


public class JsonXStreamMarshallerFactory extends XmlXStreamMarshallerFactory
{

	private static XStreamMarshaller JSON_INSTANCE = null;

	private final SingletonGetter<XStreamMarshaller> instanceGetter = new SingletonGetter<XStreamMarshaller>()
	{

		@Override
		XStreamMarshaller getInstanceProperty()
		{
			return JSON_INSTANCE;
		}

		@Override
		void setInstanceProperty(final XStreamMarshaller prop)
		{
			JSON_INSTANCE = prop;
		}

		@Override
		XStreamMarshaller create() throws Exception
		{
			return getObjectInternal();
		}

	};



	@Override
	public Object getObject() throws Exception
	{
		return instanceGetter.get();
	}


	/**
	 * creates a custom json writer which swallows top most root nodes
	 */
	@Override
	protected XStreamMarshaller createMarshaller()
	{
		final XStreamMarshaller marshaller = super.createMarshaller();
		marshaller.setStreamDriver(new com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver()
		{
			@Override
			public HierarchicalStreamWriter createWriter(final Writer writer)
			{
				return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
			}
		});
		return marshaller;
	}
}
