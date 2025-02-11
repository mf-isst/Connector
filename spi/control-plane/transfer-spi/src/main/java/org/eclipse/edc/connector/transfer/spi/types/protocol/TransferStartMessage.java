/*
 *  Copyright (c) 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
 *
 */

package org.eclipse.edc.connector.transfer.spi.types.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.eclipse.edc.spi.types.domain.DataAddress;

import java.util.Objects;

/**
 * The {@link TransferStartMessage} is sent by the provider to indicate the asset transfer has been initiated.
 */
public class TransferStartMessage implements TransferRemoteMessage {

    private String callbackAddress;
    private String protocol;
    private String processId;

    private DataAddress dataAddress;

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getCallbackAddress() {
        return callbackAddress;
    }

    @Override
    public String getProcessId() {
        return processId;
    }

    public DataAddress getDataAddress() {
        return dataAddress;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private final TransferStartMessage message;

        private Builder() {
            message = new TransferStartMessage();
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        public Builder callbackAddress(String callbackAddress) {
            message.callbackAddress = callbackAddress;
            return this;
        }

        public Builder protocol(String protocol) {
            message.protocol = protocol;
            return this;
        }

        public Builder processId(String processId) {
            message.processId = processId;
            return this;
        }

        public Builder dataAddress(DataAddress dataAddress) {
            message.dataAddress = dataAddress;
            return this;
        }

        public TransferStartMessage build() {
            Objects.requireNonNull(message.protocol, "The protocol must be specified");
            Objects.requireNonNull(message.processId, "The processId must be specified");
            return message;
        }

    }
}
