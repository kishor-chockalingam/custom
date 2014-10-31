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


/**
 * Abstract singleton getter with double null check.
 */
public abstract class SingletonGetter<T>
{

	public final T get() throws Exception
	{
		T local = getInstanceProperty();
		if (local == null)
		{
			synchronized (SingletonGetter.this)
			{
				local = getInstanceProperty();
				if (local == null)
				{
					local = create();

					setInstanceProperty(local);
				}
			}
		}
		return local;
	}

	abstract T getInstanceProperty();

	abstract void setInstanceProperty(T prop);

	abstract T create() throws Exception;
}
