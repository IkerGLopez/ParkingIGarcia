package com.lksnext.parkingplantilla;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.os.Handler;
import android.os.Looper;

import com.google.firebase.auth.FirebaseAuth;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.lksnext.parkingplantilla.view.activity.RegisterActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RegisterInstrumentedTest {

//    private final CountingIdlingResource idlingResource = new CountingIdlingResource("AuthIdlingResource");
//
//    @Before
//    public void setUp() {
//        IdlingRegistry.getInstance().register(idlingResource);
//    }
//
//    @After
//    public void tearDown() {
//        IdlingRegistry.getInstance().unregister(idlingResource);
//    }
//
//    @Test
//    public void test1SuccessfulRegistration() {
//        try (ActivityScenario<RegisterActivity> ignored = ActivityScenario.launch(RegisterActivity.class)) {
//            // Introducir datos válidos
//            onView(withId(R.id.email)).perform(replaceText("newemail@email.com"));
//            onView(withId(R.id.password)).perform(replaceText("password1"));
//            onView(withId(R.id.password2)).perform(replaceText("password1"));
//
//            // Incrementar el contador del IdlingResource
//            idlingResource.increment();
//
//            // Hacer clic en el botón de registro
//            onView(withId(R.id.btnRegister)).perform(click());
//
//            // Agregar un listener para detectar cambios en el estado de autenticación
//            FirebaseAuth.getInstance().addAuthStateListener(auth -> {
//                if (auth.getCurrentUser() != null) {
//                    idlingResource.decrement(); // Decrementar cuando el usuario esté autenticado
//                }
//            });
//
//            // Verificar que se lanza la actividad principal
//            onView(withId(R.id.fragmentContainerView)).check(matches(isDisplayed()));
//
//            // Eliminar el usuario creado
//            FirebaseAuth.getInstance().getCurrentUser().delete()
//                    .addOnCompleteListener(task -> {
//                        if (!task.isSuccessful()) {
//                            System.err.println("Error al eliminar el usuario de prueba.");
//                        }
//                    });
//        }
//    }
//
//    @Test
//    public void test2EmailAlreadyRegistered() {
//        try (ActivityScenario<RegisterActivity> ignored = ActivityScenario.launch(RegisterActivity.class)) {
//            // Introducir un correo ya registrado
//            onView(withId(R.id.email)).perform(replaceText("u3@u.com"));
//            onView(withId(R.id.password)).perform(replaceText("password1"));
//            onView(withId(R.id.password2)).perform(replaceText("password1"));
//
//            // Hacer clic en el botón de registro
//            onView(withId(R.id.btnRegister)).perform(click());
//
//            // Verificar que se muestra el mensaje de error
//            onView(withText("El correo ya está registrado."))
//                    .check(matches(isDisplayed()));
//        }
//    }
//
//    @Test
//    public void test3PasswordsDoNotMatch() {
//        try (ActivityScenario<RegisterActivity> ignored = ActivityScenario.launch(RegisterActivity.class)) {
//            // Introducir contraseñas que no coinciden
//            onView(withId(R.id.email)).perform(replaceText("tesssst@example.com"));
//            onView(withId(R.id.password)).perform(replaceText("password123"));
//            onView(withId(R.id.password2)).perform(replaceText("differentPassword"));
//
//            // Hacer clic en el botón de registro
//            onView(withId(R.id.btnRegister)).perform(click());
//
//            // Verificar que se muestra el mensaje de error
//            onView(withId(com.google.android.material.R.id.snackbar_text))
//                    .check(matches(withText("Las contraseñas no coinciden.")));
//        }
//    }
}