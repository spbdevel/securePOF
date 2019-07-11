package org.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.app.config.AppConfig;
import org.app.config.WebSecurity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.net.URI;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        WebSecurity.class,
        AppConfig.class
})
@PropertySources({
        @PropertySource("classpath:application.properties")
})
@EnableWebMvc
@WebAppConfiguration
public class WebTests {

    @Autowired
    private WebApplicationContext webApplicationContext;



    @Test
    @WithUserDetails("admin")
    public void checkDishes() throws Exception {
        URI uri = new URI("/rest/dishes");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(uri);
        mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        ResultActions res = mockMvc.perform(mockHttpServletRequestBuilder);
        MockHttpServletResponse response = res.andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        System.out.println("response: " + serializeToJson(jsonNode));
    }

    private String serializeToJson(Object d) throws JsonProcessingException {
        SimpleModule module = new SimpleModule();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.registerModule(module)
                .writer(new DefaultPrettyPrinter())
                .writeValueAsString(d);
    }

}
