package org.koenighotze.txprototype.user.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Example for using the ribbon load balancing
 *
 * @author David Schmitz
 */
@Controller
@RequestMapping(value = "/self", produces = APPLICATION_JSON_UTF8_VALUE)
public class SelfInvoker {
    private static final Logger LOGGER = getLogger(SelfInvoker.class);

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/{publicId}", method = GET)
    public HttpEntity<UserResource> ping(@PathVariable String publicId) {
        // UserAdministration is the Servicename as registered in consul
        discoveryClient.getServices().stream().forEach(s ->
        {
            LOGGER.info("Found service :  {}", s);

//            discoveryClient.getInstances("UserAdministration").
        });

        URI mapUri = UriComponentsBuilder.fromUri(loadBalancer.choose("UserAdministration").getUri())
                .build().encode().toUri();

        String userUri = new Traverson(mapUri, MediaTypes.HAL_JSON)
                .follow("users").asLink().getHref();

//        String uri = UriComponentsBuilder.fromUri(loadBalancer.choose("UserAdministration").getUri())
//                .pathSegment("users")
//                .pathSegment(publicId)
//                .build().encode().toUriString();

//        new Traverson(UriComponentsBuilder.fromHttpUrl("http://UserAdministration/users/").build().toUri(), MediaType.APPLICATION_JSON)
//                .follow("$._links.collection")
//                .asLink().getHref();

        String target = UriComponentsBuilder.fromHttpUrl(userUri).pathSegment(publicId).toUriString();
        LOGGER.info("Looking up stuff at {}", target);
        ResponseEntity<UserResource> exchange = new RestTemplate().exchange(
                target,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<UserResource>() {
                },
                publicId);


        return new ResponseEntity<>(exchange.getBody(), OK);
    }
}
