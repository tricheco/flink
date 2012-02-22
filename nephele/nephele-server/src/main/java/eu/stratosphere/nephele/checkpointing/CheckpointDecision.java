/***********************************************************************************************************************
 *
 * Copyright (C) 2010 by the Stratosphere project (http://stratosphere.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/

package eu.stratosphere.nephele.checkpointing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.stratosphere.nephele.execution.ResourceUtilizationSnapshot;
import eu.stratosphere.nephele.taskmanager.Task;

public final class CheckpointDecision {

	private static final Log LOG = LogFactory.getLog(CheckpointDecision.class);

	public static boolean getDecision(final Task task, final ResourceUtilizationSnapshot rus) {

		if (CheckpointUtils.isCheckpointingDisabled()) {
			return false;
		}

		if (rus.getForced() != null) {
			System.out.println("Checkpoint decision was forced to " + rus.getForced());
			// checkpoint decision was forced by the user
			return rus.getForced();
		}

		final double CPlower = CheckpointUtils.getCPLower();

		final double CPupper = CheckpointUtils.getCPUpper();

<<<<<<< HEAD
		if (rus.getPactRatio() >= 0 && CheckpointUtils.usePACT()) {
			System.out.println("Ratio = " + rus.getPactRatio());
			if (rus.getPactRatio() >= CPlower) {
				// amount of data is small so we checkpoint
				return true;
			}
			if (rus.getPactRatio() <= CPupper) {
				// amount of data is too big
				return false;
			}
		} else {
			// no info from upper layer so use average sizes
			if (rus.isDam() && CheckpointUtils.useAVG()) {
				System.out.println("is Dam");

				if (rus.getAverageOutputRecordSize() != 0) {
					System.out.println("avg ratio" + rus.getAverageInputRecordSize()
=======
		if (rus.getForced() == null) {
			if (rus.getPactRatio() != -1 && !CheckpointUtils.useAVG()) {
				System.out.println("Ratio = " + rus.getPactRatio());
				if (rus.getPactRatio() >= CPlower) {
					// amount of data is small so we checkpoint
					return true;
				}
				if (rus.getPactRatio() <= CPupper) {
					// amount of data is too big
					return false;
				}
			} else {
				// no info from upper layer so use average sizes
				if (rus.isDam()) {
					System.out.println("is Dam");

					if (rus.getAverageOutputRecordSize() != 0) {
						System.out.println("avg ratio " + rus.getAverageInputRecordSize()
>>>>>>> marrus_checkpointing
							/ rus.getAverageOutputRecordSize());
				}

				if (rus.getAverageOutputRecordSize() != 0 &&
							rus.getAverageInputRecordSize() / rus.getAverageOutputRecordSize() >= CPlower) {
					return true;
				}

				if (rus.getAverageOutputRecordSize() != 0 &&
							rus.getAverageInputRecordSize() / rus.getAverageOutputRecordSize() <= CPupper) {
					return false;
				}
			} else {

				// we have no data dam so we can estimate the input/output-ratio
				System.out.println("out " + rus.getTotalOutputAmount() + " in " + rus.getTotalInputAmount());
				if (rus.getTotalInputAmount() != 0) {
					System.out.println("selektivity is " + (double) rus.getTotalOutputAmount()
							/ rus.getTotalInputAmount());

				}
				if (rus.getTotalOutputAmount() != 0
						&& ((double) rus.getTotalInputAmount() / rus.getTotalOutputAmount() >= CPlower)) {
<<<<<<< HEAD
					// size off checkpoint would be to large: do not checkpoint
					// TODO progress estimation would make sense here
					System.out.println(task.getEnvironment().getTaskName() + "Checkpoint to large selektivity "
							+ ((double) rus.getTotalInputAmount() / rus.getTotalOutputAmount() > 2.0));
					return false;
=======
						// size off checkpoint would be to large: do not checkpoint
						// TODO progress estimation would make sense here
						System.out.println(task.getEnvironment().getTaskName() + " Checkpoint to large selektivity "
							+ ((double) rus.getTotalInputAmount() / rus.getTotalOutputAmount()));
						return false;
>>>>>>> marrus_checkpointing

				}
				if (rus.getTotalOutputAmount() != 0
						&& ((double) rus.getTotalInputAmount() / rus.getTotalOutputAmount() <= CPupper)) {
<<<<<<< HEAD
					// size of checkpoint will be small enough: checkpoint
					// TODO progress estimation would make sense here
					System.out.println(task.getEnvironment().getTaskName() + "Checkpoint to large selektivity "
							+ ((double) rus.getTotalInputAmount() / rus.getTotalOutputAmount() > 2.0));
					return true;
=======
						// size of checkpoint will be small enough: checkpoint
						// TODO progress estimation would make sense here 
						System.out.println(task.getEnvironment().getTaskName() + " Checkpoint small selektivity "
							+ ((double) rus.getTotalInputAmount() / rus.getTotalOutputAmount()));
						return true;

					}
>>>>>>> marrus_checkpointing

				}

			}
		}
		// between thresholds check CPU Usage.
		if (rus.getUserCPU() >= 90) {
			System.out.println(task.getEnvironment().getTaskName() + "CPU-Bottleneck");
			// CPU bottleneck
			return true;
		}
		System.out.println("Checkpoint decision false by default");
		// in case of doubt do not checkpoint
		return false;

	}
}
