/*
 * 20.09.2012
 */
package org.synyx.rand.arquilliandemo.view;

import com.thoughtworks.selenium.DefaultSelenium;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import static org.junit.Assert.*;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.synyx.rand.arquilliandronedemo.controller.LoginController;
import org.synyx.rand.arquilliandronedemo.domain.Credentials;
import org.synyx.rand.arquilliandronedemo.domain.User;

import java.net.URL;


/**
 * @author  Joachim Arrasz <arrasz@synyx.de>
 */
@RunWith(Arquillian.class)
public class LoginViewTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @Drone
    DefaultSelenium browser;

    @ArquillianResource
    URL deploymentUrl;

    @Deployment(testable = false)
    public static Archive createDeployment() {

        System.out.println("Start Deployment Archive creation");

        WebArchive arch = ShrinkWrap.create(WebArchive.class, "login.war").addClasses(Credentials.class, User.class,
                LoginController.class).merge(ShrinkWrap.create(WebArchive.class).as(ExplodedImporter.class)
                .importDirectory(WEBAPP_SRC).as(WebArchive.class), "/", Filters.include(".*\\.xhtml$"))
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml").addAsWebInfResource(new StringAsset(
                    "<faces-config version=\"2.0\"/>"), "faces-config.xml");
        System.out.println(arch.toString(true));

        return arch;
    }


    @Test
    public void should_login_successful() {

        String url = deploymentUrl + "faces/index.xhtml";
        browser.open(url);

        browser.type("id=loginForm:username", "demo");
        browser.type("id=loginForm:password", "demo");
        browser.click("id=loginForm:login");
        browser.waitForPageToLoad("15000");

        assertTrue("User should be logged in!", browser.isElementPresent("xpath=//li[contains(text(), 'Welcome')]"));
    }


    @Test
    public void should_login_NOT_successful() {

        String url = deploymentUrl + "faces/index.xhtml";
        browser.open(url);

        browser.type("id=loginForm:username", "demo");
        browser.type("id=loginForm:password", "doofesPasswd");
        browser.click("id=loginForm:login");
        browser.waitForPageToLoad("15000");

        assertFalse("User should be NOT logged in!",
            browser.isElementPresent("xpath=//li[contains(text(), 'Welcome')]"));
        assertTrue("User should be NOT logged in!",
            browser.isElementPresent("xpath=//li[contains(text(), 'Incorrect')]"));
    }
}
