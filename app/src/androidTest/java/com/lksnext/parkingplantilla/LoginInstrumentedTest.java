package com.lksnext.parkingplantilla;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.lksnext.parkingplantilla.view.activity.LoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginInstrumentedTest {

    @Before
    public void setUp() {
        // Cerrar sesión antes de cada prueba
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void test1Login() {
        try (ActivityScenario<LoginActivity> ignored = ActivityScenario.launch(LoginActivity.class)) {
            final String email = "test@test.com";
            final String password = "testpassword";

            onView(withId(R.id.email)).perform(typeText(email));
            onView(withId(R.id.password)).perform(typeText(password));

            onView(withId(R.id.email)).check(matches(withText(email)));
            onView(withId(R.id.password)).check(matches(withText(password)));

            onView(withId(R.id.btnLogin)).perform(click());
        }
    }

    @Test
    public void test2SignUp() {
        try (ActivityScenario<LoginActivity> ignored = ActivityScenario.launch(LoginActivity.class)) {
            final String email = "test2@test.com";
            final String password = "testpassword";

            onView(withId(R.id.register)).perform(click());

            onView(withId(R.id.email)).perform(typeText(email));
            onView(withId(R.id.password)).perform(typeText(password));

            onView(withId(R.id.email)).check(matches(withText(email)));
            onView(withId(R.id.password)).check(matches(withText(password)));
        }
    }

    @Test
    public void test3ForgotPassword() {
        try (ActivityScenario<LoginActivity> ignored = ActivityScenario.launch(LoginActivity.class)) {
            final String email = "test@test.com";

            // Verificar que el botón de "forgotPassword" está visible y hacer clic
            onView(withId(R.id.forgotPassword)).check(matches(isDisplayed()));
            onView(withId(R.id.forgotPassword)).perform(click());

            // Interactuar con el campo de email en el diálogo
            onView(withId(R.id.dialog_email)).perform(typeText(email));
            onView(withId(R.id.dialog_email)).check(matches(withText(email)));

            // Confirmar la acción en el diálogo
            onView(withText("Confirmar")).perform(click());
        }
    }

    @After
    public void tearDown() {
        // Cerrar sesión después de cada prueba
        FirebaseAuth.getInstance().signOut();
    }
}