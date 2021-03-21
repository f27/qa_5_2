package tests.pageobjects;

import com.codeborne.selenide.Configuration;

import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class FormWithFakerAndJavaScriptPage {
    public static void openForm() {
        open("https://demoqa.com/automation-practice-form");
    }

    public static void checkCloseButton() {
        $("#closeLargeModal").scrollIntoView(true).click();
        $(".modal-content").shouldNotBe(visible);
    }

    public static void fillForm(Map<String, String> userData) {
        $(".practice-form-wrapper").shouldHave(text(userData.get("Form Title")));
        $("#firstName").setValue(userData.get("First Name"));
        $("#lastName").setValue(userData.get("Last Name"));
        $("#userEmail").setValue(userData.get("Email"));
        $("#genterWrapper").$(byText(userData.get("Gender"))).scrollIntoView(true).click();
        $("#userNumber").setValue(userData.get("Mobile"));
        $("#dateOfBirthInput").scrollIntoView(true).click();
        $(".react-datepicker__month-select").selectOption(userData.get("Month Of Birth"));
        $(".react-datepicker__year-select").selectOption(userData.get("Year Of Birth"));
        $(String.format(".react-datepicker__day--0%s:not(.react-datepicker__day--outside-month)", userData.get("Day Of Birth"))).scrollIntoView(true).click();
        for (String subject : userData.get("Subjects").split(", ")) {
            $("#subjectsInput").setValue(subject);
            $("#react-select-2-option-0").scrollIntoView(true).click();
        }
        Configuration.clickViaJs = true; //Click via javascript for headless
        for (String hobby : userData.get("Hobbies").split(", ")) {
            $("#hobbiesWrapper").$(byText(hobby)).scrollIntoView(true).click();
        }
        Configuration.clickViaJs = false;
        $("#uploadPicture").uploadFromClasspath(userData.get("Picture"));
        $("#currentAddress").setValue(userData.get("Address"));
        $("#state").scrollIntoView(true).click();
        $("#state").find(byText(userData.get("State"))).scrollIntoView(true).click();
        $("#city").scrollIntoView(true).click();
        $("#city").find(byText(userData.get("City"))).scrollIntoView(true).click();
        $("#submit").scrollIntoView(true).click();

        $("#example-modal-sizes-title-lg").shouldHave(text(userData.get("Thanks Title")));
    }
}
