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

package org.eclipse.edc.connector.api.management.contractnegotiation.transform;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import org.eclipse.edc.api.model.CallbackAddressDto;
import org.eclipse.edc.connector.api.management.contractnegotiation.model.ContractOfferDescription;
import org.eclipse.edc.connector.api.management.contractnegotiation.model.NegotiationInitiateRequestDto;
import org.eclipse.edc.jsonld.spi.transformer.AbstractJsonLdTransformer;
import org.eclipse.edc.policy.model.Policy;
import org.eclipse.edc.transform.spi.TransformerContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static org.eclipse.edc.connector.api.management.contractnegotiation.model.NegotiationInitiateRequestDto.ASSET_ID;
import static org.eclipse.edc.connector.api.management.contractnegotiation.model.NegotiationInitiateRequestDto.CALLBACK_ADDRESSES;
import static org.eclipse.edc.connector.api.management.contractnegotiation.model.NegotiationInitiateRequestDto.CONNECTOR_ADDRESS;
import static org.eclipse.edc.connector.api.management.contractnegotiation.model.NegotiationInitiateRequestDto.CONNECTOR_ID;
import static org.eclipse.edc.connector.api.management.contractnegotiation.model.NegotiationInitiateRequestDto.CONSUMER_ID;
import static org.eclipse.edc.connector.api.management.contractnegotiation.model.NegotiationInitiateRequestDto.OFFER;
import static org.eclipse.edc.connector.api.management.contractnegotiation.model.NegotiationInitiateRequestDto.OFFER_ID;
import static org.eclipse.edc.connector.api.management.contractnegotiation.model.NegotiationInitiateRequestDto.POLICY;
import static org.eclipse.edc.connector.api.management.contractnegotiation.model.NegotiationInitiateRequestDto.PROTOCOL;
import static org.eclipse.edc.connector.api.management.contractnegotiation.model.NegotiationInitiateRequestDto.PROVIDER_ID;

public class JsonObjectToNegotiationInitiateRequestDtoTransformer extends AbstractJsonLdTransformer<JsonObject, NegotiationInitiateRequestDto> {

    public JsonObjectToNegotiationInitiateRequestDtoTransformer() {
        super(JsonObject.class, NegotiationInitiateRequestDto.class);
    }

    @Override
    public @Nullable NegotiationInitiateRequestDto transform(@NotNull JsonObject jsonObject, @NotNull TransformerContext context) {
        var builder = NegotiationInitiateRequestDto.Builder.newInstance();

        visitProperties(jsonObject, (k, v) -> setProperties(k, v, builder, context));
        return builder.build();
    }

    private void setProperties(String key, JsonValue value, NegotiationInitiateRequestDto.Builder builder, TransformerContext context) {

        var contractOfferDesc = ContractOfferDescription.Builder.newInstance();
        switch (key) {
            case CONNECTOR_ADDRESS:
                transformString(value, builder::connectorAddress, context);
                break;
            case PROTOCOL:
                transformString(value, builder::protocol, context);
                break;
            case CONNECTOR_ID:
                transformString(value, builder::connectorId, context);
                break;
            case PROVIDER_ID:
                transformString(value, builder::providerId, context);
                break;
            case CONSUMER_ID:
                transformString(value, builder::consumerId, context);
                break;
            case CALLBACK_ADDRESSES:
                var addresses = new ArrayList<CallbackAddressDto>();
                transformArrayOrObject(value, CallbackAddressDto.class, addresses::add, context);
                builder.callbackAddresses(addresses);
                break;
            case OFFER:
                transformString(value.asJsonObject().get(OFFER_ID), contractOfferDesc::offerId, context);
                transformString(value.asJsonObject().get(ASSET_ID), contractOfferDesc::assetId, context);
                contractOfferDesc.policy(context.transform(value.asJsonObject().get(POLICY), Policy.class));
                builder.offer(contractOfferDesc.build());
                break;
            default:
                context.reportProblem("Cannot convert key " + key + " as it is not known");
                break;
        }
    }
}
