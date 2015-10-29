package tests.student.designatedsupports;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.SeleniumBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 10/28/15.
 */
public class ColorContrastTest extends SeleniumBaseTest {
    private static final String COLOR_CONTRAST_SELECT_CSS_SELECTOR =  "#accs-type-SBAC-Math-3-MATH-3-colorchoices";

    private static final String BLACK_ON_ROSE_OPTION = "TDS_CCMagenta";

    private static final String YELLOW_ON_BLUE_OPTION =  "TDS_CCYellowB";

    private static final String MEDIUM_GRAY_ON_LIGHT_GRAY_OPTION = "TDS_CCMedGrayLtGray";

    private static final String REVERSE_CONTRAST_OPTION = "TDS_CCInvert";

    @Before
    public void loginAndSetup() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 3
        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();
        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select ELA HS Test
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);
    }

    @Test
    public void testBlackOnRose() {
        driver.findElement(By.cssSelector(
                COLOR_CONTRAST_SELECT_CSS_SELECTOR + " option[value='" + BLACK_ON_ROSE_OPTION + "']")).click();
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());

        WebElement htmlBodyEl = driver.findElement(By.cssSelector("body#htmlBody"));
        assertTrue(htmlBodyEl.getAttribute("class").contains(BLACK_ON_ROSE_OPTION));
        assertEquals(htmlBodyEl.getCssValue("background-color"), "rgba(255, 208, 255, 1)");
    }

    @Test
    public void testYellowOnBlue() {
        driver.findElement(By.cssSelector(
                COLOR_CONTRAST_SELECT_CSS_SELECTOR + " option[value='" + YELLOW_ON_BLUE_OPTION + "']")).click();
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());

        WebElement htmlBodyEl = driver.findElement(By.cssSelector("body#htmlBody"));
        assertTrue(htmlBodyEl.getAttribute("class").contains(YELLOW_ON_BLUE_OPTION));
        assertEquals(htmlBodyEl.getCssValue("background-color"), "rgba(0, 51, 153, 1)");
        assertEquals(htmlBodyEl.getCssValue("color"), "rgba(255, 204, 0, 1)");
    }

    @Test
    public void testGrayOnLtGray() {
        driver.findElement(By.cssSelector(
                COLOR_CONTRAST_SELECT_CSS_SELECTOR + " option[value='" + MEDIUM_GRAY_ON_LIGHT_GRAY_OPTION + "']")).click();
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());

        WebElement htmlBodyEl = driver.findElement(By.cssSelector("body#htmlBody"));
        assertTrue(htmlBodyEl.getAttribute("class").contains(MEDIUM_GRAY_ON_LIGHT_GRAY_OPTION));
        assertEquals(htmlBodyEl.getCssValue("background-color"), "rgba(229, 229, 229, 1)");
        assertEquals(htmlBodyEl.getCssValue("color"), "rgba(102, 102, 102, 1)");
    }

    @Test
    public void testReverseContrast() {
        driver.findElement(By.cssSelector(
                COLOR_CONTRAST_SELECT_CSS_SELECTOR + " option[value='" + REVERSE_CONTRAST_OPTION + "']")).click();
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());

        WebElement htmlBodyEl = driver.findElement(By.cssSelector("body#htmlBody"));
        assertTrue(htmlBodyEl.getAttribute("class").contains(REVERSE_CONTRAST_OPTION));
        assertEquals(htmlBodyEl.getCssValue("background-color"), "rgba(0, 0, 0, 1)");
        assertEquals(htmlBodyEl.getCssValue("color"), "rgba(255, 255, 255, 1)");
    }
}