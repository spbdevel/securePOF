package org.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RequestMapping("/rest")
@RestController
public class TestController {

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestResponse> hello(Principal principal) {
        return new ResponseEntity<TestResponse>(
                new TestResponse("Happy Halloween, " + principal.getName() + "!"), HttpStatus.OK);
    }

    @Secured({ "ADMIN" })
    @RequestMapping(value = "/hello1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestResponse> hello1(@RequestHeader("Authorization") String authorization) {
        return new ResponseEntity<TestResponse>(
                new TestResponse("authorization: " + authorization + "!"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/hello2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestResponse> hello2(@RequestHeader Map<String,String> headers) {
        return new ResponseEntity<TestResponse>(
                new TestResponse("headers size: " + headers.size()), HttpStatus.OK);
    }

    public static class TestResponse {
        private String message;

        public TestResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
