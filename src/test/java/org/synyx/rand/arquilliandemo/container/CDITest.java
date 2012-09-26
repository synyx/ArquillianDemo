/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.synyx.rand.arquilliandemo.container;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.synyx.rand.arquilliandronedemo.controller.LoginController;
import org.synyx.rand.arquilliandronedemo.domain.Credentials;
import org.synyx.rand.arquilliandronedemo.domain.User;

/**
 * @author Joachim Arrasz <arrasz@synyx.de>
 */
@RunWith(Arquillian.class)
public class CDITest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @Inject
    LoginController login;
    
    @Deployment
    public static Archive createDeployment() {
        System.out.println("Start Deployment Archive creation");

        WebArchive arch = ShrinkWrap.create(WebArchive.class, "login.war")
                .addClasses(Credentials.class, User.class, LoginController.class)
                .merge(ShrinkWrap.create(WebArchive.class).as(ExplodedImporter.class)
                .importDirectory(WEBAPP_SRC).as(WebArchive.class),
                "/", Filters.include(".*\\.xhtml$"))
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new StringAsset("<faces-config version=\"2.0\"/>"), "faces-config.xml");
        System.out.println(arch.toString(true));
        return arch;
    }

    @Test
    public void test() {
        Assert.assertNotNull(login);
    }
}