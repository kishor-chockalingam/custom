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

import org.springframework.beans.factory.FactoryBean;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.ArrayMapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;


/**
 * 
 * {@link XStream} instance factory
 */
public class XmlXStreamFactory implements FactoryBean
{

	private static XStream XML_INSTANCE = null;

	private final SingletonGetter<XStream> instanceGetter = new SingletonGetter<XStream>()
	{

		@Override
		XStream getInstanceProperty()
		{
			return XML_INSTANCE;
		}

		@Override
		void setInstanceProperty(final XStream prop)
		{
			XML_INSTANCE = prop;
		}

		@Override
		XStream create() throws Exception
		{
			return getObjectInternal();
		}
	};

	@Override
	public Object getObject() throws Exception
	{
		return instanceGetter.get();
	}


	protected XStream getObjectInternal() throws Exception
	{
		//		final StaxDriver driver = new StaxDriver()
		//		{
		//			@Override
		//			public StaxWriter createStaxWriter(final XMLStreamWriter out) throws XMLStreamException
		//			{
		//				out.writeStartDocument("UTF-8", "1.0");
		//				return createStaxWriter(out, false);
		//			}
		//		};

		final XStream stream = new XStream()
		{
			@Override
			protected MapperWrapper wrapMapper(final MapperWrapper next)
			{
				return createMapperWrapper(next);
			}
		};
		stream.setMode(com.thoughtworks.xstream.XStream.NO_REFERENCES);
		return stream;
	}


	/**
	 * Due to schema compatibility requirements, customizes a {@link MapperWrapper} for arrays to don't generate a -array
	 * suffixes.
	 * 
	 */
	protected MapperWrapper createMapperWrapper(final MapperWrapper parent)
	{
		final MapperWrapper mapperWrapper = new ArrayMapper(parent)
		{

			@Override
			public String aliasForSystemAttribute(final String attribute)
			{
				return "class".equals(attribute) ? null : attribute;
			}
		};
		return mapperWrapper;
	}

	@Override
	public Class getObjectType()
	{
		return XStream.class;
	}




	@Override
	public boolean isSingleton()
	{
		return true;
	}

}
