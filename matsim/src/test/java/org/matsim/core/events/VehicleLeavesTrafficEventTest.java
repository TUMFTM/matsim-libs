/* *********************************************************************** *
 * project: org.matsim.*
 * AgentWait2LinkEventTest.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
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

package org.matsim.core.events;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.events.VehicleLeavesTrafficEvent;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.testcases.MatsimTestUtils;
import org.matsim.vehicles.Vehicle;

/**
 * @author tthunig
 */
public class VehicleLeavesTrafficEventTest {

	@Rule
	public MatsimTestUtils utils = new MatsimTestUtils();


	@Test public void testWriteReadXml() {
		final VehicleLeavesTrafficEvent event1 = new VehicleLeavesTrafficEvent(8463.7301, Id.create("483", Person.class),
				Id.create("783", Link.class), Id.create("veh7", Vehicle.class), TransportMode.car,1.0);
		final VehicleLeavesTrafficEvent event2 = XmlEventsTester.testWriteReadXml(utils.getOutputDirectory() + "events.xml", event1);
		assertEquals(event1.getTime(), event2.getTime(), MatsimTestUtils.EPSILON);
		assertEquals(event1.getPersonId().toString(), event2.getPersonId().toString());
		assertEquals(event1.getLinkId().toString(), event2.getLinkId().toString());
		assertEquals(event1.getVehicleId(), event2.getVehicleId());
		assertEquals(event1.getNetworkMode(), event2.getNetworkMode());
	}
}
