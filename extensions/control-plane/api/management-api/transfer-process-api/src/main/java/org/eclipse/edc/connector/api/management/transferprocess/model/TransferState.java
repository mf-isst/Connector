/*
 *  Copyright (c) 2020 - 2022 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *
 */

package org.eclipse.edc.connector.api.management.transferprocess.model;

import org.eclipse.edc.connector.transfer.spi.types.TransferProcessStates;

import static org.eclipse.edc.spi.CoreConstants.EDC_NAMESPACE;

/**
 * Wrapper for {@link TransferProcessStates} formatted as String. Used to format a simple string as JSON.
 */
public class TransferState {

    public static final String EDC_TRANSFER_STATE_TYPE = EDC_NAMESPACE + "TransferState";
    public static final String EDC_TRANSFER_STATE_STATE = EDC_NAMESPACE + "state";

    private final String state;

    public TransferState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
