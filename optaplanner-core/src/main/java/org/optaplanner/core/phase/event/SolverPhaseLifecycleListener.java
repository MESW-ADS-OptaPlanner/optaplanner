/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.phase.event;

import org.optaplanner.core.phase.AbstractSolverPhaseScope;
import org.optaplanner.core.phase.step.AbstractStepScope;
import org.optaplanner.core.solver.event.SolverLifecycleListener;

public interface SolverPhaseLifecycleListener extends SolverLifecycleListener {

    void phaseStarted(AbstractSolverPhaseScope phaseScope);

    void stepStarted(AbstractStepScope stepScope);

    void stepEnded(AbstractStepScope stepScope);

    void phaseEnded(AbstractSolverPhaseScope phaseScope);

}