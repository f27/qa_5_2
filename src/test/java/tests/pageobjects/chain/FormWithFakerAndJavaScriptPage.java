package tests.pageobjects.chain;

import com.codeborne.selenide.Configuration;

import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.FileUtils.readStringFromFile;
import static utils.JsonUtils.mapFromJson;

public class FormWithFakerAndJavaScriptPage {

    public FormWithFakerAndJavaScriptPage fillForm(Map<String,String> userData) {
        $(".practice-form-wrapper").shouldHave(text(userData.get("Form Title")));
        $("#firstName").setValue(userData.get("First Name"));
        $("#lastName").setValue(userData.get("Last Name"));
        $("#userEmail").setValue(userData.get("Email"));
        $("#genterWrapper").$(byText(userData.get("Gender"))).scrollIntoView(true).click();
        $("#userNumber").setValue(userData.get("Mobile"));
        $("#dateOfBirthInput").scrollIntoView(true).click();
        fillDatePicker(userData.get("Year Of Birth"), userData.get("Month Of Birth"), userData.get("Day Of Birth"));
        for (String subject : userData.get("Subjects").split(", ")) {
            addSubject(subject);
        }
        Configuration.clickViaJs = true; //Click via javascript for headless
        for (String hobby : userData.get("Hobbies").split(", ")) {
            addHobby(hobby);
        }
        Configuration.clickViaJs = false;
        $("#uploadPicture").uploadFromClasspath(userData.get("Picture"));
        $("#currentAddress").setValue(userData.get("Address"));
        $("#state").scrollIntoView(true).click();
        $("#state").find(byText(userData.get("State"))).scrollIntoView(true).click();
        $("#city").scrollIntoView(true).click();
        $("#city").find(byText(userData.get("City"))).scrollIntoView(true).click();
        $("#submit").scrollIntoView(true).click();

        return this;
    }

    public FormWithFakerAndJavaScriptPage closeModal() {
        $("#closeLargeModal").scrollIntoView(true).click();
        $(".modal-content").shouldNotBe(visible);

        return this;
    }

    public FormWithFakerAndJavaScriptPage checkData(Map<String, String> expectedData) {
        String js = readStringFromFile("./src/test/resources/js/get_table_data.js");
        Map<String, String> actualData = mapFromJson(executeJavaScript(js));

        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            assertThat(actualData.get(entry.getKey()), is(entry.getValue()));
        }

        return this;
    }

    private static void fillDatePicker(String year, String month, String day) {
        $(".react-datepicker__month-select").selectOption(month);
        $(".react-datepicker__year-select").selectOption(year);
        $(String.format(".react-datepicker__day--0%s:not(.react-datepicker__day--outside-month)", day)).scrollIntoView(true).click();
    }

    private static void addSubject(String subject) {
        $("#subjectsInput").setValue(subject);
        $("#react-select-2-option-0").scrollIntoView(true).click();
    }

    private static void addHobby(String hobby) {
        $("#hobbiesWrapper").$(byText(hobby)).scrollIntoView(true).click();
    }

}
