package pt.ulisboa.tecnico.cmov.g15.airdesk.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.g15.airdesk.R;


public class EditFileActivity extends ActionBarActivity {

    public final static String EXTRA_FILE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditFileActivity.FILE_NAME";

    public final static String EXTRA_WORKSPACE_NAME
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditFileActivity.WORKSPACE_NAME";

    public final static String EXTRA_IS_OWNER
            = "pt.ulisboa.tecnico.cmov.g15.airdesk.view.EditFileActivity.EXTRA_IS_OWNER";

    private String mFileName;
    private String mWorkspaceName;
    private boolean mIsOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_file);

        Intent intent = getIntent();
        mFileName      = intent.getStringExtra(EXTRA_FILE_NAME);
        mWorkspaceName = intent.getStringExtra(EXTRA_WORKSPACE_NAME);
        mIsOwner       = intent.getBooleanExtra(EXTRA_IS_OWNER, false);

        TextView fileNameView = (TextView) findViewById(R.id.file_name);
        fileNameView.setText("isOwner="+mIsOwner+"; "+mWorkspaceName+": "+mFileName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_file, menu);
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
