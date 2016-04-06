package Kostya;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.After;
import org.junit.Before;

import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by KOT on 04.04.2016.
 */
public class HelloWorldTest extends TestCase{

    @Before
    public void testLocalTime(){
        int localTime = new Date().getHours();
        assertTrue("Время возвращает корректно", (localTime >= 0) && (localTime <= 24));
    }

    @After
    public void testCreateMyControl() throws Exception{
        HelloWorld helloWorld = new HelloWorld();
        ResourceBundle.Control myControl = helloWorld.createMyControl();
        assertNotNull(myControl);  // myControl создан (не пустой)
    }

//    @AfterClass
//    public static void testUserName(){
//        String userName = System.getenv().get("USERNAME");
//        assertTrue("Имя пользователя создано", (userName != null));
//    }

    public HelloWorldTest(String testName)
    {
        super( testName );
    }


    public static Test suite()
    {
        return new TestSuite( HelloWorldTest.class );
    }

    public void testApp()
    {
        assertTrue( true );
    }

}
