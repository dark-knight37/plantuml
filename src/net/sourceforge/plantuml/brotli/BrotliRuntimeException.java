/* Copyright 2015 Google Inc. All Rights Reserved.

   https://github.com/google/brotli/blob/master/LICENSE

   Distributed under MIT license.
   See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
*/

package net.sourceforge.plantuml.brotli;

/**
 * Unchecked exception used internally.
 */
class BrotliRuntimeException extends RuntimeException {

	BrotliRuntimeException(String message) {
		super(message);
	}

	BrotliRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
