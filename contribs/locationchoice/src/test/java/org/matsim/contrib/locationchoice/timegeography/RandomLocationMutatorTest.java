/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2009 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.contrib.locationchoice.timegeography;

import java.util.Random;

import org.junit.Rule;
import org.junit.Test;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.locationchoice.Initializer;
import org.matsim.core.scenario.MutableScenario;
import org.matsim.testcases.MatsimTestUtils;


public class RandomLocationMutatorTest  {

	@Rule
	public MatsimTestUtils utils = new MatsimTestUtils();


	private MutableScenario scenario;

	private RandomLocationMutator initialize() {
		Initializer initializer = new Initializer();
		initializer.init(utils);
		scenario = (MutableScenario) initializer.getControler().getScenario();
		return new RandomLocationMutator(scenario, new Random(1111));
	}

	/*
	 * TODO: Construct scenario with knowledge to compare plans before and after loc. choice
	 */
	@Test public void testHandlePlan() {
		RandomLocationMutator randomlocationmutator = this.initialize();
		randomlocationmutator.run(scenario.getPopulation().getPersons().get(Id.create("1", Person.class)).getSelectedPlan());
	}
}
