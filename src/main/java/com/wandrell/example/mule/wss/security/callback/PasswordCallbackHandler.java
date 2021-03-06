/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.wandrell.example.mule.wss.security.callback;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Callback handler for handling users authentication data.
 * <p>
 * This is used by the the user-based authentications.
 *
 * @author Bernardo Martínez Garrido
 */
public final class PasswordCallbackHandler implements CallbackHandler {

    /**
     * The logger used for logging the password callback handler usage.
     */
    private static final Logger LOGGER = LoggerFactory
                                               .getLogger(PasswordCallbackHandler.class);

    /**
     * Valid user.
     */
    private final String        user;

    /**
     * Password for the user.
     */
    private final String        password;

    /**
     * Default constructor.
     * 
     * @param username
     *            valid username
     * @param pass
     *            password for the username
     */
    public PasswordCallbackHandler(final String username, final String pass) {
        super();

        user = checkNotNull(username, "Received a null pointer as username");
        password = checkNotNull(pass, "Received a null pointer as password");
    }

    @Override
    public final void handle(final Callback[] callbacks) throws IOException,
            UnsupportedCallbackException {
        WSPasswordCallback passCallb; // Casted callback

        checkNotNull(callbacks, "Received a null pointer as callbacks");

        for (final Callback callback : callbacks) {
            if (callback instanceof WSPasswordCallback) {
                passCallb = (WSPasswordCallback) callback;

                if (getUser().equals(passCallb.getIdentifier())) {
                    // Valid user
                    passCallb.setPassword(getPassword());
                } else {
                    // Invalid user
                    LOGGER.debug(String.format(
                            "User data for username %s not found",
                            passCallb.getIdentifier()));
                }
            } else {
                throw new UnsupportedCallbackException(callback,
                        "Unrecognized callback");
            }
        }

    }

    /**
     * Returns the user password.
     * 
     * @return the user password
     */
    private final String getPassword() {
        return password;
    }

    /**
     * Returns the valid user.
     * 
     * @return the valid user
     */
    private final String getUser() {
        return user;
    }

}
