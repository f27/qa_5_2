package tests.pageobjects.chain;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;

import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.FileUtils.readStringFromFile;
import static utils.JsonUtils.mapFromJson;

public class FormWithFakerAndJavaScriptPage {
    private static final SelenideElement
            formTitleField = $(".practice-form-wrapper"),
            firstNameField = $("#firstName"),
            lastNameField = $("#lastName"),
            emailField = $("#userEmail"),
            genderField = $("#genterWrapper"),
            mobileField = $("#userNumber"),
            datePickerField = $("#dateOfBirthInput"),
            yearField = $(".react-datepicker__year-select"),
            monthField = $(".react-datepicker__month-select"),
            subjectsInput = $("#subjectsInput"),
            firstSubject = $("#react-select-2-option-0"),
            hobbiesWrapper = $("#hobbiesWrapper"),
            pictureUpload = $("#uploadPicture"),
            addressField = $("#currentAddress"),
            stateField = $("#state"),
            cityField = $("#city"),
            submitButton = $("#submit"),
            modalWindow = $(".modal-content"),
            closeModalButton = $("#closeLargeModal");
    private static final String classOfDay = ".react-datepicker__day--0%s:not(.react-datepicker__day--outside-month)";

    public FormWithFakerAndJavaScriptPage fillForm(Map<String,String> userData) {
        formTitleField.shouldHave(text(userData.get("Form Title")));
        firstNameField.setValue(userData.get("First Name"));
        lastNameField.setValue(userData.get("Last Name"));
        emailField.setValue(userData.get("Email"));
        genderField.$(byText(userData.get("Gender"))).scrollIntoView(true).click();
        mobileField.setValue(userData.get("Mobile"));
        datePickerField.scrollIntoView(true).click();
        fillDatePicker(userData.get("Year Of Birth"), userData.get("Month Of Birth"), userData.get("Day Of Birth"));
        for (String subject : userData.get("Subjects").split(", ")) {
            addSubject(subject);
        }
        Configuration.clickViaJs = true; //Click via javascript for headless
        for (String hobby : userData.get("Hobbies").split(", ")) {
            addHobby(hobby);
        }
        Configuration.clickViaJs = false;
        pictureUpload.uploadFromClasspath(userData.get("Picture"));
        addressField.setValue(userData.get("Address"));
        stateField.scrollIntoView(true).click();
        stateField.find(byText(userData.get("State"))).scrollIntoView(true).click();
        cityField.scrollIntoView(true).click();
        cityField.find(byText(userData.get("City"))).scrollIntoView(true).click();
        submitButton.scrollIntoView(true).click();

        return this;
    }

    public FormWithFakerAndJavaScriptPage closeModal() {
        closeModalButton.scrollIntoView(true).click();
        modalWindow.shouldNotBe(visible);

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
        monthField.selectOption(month);
        yearField.selectOption(year);
        $(String.format(classOfDay, day)).scrollIntoView(true).click();
    }

    private static void addSubject(String subject) {
        subjectsInput.setValue(subject);
        firstSubject.scrollIntoView(true).click();
    }

    private static void addHobby(String hobby) {
        hobbiesWrapper.$(byText(hobby)).scrollIntoView(true).click();
    }

}
