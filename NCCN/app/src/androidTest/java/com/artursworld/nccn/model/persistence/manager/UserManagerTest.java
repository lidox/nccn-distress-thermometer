package com.artursworld.nccn.model.persistence.manager;

import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.artursworld.nccn.controller.util.Bits;
import com.artursworld.nccn.model.entity.User;

import org.junit.Test;

public class UserManagerTest extends InstrumentationTestCase {

    private UserManager userDb;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), "test_");
        userDb = new UserManager(context);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }


    @Test
    public void testRename(){
        User newUser = new User("Artur");
        userDb.insertUser(newUser);

        // update name
        String updatedName = "Arturo";
        newUser.setName(updatedName);
        userDb.update(newUser);

        User result = userDb.getUserByName(updatedName);
        assertEquals(updatedName, result.getName());

        User notExistingUser = userDb.getUserByName("Artur");
        assertNull(notExistingUser);
    }
}
