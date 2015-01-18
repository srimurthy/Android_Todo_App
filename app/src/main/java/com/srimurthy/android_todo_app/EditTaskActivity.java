package com.srimurthy.android_todo_app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class EditTaskActivity extends ActionBarActivity {

    private int taskPosition;
    private EditText taskTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        this.taskTextView = (EditText) findViewById(R.id.editTaskTextView);
        String taskText = getIntent().getStringExtra("task_text");
        this.taskTextView.setText(taskText);
        this.taskPosition = getIntent().getIntExtra("task_position", 0);

    }

    //Invoked from the button click. Configured in the activity xml file.
    public void onSubmit(View v) {
        Intent data = new Intent();
        data.putExtra("edited_task_text", taskTextView.getText().toString());
        data.putExtra("task_position", this.taskPosition);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
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
