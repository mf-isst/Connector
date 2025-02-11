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

package org.eclipse.edc.protocol.dsp.transferprocess.transformer.to;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.eclipse.edc.protocol.dsp.spi.types.HttpMessageProtocol;
import org.eclipse.edc.protocol.dsp.transferprocess.transformer.type.to.JsonObjectToTransferStartMessageTransformer;
import org.eclipse.edc.spi.types.domain.DataAddress;
import org.eclipse.edc.transform.spi.TransformerContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.edc.jsonld.spi.JsonLdKeywords.CONTEXT;
import static org.eclipse.edc.jsonld.spi.JsonLdKeywords.TYPE;
import static org.eclipse.edc.protocol.dsp.transferprocess.transformer.DspTransferProcessPropertyAndTypeNames.DSPACE_DATAADDRESS_TYPE;
import static org.eclipse.edc.protocol.dsp.transferprocess.transformer.DspTransferProcessPropertyAndTypeNames.DSPACE_PROCESSID_TYPE;
import static org.eclipse.edc.protocol.dsp.transferprocess.transformer.DspTransferProcessPropertyAndTypeNames.DSPACE_SCHEMA;
import static org.eclipse.edc.protocol.dsp.transferprocess.transformer.DspTransferProcessPropertyAndTypeNames.DSPACE_TRANSFER_START_TYPE;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonObjectToTransferStartMessageTransformerTest {

    private final String processId = "TestProcessId";

    private TransformerContext context = mock(TransformerContext.class);

    private JsonObjectToTransferStartMessageTransformer transformer;

    @BeforeEach
    void setUp() {
        transformer = new JsonObjectToTransferStartMessageTransformer();
    }

    @Test
    void jsonObjectToTransferStartMessage() {
        var json = Json.createObjectBuilder()
                .add(CONTEXT, DSPACE_SCHEMA)
                .add(TYPE, DSPACE_TRANSFER_START_TYPE)
                .add(DSPACE_PROCESSID_TYPE, processId)
                .build();

        var result = transformer.transform(json, context);

        assertThat(result).isNotNull();

        assertThat(result.getProcessId()).isEqualTo(processId);
        assertThat(result.getProtocol()).isEqualTo(HttpMessageProtocol.DATASPACE_PROTOCOL_HTTP);

        verify(context, never()).reportProblem(anyString());
    }

    @Test
    void jsonObjectToTransferStartMessageWithDataAddress() {
        var json = Json.createObjectBuilder()
                .add(CONTEXT, DSPACE_SCHEMA)
                .add(TYPE, DSPACE_TRANSFER_START_TYPE)
                .add(DSPACE_PROCESSID_TYPE, processId)
                .add(DSPACE_DATAADDRESS_TYPE, Json.createObjectBuilder()
                        .add("type", "AWS")
                        .build())
                .build();

        var dataAddress = DataAddress.Builder.newInstance()
                .type("AWS")
                .build();
        when(context.transform(isA(JsonObject.class), eq(DataAddress.class))).thenReturn(dataAddress);

        var result = transformer.transform(json, context);

        assertThat(result).isNotNull();

        assertThat(result.getProcessId()).isEqualTo(processId);
        assertThat(result.getProtocol()).isEqualTo(HttpMessageProtocol.DATASPACE_PROTOCOL_HTTP);
        assertThat(result.getDataAddress()).isSameAs(dataAddress);

        verify(context, never()).reportProblem(anyString());
    }

    @Test
    void jsonObjectToTransferStartMessageWithEmptyDataAddress() {
        var json = Json.createObjectBuilder()
                .add(CONTEXT, DSPACE_SCHEMA)
                .add(TYPE, DSPACE_TRANSFER_START_TYPE)
                .add(DSPACE_PROCESSID_TYPE, processId)
                .add(DSPACE_DATAADDRESS_TYPE, Json.createObjectBuilder()
                        .build())
                .build();

        var result = transformer.transform(json, context);

        assertThat(result).isNotNull();

        assertThat(result.getProcessId()).isEqualTo(processId);
        assertThat(result.getProtocol()).isEqualTo(HttpMessageProtocol.DATASPACE_PROTOCOL_HTTP);
        assertThat(result.getDataAddress()).isNull();

        verify(context, never()).reportProblem(anyString());
    }
}
