package org.synyx.rand.arquilliandemo.container;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import static org.junit.Assert.*;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.synyx.rand.arquilliandronedemo.controller.LoginController;
import org.synyx.rand.arquilliandronedemo.controller.UserController;
import org.synyx.rand.arquilliandronedemo.controller.util.JsfUtil;
import org.synyx.rand.arquilliandronedemo.dao.AbstractFacade;
import org.synyx.rand.arquilliandronedemo.domain.Credentials;
import org.synyx.rand.arquilliandronedemo.domain.User;

import java.io.File;

import java.util.Date;

import javax.inject.Inject;


/**
 * @author  Joachim Arrasz <arrasz@synyx.de>
 */
@RunWith(Arquillian.class)
public class JpaTest {

    private static final String WEBAPP_SRC = "src/main/webapp";
    private final String email = "user@userland.org";

    @Inject
    UserController userController;

    @Deployment
    public static Archive createDeployment() {

        System.out.println("Start Deployment Archive creation");

        WebArchive arch = ShrinkWrap.create(WebArchive.class, "login.war").addPackage(LoginController.class
                .getPackage()).addPackage(JsfUtil.class.getPackage()).addPackage(AbstractFacade.class.getPackage())
            .addPackage(Credentials.class.getPackage()).merge(ShrinkWrap.create(WebArchive.class).as(
                    ExplodedImporter.class).importDirectory(WEBAPP_SRC).as(WebArchive.class), "/",
                Filters.include(".*\\.xhtml$")).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("Bundle.properties", ArchivePaths.create("/classes/Bundle.properties"))
            .addAsWebInfResource(new StringAsset("<faces-config version=\"2.0\"/>"), "faces-config.xml")
            .addAsWebInfResource(new FileAsset(new File("src/main/resources/META-INF/persistence.xml")),
                "/classes/META-INF/persistence.xml");
        System.out.println(arch.toString(true));

        return arch;
    }


    @Test
    // @DataSet("test-data.xml") //just an option
    public void testControllerInjectionandJpaUpdates() {

        System.out.println("Started ControllerInjectionAndJpaUpdatesTest");

        final Date startDate = new Date();

        assertNotNull("Inject must not be null", userController);
        assertNotNull("User Backingbean must not be null", userController.getSelected());

        User user = userController.getSelected();
        user.setCreationDate(startDate);

        user.setEmail(email);
        user.setName("Max");
        user.setSurname("Mustermann");
        user.setLastLoginDate(new Date());
        userController.create();

        String output = String.format("Created User[%s] with email: %s", user.getId(), user.getEmail());
        System.out.println(output);

        user = userController.findByEmail(email);
        assertNotNull("User must not be null via emailfinder", user);

        // lets merge it
        Date updateDate = new Date();
        user.setLastLoginDate(updateDate);
        userController.update();
        user = userController.findByEmail(email);

        assertTrue("The Date must be later than startdate", startDate.before(user.getLastLoginDate()));
        System.out.println("Ended ControllerInjectionAndJpaUpdatesTest");
    }


    @Test(expected = Exception.class)
    public void testJpaDelete() {

        System.out.println("Started JpaDeleteTest");

        User user;

        // now delete it
        user = userController.findByEmail(email);
        System.out.println("Found User: " + user);
        assertNotNull("We should find a user!", user);

        userController.destroy();
        user = userController.findByEmail(email);
        assertNull("Now the user must be destroyed", user);
        System.out.println("Ended JpaDeleteTest");
    }
}
