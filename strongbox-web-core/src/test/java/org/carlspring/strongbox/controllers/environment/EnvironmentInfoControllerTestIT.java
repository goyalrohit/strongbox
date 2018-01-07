package org.carlspring.strongbox.controllers.environment;

import org.carlspring.strongbox.controllers.context.IntegrationTest;
import org.carlspring.strongbox.rest.common.RestAssuredBaseTest;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Ordering;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.junit.Assert.*;

/**
 * @author Pablo Tirado
 */
@IntegrationTest
@RunWith(SpringRunner.class)
public class EnvironmentInfoControllerTestIT
        extends RestAssuredBaseTest
{

    @Inject
    private ObjectMapper mapper;

    @Test
    public void testGetEnvironmentAndSystemProperties()
            throws Exception
    {
        String path = "/configuration/environment/info";

        String envSystemProperties = given().contentType(MediaType.APPLICATION_JSON_VALUE)
                                            .when()
                                            .get(path)
                                            .prettyPeek()
                                            .asString();

        Map<String, List<EnvironmentInfo>> returnedMap = mapper.readValue(envSystemProperties,
                                                                          new TypeReference<Map<String, List<EnvironmentInfo>>>()
                                                                          {
                                                                          });

        assertNotNull("Failed to get environment and system properties list!", returnedMap);
        assertNotNull("Failed to get environment variables list!", returnedMap.get("environment"));
        assertFalse("Returned environment variables are empty", returnedMap.get("environment").isEmpty());
        assertNotNull("Failed to get system properties list!", returnedMap.get("system"));
        assertFalse("Returned system properties are empty", returnedMap.get("system").isEmpty());
    }

    @Test
    public void testGetEnvironmentAndSystemPropertiesCheckSorted()
            throws Exception
    {
        String path = "/configuration/environment/info";

        String envSystemProperties = given().contentType(MediaType.APPLICATION_JSON_VALUE)
                                            .when()
                                            .get(path)
                                            .prettyPeek()
                                            .asString();

        Map<String, List<EnvironmentInfo>> returnedMap = mapper.readValue(envSystemProperties,
                                                                          new TypeReference<Map<String, List<EnvironmentInfo>>>()
                                                                          {
                                                                          });

        List<EnvironmentInfo> environmentVariables = returnedMap.get("environment");
        assertNotNull("Failed to get environment variables list!", environmentVariables);
        assertTrue("Environment variables list is not sorted!", Ordering.natural().isOrdered(environmentVariables));

        List<EnvironmentInfo> systemProperties = returnedMap.get("system");
        assertNotNull("Failed to get system properties list!", systemProperties);
        assertTrue("System properties list is not sorted!", Ordering.natural().isOrdered(systemProperties));
    }
}