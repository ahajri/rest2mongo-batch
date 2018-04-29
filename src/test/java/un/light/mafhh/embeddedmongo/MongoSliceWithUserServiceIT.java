package un.light.mafhh.embeddedmongo;

import static org.junit.Assert.*;
import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import un.light.mafhh.collection.User;
import un.light.mafhh.mongo.config.SystemProfileValueSource2;
import un.light.mafhh.service.impl.UserServiceImpl;

//@ProfileValueSourceConfiguration(value = SystemProfileValueSource2.class)
//@IfProfileValue(name = ACTIVE_PROFILES_PROPERTY_NAME, value = "it-embedded") //"spring.profiles.active"
//@RunWith(SpringRunner.class)
//@DataMongoTest(includeFilters = @Filter(Service.class))
public class MongoSliceWithUserServiceIT {

	
	@Autowired
    private UserServiceImpl userService;

    @Test
    public void ensureLoggingWorks() {
        User userRecord = new User("TestServiceUser", "TestServiceUserPwd");
        User logWithPK = userService.save(userRecord);
        assertNotNull(logWithPK.getId());
        assertEquals(userRecord.getPassword(), logWithPK.getPassword());
    }
}
