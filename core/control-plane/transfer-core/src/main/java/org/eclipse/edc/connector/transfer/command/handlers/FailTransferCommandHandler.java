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
 *       Fraunhofer Institute for Software and Systems Engineering - refactored
 *
 */

package org.eclipse.edc.connector.transfer.command.handlers;

import org.eclipse.edc.connector.transfer.spi.store.TransferProcessStore;
import org.eclipse.edc.connector.transfer.spi.types.TransferProcess;
import org.eclipse.edc.connector.transfer.spi.types.TransferProcessStates;
import org.eclipse.edc.connector.transfer.spi.types.command.FailTransferCommand;

import static org.eclipse.edc.connector.transfer.spi.types.TransferProcessStates.STARTED;

/**
 * Fails a transfer process and sends it to the {@link TransferProcessStates#TERMINATED} state.
 */
public class FailTransferCommandHandler extends SingleTransferProcessCommandHandler<FailTransferCommand> {

    public FailTransferCommandHandler(TransferProcessStore store) {
        super(store);
    }

    @Override
    public Class<FailTransferCommand> getType() {
        return FailTransferCommand.class;
    }

    @Override
    protected boolean modify(TransferProcess process, FailTransferCommand command) {
        if (process.getState() == STARTED.code()) {
            process.transitionTerminating(command.getErrorMessage());
            return true;
        }
        return false;
    }


    @Override
    protected void postAction(TransferProcess process) {
    }
}
