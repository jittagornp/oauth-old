///*
// * Copyright 2017-2019 Pamarin.com
// */
//package com.pamarin.oauth2.model;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//
///**
// * @author jittagornp <http://jittagornp.me>
// * create : 2017/10/08
// */
//public class ErrorResponse_makeRedirectUriTest {
//
//    @Rule
//    public final ExpectedException exception = ExpectedException.none();
//
//    @Test
//    public void shouldBeThrowIllegalArgumentException_whenInputIsNull() {
//
//        exception.expect(IllegalArgumentException.class);
//        exception.expectMessage("Required redirectUri.");
//
//        String input = null;
//        String output = ErrorResponse.invalidRequest().makeRedirectUri(input);
//    }
//
//    @Test
//    public void shouldBeThrowIllegalArgumentException_whenInputIsEmptyString() {
//
//        exception.expect(IllegalArgumentException.class);
//        exception.expectMessage("Required redirectUri.");
//
//        String input = "";
//        String output = ErrorResponse.invalidRequest().makeRedirectUri(input);
//    }
//
//    @Test
//    public void shouldBeOk_whenValidInputAndEmptyParameters() {
//        String input = "https://pamarin.com";
//        String output = ErrorResponse.invalidRequest().makeRedirectUri(input);
//        String expected = "https://pamarin.com?error=invalid_request";
//    }
//
//    @Test
//    public void shouldBeOk_whenValidInputAndHaveSomeParameters() {
//        String input = "https://pamarin.com?state=XYZ";
//        String output = ErrorResponse.invalidRequest().makeRedirectUri(input);
//        String expected = "https://pamarin.com?state=XYZ&error=invalid_request";
//    }
//
//}
