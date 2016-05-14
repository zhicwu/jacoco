/*******************************************************************************
 * Copyright (c) 2009, 2016 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Evgeny Mandrikov - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.core.internal;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;

/**
 * Patching for Java 9 classes, so that ASM can read them.
 */
public final class Java9Support {

	public static final int V1_9 = Opcodes.V1_8 + 1;

	private Java9Support() {
	}

	/**
	 * Copy of {@link ClassReader#readClass(InputStream, boolean)}.
	 */
	public static byte[] readClass(final InputStream is, boolean close)
			throws IOException {
		if (is == null) {
			throw new IOException("Class not found");
		}
		try {
			byte[] b = new byte[is.available()];
			int len = 0;
			while (true) {
				int n = is.read(b, len, b.length - len);
				if (n == -1) {
					if (len < b.length) {
						byte[] c = new byte[len];
						System.arraycopy(b, 0, c, 0, len);
						b = c;
					}
					return b;
				}
				len += n;
				if (len == b.length) {
					int last = is.read();
					if (last < 0) {
						return b;
					}
					byte[] c = new byte[b.length + 1000];
					System.arraycopy(b, 0, c, 0, len);
					c[len++] = (byte) last;
					b = c;
				}
			}
		} finally {
			if (close) {
				is.close();
			}
		}
	}

	private static void putShort(byte[] b, int index, int s) {
		b[index] = (byte) (s >>> 8);
		b[index + 1] = (byte) s;
	}

	private static short readShort(byte[] b, int index) {
		return (short) (((b[index] & 0xFF) << 8) | (b[index + 1] & 0xFF));
	}

	public static boolean isPatchRequired(byte[] buffer) {
		return readShort(buffer, 6) == V1_9;
	}

	public static byte[] downgradeIfRequired(byte[] buffer) {
		return isPatchRequired(buffer) ? downgrade(buffer) : buffer;
	}

	public static byte[] downgrade(byte[] b) {
		byte[] result = new byte[b.length];
		System.arraycopy(b, 0, result, 0, b.length);
		putShort(result, 6, Opcodes.V1_8);
		return result;
	}

	public static void upgrade(byte[] b) {
		putShort(b, 6, V1_9);
	}

}
