/*
 *  Copyright (c) 2023 Fraunhofer Institute for Software and Systems Engineering
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Fraunhofer Institute for Software and Systems Engineering - initial API and implementation
 *
 */

package org.eclipse.edc.protocol.dsp.negotiation.dispatcher.delegate;

import org.eclipse.edc.connector.contract.spi.types.agreement.ContractAgreementVerificationMessage;
import org.eclipse.edc.protocol.dsp.spi.dispatcher.DspHttpDispatcherDelegate;
import org.eclipse.edc.protocol.dsp.spi.testfixtures.dispatcher.DspHttpDispatcherDelegateTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.edc.protocol.dsp.negotiation.dispatcher.NegotiationApiPaths.AGREEMENT;
import static org.eclipse.edc.protocol.dsp.negotiation.dispatcher.NegotiationApiPaths.BASE_PATH;
import static org.eclipse.edc.protocol.dsp.negotiation.dispatcher.NegotiationApiPaths.VERIFICATION;

class ContractAgreementVerificationMessageHttpDelegateTest extends DspHttpDispatcherDelegateTestBase<ContractAgreementVerificationMessage> {
    
    private ContractAgreementVerificationMessageHttpDelegate delegate;

    @BeforeEach
    void setUp() {
        delegate = new ContractAgreementVerificationMessageHttpDelegate(serializer);
    }

    @Test
    void getMessageType() {
        assertThat(delegate.getMessageType()).isEqualTo(ContractAgreementVerificationMessage.class);
    }

    @Test
    void buildRequest() throws IOException {
        var message = message();
        testBuildRequest_shouldReturnRequest(message, BASE_PATH + message.getProcessId() + AGREEMENT + VERIFICATION);
    }

    @Test
    void buildRequest_serializationFails_throwException() {
        testBuildRequest_shouldThrowException_whenSerializationFails(message());
    }

    @Test
    void parseResponse_returnNull() {
        testParseResponse_shouldReturnNullFunction_whenResponseBodyNotProcessed();
    }

    private ContractAgreementVerificationMessage message() {
        var value = "example";
        return ContractAgreementVerificationMessage.Builder.newInstance()
                .protocol(value)
                .processId(value)
                .callbackAddress("http://connector")
                .build();
    }
    
    @Override
    protected DspHttpDispatcherDelegate<ContractAgreementVerificationMessage, ?> delegate() {
        return delegate;
    }
}