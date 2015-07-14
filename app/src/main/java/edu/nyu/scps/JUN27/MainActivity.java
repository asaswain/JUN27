package edu.nyu.scps.JUN27;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private final String buttonNames[] = new String[]{
            "Asa",
            "Abdoulaye",
            "Alrick",
            "David",
            "Deepali",
            "Jaxon",
            "Jeffrey",
            "Joey",
            "Keenen",
            "Mark",
    };

    private final String packageNames[] = new String[]{
            "asa.scps.nyu.edu",
            "abdoulaye.scps.nyu.edu",
            "alrick.scps.nyu.edu",
            "david.scps.nyu.edu",
            "deepali.scps.nyu.edu",
            "jaxon.scps.nyu.edu",
            "jeffrey.scps.nyu.edu",
            "joey.scps.nyu.edu",
            "keenen.scps.nyu.edu",
            "mark.scps.nyu.edu",
    };

    private final String activityNames[] = new String[]{
            "AsaActivity",
            "AbdoulayeActivity",
            "AlrickActivity",
            "DavidActivity",
            "DeepaliActivity",
            "JaxonActivity",
            "JeffreyActivity",
            "JoeyActivity",
            "KeenenActivity",
            "MarkActivity",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources resources = getResources();

        final LinearLayout contents = (LinearLayout) findViewById(R.id.contents);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, // width
                ViewGroup.LayoutParams.WRAP_CONTENT  //height
        );

        layoutParams.setMargins(30, 20, 30, 20);

        // add listener for each color in color palette to control the color of the image drawn by the user

        ButtonOnClickListener buttonOnClickListener = new ButtonOnClickListener();

        for (int i = 0; i < activityNames.length; ++i) {
            Button activityButton = new Button(MainActivity.this);
            activityButton.setBackgroundColor(resources.getColor(R.color.darkgrey));
            activityButton.setText(buttonNames[i]);
            activityButton.setLayoutParams(layoutParams);

            contents.addView(activityButton);
            // set an onClickListener for all the buttons to start
            activityButton.setOnClickListener(buttonOnClickListener);
        }
    }

    // when user clicks on a button, run the associated activity
    class ButtonOnClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            Button myButton = (Button) v;

            int index = Arrays.asList(buttonNames).indexOf(myButton.getText());

            Intent intent = new Intent();
            if (index == 0) {
                intent = new Intent(MainActivity.this, AsaActivity.class);
            } else {

                /* this code only works if you include the following code in the AndroidManifest.xml file you are opening
                <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
                </intent-filter>
                */

                intent.setComponent(new ComponentName(packageNames[index], packageNames[index] + activityNames[index]));
            }
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
