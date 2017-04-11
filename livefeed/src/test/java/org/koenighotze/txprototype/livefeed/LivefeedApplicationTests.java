package org.koenighotze.txprototype.livefeed;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.annotation.*;
import org.springframework.test.context.junit4.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT, classes = {LivefeedApplication.class})
@DirtiesContext
public class LivefeedApplicationTests {

    @Test
    public void contextLoads() {
    }

}
