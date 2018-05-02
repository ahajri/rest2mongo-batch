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

import com.ahajri.heaven.calendar.collection.UserAuth;
import com.ahajri.heaven.calendar.mongo.config.SystemProfileValueSource2;
import com.ahajri.heaven.calendar.service.impl.UserServiceImpl;

//@ProfileValueSourceConfiguration(value = SystemProfileValueSource2.class)
//@IfProfileValue(name = ACTIVE_PROFILES_PROPERTY_NAME, value = "it-embedded") //"spring.profiles.active"
//@RunWith(SpringRunner.class)
//@DataMongoTest(includeFilters = @Filter(Service.class))
public class MongoSliceWithUserServiceIT {

	
	@Autowired
    private UserServiceImpl userService;

    @Test
    public void ensureLoggingWorks() {
        UserAuth userRecord = new UserAuth("TestServiceUser", "TestServiceUserPwd");
        UserAuth logWithPK = userService.save(userRecord);
        assertNotNull(logWithPK.getId());
        assertEquals(userRecord.getPassword(), logWithPK.getPassword());
    }
}
