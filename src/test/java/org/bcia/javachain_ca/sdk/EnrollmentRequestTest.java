/*
 *  Copyright 2016, 2017 DTCC, Fujitsu Australia Software Technology, IBM - All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.bcia.javachain_ca.sdk;

import org.bcia.javachain_ca.sdk.EnrollmentRequest;
import org.bcia.javachain_ca.sdk.exception.InvalidArgumentException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.String.format;

import java.security.KeyPair;

import static org.junit.Assert.*;

public class EnrollmentRequestTest {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentRequestTest.class);


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final String caName = "certsInc";
    private static final String csr = "11436845810603";
    private static final String profile = "test profile";
    private static final String label = "test label";
    private static final KeyPair keyPair = null;

    @Test
    public void testNewInstanceEmpty() {

        try {
            EnrollmentRequest testEnrollReq = new EnrollmentRequest();
            Assert.assertNull(testEnrollReq.getCsr());
            assertTrue(testEnrollReq.getHosts().isEmpty());
            Assert.assertNull(testEnrollReq.getProfile());
            Assert.assertNull(testEnrollReq.getLabel());
            Assert.assertNull(testEnrollReq.getKeyPair());

        } catch (Exception e) {
            Assert.fail("Unexpected Exception " + e.getMessage());
        }
    }

    @Test
    public void testNewInstanceParms() {

        try {
            EnrollmentRequest testEnrollReq = new EnrollmentRequest(profile, label, keyPair);
            Assert.assertNull(testEnrollReq.getCsr());
            assertTrue(testEnrollReq.getHosts().isEmpty());
            Assert.assertEquals(testEnrollReq.getProfile(), profile);
            Assert.assertEquals(testEnrollReq.getLabel(), label);
            Assert.assertNull(testEnrollReq.getKeyPair());

        } catch (Exception e) {
            Assert.fail("Unexpected Exception " + e.getMessage());
        }
    }

    @Test
    public void testEnrollReqSetGet() {

        try {
            EnrollmentRequest testEnrollReq = new EnrollmentRequest();
            testEnrollReq.addHost("d.com");
            testEnrollReq.setCsr(csr);
            testEnrollReq.setCSR(csr); // Unsure why there are two methods that
            // set csr
            testEnrollReq.setProfile(profile);
            testEnrollReq.setLabel(label);
            testEnrollReq.setKeyPair(null);
            testEnrollReq.setCAName(caName);
            Assert.assertEquals(testEnrollReq.getCsr(), csr);
            assertTrue(testEnrollReq.getHosts().contains("d.com"));
            Assert.assertEquals(testEnrollReq.getProfile(), profile);
            Assert.assertEquals(testEnrollReq.getLabel(), label);
            Assert.assertNull(testEnrollReq.getKeyPair());

        } catch (Exception e) {
            Assert.fail("Unexpected Exception " + e.getMessage());
        }
    }

    @Test
    public void testEnrollReqToJson() {

        try {
            EnrollmentRequest testEnrollReq = new EnrollmentRequest();
            testEnrollReq.addHost("d.com");
            testEnrollReq.setCsr(csr);
            testEnrollReq.setCSR(csr); // Two setters perform the same function
            testEnrollReq.setProfile(profile);
            testEnrollReq.setLabel(label);
            testEnrollReq.setKeyPair(null);
            testEnrollReq.setCAName(caName);
            log.info(format("<<%s>>", testEnrollReq.toJson()));
            assertTrue(testEnrollReq.toJson().contains(csr));

        } catch (Exception e) {
            Assert.fail("Unexpected Exception " + e.getMessage());
        }
    }

    @Test
    public void testEnrollReqToJsonAttr() throws Exception {

        EnrollmentRequest testEnrollReq = new EnrollmentRequest();
        testEnrollReq.addHost("d.com");
        testEnrollReq.setCsr(csr);
        testEnrollReq.setProfile(profile);
        testEnrollReq.setLabel(label);
        testEnrollReq.setKeyPair(null);
        testEnrollReq.setCAName(caName);
        testEnrollReq.addAttrReq("foo");
        testEnrollReq.addAttrReq("foorequired").setOptional(false);
        testEnrollReq.addAttrReq("foofalse").setOptional(true);

        String s = testEnrollReq.toJson();
        assertNotNull(s);
        assertTrue(s.contains("\"attr_reqs\":["));
        assertTrue(s.contains("\"name\":\"foorequired\",\"optional\":false"));
        assertTrue(s.contains("\"name\":\"foofalse\",\"optional\":true"));

    }

    @Test
    public void testEnrollReqToJsonAttrNotThere() throws Exception {

        EnrollmentRequest testEnrollReq = new EnrollmentRequest();
        testEnrollReq.addHost("d.com");
        testEnrollReq.setCsr(csr);
        testEnrollReq.setProfile(profile);
        testEnrollReq.setLabel(label);
        testEnrollReq.setKeyPair(null);
        testEnrollReq.setCAName(caName);

        String s = testEnrollReq.toJson();
        assertNotNull(s);
        assertFalse(s.contains("\"attr_reqs\":["));
    }

    @Test
    public void testEnrollReqToJsonAttrEmpty() throws Exception {

        EnrollmentRequest testEnrollReq = new EnrollmentRequest();
        testEnrollReq.addHost("d.com");
        testEnrollReq.setCsr(csr);
        testEnrollReq.setProfile(profile);
        testEnrollReq.setLabel(label);
        testEnrollReq.setKeyPair(null);
        testEnrollReq.setCAName(caName);
        testEnrollReq.addAttrReq(); // means empty. force no attributes.

        String s = testEnrollReq.toJson();
        assertNotNull(s);
        assertTrue(s.contains("\"attr_reqs\":[]"));
    }

    @Test
    public void testEnrollReqToJsonAttrNullName() throws Exception {
        thrown.expect(InvalidArgumentException.class);
        thrown.expectMessage("name may not be null or empty.");

        EnrollmentRequest testEnrollReq = new EnrollmentRequest();
        testEnrollReq.addAttrReq(null);

    }

    @Test
    public void testEnrollReqToJsonAttrEmptyName() throws Exception {
        thrown.expect(InvalidArgumentException.class);
        thrown.expectMessage("name may not be null or empty.");

        EnrollmentRequest testEnrollReq = new EnrollmentRequest();
        testEnrollReq.addAttrReq("");

    }

}