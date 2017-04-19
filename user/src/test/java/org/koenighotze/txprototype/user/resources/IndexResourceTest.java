package org.koenighotze.txprototype.user.resources;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.*;
import org.springframework.mock.web.*;
import org.springframework.web.context.request.*;

public class IndexResourceTest {
    @Test
    public void index_resource_contains_self_rel_link() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        IndexResource indexResource = new IndexResource();

        assertThat(indexResource.getLink("self")).isNotNull();
    }

    @Test
    public void index_resource_contains_link_to_user_controller() {
    }
}