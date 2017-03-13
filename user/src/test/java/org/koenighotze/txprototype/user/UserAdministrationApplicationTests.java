package org.koenighotze.txprototype.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {UserAdministrationApplication.class})
@DirtiesContext
public class UserAdministrationApplicationTests {

    @Test
    public void contextLoads() {
    }
}
