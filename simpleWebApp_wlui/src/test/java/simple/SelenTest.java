package simple;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.firefox.FirefoxOptions;

public class SelenTest {

	private WebDriver driver;
	private Map<String, Object> vars;
	JavascriptExecutor js;

	@Before
	public void setUp() {
		FirefoxOptions options = new FirefoxOptions();
		options.addArguments("--headless");
		driver = new FirefoxDriver(options);
		driver.manage().window().maximize();
		js = (JavascriptExecutor) driver;
		vars = new HashMap<String, Object>();

	}

	@After
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void testLogin() {
		driver.get("http://localhost:8080/simple/");
		driver.manage().window().setSize(new Dimension(708, 688));
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("username")).sendKeys("johndoe123");
		driver.findElement(By.id("loginButton")).click();
		driver.findElement(By.id("bodyformat")).click();
	}

	/**
	 * insertAndDeleteTest method tests whether a successful inserted
	 */
	  @Test
	  public void insertAndDeleteTest() {
	    driver.get("http://localhost:8080/simple/");
	    driver.manage().window().setSize(new Dimension(1054, 725));
	    driver.findElement(By.id("username")).click();
	    driver.findElement(By.id("password")).sendKeys("123456");
	    driver.findElement(By.id("username")).sendKeys("johndoe123");
	    driver.findElement(By.id("loginButton")).click();
	    driver.findElement(By.cssSelector("#reportCaseButton > input:nth-child(2)")).click();
	    driver.findElement(By.id("newSpecies")).click();
	    driver.findElement(By.id("newSpecies")).sendKeys("Yellow Beetle");
	    driver.findElement(By.id("formSpecies")).click();
	    driver.findElement(By.id("dateReport")).click();
	    driver.findElement(By.id("dateReport")).sendKeys("2023-05-10");
	    driver.findElement(By.id("reporterName")).click();
	    driver.findElement(By.id("reporterName")).sendKeys("John Doe");
	    driver.findElement(By.id("province")).click();
	    {
	      WebElement dropdown = driver.findElement(By.id("province"));
	      dropdown.findElement(By.xpath("//option[. = 'Northwest Territories']")).click();
	    }
	    driver.findElement(By.cssSelector("option:nth-child(7)")).click();
	    driver.findElement(By.id("coordinates")).click();
	    driver.findElement(By.id("coordinates")).sendKeys("555,555");
	    driver.findElement(By.id("submitButton")).click();
	    driver.findElement(By.cssSelector(".container:nth-child(12) .Deleting")).click();
	  }

	@Test
	public void insertionTest() {
		driver.get("http://localhost:8080/simple/");
		driver.manage().window().setSize(new Dimension(708, 688));
		driver.findElement(By.cssSelector("body")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("username")).sendKeys("johndoe123");
		driver.findElement(By.id("loginButton")).click();
		driver.findElement(By.cssSelector("#reportCaseButton > input:nth-child(2)")).click();
		driver.findElement(By.id("newSpecies")).click();
		driver.findElement(By.id("newSpecies")).sendKeys("long");
		driver.findElement(By.id("newSpecies")).sendKeys(Keys.DOWN);
		driver.findElement(By.id("newSpecies")).sendKeys("brown spruce longhorn beetle");
		driver.findElement(By.id("dateReport")).click();
		driver.findElement(By.id("dateReport")).click();
		driver.findElement(By.id("dateReport")).sendKeys("2023-05-18");
		driver.findElement(By.id("reporterName")).click();
		driver.findElement(By.id("reporterName")).sendKeys("EE Tan");
		driver.findElement(By.id("formSpecies")).click();
		driver.findElement(By.id("province")).click();
		{
			WebElement dropdown = driver.findElement(By.id("province"));
			dropdown.findElement(By.xpath("//option[. = 'Alberta']")).click();
		}
		driver.findElement(By.cssSelector("option:nth-child(3)")).click();
		{
			WebElement dropdown = driver.findElement(By.id("province"));
			dropdown.findElement(By.xpath("//option[. = 'Nova Scotia']")).click();
		}
		driver.findElement(By.cssSelector("option:nth-child(8)")).click();
		driver.findElement(By.id("coordinates")).click();
		driver.findElement(By.id("coordinates")).sendKeys("777,888");
		driver.findElement(By.id("submitButton")).click();
	}

	@Test
	public void logintestFailErrorMsg() {
		driver.get("http://localhost:8080/simple/");
		driver.manage().window().setSize(new Dimension(708, 688));
		driver.findElement(By.cssSelector("form")).click();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).sendKeys("johndoe123");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).sendKeys("123");
		driver.findElement(By.id("loginButton")).click();
	}

	@Test
	public void refreshpageTest() {
		driver.get("http://localhost:8080/simple/");
		driver.manage().window().setSize(new Dimension(708, 688));
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("username")).sendKeys("johndoe123");
		driver.findElement(By.id("loginButton")).click();
		driver.findElement(By.cssSelector("#refreshButton > input:nth-child(1)")).click();
		driver.findElement(By.id("bodyformat")).click();
	}

	  @Test
	  public void reportCasePageBackButton() {
	    driver.get("http://localhost:8080/simple/");
	    driver.manage().window().setSize(new Dimension(1054, 725));
	    driver.findElement(By.cssSelector("label:nth-child(1)")).click();
	    driver.findElement(By.id("password")).sendKeys("123456");
	    driver.findElement(By.id("username")).sendKeys("johndoe123");
	    driver.findElement(By.id("loginButton")).click();
	    driver.findElement(By.cssSelector("#reportCaseButton > input:nth-child(2)")).click();
	    driver.findElement(By.id("insertBackButton")).click();
	  }

	@Test
	public void updatePageBackButtonTest() {
		driver.get("http://localhost:8080/simple/");
		driver.manage().window().setSize(new Dimension(708, 688));
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.id("username")).sendKeys("johndoe123");
		driver.findElement(By.id("loginButton")).click();
		driver.findElement(By.cssSelector(".container:nth-child(7)")).click();
		driver.findElement(By.cssSelector(".container:nth-child(7) .Updating")).click();
		driver.findElement(By.id("updateBackButton")).click();
		driver.findElement(By.id("bodyformat")).click();	
	}

	  @Test
	  public void insertAndUpdate() {
	    driver.get("http://localhost:8080/simple/");
	    driver.manage().window().setSize(new Dimension(1054, 725));
	    driver.findElement(By.id("password")).sendKeys("123456");
	    driver.findElement(By.id("username")).click();
	    driver.findElement(By.id("username")).sendKeys("johndoe123");
	    driver.findElement(By.id("loginButton")).click();
	    driver.findElement(By.cssSelector("#reportCaseButton > input:nth-child(2)")).click();
	    driver.findElement(By.id("newSpecies")).click();
	    driver.findElement(By.id("newSpecies")).sendKeys("Red Beetles");
	    driver.findElement(By.id("formSpecies")).click();
	    driver.findElement(By.id("dateReport")).click();
	    driver.findElement(By.id("dateReport")).sendKeys("2023-05-17");
	    driver.findElement(By.id("reporterName")).click();
	    driver.findElement(By.id("reporterName")).sendKeys("Thomas Lui");
	    driver.findElement(By.id("formSpecies")).click();
	    driver.findElement(By.id("province")).click();
	    {
	      WebElement dropdown = driver.findElement(By.id("province"));
	      dropdown.findElement(By.xpath("//option[. = 'Ontario']")).click();
	    }
	    driver.findElement(By.cssSelector("option:nth-child(10)")).click();
	    driver.findElement(By.id("coordinates")).click();
	    driver.findElement(By.id("coordinates")).sendKeys("222,222");
	    driver.findElement(By.id("formSpecies")).click();
	    driver.findElement(By.id("submitButton")).click();
	    driver.findElement(By.cssSelector(".container:nth-child(9) .Updating")).click();
	    driver.findElement(By.id("newSpecies")).click();
	    driver.findElement(By.id("newSpecies")).sendKeys("Red Green Beetle");
	    driver.findElement(By.id("reporterName")).click();
	    driver.findElement(By.id("reporterName")).sendKeys("Tom Lui");
	    driver.findElement(By.cssSelector("form:nth-child(9)")).click();
	    driver.findElement(By.id("province")).click();
	    {
	      WebElement dropdown = driver.findElement(By.id("province"));
	      dropdown.findElement(By.xpath("//option[. = 'Nova Scotia']")).click();
	    }
	    driver.findElement(By.cssSelector("option:nth-child(8)")).click();
	    driver.findElement(By.id("coordinates")).click();
	    driver.findElement(By.id("coordinates")).sendKeys("111,111");
	    driver.findElement(By.id("updateButton")).click();
	    driver.findElement(By.cssSelector(".container:nth-child(9) .Deleting")).click();
	  }
	  @Test
	  public void searchFilterTest() {
	    driver.get("http://localhost:8080/simple/");
	    driver.manage().window().setSize(new Dimension(1054, 725));
	    driver.findElement(By.id("username")).click();
	    driver.findElement(By.id("password")).sendKeys("123456");
	    driver.findElement(By.id("username")).sendKeys("johndoe123");
	    driver.findElement(By.id("loginButton")).click();
	    driver.findElement(By.id("speciesName_search")).click();
	    driver.findElement(By.id("speciesName_search")).sendKeys("Beetle");
	    driver.findElement(By.id("reporterName_search")).click();
	    driver.findElement(By.id("reporterName_search")).sendKeys("John");
	    driver.findElement(By.id("top")).click();
	    driver.findElement(By.id("provinceName_search")).click();
	    driver.findElement(By.id("provinceName_search")).sendKeys("a");
	    driver.findElement(By.id("provinceName_search")).sendKeys(Keys.DOWN);
	    driver.findElement(By.id("provinceName_search")).sendKeys("Alberta");
	    driver.findElement(By.id("searchButton")).click();
	    driver.findElement(By.id("top")).click();
	    driver.findElement(By.id("provinceName_search")).click();
	    driver.findElement(By.id("top")).click();
	    driver.findElement(By.id("speciesName_search")).click();
	    driver.findElement(By.id("speciesName_search")).sendKeys("Beetle");
	    driver.findElement(By.id("speciesName_search")).sendKeys(Keys.ENTER);
	  }

}
