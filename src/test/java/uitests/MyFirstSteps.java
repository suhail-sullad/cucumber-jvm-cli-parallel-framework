package uitests;

import java.io.File;
import java.io.IOException;

import org.sikuli.script.ImagePath;
import org.sikuli.script.Screen;

import com.jacob.com.LibraryLoader;

import core.sikuli.SikuliHandler;
import plugins.autoitinitilization.*;
import autoitx4java.AutoItX;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MyFirstSteps {
	private AutoItX window;

	@Given("^i am on login page$")
	public void i_am_on_login_page() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		// throw new PendingException();
		/*AutoItInit.registerdlls();
		AutoItInit.loadjacobx64dll();*/
		/*window=new AutoItX();
		Runtime.getRuntime().exec("C://windows//notepad.exe");
		window.winWaitActive("Untitled - Notepad");*/
		SikuliHandler sk = new SikuliHandler("C:\\Users\\CHAMP ONE\\Desktop");
		sk.performclick("simple.png");
		

	}

	@When("^i enter the login credentials$")
	public void i_enter_the_login_credentials() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		//throw new PendingException();
		//window.send("This is some text.");
		
	}

	@Then("^appropriate action has to be executed$")
	public void appropriate_action_has_to_be_executed() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		// throw new PendingException();
/*		window.winClose("Untitled - Notepad");
		window.winWaitActive("Notepad", "Save");
		window.send("!n");*/

	}

}
