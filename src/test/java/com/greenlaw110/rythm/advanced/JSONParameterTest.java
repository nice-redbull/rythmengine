package com.greenlaw110.rythm.advanced;

import com.greenlaw110.rythm.TestBase;
import com.greenlaw110.rythm.utils.JSONWrapper;
import org.junit.Test;

/**
 * Test passing JSON string as template parameter
 */
public class JSONParameterTest extends TestBase {

    public static class User {
        public String name;
        public int age;
    }
    
    @Test
    public void testArray() {
        t = "@args List<com.greenlaw110.rythm.advanced.JSONParameterTest.User> users\n<ul>@for(users){\n@_.name: @_.age\n}</ul>";
        String params = "{users: [{\"name\":\"Tom\", \"age\": 12}, {\"name\":\"Peter\", \"age\": 11}]}";
        s = r(t, JSONWrapper.wrap(params));
        eq("<ul>\nTom: 12\nPeter: 11\n</ul>");
    }
    
    @Test
    public void testArray2() {
        t = "@args List<com.greenlaw110.rythm.advanced.JSONParameterTest.User> users\n<ul>@for(users){\n@_.name: @_.age\n}</ul>";
        String params = "[{\"name\":\"Tom\", \"age\": 12}, {\"name\":\"Peter\", \"age\": 11}]";
        s = r(t, JSONWrapper.wrap(params));
        eq("<ul>\nTom: 12\nPeter: 11\n</ul>");
    }
    
    @Test
    public void testObject() {
        t = "@args com.greenlaw110.rythm.advanced.JSONParameterTest.User user\n@user.name: @user.age";
        String params = "{user: {\"name\":\"Tom\", \"age\": 12}}";
        s = r(t, JSONWrapper.wrap(params));
        eq("Tom: 12");
    }

    public static void main(String[] args) {
        run(JSONParameterTest.class);
    }
}
