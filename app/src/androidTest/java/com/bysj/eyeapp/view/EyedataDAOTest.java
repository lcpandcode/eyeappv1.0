package com.bysj.eyeapp.view;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by lcplcp on 2018/1/12.
 */

@RunWith(AndroidJUnit4.class)
public class EyedataDAOTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.lcplcp.view", appContext.getPackageName());
    }
}
