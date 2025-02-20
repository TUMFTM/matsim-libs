package org.matsim.facilities;

import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;

public class ActivityWithOnlyFacilityIdTest {

    @Test
    public void testSiouxFallsWithOnlyFacilityIds() {
        URL scenarioURL = ExamplesUtils.getTestScenarioURL("siouxfalls-2014");

        Config config = ConfigUtils.loadConfig(IOUtils.extendUrl(scenarioURL, "config_default.xml"));
        config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
        config.controler().setLastIteration(1);

        Scenario scenario = ScenarioUtils.loadScenario(config);

        // Ensure, that there are activities with facilityIds.
        Set<Activity> activitiesWithFacilityIds = scenario.getPopulation().getPersons().values().stream()
                .flatMap(person -> person.getPlans().stream()).flatMap(plan -> plan.getPlanElements().stream())
                .filter(Activity.class::isInstance).map(Activity.class::cast).filter(act -> act.getFacilityId() != null)
                .collect(Collectors.toSet());
        assertTrue("Need at least some activities with facilityIds.", activitiesWithFacilityIds.size() > 0);

        // Remove all (redundant) coords and linkIds from activities with facilityIds.
        activitiesWithFacilityIds.forEach(act -> {
            act.setLinkId(null);
            act.setCoord(null);
        });

        // MATSim has to use coords/linkIds from facilities
        Controler controller = new Controler(scenario);
        controller.run();
    }
}
