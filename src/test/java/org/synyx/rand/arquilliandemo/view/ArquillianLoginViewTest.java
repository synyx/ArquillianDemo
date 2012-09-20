/*
 * 20.09.2012
 */
package org.synyx.rand.arquilliandemo.view;

import com.thoughtworks.selenium.DefaultSelenium;
import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.synyx.rand.arquilliandronedemo.controller.LoginController;
import org.synyx.rand.arquilliandronedemo.domain.Credentials;
import org.synyx.rand.arquilliandronedemo.domain.User;

/**
 *
 * @author Joachim Arrasz <arrasz@synyx.de>
 */
@RunWith(Arquillian.class)
public class ArquillianLoginViewTest {

    private static final String WEBAPP_SRC = "src/main/webapp";
    
    @Drone
    DefaultSelenium browser;
    
    @ArquillianResource
    URL deploymentUrl;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "login.war")
                .addClasses(Credentials.class, User.class, LoginController.class)
                .addAsWebResource(new File(WEBAPP_SRC, "login.xhtml"))
                .addAsWebResource(new File(WEBAPP_SRC, "home.xhtml"))
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new StringAsset("<faces-config version=\"2.0\"/>"), "faces-config.xml");
    }

    @Test
    public void should_login_successfully() {
        browser.open(deploymentUrl + "login.jsf");

        browser.type("id=loginForm:username", "demo");
        browser.type("id=loginForm:password", "demo");
        browser.click("id=loginForm:login");
        browser.waitForPageToLoad("15000");

        assertTrue("User should be logged in!", browser.isElementPresent("xpath=//li[contains(text(), 'Welcome')]"));
    }
}