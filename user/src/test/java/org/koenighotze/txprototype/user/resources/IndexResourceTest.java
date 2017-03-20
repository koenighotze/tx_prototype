package org.koenighotze.txprototype.user.resources;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.Test;

public class IndexResourceTest {
    @Test
    public void index_resource_contains_self_rel_link() {
        IndexResource indexResource = new IndexResource();

        assertThat(indexResource.getLink("self")).isNotNull();
    }

    @Test
    public void index_resource_contains_link_to_user_controller() {
    }
}