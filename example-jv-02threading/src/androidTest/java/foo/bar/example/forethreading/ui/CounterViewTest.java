package foo.bar.example.forethreading.ui;

import android.app.Activity;
import android.app.Application;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import foo.bar.example.forethreading.ProgressBarIdler;
import foo.bar.example.forethreading.R;
import foo.bar.example.forethreading.feature.counter.CounterWithLambdas;
import foo.bar.example.forethreading.feature.counter.CounterWithProgress;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Here we make sure that the view elements accurately reflect the state of the models
 * and that clicking the buttons results in the correct action being performed
 */
@RunWith(AndroidJUnit4.class)
public class CounterViewTest {


    private CounterWithLambdas mockCounterWithLambdas;
    private CounterWithProgress mockCounterWithProgress;


    @Before
    public void setup(){

        //MockitoAnnotations.initMocks(CounterView.this);
        System.setProperty("dexmaker.dexcache", InstrumentationRegistry.getTargetContext().getCacheDir().getPath());

        mockCounterWithLambdas = mock(CounterWithLambdas.class);
        mockCounterWithProgress = mock(CounterWithProgress.class);

        Application application = (Application)getInstrumentation().getTargetContext().getApplicationContext();
        application.registerActivityLifecycleCallbacks(new ProgressBarIdler());
    }

    @Test
    public void initialState() throws Exception {

        //arrange
        new StateBuilder(mockCounterWithLambdas, mockCounterWithProgress)
                .counterWithProgressIsBusy(true)
                .counterWithProgressProgressValue(7)
                .counterWithProgressCount(40)
                .counterBasicIsBusy(false)
                .counterBasicCount(0)
                .createRule()
                .launchActivity(null);

        //act

        //assert
        onView(withId(R.id.counterwprog_increase_btn)).check(matches(not(isEnabled())));
        onView(withId(R.id.counterwprog_busy_progress)).check(matches(isDisplayed()));
        onView(withId(R.id.counterwprog_progress_txt)).check(matches(withText("7")));
        onView(withId(R.id.counterwprog_current_txt)).check(matches(withText("40")));
        onView(withId(R.id.counterwlambda_increase_btn)).check(matches(isEnabled()));
        onView(withId(R.id.counterwlambda_busy_progress)).check(matches(not(isDisplayed())));
        onView(withId(R.id.counterwlambda_current_txt)).check(matches(withText("0")));
    }

    @Test
    public void rotationState() throws Exception {

        //arrange
        Activity activity = new StateBuilder(mockCounterWithLambdas, mockCounterWithProgress)
                .counterWithProgressIsBusy(true)
                .counterWithProgressProgressValue(7)
                .counterWithProgressCount(40)
                .counterBasicIsBusy(false)
                .counterBasicCount(0)
                .createRule()
                .launchActivity(null);
        activity.setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);

        //act
        activity.setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        //assert
        onView(withId(R.id.counterwprog_increase_btn)).check(matches(not(isEnabled())));
        onView(withId(R.id.counterwprog_busy_progress)).check(matches(isDisplayed()));
        onView(withId(R.id.counterwprog_progress_txt)).check(matches(withText("7")));
        onView(withId(R.id.counterwprog_current_txt)).check(matches(withText("40")));
        onView(withId(R.id.counterwlambda_increase_btn)).check(matches(isEnabled()));
        onView(withId(R.id.counterwlambda_busy_progress)).check(matches(not(isDisplayed())));
        onView(withId(R.id.counterwlambda_current_txt)).check(matches(withText("0")));
    }

    @Test
    public void clickCallsBasicModel() throws Exception {
        //arrange
        new StateBuilder(mockCounterWithLambdas, mockCounterWithProgress)
                .counterBasicIsBusy(false)
                .createRule()
                .launchActivity(null);
        //act
        onView(withId(R.id.counterwlambda_increase_btn)).perform(click());

        //assert
        verify(mockCounterWithLambdas).increaseBy20();
    }

    @Test
    public void clickCallsWithProgressModel() throws Exception {
        //arrange
        new StateBuilder(mockCounterWithLambdas, mockCounterWithProgress)
                .counterWithProgressIsBusy(false)
                .createRule()
                .launchActivity(null);

        //act
        onView(withId(R.id.counterwprog_increase_btn)).perform(click());

        //assert
        verify(mockCounterWithProgress).increaseBy20();
    }
}
