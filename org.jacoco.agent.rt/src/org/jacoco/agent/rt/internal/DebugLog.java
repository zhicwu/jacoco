/*******************************************************************************
 * Copyright (c) 2009, 2015 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *    
 *******************************************************************************/
package org.jacoco.agent.rt.internal;

/**
 * Temporary debug logging for jacoco agent
 */
public class DebugLog {

	/**
	 * Prints the given debug informtion to the console
	 * 
	 * @param msg
	 *            message with optional placeholders
	 * @param args
	 *            optional placeholder values
	 */
	public static void debug(final String msg, final Object... args) {
		System.out.println("[JaCoCo Agent] " + String.format(msg, args));
	}

}
