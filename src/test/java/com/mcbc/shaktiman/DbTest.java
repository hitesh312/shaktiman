package com.mcbc.shaktiman;

import com.mcbc.shaktiman.common.DbOps;
import com.mcbc.shaktiman.common.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DbTest {

    @Test
    public void crudTest(){
        User user = new User("a","a","a","b",0,0);
        DbOps.addUser(user);
        Assert.assertEquals(user,DbOps.getUser(user.getUserid()));
        user.setName("hitesh");
        DbOps.updateUser(user);
        Assert.assertEquals(user,DbOps.getUser(user.getUserid()));
        DbOps.deleteUser(user.getUserid());
    }
}
