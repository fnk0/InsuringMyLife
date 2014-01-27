package com.gabilheri.insuringmylife;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class QuizActivity extends FragmentActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;

    public static ArrayList<Integer> radioGroups = new ArrayList<Integer>();

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Question 1";
                case 1:
                    return "Question 2";
                case 2:
                    return "Question 3";
                case 3:
                    return "Question 4";
                case 4:
                    return "Question 5";
                case 5:
                    return "Question 6";
            }
            return null;
        }
    }

    public static class DummySectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            int[] titles = {R.string.q1, R.string.q2, R.string.q3, R.string.q4, R.string.q5, R.string.q6};
            int[] questions = {R.array.array1, R.array.array2, R.array.array3, R.array.array4, R.array.array5, R.array.array6};


            String[] questionsArray = getResources().getStringArray(questions[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);

            View rootView = inflater.inflate(R.layout.fragment_quiz,
                    container, false);

            RadioGroup group = (RadioGroup) rootView.findViewById(R.id.quizAnswers);

            TextView titleView = (TextView) rootView.findViewById(R.id.quizTitles);
            titleView.setText(titles[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);

            RadioButton num1 = (RadioButton) rootView.findViewById(R.id.num1);
            num1.setText(questionsArray[0]);

            RadioButton num2 = (RadioButton) rootView.findViewById(R.id.num2);
            num2.setText(questionsArray[1]);

            RadioButton num3 = (RadioButton) rootView.findViewById(R.id.num3);
            num3.setText(questionsArray[2]);

            RadioButton num4 = (RadioButton) rootView.findViewById(R.id.num4);
            num4.setText(questionsArray[3]);

            RadioButton num5 = (RadioButton) rootView.findViewById(R.id.num5);
            num5.setText(questionsArray[4]);

            return rootView;
        }
    }

    public void submitQuiz(View v) {

        for(int i = 0; i < radioGroups.size(); i++) {

            Log.d("ANSWERS", "" + radioGroups.get(i));

        }

    }

}
