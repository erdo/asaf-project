package foo.bar.example.foreadapters.ui;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 *
 * https://stackoverflow.com/questions/30361068/assert-proper-number-of-items-in-list-with-espresso
 *
 */
public class EspressoTestMatchers {

    public static Matcher<View> withRecyclerViewItems (final int size) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                return ((RecyclerView) view).getAdapter().getItemCount () == size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("RecycleView should have " + size + " items");
            }
        };
    }
}
